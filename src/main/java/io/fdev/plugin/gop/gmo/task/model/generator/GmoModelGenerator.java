package io.fdev.plugin.gop.gmo.task.model.generator;

import io.fdev.plugin.gop.gmo.task.model.context.GmoModelContext;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class GmoModelGenerator {

	private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("\\s*^\\s*$\\s*", Pattern.MULTILINE);

	public void generate(GmoModelContext context) throws IOException {

		Path generatedOutPath = context.getGeneratedOutPath();
		if (Files.exists(generatedOutPath)) {

			log.debug("Remove existing model");
			long count = Files.walk(generatedOutPath)
					.map(Path::toFile)
					.map(File::delete)
					.count();
			log.debug("{} Removed files", count);
		} else {

			log.debug("Create out directory");
			Files.createDirectories(generatedOutPath);
		}

		log.info("Generate DTO model");
		for (GmoModel gmoModel : createClassModels(context)) {

			String fileName = String.valueOf(gmoModel.getModelType()) +
					GmoGeneratorConstants.modelPostfix +
					GmoGeneratorConstants.modelExtension;

			Path modelPath = Path.of(
					generatedOutPath.toString(),
					fileName
			);
			Files.write(modelPath, List.of(gmoModel.generate()), StandardCharsets.UTF_16);
			log.trace("Generated {}", modelPath);
		}
	}

	private List<GmoModel> createClassModels(GmoModelContext context) {

		String[] entityRawDefinitions = EMPTY_LINE_PATTERN.split(context.getContextDefinition());
		return Stream.of(entityRawDefinitions)
				.filter(GmoGeneratorPredicates.noTransient)
				.map(def -> new GmoModel(def, context))
				.collect(Collectors.toList());
	}
}
