package org.unix4j.unix.ls;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import org.unix4j.util.StringUtil;

/**
 * Long format for java 7.
 */
/* NOTE: must be public for reflection */
public class LsFormatterLong7 extends LsFormatterLong {
	
	public LsFormatterLong7() {
		//force loading the java7 Files class here to provoke an error if it is a jdk < 7
		if (Files.class.getName().length() == 0) {
			throw new NoClassDefFoundError("internal error: " + getClass().getName());
		}
	}

	@Override
	protected String getOwner(File file, LsArgs args) {
		try {
			final String group = Files.getOwner(file.toPath()).getName();
			return StringUtil.fixSizeString(7, true, group);
		} catch (IOException e) {
			return super.getGroup(file, args);
		}
	}

	@Override
	protected String getGroup(File file, LsArgs args) {
		try {
			final PosixFileAttributeView view = Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class);
			final String group = view.readAttributes().group().getName();
			return StringUtil.fixSizeString(7, true, group);
		} catch (IOException e) {
			return super.getGroup(file, args);
		}
	}

	@Override
	protected String getFilePermissions(File file, LsArgs args) {
		try {
			final Set<PosixFilePermission> perms = Files.getPosixFilePermissions(file.toPath());
			return (file.isDirectory() ? "d" : "-") +
				(perms.contains(PosixFilePermission.OWNER_READ) ? 'r' : '-') + 
				(perms.contains(PosixFilePermission.OWNER_WRITE) ? 'w' : '-') + 
				(perms.contains(PosixFilePermission.OWNER_EXECUTE) ? 'x' : '-') + 
				(perms.contains(PosixFilePermission.GROUP_READ) ? 'r' : '-') + 
				(perms.contains(PosixFilePermission.GROUP_WRITE) ? 'w' : '-') + 
				(perms.contains(PosixFilePermission.GROUP_EXECUTE) ? 'x' : '-') + 
				(perms.contains(PosixFilePermission.OTHERS_READ) ? 'r' : '-') + 
				(perms.contains(PosixFilePermission.OTHERS_WRITE) ? 'w' : '-') + 
				(perms.contains(PosixFilePermission.OTHERS_EXECUTE) ? 'x' : '-');
		} catch (IOException e) {
			return super.getFilePermissions(file, args);
		}
	}
	
	@Override
	protected long getLastModifiedMS(File file, LsArgs args) {
		try {
			return Files.getLastModifiedTime(file.toPath()).toMillis();
		} catch (Exception e) {
			return super.getLastModifiedMS(file, args);
		}
	}
	
	@Override
	protected String getSize(File file, LsArgs args) {
		final long size;
		try {
			size = Files.size(file.toPath());
		} catch (Exception e) {
			return super.getSize(file, args);
		}
		final String sizeString = args.getSizeString(size);
		return StringUtil.fixSizeString(maxSizeStringLength.get(), false, sizeString);
	}
}
