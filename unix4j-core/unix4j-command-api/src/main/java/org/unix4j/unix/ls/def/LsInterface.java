package org.unix4j.unix.ls.def;

import java.io.File;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.codegen.annotation.Options;
import org.unix4j.command.Command;
import org.unix4j.io.Output;

/**
 * <b>NAME</b>
 * <p>
 * ls - list directory contents
 * </p>
 * <b>SYNOPSIS</b>
 * <p>
 * <pre>
 * ls [-ahlRrt] [file...]
 * </pre>
 * </p>
 * <b>DESCRIPTION</b>
 * <p>
 * For each operand that names a file of a type other than directory or symbolic
 * link to a directory, {@code ls} writes the name of the file as well as any
 * requested, associated information. For each operand that names a file of type
 * directory, {@code ls} writes the names of files contained within the
 * directory as well as any requested, associated information. If the <b>-l</b>
 * option is specified, for each operand that names a file of type symbolic link
 * to a directory, {@code ls} writes the name of the file as well as any
 * requested, associated information. If the <b>-l</b> option is not specified,
 * for each operand that names a file of type symbolic link to a directory,
 * {@code ls} writes the names of files contained within the directory as well
 * as any requested, associated information.
 * </p><p>
 * If no operands are specified, {@code ls} writes the contents of the current
 * directory. If more than one operand is specified, {@code ls} writes
 * non-directory operands first; it sorts directory and non-directory operands
 * separately according to the name of the file or directory.
 * </p><p>
 * TODO The {@code ls} utility detects infinite loops; that is, entering a
 * previously visited directory that is an ancestor of the last file
 * encountered. When it detects an infinite loop, {@code ls} aborts the
 * recursion.
 * </p>
 * <b>NOTES</b>
 * <p>
 * <ul>
 * <li>Infinite loop detection is currently NOT implemented.</li>
 * </ul>
 * </p>
 * <b>OPTIONS</b>
 * <p>
 * The following options are supported:
 * </p><p>
 * <table>
 * <tr><td><pre>-a  --allFiles        </pre></td><td>Lists all files in the given directory, including those whose names start with "." (which are hidden files in Unix). By default, these files are excluded from the list.</td></tr>
 * <tr><td><pre>-h  --humanReadable   </pre></td><td>Print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.).</td></tr>
 * <tr><td><pre>-l  --longFormat      </pre></td><td>Long format, displaying Unix file types, permissions, number of hard links, owner, group, size, date, and filename.</td></tr>
 * <tr><td><pre>-r  --reverseOrder    </pre></td><td>Reverses the order of the sort to get reverse collating sequence or oldest first.</td></tr>
 * <tr><td><pre>-R  --recurseSubdirs  </pre></td><td>Recursively lists subdirectories encountered.</td></tr>
 * <tr><td><pre>-t  --timeSorted      </pre></td><td>Sort the list of files by last modification time.</td></tr>
 * </table>
 * </p>
 * <b>OPERANDS</b>
 * </p><p>
 * The following operand is supported:
 * <table>
 * <tr><td><pre>   file   </pre></td><td>A pathname of a file to be written. Wildcards * and ? are
 * supported</td></tr>
 * </table>
 * 
 * @param <R>
 *            the return type for all command signature methods, usually a
 *            new command instance or a command fromFile providing methods
 *            for chained invocation of following commands
 */
@Options(LsOption.class)
public interface LsInterface<R> {

	/**
	 * The "ls" command name.
	 */
	String NAME = "ls";

	/**
	 * Lists all files and directories in the user's current working directory
	 * and writes them to the output.
	 * 
	 * @return the generic type {@code <R>} defined by the implementing class,
	 *         even if the command itself returns no value and writes its result
	 *         to an {@link Output} object. This serves implementing classes
	 *         like the command {@link LsFactory} to return a new
	 *         {@link Command} instance for the argument values passed to this
	 *         method. {@link CommandBuilder} extensions also implementing this
	 *         this command interface usually return an instance to itself
	 *         facilitating chained invocation of joined commands.
	 */
	R ls();

	/**
	 * Prints the name of the given files and lists all files contained in
	 * directories for every directory in {@code files}.
	 * 
	 * @param files
	 *            the files or directories used as starting point for the
	 *            listing
	 * @return the generic type {@code <R>} defined by the implementing class,
	 *         even if the command itself returns no value and writes its result
	 *         to an {@link Output} object. This serves implementing classes
	 *         like the command {@link LsFactory} to return a new
	 *         {@link Command} instance for the argument values passed to this
	 *         method. {@link CommandBuilder} extensions also implementing this
	 *         this command interface usually return an instance to itself
	 *         facilitating chained invocation of joined commands.
	 */
	R ls(File... files);

	/**
	 * Prints the name of the given files and lists all files contained in
	 * directories for every directory in {@code files}.
	 * 
	 * @param files
	 *            the files or directories used as starting point for the
	 *            listing
	 * @return the generic type {@code <R>} defined by the implementing class,
	 *         even if the command itself returns no value and writes its result
	 *         to an {@link Output} object. This serves implementing classes
	 *         like the command {@link LsFactory} to return a new
	 *         {@link Command} instance for the argument values passed to this
	 *         method. {@link CommandBuilder} extensions also implementing this
	 *         this command interface usually return an instance to itself
	 *         facilitating chained invocation of joined commands.
	 */
	R ls(String... files);

	/**
	 * Lists all files and directories in the user's current working directory
	 * and writes them to the output using the given options specifying the
	 * details of the output format.
	 * 
	 * @param options
	 *            the options defining the output format
	 * @return the generic type {@code <R>} defined by the implementing class,
	 *         even if the command itself returns no value and writes its result
	 *         to an {@link Output} object. This serves implementing classes
	 *         like the command {@link LsFactory} to return a new
	 *         {@link Command} instance for the argument values passed to this
	 *         method. {@link CommandBuilder} extensions also implementing this
	 *         this command interface usually return an instance to itself
	 *         facilitating chained invocation of joined commands.
	 */
	R ls(LsOptions options);

	/**
	 * Prints the name of the given files and lists all files contained in
	 * directories for every directory in {@code files}. The given options
	 * define the details of the output format.
	 * 
	 * @param options
	 *            the options defining the output format
	 * @param files
	 *            the files or directories used as starting point for the
	 *            listing
	 * @return the generic type {@code <R>} defined by the implementing class,
	 *         even if the command itself returns no value and writes its result
	 *         to an {@link Output} object. This serves implementing classes
	 *         like the command {@link LsFactory} to return a new
	 *         {@link Command} instance for the argument values passed to this
	 *         method. {@link CommandBuilder} extensions also implementing this
	 *         this command interface usually return an instance to itself
	 *         facilitating chained invocation of joined commands.
	 */
	R ls(LsOptions options, File... files);

	/**
	 * Prints the name of the given files and lists all files contained in
	 * directories for every directory in {@code files}. The given options
	 * define the details of the output format.
	 * 
	 * @param options
	 *            the options defining the output format
	 * @param files
	 *            the files or directories used as starting point for the
	 *            listing
	 * @return the generic type {@code <R>} defined by the implementing class,
	 *         even if the command itself returns no value and writes its result
	 *         to an {@link Output} object. This serves implementing classes
	 *         like the command {@link LsFactory} to return a new
	 *         {@link Command} instance for the argument values passed to this
	 *         method. {@link CommandBuilder} extensions also implementing this
	 *         this command interface usually return an instance to itself
	 *         facilitating chained invocation of joined commands.
	 */
	R ls(LsOptions options, String... files);
}
