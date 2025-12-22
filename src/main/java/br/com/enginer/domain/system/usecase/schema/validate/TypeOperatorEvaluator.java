package br.com.enginer.domain.system.usecase.schema.validate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import br.com.enginer.domain.system.usecase.enums.TypeOperator;

/**
 * 
 */
public final class TypeOperatorEvaluator {

	/**
	 * 
	 */
	private TypeOperatorEvaluator() {
		// NOP
	}

	/**
	 * @param left
	 * @param operator
	 * @param matchs
	 * @return
	 */
	public static boolean test(Object left, TypeOperator operator, List<String> matchs) {
		if (operator == null)
			return false;

		return switch (operator) {

		// ----------------------------
		// SPECIAL
		// ----------------------------
		case IS_NULL -> left == null;

		case IS_NOT_NULL -> left != null;

		case EMPTY -> isEmpty(left);

		case NOT_EMPTY -> !isEmpty(left);

		// ----------------------------
		// EQUALS / NOT_EQUALS
		// ----------------------------
		case EQUALS -> equalsAny(left, matchs);

		case NOT_EQUALS -> !equalsAny(left, matchs);

		// ----------------------------
		// TEXT
		// ----------------------------
		case CONTAINS -> containsAny(left, matchs);

		case NOT_CONTAINS -> !containsAny(left, matchs);

		case STARTS_WITH -> startsWithAny(left, matchs);

		case ENDS_WITH -> endsWithAny(left, matchs);

		// ----------------------------
		// LIST
		// ----------------------------
		case IN -> in(left, matchs);

		case NOT_IN -> !in(left, matchs);

		// ----------------------------
		// NUMERIC
		// ----------------------------
		case GREATER_THAN -> compareNumber(left, first(matchs)) > 0;

		case GREATER_THAN_OR_EQUALS -> compareNumber(left, first(matchs)) >= 0;

		case LESS_THAN -> compareNumber(left, first(matchs)) < 0;

		case LESS_THAN_OR_EQUALS -> compareNumber(left, first(matchs)) <= 0;

		// ----------------------------
		// RANGE
		// ----------------------------
		case BETWEEN, RANGE -> betweenNumber(left, matchs);

		case NOT_BETWEEN -> !betweenNumber(left, matchs);

		// ----------------------------
		// DATE
		// ----------------------------
		case DATE_EQUALS -> compareDate(left, first(matchs)) == 0;

		case DATE_BEFORE -> compareDate(left, first(matchs)) < 0;

		case DATE_AFTER -> compareDate(left, first(matchs)) > 0;

		case DATE_BETWEEN -> betweenDate(left, matchs);

		case DATE_NOT_BETWEEN -> !betweenDate(left, matchs);

		// ----------------------------
		// LENGTH
		// ----------------------------
		case LENGTH_GREATER_THAN -> length(left) > toInt(first(matchs));

		case LENGTH_LESS_THAN -> length(left) < toInt(first(matchs));

		case LENGTH_BETWEEN -> {
			int a = toInt(get(matchs, 0));
			int b = toInt(get(matchs, 1));
			int len = length(left);
			yield len >= Math.min(a, b) && len <= Math.max(a, b);
		}

		case LENGTH_NOT_BETWEEN -> {
			int a = toInt(get(matchs, 0));
			int b = toInt(get(matchs, 1));
			int len = length(left);
			yield !(len >= Math.min(a, b) && len <= Math.max(a, b));
		}
		};
	}

	// ============================================================
	// Helpers
	// ============================================================

	/**
	 * @param left
	 * @return
	 */
	private static boolean isEmpty(Object left) {
		if (left == null)
			return true;
		if (left instanceof String s)
			return s.trim().isEmpty();
		if (left instanceof List<?> l)
			return l.isEmpty();
		return false;
	}

	/**
	 * @param left
	 * @param matchs
	 * @return
	 */
	private static boolean equalsAny(Object left, List<String> matchs) {
		if (matchs == null || matchs.isEmpty())
			return false;
		String leftStr = Objects.toString(left, null);
		return matchs.stream().anyMatch(m -> Objects.equals(leftStr, m));
	}

