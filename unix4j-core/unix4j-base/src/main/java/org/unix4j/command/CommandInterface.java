package org.unix4j.command;

import org.unix4j.builder.GenericCommandBuilder;

/**
 * A command interface defines the different ways a certain {@link Command} can
 * be invoked (or instantiated). It consists of the different method signatures
 * for the command.
 * <p>
 * Consider for instance the following simplified {@code ls} command. It can be
 * called without options, with a file, with option flags or both a file and
 * options. The command interface for {@code ls} would therefore define four
 * methods:
 * 
 * <pre>
 * R ls()
 * R ls(File file)
 * R ls(Ls.Option... options)
 * R ls(File file, Ls.Option... options)
 * </pre>
 * 
 * Note that all command methods in the interface usually return the generic
 * type {@code R}. Command factories (see {@code FACTORY} constant of a specific
 * command} return a new command instance. Command builders implementing many
 * different command interfaces return an instance to itself (the BUILDER) to
 * allow for method chaining, for instance when creating a joined command.
 * <p>
 * This interface does not define any methods since all methods are defined by
 * the concrete command. The interface serves as a marker and documentation
 * interface. Theoretically, a command interface is not required to extend this
 * interface, but it is highly recommended. Also, if a command factory is used
 * with {@link GenericCommandBuilder}, it must implement this interface.
 * 
 * @param <R>
 *            the return type for all command signature methods, usually a new
 *            command instance or a command fromFile providing methods for
 *            chained invocation of following commands
 */
public interface CommandInterface<R> {
	// interface defines no methods as they are all defined by the command
	// interfaces being sub-interfaces of this class
}
