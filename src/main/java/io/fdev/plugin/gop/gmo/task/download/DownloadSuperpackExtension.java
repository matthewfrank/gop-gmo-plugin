package io.fdev.plugin.gop.gmo.task.download;

import lombok.Data;

@Data
public class DownloadSuperpackExtension {

	private String apiEndpoint;
	private String destinationPath;
	private String superpackDefinitionPath;
	private String user;
	private String password;
}
