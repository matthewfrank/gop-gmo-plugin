package io.fdev.plugin.gop.gmo.task.model.generator;

import io.fdev.plugin.gop.gmo.task.model.context.GmoModelContext;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
class GmoModel implements Model {

	private static final Pattern DEFINITION_PATTERN = Pattern.compile("^(def)?\\s*(\\S+)\\s*(\\w+)?\\s*(\\w+)?\\s*(?:(@desc)\\s*(.+))");
	private static final Pattern MEMBER_SPLIT_PATTERN = Pattern.compile("\\R");
	private final List<Model> members;
	private final GmoModelContext context;

	private String modelType;
	private String modelTable;
	private String modelDescription;

	GmoModel(String rawClassModel, GmoModelContext context) {

		this.defineModel(MEMBER_SPLIT_PATTERN.split(rawClassModel, 2)[0]);
		this.context = context;
		this.members = generateMembers(rawClassModel);
	}

	@Override
	public String generate() {

		StringBuilder builder = new StringBuilder();
		builder.append(buildPackage());
		builder.append(buildClassDefinition());
		this.members.forEach(cm -> builder.append(cm.generate()));
		return builder.append("}").toString();
	}

	private void defineModel(String rawModelDefinition) {

		if (GmoGeneratorConstants.root.contains(rawModelDefinition)) {
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

	private List<Model> generateMembers(String rawClassModel) {

		Map<String, String> customFields = context.getCustomFields().getOrDefault(this.modelType, Map.of());
		Map<String, String> langFields = context.getLangFields();

		return rawClassModel.lines()
				.skip(1)
				.filter(GmoGeneratorPredicates.noTransient)
				.map(def -> new GmoModelMember(def, customFields, langFields))
				.collect(Collectors.toList());
	}

	private String buildPackage() {

		return "package " + context.getModelPackage() + System.lineSeparator().repeat(1);
	}

	private String buildClassDefinition() {

		return System.lineSeparator() +
				"@lombok.Data" + System.lineSeparator() +
				"public class " + this.modelType + "Dto implements java.io.Serializable  {" + System.lineSeparator().repeat(1);
	}
}
