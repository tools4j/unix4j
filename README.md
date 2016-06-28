[![Build Status](https://travis-ci.org/tools4j/unix4j.svg?branch=master)](https://travis-ci.org/tools4j/unix4j)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.unix4j/unix4j-command/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.unix4j/unix4j-command)

<b>Unix4j</b> is an implementation of Unix command line tools in Java. You can use the commands that you know from Unix in a Java program---you can pipe the results of one command to another as you know it from Unix.

Unix4j gives you the power of unix commands, e.g. text processing, file management, with the benefits that come from a strongly typed and testable language such as Java.

In Unix, you might see a command such as:
```java
cat test.txt | grep "Tuesday" | sed "s/kilogram/kg/g" | sort 
```

In Unix4j this would look like:
```java
Unix4j.cat("test.txt").grep("Tuesday").sed("s/kilogram/kg/g").sort();
```

Currently supported commands are: 
[cat](http://www.unix4j.org/javadoc/org/unix4j/unix/Cat.html),
[cd](http://www.unix4j.org/javadoc/org/unix4j/unix/Cd.html),
[cut](http://www.unix4j.org/javadoc/org/unix4j/unix/Cut.html),
[echo](http://www.unix4j.org/javadoc/org/unix4j/unix/Echo.html),
[find](http://www.unix4j.org/javadoc/org/unix4j/unix/Find.html),
[grep](http://www.unix4j.org/javadoc/org/unix4j/unix/Grep.html),
[head](http://www.unix4j.org/javadoc/org/unix4j/unix/Head.html),
[ls](http://www.unix4j.org/javadoc/org/unix4j/unix/Ls.html),
[sed](http://www.unix4j.org/javadoc/org/unix4j/unix/Sed.html),
[sort](http://www.unix4j.org/javadoc/org/unix4j/unix/Sort.html),
[tail](http://www.unix4j.org/javadoc/org/unix4j/unix/Tail.html),
[uniq](http://www.unix4j.org/javadoc/org/unix4j/unix/Uniq.html),
[wc](http://www.unix4j.org/javadoc/org/unix4j/unix/Wc.html),
[xargs](http://www.unix4j.org/javadoc/org/unix4j/unix/Xargs.html).

## Maven ##
```xml
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
```java
String result = Unix4j.fromString("hello world").grep("hello").wc().toStringResult();
```
Providing such method chaining whilst still keeping a clean and well structured code base has been a real challenge.  It may have increased the complexity of the code base in some instances.  But we feel that Unix4j <b>must</b> be easy to use, and must result in code which is concise and easy to read.

## Remaining true to the behaviour of the emulated Unix commands ##
Where possible we have tried to stick as closely to the behaviour of the original command as much as possible.  Options and argument names have also been aligned with those in the original Unix commands.

Examples:
```java
//list all files and directories in the long listing format (unix: ls -la)
Unix4j.ls(Ls.Options.l.a).toStdOut();

//print all lines of the file "fruitlist.txt" that are not containing "apple" (unix: grep -v apple fruitlist.txt)
Unix4j.grep(Grep.Options.v, "apple", "fruitlist.txt").toStdOut();
```


---

##### Some command usage examples
[CommandBuilderTest.java](https://github.com/tools4j/unix4j/blob/master/unix4j-core/unix4j-command/src/test/java/org/unix4j/builder/CommandBuilderTest.java)

[ExampleTest.java](https://github.com/tools4j/unix4j/blob/master/unix4j-examples/src/test/java/org/unix4j/example/ExampleTest.java)

##### FAQ
[Questions & Answers](https://github.com/tools4j/unix4j/issues?utf8=%E2%9C%93&q=is%3Aissue+label%3Aquestion)

##### API Javadoc
http://www.unix4j.org/javadoc/index.html
