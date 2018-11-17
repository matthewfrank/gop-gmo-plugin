package io.fdev.plugin.gop.gmo.task.model;

import lombok.Data;

@Data
public class GenerateGmoModelExtension {

	private String modelPackage;
	private String basePath;
	private String mappingsPath;
	private String langMappingPath;
	private String contextPath;
}
