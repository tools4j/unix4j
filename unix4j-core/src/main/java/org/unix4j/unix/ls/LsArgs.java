package org.unix4j.unix.ls;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.unix4j.command.AbstractArgs;
import org.unix4j.unix.Ls.Option;
import org.unix4j.util.FileUtil;
import org.unix4j.util.TypedMap;

/**
 * Arguments and options for the ls command.
 */
class LsArgs extends AbstractArgs<Option, LsArgs> {
	public static final TypedMap.Key<List<File>> FILES = TypedMap.DefaultKey.keyForListOf("files", File.class);

	public LsArgs() {
		this(FileUtil.getUserDir());
	}

	public LsArgs(File file) {
		this(Collections.singletonList(file));
	}

	public LsArgs(File... files) {
		this(Arrays.asList(files));
	}
	
	public LsArgs(String... files) {
		this(toFile(files));
	}

	private static File[] toFile(String... files) {
		final File[] f = new File[files.length];
		for (int i = 0; i < f.length; i++) {
			f[i] = new File(files[i]);
		}
		return f;
	}

	public LsArgs(List<File> files) {
		super(Option.class);
		setArg(FILES, files);
	}

	public List<File> getFiles() {
		return getArg(FILES);
	}

	@Override
	public boolean hasOpt(Option opt) {
		return opt.isSet(getOpts());
	}

	public String getSizeString(long bytes) {
		if (hasOpt(Option.humanReadable)) {
			final String units = "BKMG";
			int unit = 0;
			int fraction = 0;
			while (bytes > 1000 && (unit + 1) < units.length()) {
				bytes /= 100;
				fraction = (int) (bytes % 10);
				bytes /= 10;
				unit++;
			}
			if (bytes < 10) {
				return bytes + "." + fraction + units.charAt(unit);
			} else {
				return (bytes < 100 ? " " : "") + bytes + units.charAt(unit);
			}
		}
		return String.valueOf(bytes);
	}

}