	/**
	 * @param left
	 * @param matchs
	 * @return
	 */
	private static boolean containsAny(Object left, List<String> matchs) {
		if (!(left instanceof String s) || matchs == null)
			return false;
		String base = s;
		return matchs.stream().filter(Objects::nonNull).anyMatch(base::contains);
	}

	/**
	 * @param left
	 * @param matchs
	 * @return
	 */
	private static boolean startsWithAny(Object left, List<String> matchs) {
		if (!(left instanceof String s) || matchs == null)
			return false;
		return matchs.stream().filter(Objects::nonNull).anyMatch(s::startsWith);
	}

	/**
	 * @param left
	 * @param matchs
	 * @return
	 */
	private static boolean endsWithAny(Object left, List<String> matchs) {
		if (!(left instanceof String s) || matchs == null)
			return false;
		return matchs.stream().filter(Objects::nonNull).anyMatch(s::endsWith);
	}

	/**
	 * @param left
	 * @param matchs
	 * @return
	 */
	private static boolean in(Object left, List<String> matchs) {
		if (matchs == null)
			return false;

		// suporte: "a,b,c" em uma única string
		if (matchs.size() == 1 && matchs.get(0) != null && matchs.get(0).contains(",")) {
			matchs = List.of(matchs.get(0).split("\\s*,\\s*"));
		}

		String leftStr = Objects.toString(left, null);
		return matchs.stream().anyMatch(m -> Objects.equals(leftStr, m));
	}

	/**
	 * @param left
	 * @param right
	 * @return
	 */
	private static int compareNumber(Object left, String right) {
		if (left == null || right == null)
			return -1;
		BigDecimal a = new BigDecimal(left.toString());
		BigDecimal b = new BigDecimal(right);
		return a.compareTo(b);
	}

	/**
	 * @param left
	 * @param matchs
	 * @return
	 */
	private static boolean betweenNumber(Object left, List<String> matchs) {
		if (left == null || matchs == null || matchs.size() < 2)
			return false;
		BigDecimal v = new BigDecimal(left.toString());
		BigDecimal a = new BigDecimal(get(matchs, 0));
		BigDecimal b = new BigDecimal(get(matchs, 1));
		BigDecimal min = a.min(b);
		BigDecimal max = a.max(b);
		return v.compareTo(min) >= 0 && v.compareTo(max) <= 0;
	}

	/**
	 * @param left
	 * @param right
	 * @return
	 */
	private static int compareDate(Object left, String right) {
		if (left == null || right == null)
			return -1;

		// formatos comuns: yyyy-MM-dd ou yyyy-MM-ddTHH:mm:ss
		if (left instanceof LocalDate ld) {
			LocalDate r = LocalDate.parse(right);
			return ld.compareTo(r);
		}
		if (left instanceof LocalDateTime ldt) {
			LocalDateTime r = LocalDateTime.parse(right);
			return ldt.compareTo(r);
		}

		// fallback: tenta LocalDate
		LocalDate l = LocalDate.parse(left.toString());
		LocalDate r = LocalDate.parse(right);
		return l.compareTo(r);
	}

	/**
	 * @param left
	 * @param matchs
	 * @return
	 */
	private static boolean betweenDate(Object left, List<String> matchs) {
		if (left == null || matchs == null || matchs.size() < 2)
			return false;
		String a = get(matchs, 0);
		String b = get(matchs, 1);

		int c1 = compareDate(left, a);
		int c2 = compareDate(left, b);

		// left entre a e b (qualquer ordem)
		return (c1 >= 0 && c2 <= 0) || (c2 >= 0 && c1 <= 0);
	}

	/**
	 * @param left
	 * @return
	 */
	private static int length(Object left) {
		if (left == null)
			return 0;
		return left.toString().length();
	}

	/**
	 * @param matchs
	 * @return
	 */
	private static String first(List<String> matchs) {
		return get(matchs, 0);
	}

	/**
	 * @param matchs
	 * @param idx
	 * @return
	 */
	private static String get(List<String> matchs, int idx) {
		if (matchs == null || idx < 0 || idx >= matchs.size())
			return null;
		return matchs.get(idx);
	}

	/**
	 * @param v
	 * @return
	 */
	private static int toInt(String v) {
		if (v == null || v.isBlank())
			return 0;
		return Integer.parseInt(v.trim());
	}
}
