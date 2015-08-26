<b>Unix4j</b> is an implementation of Unix command line tools in Java. You can use the commands that you know from Unix in a Java program---you can pipe the results of one command to another as you know it from Unix.

Unix4j gives you the power of unix commands, e.g. text processing, file management, with the benefits that come from a strongly typed and testable language such as Java.

In Unix, you might see a command such as:
```
cat test.txt | grep "Tuesday" | sed "s/kilogram/kg/g" | sort 
```

In Unix4j this would look like:
```
Unix4j.cat("test.txt").grep("Tuesday").sed("s/kilogram/kg/g").sort();
```

Currently supported commands are: cat, cd, cut, echo, find, grep, head, ls, sed, sort, tail, uniq, wc, xargs.

## Maven ##
```
<dependency>
	<groupId>org.unix4j</groupId>
	<artifactId>unix4j-command</artifactId>
	<version>0.3</version>
</dependency>
```

## Easy text processing ##
Working in the finance industry in Melbourne, the authors of Unix4j spent a lot of time writing complex text processing applications in bash.  Frustrated with the inherent limitations of the bash language; lack of language support, IDEs, test frameworks etc, the authors decided to try and bring the convenience of some of the Unix commands into the Java language.
<br />
You may notice that the implemented commands are more bent towards text processing rather than OS and file manipulation.  This is intended as we see text processing to be the real benefit of Unix4j.  Not to say that this will always be the case.

## Simple to use ##
Using Unix commands, it is very easy to achieve powerful text processing with very concise expressions.  Achieving the same thing in Java is often very verbose. For Unix4j to be useful it had to be simple to use.  The authors have gone to great lengths to provide an API which hopefully achieves this.
<br />
To emulate the "piping" that is used within Unix, a feature which is embraced to great length in Unix4j is "method chaining". e.g.:
```
String result = Unix4j.fromString("hello world").grep("hello").wc().toStringResult();
```
Providing such method chaining whilst still keeping a clean and well structured code base has been a real challenge.  It may have increased the complexity of the code base in some instances.  But we feel that Unix4j <b>must</b> be easy to use, and must result in code which is concise and easy to read.

## Remaining true to the behaviour of the emulated Unix commands ##
Where possible we have tried to stick as closely to the behaviour of the original command as much as possible.  Options and argument names have also been aligned with those in the original Unix commands.

Examples:
```
//list all files and directories in the long listing format (unix: ls -la)
Unix4j.ls(Ls.Options.l.a).toStdOut();

//print all lines of the file "fruitlist.txt" that are not containing "apple" (unix: grep -v apple fruitlist.txt)
Unix4j.grep(Grep.Options.v, "apple", "fruitlist.txt").toStdOut();
```


---

<b>Some command usage examples:</b>
<br />
http://code.google.com/p/unix4j/source/browse/unix4j-core/unix4j-command/src/test/java/org/unix4j/builder/CommandBuilderTest.java

<b>API Javadoc:</b>
<br />
http://www.unix4j.org/javadoc/index.html

<b>A list of known issues</b>
<br />
http://code.google.com/p/unix4j/wiki/KnownIssues