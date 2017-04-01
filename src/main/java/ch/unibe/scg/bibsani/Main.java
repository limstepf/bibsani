package ch.unibe.scg.bibsani;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Bib(TeX) Sani(tizer).
 */
public class Main {

	/**
	 * Main method.
	 *
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {

		// parse command line arguments
		final CommandLineArguments cla = new CommandLineArguments(Main.class, args);
		final CommandLineArguments.Argument fileArg = cla.add(
				"The BibTeX file to process.",
				"<file>",
				"f", "file"
		);
		final CommandLineArguments.Argument dirArg = cla.add(
				"The output directory where to put the sanitized BibTeX file. (OPTIONAL)",
				"<file>",
				"d", "directory"
		);
		final CommandLineArguments.Argument usageArg = cla.add(
				"Print the usage of this program.",
				"",
				"u", "usage"
		);

		if (args.length == 0 || usageArg.isSet() || !CommandLineArguments.areAllSet(fileArg)) {
			cla.printUsage();
			kthxbai();
		}

		final File inputFile = new File(fileArg.getString());
		if (!inputFile.exists()) {
			printError("ERROR: input file does not exists: " + inputFile);
			kthxbai();
		}
		System.out.println("input file: " + inputFile);

		File outputDirectory = null;
		if (!dirArg.isEmpty()) {
			outputDirectory = new File(dirArg.getString());
			if (!outputDirectory.exists()) {
				System.out.println("creating output directory: " + outputDirectory);
				outputDirectory.mkdirs();
			}
		}
		// just put output file where the input file is if no directory is specified
		if (outputDirectory == null) {
			outputDirectory = inputFile.getParentFile();
		}
		final File outputFile = new File(
				outputDirectory,
				getOutputFilename(inputFile)
		);
		System.out.println("output file: " + outputFile);
		System.out.print("\n");

		final LineSanitizer sanitizer = LineSanitizer.REMOVE_ACCENTS_THEN_INVALID_CHARS_IN_KEYS;
		int numLine = 0;
		int numSanitizedLines = 0;

		try (BufferedReader reader = newReader(inputFile)) {
			try (BufferedWriter writer = newWriter(outputFile)) {
				String line;
				while ((line = reader.readLine()) != null) {
					numLine++;
					final String sanitizedLine = sanitizer.sanitize(line);
					if (!line.equals(sanitizedLine)) {
						numSanitizedLines++;
						System.out.println(String.format(
								"line %8d: %s",
								numLine, line
						));
						System.out.println(String.format(
								" sanitized to: %s",
								sanitizedLine
						));
					}
					writer.write(sanitizedLine);
					writer.newLine();
				}
			} catch (IOException ex) {
				printError(ex, "ERROR: failed to write to output file: " + outputFile);
			}
		} catch (IOException ex) {
			printError(ex, "ERROR: failed to read input file: " + inputFile);
		}

		kthxbai();
	}

	public static void kthxbai() {
		System.out.println("\nkthxbai.");
		System.exit(0);
	}

	public static void printError(String... messages) {
		printError(null, messages);
	}

	public static void printError(Exception ex, String... messages) {
		for (String msg : messages) {
			System.err.println(msg);
		}
		if (ex != null) {
			ex.printStackTrace(System.err);
		}
	}

	public static String getOutputFilename(File file) {
		final String filename = file.getName();
		final int n = filename.lastIndexOf('.');
		return filename.substring(0, n) + "-bibsani.bib";
	}

	public static BufferedReader newReader(File file) throws UnsupportedEncodingException, FileNotFoundException {
		return new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"
		));
	}

	public static BufferedWriter newWriter(File file) throws UnsupportedEncodingException, FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF-8"
		));
	}

}
