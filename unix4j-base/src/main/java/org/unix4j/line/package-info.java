/**
 * Contains the {@link org.unix4j.line.Line Line} interface used for 
 * line-by-line processing of input data and provides two alternative line 
 * implementations. 
 * <p>
 * The two variants implementing the {@code Line} interface are:
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