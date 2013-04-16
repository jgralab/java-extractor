package de.uni_koblenz.jgralab.java_extractor.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;

import de.uni_koblenz.edl.parser.Position;
import de.uni_koblenz.edl.parser.symboltable.SymbolTableStack;
import de.uni_koblenz.jgralab.Edge;
import de.uni_koblenz.jgralab.EdgeDirection;
import de.uni_koblenz.jgralab.Vertex;
import de.uni_koblenz.jgralab.java_extractor.builder.Java5Builder;
import de.uni_koblenz.jgralab.java_extractor.schema.common.AttributedEdge;
import de.uni_koblenz.jgralab.java_extractor.schema.program.Program;
import de.uni_koblenz.jgralab.java_extractor.schema.type.definition.HasTypeParameterUpperBound;
import de.uni_koblenz.jgralab.java_extractor.schema.type.definition.Type;
import de.uni_koblenz.jgralab.java_extractor.schema.type.definition.TypeParameterDeclaration;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.BuiltInType;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.BuiltInTypes;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.EnclosedType;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.HasEnclosedType;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.HasEnclosingType;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.HasLowerBound;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.HasSimpleArgumentType;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.HasSimpleName;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.HasUpperBound;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.IsDefinedByType;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.QualifiedType;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.SimpleArgument;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.TypeArgument;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.TypeParameterUsage;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.TypeSpecification;
import de.uni_koblenz.jgralab.java_extractor.schema.type.specification.WildcardArgument;

public class SemanticActionUtilities {

	public static String DEFAULT_PROGRAM_NAME = "program";

	private final Java5Builder graphBuilder;

	public SemanticActionUtilities(Java5Builder java5Builder) {
		graphBuilder = java5Builder;
	}

