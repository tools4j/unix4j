package org.unix4j.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.unix4j.Input;

public class ReaderInput implements Input {
	
	private final String[] nextLine = new String[1]; 
	private final BufferedReader reader;
	
	public ReaderInput(Reader reader) {
		this.reader = reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
		try {
			this.nextLine[0] = this.reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasMoreLines() {
		return nextLine[0] != null;
	}

	@Override
	public String readLine() {
		String next = nextLine[0];
		if (next != null) {
			try {
				nextLine[0] = reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return next;
	}

}
