/**
 * Contains {@link org.unix4j.line.Line Line} and 
 * {@link org.unix4j.line.LineProcessor LineProcessor} defining the basic 
 * interfaces for line-by-line input processing. 
 * <p>
 * The package also contains two alternative implementations for the 
 * {@code Line} interface:
 * <ul>
 * <li>{@link org.unix4j.line.SimpleLine SimpleLine} which is based on two 
 * strings or character sequences: one for the line <i>contents</i> and one for
 * the line <i>ending</i>.
 * </li>
 * <li>{@link org.unix4j.line.SingleCharSequenceLine SingleCharSequenceLine}: a
 * line based on a single string or character sequence. The sequence starts
 * with the line <i>contents</i> and is terminated with some line <i>ending</i>
 * character(s).
 * </li>
 * </ul>
 */
package org.unix4j.line;