	public String readInput(String inputFile, String encoding)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		PushbackReader pbr = null;
		try {
			pbr = new PushbackReader(new BufferedReader(new InputStreamReader(
					new FileInputStream(inputFile), encoding)), 5);
			boolean evenNumberOfBackslashes = true;
			for (int current = pbr.read(); current != -1; current = pbr.read()) {
				if (current == '\\') {
					evenNumberOfBackslashes = !evenNumberOfBackslashes;
				} else {
					evenNumberOfBackslashes = true;
				}
				if (!evenNumberOfBackslashes) {
					try {
						current = readUnicodeEscape(pbr);
						// if a correct unicode escape was read, the counter for
						// backslaches can be reset
						evenNumberOfBackslashes = true;
					} catch (NumberFormatException e) {
						// this was no unicode escape. Corresponding chars ar
						// already back in the pushback reader
					}
				}
				sb.append((char) current);
			}
		} finally {
			if (pbr != null) {
				pbr.close();
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	private int readUnicodeEscape(PushbackReader reader) throws IOException {
		int firstHex, secondHex, thirdHex, fourthHex;
		int numberOfReadChars = 0;
		do {
			firstHex = reader.read();
			numberOfReadChars++;
		} while (firstHex == 'u');
		if (numberOfReadChars == 1) {
			// only one char was read which was no 'u'
			reader.unread(firstHex);
			throw new NumberFormatException();
		}
		if (isNotHexDigit(firstHex)) {
			if (firstHex == -1) {
				reader.unread(new char[] { 'u' });
			} else {
				reader.unread(new char[] { 'u', (char) firstHex });
			}
			throw new NumberFormatException();
		}
		secondHex = reader.read();
		if (isNotHexDigit(secondHex)) {
			if (secondHex == -1) {
				reader.unread(new char[] { 'u', (char) firstHex });
			} else {
				reader.unread(new char[] { 'u', (char) firstHex,
						(char) secondHex });
			}
			throw new NumberFormatException();
		}
		thirdHex = reader.read();
		if (isNotHexDigit(thirdHex)) {
			if (thirdHex == -1) {
				reader.unread(new char[] { 'u', (char) firstHex,
						(char) secondHex });
			} else {
				reader.unread(new char[] { 'u', (char) firstHex,
						(char) secondHex, (char) thirdHex });
			}
			throw new NumberFormatException();
		}
		fourthHex = reader.read();
		if (isNotHexDigit(fourthHex)) {
			if (fourthHex == -1) {
				reader.unread(new char[] { 'u', (char) firstHex,
						(char) secondHex, (char) thirdHex });
			} else {
				reader.unread(new char[] { 'u', (char) firstHex,
						(char) secondHex, (char) thirdHex, (char) fourthHex });
			}
			throw new NumberFormatException();
		}
		return Integer.parseInt("" + ((char) firstHex) + ((char) secondHex)
				+ ((char) thirdHex) + ((char) fourthHex), 16);
	}

	private boolean isNotHexDigit(int firstHex) {
		return firstHex == -1
				|| !((firstHex >= 'a' && firstHex <= 'f')
						|| (firstHex >= 'A' && firstHex <= 'F') || (firstHex >= '0' && firstHex <= '9'));
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

	/**
	 * If a simple name is used in a Java file, then this method resolves the
	 * corresponding qualified name.
	 * 
	 * @param simpleName
	 * @return
	 */
	public String resolveQualifiedName(Object simpleName) {
		// TODO extend complete name resolution
		return (String) simpleName;
	}

	/**
	 * If a type parameter is used in the type parameter declaration before its
	 * own declaration, then the type parameter is represented as
	 * {@link QualifiedType} instead of {@link TypeParameterUsage}.
	 * 
	 * @param name2TypeParameter
	 * @param typeDefinition
	 */
	public void correctTypeParameterUsage(SymbolTableStack name2TypeParameter,
			Vertex typeDefinition) {
		for (TypeParameterDeclaration typeParamDecl : ((Type) typeDefinition)
				.get_typeParameters()) {
			HasTypeParameterUpperBound htpub = typeParamDecl
					.getFirstHasTypeParameterUpperBoundIncidence(EdgeDirection.OUT);
			while (htpub != null) {
				HasTypeParameterUpperBound next = htpub
						.getNextHasTypeParameterUpperBoundIncidence(EdgeDirection.OUT);
				TypeSpecification typeSpecification = (TypeSpecification) htpub
						.getThat();
				checkAndCorrectQualifiedTyps(name2TypeParameter, htpub,
						typeSpecification);
				htpub = next;
			}
		}
	}

	/**
	 * @param name2TypeParameter
	 * @param edge2WrongQualifiedType
	 * @param possiblyWrongQualifiedType
	 */
	private void checkAndCorrectQualifiedTyps(
			SymbolTableStack name2TypeParameter,
			AttributedEdge edge2WrongQualifiedType,
			TypeSpecification possiblyWrongQualifiedType) {
		// correct type arguments
		for (TypeArgument typeArgument : possiblyWrongQualifiedType
				.get_typeArguments()) {
			for (Vertex argument : typeArgument.get_arguments()) {
				if (argument.isInstanceOf(SimpleArgument.VC)) {
					HasSimpleArgumentType hsat = (HasSimpleArgumentType) argument
							.getFirstIncidence(HasSimpleArgumentType.EC);
					checkAndCorrectQualifiedTyps(name2TypeParameter, hsat,
							hsat.getOmega());
				} else if (argument.isInstanceOf(WildcardArgument.VC)) {
					HasUpperBound hub = (HasUpperBound) argument
							.getFirstIncidence(HasUpperBound.EC);
					if (hub != null) {
						checkAndCorrectQualifiedTyps(name2TypeParameter, hub,
								hub.getOmega());
					}
					HasLowerBound hlb = (HasLowerBound) argument
							.getFirstIncidence(HasLowerBound.EC);
					if (hlb != null) {
						checkAndCorrectQualifiedTyps(name2TypeParameter, hlb,
								hlb.getOmega());
					}
				}
			}
		}
		if (possiblyWrongQualifiedType.isInstanceOf(QualifiedType.VC)) {
			TypeParameterDeclaration declarationOfUsedTypeParameter = (TypeParameterDeclaration) name2TypeParameter
					.use(((QualifiedType) possiblyWrongQualifiedType)
							.get_fullyQualifiedName());
			if (declarationOfUsedTypeParameter != null) {
				correctQualifiedType(edge2WrongQualifiedType,
						(QualifiedType) possiblyWrongQualifiedType,
						declarationOfUsedTypeParameter);
			}
		} else if (possiblyWrongQualifiedType.isInstanceOf(EnclosedType.VC)) {
			HasEnclosedType hasEnclosedType = (HasEnclosedType) possiblyWrongQualifiedType
					.getFirstIncidence(HasEnclosedType.EC);
			checkAndCorrectQualifiedTyps(name2TypeParameter, hasEnclosedType,
					hasEnclosedType.getOmega());
			HasEnclosingType hasEnclosingType = (HasEnclosingType) possiblyWrongQualifiedType
					.getFirstIncidence(HasEnclosingType.EC);
			checkAndCorrectQualifiedTyps(name2TypeParameter, hasEnclosingType,
					hasEnclosingType.getOmega());
		} else if (possiblyWrongQualifiedType
				.isInstanceOf(TypeParameterUsage.VC)) {
			// public class TestClass<V> {
			// public interface TestClass<W extends V, V> {
			// }
			// }
			// In this case the "W extends V" uses the wrong V
			IsDefinedByType idbt = (IsDefinedByType) possiblyWrongQualifiedType
					.getFirstIncidence(IsDefinedByType.EC);
			TypeParameterDeclaration usedTypeParamDecl = (TypeParameterDeclaration) idbt
					.getOmega();
			Vertex correctTypeParamDecl = name2TypeParameter
					.use(usedTypeParamDecl.get_simpleName().get_name());
			if (correctTypeParamDecl != usedTypeParamDecl) {
				idbt.setOmega(correctTypeParamDecl);
			}
		}
	}

	/**
	 * @param edge2WrongQualifiedType
	 * @param wrongQualifiedType
	 * @param declarationOfUsedTypeParameter
	 */
	private void correctQualifiedType(AttributedEdge edge2WrongQualifiedType,
			QualifiedType wrongQualifiedType,
			TypeParameterDeclaration declarationOfUsedTypeParameter) {
		Position pos = new Position(edge2WrongQualifiedType.get_offset(),
				edge2WrongQualifiedType.get_length(),
				edge2WrongQualifiedType.get_line(),
				edge2WrongQualifiedType.get_column());
		TypeParameterUsage newUsage = (TypeParameterUsage) graphBuilder
				.createVertex(TypeParameterUsage.VC, pos);
		Edge edge = wrongQualifiedType.getFirstIncidence();
		while (edge != null) {
			Edge next = edge.getNextIncidence();
			if (edge.isInstanceOf(HasSimpleName.EC)) {
				edge.delete();
				graphBuilder.createEdge(IsDefinedByType.EC, newUsage,
						declarationOfUsedTypeParameter);
			} else if (edge.getAlpha() == wrongQualifiedType) {
				edge.setAlpha(newUsage);
			} else {
				assert edge.getOmega() == wrongQualifiedType;
				edge.setOmega(newUsage);
			}
			edge = next;
		}
		assert wrongQualifiedType.getDegree() == 0;
		wrongQualifiedType.delete();
	}

	// TODO link qualified names to types, packages, members (especially
	// imports)

	// TODO add comments

}
