package io.fdev.plugin.gop.gmo.task.model.generator;

import lombok.Data;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
class GmoModelMember implements Model {

	private static final Pattern PATTERN = Pattern.compile("(\\w+)\\s*(\\S+)\\s*(\\w+)?\\s*(\\w+)?\\s*(?:(@desc)\\s*(.+))");
	private String memberType;
	private String memberImport;
	private String memberName;
	private String memberColumn;
	private String memberDescription;

	GmoModelMember(String rawMember, Map<String, String> classCustomMembers, Map<String, String> langTypes) {

		Matcher matcher = PATTERN.matcher(rawMember);
		while (matcher.find()) {
			memberName = matcher.group(1);
			if (classCustomMembers.containsKey(memberName)) {
				memberType = classCustomMembers.get(memberName);
			} else {
				String type = matcher.group(2);
				memberType = langTypes.getOrDefault(type, type + GmoGeneratorConstants.modelPostfix);
				if (GmoGeneratorPredicates.isCollection.test(type)) {
					memberType = GmoGeneratorPredicates.toRawList.apply(memberType);
				}
			}
			memberColumn = matcher.group(4);
			memberDescription = matcher.group(6);
		}
	}

	@Override
	public String generate() {

		StringBuilder builder = new StringBuilder();
		builder.append("\t").append("/**").append(System.lineSeparator());
		builder.append("\t").append("* @description ").append(memberDescription).append(System.lineSeparator());
		if (Objects.nonNull(memberColumn)) {
			builder.append("\t").append("* @column ").append(memberColumn).append(System.lineSeparator());
		}
		builder.append("\t").append("**/").append(System.lineSeparator());
		builder.append("\t").append("private ").append(memberType).append(" ").append(memberName).append(";");
		builder.append("\t").append(System.lineSeparator());
		builder.append("\t").append(System.lineSeparator());

		return builder.toString();
	}
}
