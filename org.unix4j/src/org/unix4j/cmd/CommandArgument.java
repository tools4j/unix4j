package org.unix4j.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandArgument<O extends Enum<O>> implements Cloneable  {
	private O option;
	private List<String> values;

	public CommandArgument(O option, String value) {
		super();
		this.option = option;
		this.values = new ArrayList<String>();
		values.add(value);
	}

	public List<String> getValues() {
		return values;
	}

	public O getOption() {
		return option;
	}

	public String joinValues(final String delimiter){
		final Iterator<String> i = values.iterator();
		final StringBuffer sb = new StringBuffer();
		while(i.hasNext()){
			sb.append(i.next());
			if( i.hasNext()) sb.append(delimiter);
		}
		return sb.toString();
	}

	@Override
	public String toString(){
		final StringBuffer sb = new StringBuffer(option.toString());
		final Iterator<String> i = values.iterator();

		if(i.hasNext()){
			sb.append("=");
		}

		while(i.hasNext()){
			sb.append(i.next());
			if(i.hasNext()){
				sb.append(",");
			}
		}
		return sb.toString();
	}

	@Override
	public CommandArgument<O> clone() {
		try {
			@SuppressWarnings("unchecked")
			final CommandArgument<O> clone = (CommandArgument<O>) super.clone();
			clone.option = this.option;
			clone.values = new ArrayList<String>(values);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable: " + getClass().getName());
		}
	}
}
