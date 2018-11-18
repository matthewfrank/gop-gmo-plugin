package io.fdev.plugin.gop.gmo.task.download;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class GopAuthenticator extends Authenticator {

	private final DownloadSuperpackExtension extension;

	GopAuthenticator(DownloadSuperpackExtension extension) {

		this.extension = extension;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {

		return new PasswordAuthentication(extension.getUser(), extension.getPassword().toCharArray());
	}
}
