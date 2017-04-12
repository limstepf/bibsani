package ch.unibe.scg.bibsani;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Line sanitizers.
 */
public enum LineSanitizer {

	/**
	 * Copy. As if I give a fuck...
	 */
	COPY() {
				@Override
				public String sanitize(String line) {
					return line;
				}
			},
	/**
	 * Remove funny UTF-8 accents from BibTeX key/entry start lines.
	 */
	REMOVE_ACCENTS_IN_KEYS() {
				@Override
				public String sanitize(String line) {
					if (isEntryStart(line)) {
						return removeAccents(line);
					}
					return line;
				}
			},
	/**
	 * Remove funny UTF-8 accents from BibTeX key/entry start lines, then remove
	 * all illegal chars.
	 */
	REMOVE_ACCENTS_THEN_INVALID_CHARS_IN_KEYS() {
				@Override
				public String sanitize(String line) {
					if (isEntryStart(line)) {
						final String noaccents = removeAccents(line);
						return removeInvalidChars(noaccents);
					}
					return line;
				}
			},
	/**
	 * Remove all illegal chars.
	 */
	REMOVE_INVALID_CHARS_IN_KEYS() {
				@Override
				public String sanitize(String line) {
					if (isEntryStart(line)) {
						return removeInvalidChars(line);
					}
					return line;
				}
			};

	private static final String WHITELIST_CHARS_PATTERN = "[^\\sa-zA-Z0-9.,:\\?!`'\\(\\)\\{\\}\\[\\]/@_\\+=-]";
	private static final String ACCENT_PATTERN = "\\p{InCombiningDiacriticalMarks}+";
	private static final Pattern ENTRY_START_LINE_PATTERN = Pattern.compile(
			"^\\s*@([a-zA-Z]+)\\{"
	);

	private static boolean isEntryStart(String line) {
		final Matcher m = ENTRY_START_LINE_PATTERN.matcher(line);
		return m.find();
	}

	private static String removeAccents(String line) {
		line = line.replaceAll("ß", "ss");
		return Normalizer.normalize(line, Normalizer.Form.NFD).replaceAll(ACCENT_PATTERN, "");
	}

	private static String removeInvalidChars(String line) {
		return line.replaceAll(WHITELIST_CHARS_PATTERN, "");
	}

	/**
	 * Sanitizes the line.
	 *
	 * @param line the dirty line.
	 * @return the sanitized line.
	 */
	abstract public String sanitize(String line);

}
