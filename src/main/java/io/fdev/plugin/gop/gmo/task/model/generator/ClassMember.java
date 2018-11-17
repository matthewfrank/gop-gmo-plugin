package io.fdev.plugin.gop.gmo.task.model.generator;

import lombok.Data;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
class ClassMember {

	private final static Pattern PATTERN = Pattern.compile("(\\w+)\\s*(\\S+)\\s*(\\w+)?\\s*(\\w+)?\\s*(?:(@desc)\\s*(.+))");
	private String memberType;
	private String memberImport;
	private String memberName;
	private String memberColumn;
	private String memberDescription;

	public ClassMember(String rawMember, Map<String, String> classCustomMembers, Map<String, String> langTypes) {

		Matcher matcher = PATTERN.matcher(rawMember);
		while (matcher.find()) {
			this.memberName = matcher.group(1);
			if (classCustomMembers.containsKey(this.memberName)) {
				this.memberType = classCustomMembers.get(this.memberName);
			} else {
				String type = matcher.group(2);
				if (type.contains("*")) {
					this.memberType = "java.util.List<" + type.replace("*", "Dto") + ">";
				} else {
					this.memberType = langTypes.getOrDefault(type, type + "Dto");
				}
			}
			this.memberColumn = matcher.group(4);
			this.memberDescription = matcher.group(6);
		}
	}

	String prettyPrint() {

		StringBuilder builder = new StringBuilder();
		builder.append("\t").append("/**").append(System.lineSeparator());
		builder.append("\t").append("* @description ").append(memberDescription).append(System.lineSeparator());
		if (Objects.nonNull(memberColumn)) {
			builder.append("\t").append("* @column ").append(memberColumn).append(System.lineSeparator());
		}
		builder.append("\t").append("**/").append(System.lineSeparator());
		builder.append("\t").append("private ").append(this.memberType).append(" ").append(memberName).append(";");
		builder.append("\t").append(System.lineSeparator());
		builder.append("\t").append(System.lineSeparator());

		return builder.toString();
	}
}
