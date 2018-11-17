package io.fdev.plugin.gop.gmo.task.model.generator;

import java.util.function.Predicate;

import static java.util.function.Predicate.not;

interface GeoGeneratorPredicates {

	Predicate<String> noTransient = not(s -> s.startsWith("@transient"));
}
