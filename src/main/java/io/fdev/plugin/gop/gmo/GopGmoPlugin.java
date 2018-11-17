package io.fdev.plugin.gop.gmo;

import io.fdev.plugin.gop.gmo.task.model.GenerateModelTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GopGmoPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {

		project.getTasks().create("generateModel", GenerateModelTask.class, (task) -> {
		});
	}
}
