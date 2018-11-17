package io.fdev.plugin.gop.gmo.task.model;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public class GenerateModelTask extends DefaultTask {

	@TaskAction
	void generate() throws IOException {

		System.out.println("Test");
	}
}
