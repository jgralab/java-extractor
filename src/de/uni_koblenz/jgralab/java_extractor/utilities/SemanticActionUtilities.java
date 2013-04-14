package de.uni_koblenz.jgralab.java_extractor.utilities;

import java.util.HashMap;
import java.util.Map;

import de.uni_koblenz.edl.parser.Position;
import de.uni_koblenz.jgralab.java_extractor.builder.Java5Builder;
import de.uni_koblenz.jgralab.java_extractor.schema.program.Program;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.BuiltInType;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.BuiltInTypes;

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
	 * BuiltInTypes
	 */

	private final Map<BuiltInTypes, BuiltInType> builtInTypeMap = new HashMap<BuiltInTypes, BuiltInType>();

	public BuiltInType getBuiltInType(Position currentPosition,
			Object builtInTypes) {
		BuiltInTypes builtInTypes_ = (BuiltInTypes) builtInTypes;
		BuiltInType result = builtInTypeMap.get(builtInTypes_);
		if (result == null) {
			result = (BuiltInType) graphBuilder.createVertex(BuiltInType.VC,
					currentPosition);
			result.set_type(builtInTypes_);
			builtInTypeMap.put(builtInTypes_, result);
		} else {
			// update position to the current one because the AttributedEdges
			// should note the correct positions
			graphBuilder.getPositionsMap().put(result, currentPosition);
		}
		return result;
	}

	/*
	 * further helping methods
	 */

	public String extractSimpleName(String qualifiedName) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
	}

	public String resolveQualifiedName(Object simpleName) {
		// TODO extend complete name resolution
		return (String) simpleName;
	}

	// TODO link qualified names to types, packages, members (especially
	// imports)

	// TODO add comments

}
