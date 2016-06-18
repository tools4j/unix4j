package org.unix4j.util;


import java.util.ArrayList;
import java.util.List;

/**
 * Range offers ways of specifying a range of numbers.
 * <b>USAGE:</b><br/>
 * There are no public constructors in this object. The user should use one of the static factory
 * methods to create a Range.<br/>
 * e.g. The following will create a range of numbers from 1 to 5<br/>
 * <pre>Range.between(1,5);</pre>
 * <br/>
 * Chaining can be used to specify multiple range "elements"<br/>
 * e.g. The following will create a range of numbers from 1 to 5, and all numbers above 10<br/>
 * <pre>Range.between(1,5).andToEndFrom(10);</pre>
 */
public class Range{
	final CompositeRange compositeRange = new CompositeRange();

	private Range(){}
	private Range(final AbstractRange range){
		compositeRange.add(range);
	}

	/**
	 * @param index
	 * @return whether the given index is within this range
	 */
	public boolean isWithinRange(final int index) {
		return compositeRange.isWithinRange(index);
	}

	/**
	 * @param indexes a list of indexes to include in the range
	 * @return a range object consisting of this range, and all
	 * previous range elements in the chain so far.
	 */
	public Range andOf(final int... indexes) {
		compositeRange.add(Range.of(indexes));
		return this;
	}

	/**
	 * Adds a range element to include all indexes from 1 to and inclusive of the
	 * given index.
	 * @param to
	 * @return A range object consisting of this range, and all
	 * previous range elements in the chain so far.
	 */
	public Range andFromStartTo(final int to) {
		compositeRange.add(Range.fromStartTo(to));
		return this;
	}

	/**
	 * Adds a range element to include all indexes from (and including) the given index
	 * to any greater index.
	 * @param from
	 * @return A range object consisting of this range, and all
	 * previous range elements in the chain so far.
	 */
	public Range andToEndFrom(final int from) {
		compositeRange.add(Range.toEndFrom(from));
		return this;
	}

	/**
	 * Adds a range element to include all indexes between (and including) the given
	 * indexes.
	 * @param from
	 * @param to
	 * @return A range object consisting of this range, and all
	 * previous range elements in the chain so far.
	 */
	public Range andBetween(final int from, final int to) {
		compositeRange.add(Range.between(from, to));
		return this;
	}

	@Override
	public String toString() {
		return compositeRange.toString();
	}

	/**
	 * @param indexes a list of indexes to include in the range
	 * @return A new range object consisting of this range.
	 */
	public static Range of(int ... indexes){
		final Range range = new Range();
		for(int i: indexes){
			range.compositeRange.add(Range.includingSingleIndexOf(i));
		}
		return range;
	}

	/**
	 * Creates a range element to include all indexes from 1 to (and inclusive of) the
	 * given index.
	 * @param to
	 * @return A range object consisting of this range, and all
	 * previous range elements in the chain so far.
	 */
	public static Range fromStartTo(final int to){
		Assert.assertArgGreaterThan("index must be greater than zero", to, 0);
		return new Range(new AbstractRange(){
			@Override public String toString(){ return "-" + to; }
			@Override public boolean isWithinRange(int index){return index <= to;}
		});
	}

	/**
	 * Creates a range element to include all elements from (and including) the given
	 * index to any greater index.
	 * @param from
	 * @return A range object consisting of this range.
	 */
	public static Range toEndFrom(final int from){
		Assert.assertArgGreaterThan("index must be greater than zero", from, 0);
		return new Range(new AbstractRange(){
			private final int index = from;
			@Override public String toString(){ return index + "-";}
			@Override public boolean isWithinRange(int index){return index >= from;}
		});
	}

	/**
	 * Creates a range element to include all elements between (and including) the given
	 * indexes.
	 * @param from
	 * @param to
	 * @return A new range object consisting of this range.
	 */
	public static Range between(final int from, final int to){
		Assert.assertArgGreaterThan("index must be greater than zero", from, 0);
		return new Range(new AbstractRange(){
			@Override public String toString(){ return from + "-" + to;}
			@Override public boolean isWithinRange(int index){return from <= index && index <= to;}
		});
	}

	private static AbstractRange includingSingleIndexOf(final int singleIndex){
		Assert.assertArgGreaterThan("index must be greater than zero", singleIndex, 0);
		return new AbstractRange(){
			@Override public String toString(){ return "" + singleIndex;}
			@Override public boolean isWithinRange(int index){return index == singleIndex;}
		};
	}
}

abstract class AbstractRange {
	public abstract boolean isWithinRange(int index);
}

class CompositeRange extends AbstractRange {
	private List<AbstractRange> rangeElements = new ArrayList<AbstractRange>();

	public void add(final AbstractRange range){
		rangeElements.add(range);
	}

	public void add(final Range range){
		rangeElements.addAll(range.compositeRange.rangeElements);
	}

	public AbstractRange first() {
		return rangeElements.get(0);
	}

	public AbstractRange last() {
		return rangeElements.get(rangeElements.size() - 1);
	}

	@Override
	public boolean isWithinRange(int index){
		for(final AbstractRange range: rangeElements){
			if(range.isWithinRange(index)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (AbstractRange range : rangeElements) {
			if (sb.length() > 0) sb.append(",");
			sb.append(range);
		}
		return sb.toString();
	}
}





