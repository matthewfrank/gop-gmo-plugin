package io.fdev.plugin.gop.gmo.task.download;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.function.Supplier;

public class DownloadSuperpackTask extends DefaultTask {

	private static final String MULTIPART_FORM_DATA_BOUNDARY = "superpack-export-definition";

	@TaskAction
	void downloadSuperpack() throws IOException, InterruptedException {

		var output = new ByteArrayOutputStream();
		var extension = getProject()
				.getExtensions()
				.getByType(DownloadSuperpackExtension.class);

		var multipartEntity = MultipartEntityBuilder.create()
				.addPart("file", new FileBody(Path.of(extension.getSuperpackDefinitionPath()).toFile()))
				.setBoundary(MULTIPART_FORM_DATA_BOUNDARY)
				.build();

		var request = HttpRequest.newBuilder()
				.uri(URI.create(extension.getApiEndpoint()))
				.header(MIME.CONTENT_TYPE, "multipart/form-data; boundary=" + MULTIPART_FORM_DATA_BOUNDARY)
				.POST(HttpRequest.BodyPublishers.ofInputStream(multipartStreamSupplier(multipartEntity)))
				.build();

		System.out.println("downloading... keep calm");

		HttpClient.newBuilder()
				.authenticator(new GopAuthenticator(extension))
				.build()
				.send(request, HttpResponse.BodyHandlers.ofByteArrayConsumer(bytes -> bytes.ifPresent(output::writeBytes)));

		String name = extension.getDestinationPath() + File.separator + "gop-superpack.zip";
		try (OutputStream outputStream = new FileOutputStream(name)) {
			output.writeTo(outputStream);
		}
	}

	private Supplier<? extends InputStream> multipartStreamSupplier(HttpEntity multipartEntity) {

		return (Supplier<InputStream>) () -> {

			try {
				return multipartEntity.getContent();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		};
	}
}
