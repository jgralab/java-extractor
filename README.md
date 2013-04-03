# The Java-Extractor Project

TODO under construction
This projects contains an extractor for converting the complete java sources to abstract syntax graphs.

## Building JEx

The project structure should be like this:

    jgsrc/               # your jgralab workspace (the name doesn't matter)
    +-> jgralab/         # jgralab itself
    +-> edl/             # the EDL project (extractor description language)
    `-> java-extractor/  # this project

Before building java-extractor, be sure to build jgralab, and edl.  Thereafter, build JEx:

    $ pwd
    /path/to/jgsrc/
    $ cd java-extractor
    $ ant
    ...
    BUILD SUCCESSFUL

## Using Java-Extractor

To parse your java source files, you can either use the `jex.sh` script or
invoke java from the command line.  JEx uses reflection for type resolution, so
be sure to put the class files compiled from you source files including all
dependencies into CLASSPATH.

    $ CLASSPATH="foo.jar:bar.jar" ./jex.sh -o my-asg.tg src/**/*.java

For more options, check out `./jex.sh --help`.

## The Schema

### The type hierarchy

![Type Hierarchy](https://raw.github.com/jgralab/jex/master/java-schema/TypeHierarchy.png)

### Classifiers and extends/implements

![Classifiers and extends/implements](https://raw.github.com/jgralab/jex/master/java-schema/ClassifierInheritance.png)

### Array types

![Array types](https://raw.github.com/jgralab/jex/master/java-schema/Arrays.png)

### Members

![Members](https://raw.github.com/jgralab/jex/master/java-schema/Members.png)

### Methods

![Methods](https://raw.github.com/jgralab/jex/master/java-schema/Methods.png)

### Modifieable and typed elements

![Modifiable and types elements](https://raw.github.com/jgralab/jex/master/java-schema/ModifiableAndTypedElements.png)

### Generics

![Generics](https://raw.github.com/jgralab/jex/master/java-schema/TypeParameters.png)

## License

Copyright (C) 2007-2012 The JGraLab Team <ist@uni-koblenz.de>

Distributed under the GNU General Public License (Version 3 or later), with the
following additional grant:

    Additional permission under GNU GPL version 3 section 7

    If you modify this Program, or any covered work, by linking or combining it
    with Eclipse (or a modified version of that program or an Eclipse plugin),
    containing parts covered by the terms of the Eclipse Public License (EPL),
    the licensors of this Program grant you additional permission to convey the
    resulting work.  Corresponding Source for a non-source form of such a
    combination shall include the source code for the parts of JGraLab used as
    well as that of the covered work.


<!-- Local Variables:        -->
<!-- mode: markdown          -->
<!-- indent-tabs-mode: nil   -->
<!-- End:                    -->
