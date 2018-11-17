package io.fdev.plugin.gop.gmo.task.model.context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.fdev.plugin.gop.gmo.task.model.GenerateGmoModelExtension;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Getter
public class GmoModelContext {

	private static final Gson GSON = new Gson();
	private static final Type MAP_TYPE = TypeToken.get(Map.class).getType();

	private final String modelPackage;
	private final Path generatedOutPath;
	private final String contextDefinition;
	private final Map<String, Map<String, String>> customFields;
	private final Map<String, String> langFields;

	public GmoModelContext(GenerateGmoModelExtension extension) throws IOException {

		this.modelPackage = extension.getModelPackage();
		this.generatedOutPath = Path.of(extension.getBasePath() + extension.getModelPackage().replace(".", File.separator) + File.separator);
		this.contextDefinition = Files.readString(Path.of(extension.getContextPath()));
		this.customFields = GSON.fromJson(Files.readString(Path.of(extension.getMappingsPath())), MAP_TYPE);
		this.langFields = GSON.fromJson(Files.readString(Path.of(extension.getLangMappingPath())), MAP_TYPE);
	}
}
