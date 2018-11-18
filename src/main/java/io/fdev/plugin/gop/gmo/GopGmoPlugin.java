package io.fdev.plugin.gop.gmo;

import io.fdev.plugin.gop.gmo.task.download.DownloadSuperpackExtension;
import io.fdev.plugin.gop.gmo.task.download.DownloadSuperpackTask;
import io.fdev.plugin.gop.gmo.task.model.GenerateGmoModelExtension;
import io.fdev.plugin.gop.gmo.task.model.GenerateGmoModelTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GopGmoPlugin implements Plugin<Project> {

	private static final String HYPERON_GROUP = "Hyperon";

	@Override
	public void apply(Project project) {

		project
				.getExtensions()
				.create(GopGmoPluginExtensions.gmoModelConfiguration, GenerateGmoModelExtension.class);
		project
				.getExtensions()
				.create(GopGmoPluginExtensions.superpackConfiguration, DownloadSuperpackExtension.class);

		project
				.getTasks()
				.create(GopGmoPluginTasks.generateGmoModel, GenerateGmoModelTask.class)
				.setGroup(HYPERON_GROUP);
		project
				.getTasks()
				.create(GopGmoPluginTasks.downloadSuperpack, DownloadSuperpackTask.class)
				.setGroup(HYPERON_GROUP);
	}
}
