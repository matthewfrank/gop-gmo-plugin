package io.fdev.plugin.gop.gmo.task.model.generator;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

interface GmoGeneratorPredicates {

	Predicate<String> noTransient = not(s -> s.trim().startsWith("@transient"));

	Predicate<String> isCollection = not(s -> s.trim().contains("*"));

	Function<String, String> toRawList = type -> "java.util.List<" + type.replace("*", GmoGeneratorConstants.modelPostfix) + ">";
}
