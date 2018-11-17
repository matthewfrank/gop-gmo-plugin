package io.fdev.plugin.gop.gmo;

import io.fdev.plugin.gop.gmo.task.model.GenerateGmoModelExtension;
import io.fdev.plugin.gop.gmo.task.model.GenerateGmoModelTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GopGmoPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {

		project
				.getExtensions()
				.create(GopGmoPluginExtensions.gmoModelConfiguration, GenerateGmoModelExtension.class);
		project
				.getTasks()
				.create(GopGmoPluginTasks.generateGmoModel, GenerateGmoModelTask.class);
	}
}
