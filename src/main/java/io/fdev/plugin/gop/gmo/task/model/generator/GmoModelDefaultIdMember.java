package io.fdev.plugin.gop.gmo.task.model.generator;

import lombok.Data;

import java.util.Map;

@Data
class GmoModelDefaultIdMember implements Model {

	private final String idRaw;

	GmoModelDefaultIdMember(Map<String, String> classCustomMembers) {

		idRaw = classCustomMembers.containsKey("id")
				? generateCustom(classCustomMembers)
				: generateDefault();
	}

	@Override
	public String generate() {

		return idRaw;
	}

	String generateDefault() {

		StringBuilder builder = new StringBuilder();
		builder.append(System.lineSeparator());
		builder.append("\t").append("@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)");
		builder.append(System.lineSeparator());
		builder.append("\t").append("private java.lang.Long id;");
		builder.append(System.lineSeparator().repeat(2));
		return builder.toString();
	}

	String generateCustom(Map<String, String> classCustomMembers) {

		StringBuilder builder = new StringBuilder();
		builder.append(System.lineSeparator());
		builder.append("\t").append("private ").append(classCustomMembers.get("id")).append(" id;");
		builder.append(System.lineSeparator().repeat(2));
		return builder.toString();
	}
}
