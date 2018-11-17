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
import java.util.HashMap;
import java.util.Map;

@Getter
public class GmoModelContext {

	private static final Gson GSON = new Gson();
	private static final Type MAP_TYPE = TypeToken.get(Map.class).getType();

	private final Path generatedOutPath;
	private final String contextDefinition;
	private final Map<String, Map<String, String>> customFields;
	private final Map<String, String> langFields;

	public GmoModelContext(GenerateGmoModelExtension extension) throws IOException {

		this.generatedOutPath = Path.of(extension.basePath + extension.modelPackage.replace(".", File.separator) + File.separator);
		this.contextDefinition = Files.readString(Path.of(extension.contextPath));
		this.customFields = GSON.fromJson(Files.readString(Path.of(extension.mappingsPath)), MAP_TYPE);
		this.langFields = GSON.fromJson(Files.readString(Path.of(extension.langMappingPath)), MAP_TYPE);
	}

	@Deprecated
	public Map<String, String> getClassModelCustomMembers(String classModelName) {

		return customFields.containsKey(classModelName)
				? customFields.get(classModelName)
				: new HashMap<>();
	}
}
