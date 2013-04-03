package misc_tests;

import java.io.File;
import java.io.FileFilter;

import de.uni_koblenz.edl.GraphBuilder;
import de.uni_koblenz.edl.GraphBuilderBaseImpl;
import de.uni_koblenz.jgralab.java_extractor.builder.Java5Builder;
import de.uni_koblenz.jgralab.java_extractor.schema.Java5Schema;

public class ParseFiles {

	public static void main(String[] args) {
		GraphBuilderBaseImpl.printDebugInformationToTheConsole = true;
		Java5Builder builder = new Java5Builder(Java5Schema.instance());
		// JavaGraphBuilder builder = new JavaGraphBuilder();//
		// JavaSchema.instance());
		parseAllJavaFiles(builder, new File("D:/JDK6SourceCode/" + "langtools"
				+ File.separator + "test" + File.separator + "tools"
				+ File.separator + "javac" + File.separator + "unicode"));
	}

	private static void parseAllJavaFiles(GraphBuilder builder, File file) {
		if (file.isDirectory()) {
			for (File containedFile : file.listFiles(jdk6SourceCodeFilter)) {
				parseAllJavaFiles(builder, containedFile);
			}
		} else {
			builder.parse(file.getAbsolutePath());
		}
	}

	private static FileFilter jdk6SourceCodeFilter = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory()
					|| pathname.getName().toLowerCase().endsWith(".java")
					&& !(pathname.getName().contains("-X-")
							|| pathname.getName().contains("X-")
							|| pathname.getName().contains("-X")
							|| pathname.getName().equals("BadSource.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "4846262"
											+ File.separator + "Test.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "6302184"
											+ File.separator + "T6302184.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "6440583"
											+ File.separator + "A.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Syntax1.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z13.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z14.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z15.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z16.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z2.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z3.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z4.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z5.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z8.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "annotations"
											+ File.separator + "neg"
											+ File.separator + "Z9.java")
							|| pathname.toString()
									.endsWith(
											"langtools" + File.separator
													+ "test" + File.separator
													+ "tools" + File.separator
													+ "javac" + File.separator
													+ "api" + File.separator
													+ "T6265137a.java")
							|| (pathname.toString().contains(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator)
									&& pathname.getName().startsWith("Bad") || pathname
									.toString().endsWith(
											"langtools" + File.separator
													+ "test" + File.separator
													+ "tools" + File.separator
													+ "javac" + File.separator
													+ "api" + File.separator
													+ "T6265137a.java"))
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "Digits.java")
							|| pathname.toString().contains(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "enum"
											+ File.separator + "6384542"
											+ File.separator)
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "EOI.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator
											+ "ExtendArray.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator
											+ "ExtraneousEquals.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "generics"
											+ File.separator + "6413682"
											+ File.separator + "T6413682.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator
											+ "IllegalAnnotation.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "javazip"
											+ File.separator + "bad"
											+ File.separator + "B.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "Parens3.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "Parens4.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator
											+ "ParseConditional.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "policy"
											+ File.separator + "test3"
											+ File.separator + "A.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "rawDiags"
											+ File.separator + "Error.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator
											+ "StandaloneQualifiedSuper.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator
											+ "StoreClass.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "T4994049"
											+ File.separator + "T4994049.java")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "T6882235.java")
							|| pathname.toString().contains(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "unicode"
											+ File.separator
											+ "NonasciiDigit.java")
							|| pathname.toString().contains(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "unicode"
											+ File.separator
											+ "NonasciiDigit2.java")
							|| pathname.toString().contains(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "unicode"
											+ File.separator
											+ "TripleQuote.java")
							|| pathname.toString().contains(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "unicode"
											+ File.separator
											+ "SupplementaryJavaID2")
							|| pathname.toString().contains(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javac"
											+ File.separator + "unicode"
											+ File.separator
											+ "SupplementaryJavaID3")
							|| pathname.toString().endsWith(
									"langtools" + File.separator + "test"
											+ File.separator + "tools"
											+ File.separator + "javadoc"
											+ File.separator + "sourceOption"
											+ File.separator + "p"
											+ File.separator + "A.java")
							|| pathname.getName().equals("EUC_TW.java")
							|| pathname.getName().equals("IBM964.java") || pathname
							.getName().equals("DeepStringConcat.java"));
		}
	};
}
