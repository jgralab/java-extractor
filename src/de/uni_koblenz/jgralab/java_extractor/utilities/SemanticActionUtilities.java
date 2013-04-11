package de.uni_koblenz.jgralab.java_extractor.utilities;

import de.uni_koblenz.edl.parser.Position;
import de.uni_koblenz.jgralab.java_extractor.builder.Java5Builder;
import de.uni_koblenz.jgralab.java_extractor.schema.program.Program;

public class SemanticActionUtilities {

	public static String DEFAULT_PROGRAM_NAME = "program";

	private final Java5Builder graphBuilder;

	public SemanticActionUtilities(Java5Builder java5Builder) {
		graphBuilder = java5Builder;
	}

	/*
	 * Programm
	 */

	private Program program;

	public void setProgramName(String name) {
		assert program != null;
		program.set_name(name);
	}

	public Program getProgram(Position position) {
		if (program == null) {
			program = (Program) graphBuilder.createVertex(Program.VC, position);
		}
		return program;
	}

	/*
	 * further helping methods
	 */

	public String extractSimpleName(String qualifiedName) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
	}

	// TODO link qualified names to types, packages, members (especially
	// imports)

	// TODO add comments

}
