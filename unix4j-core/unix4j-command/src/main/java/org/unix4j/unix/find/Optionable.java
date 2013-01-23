package org.unix4j.unix.find;

import org.unix4j.option.Option;

interface Optionable<O extends Option> {
	O getOption();
}
