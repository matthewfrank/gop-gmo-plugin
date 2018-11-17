package io.fdev.plugin.gop.gmo.task.model.generator;

import io.fdev.plugin.gop.gmo.task.model.context.GmoModelContext;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
class ClassModel {

	public static final String ROOT_NAME = "/root";
	public static final Pattern DEFINITION_PATTERN = Pattern.compile("^(def)?\\s*(\\S+)\\s*(\\w+)?\\s*(\\w+)?\\s*(?:(@desc)\\s*(.+))");
	public static final Pattern MEMBER_SPLIT_PATTERN = Pattern.compile("\\R");
	private final List<ClassMember> members;
	private final GmoModelContext context;

	private String modelType;
	private String modelTable;
	private String modelDescription;

	public ClassModel(String rawClassModel, GmoModelContext context) {

		this.context = context;
		this.defineModel(MEMBER_SPLIT_PATTERN.split(rawClassModel, 2)[0]);
		this.members = generateMembers(rawClassModel);
	}

	void defineModel(String rawModelDefinition) {

		if (ROOT_NAME.contains(rawModelDefinition)) {
			this.modelType = "BundleRoot";
			this.modelTable = "BUNDLE";
			this.modelDescription = "Root table, bundle holder";
		} else {
			Matcher matcher = DEFINITION_PATTERN.matcher(rawModelDefinition);
			while (matcher.find()) {
				this.modelType = matcher.group(2);
				this.modelTable = matcher.group(4);
				this.modelDescription = matcher.group(6);
			}
		}
	}

	List<ClassMember> generateMembers(String rawClassModel) {

		Map<String, String> classModelCustomMembers = context.getClassModelCustomMembers(this.modelType);
		Map<String, String> langFields = context.getLangFields();
		return rawClassModel.lines().skip(1)
				.map(String::trim)
				.filter(GeoGeneratorPredicates.noTransient)
				.map(d -> new ClassMember(d, classModelCustomMembers, langFields))
				.collect(Collectors.toList());
	}

	String prettyPrint() {

		StringBuilder builder = new StringBuilder();
		builder.append(buildPackage());
		builder.append(buildClassDefinition());
		builder.append(" {").append(System.lineSeparator());
		builder.append(System.lineSeparator());
		this.members.forEach(cm -> builder.append(cm.prettyPrint()));
		builder.append("}").append(System.lineSeparator());
		return builder.toString();
	}

	private String buildPackage() {

		return "package " + context.getModelPackage() + System.lineSeparator().repeat(1);
	}

	private String buildClassDefinition() {

		StringBuilder builder = new StringBuilder();
		builder.append(System.lineSeparator());
		builder.append("@lombok.Data").append(System.lineSeparator());
		builder.append("public class ").append(this.modelType).append("Dto implements java.io.Serializable");
		return builder.toString();
	}
}
