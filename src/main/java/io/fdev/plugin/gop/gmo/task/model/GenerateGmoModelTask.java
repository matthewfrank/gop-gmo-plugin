package io.fdev.plugin.gop.gmo.task.model;

import io.fdev.plugin.gop.gmo.task.model.context.GmoModelContext;
import io.fdev.plugin.gop.gmo.task.model.generator.GmoModelGenerator;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public class GenerateGmoModelTask extends DefaultTask {

	@TaskAction
	void generateGmoModel() throws IOException {

		var gmoModelConfiguration = getProject()
				.getExtensions()
				.getByType(GenerateGmoModelExtension.class);

		var context = new GmoModelContext(gmoModelConfiguration);
		var generator = new GmoModelGenerator();
		generator.generate(context);
	}
}
