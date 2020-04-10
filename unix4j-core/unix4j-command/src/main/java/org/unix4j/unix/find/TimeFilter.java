package org.unix4j.unix.find;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

/**
 * File filter based on last modified, last accessed or created time of the
 * file.
 */
class TimeFilter implements FileFilter {
	public static enum TimeType implements Optionable<FindOption> {
		Create(FindOption.timeCreate) {
			@Override
			public long getTime(File file) {
				return FileAttributes.getCreationTime(file);
			}
		},
		Access(FindOption.timeAccess) {
			@Override
			public long getTime(File file) {
				return FileAttributes.getLastAccessedTime(file);
			}
		},
		Modified(FindOption.timeModified) {
			@Override
			public long getTime(File file) {
				return FileAttributes.getLastModifiedTime(file);
			}
		};
		abstract public long getTime(File file);

		private final FindOption option;

		private TimeType(FindOption option) {
			this.option = option;
		}

		@Override
		public FindOption getOption() {
			return option;
		}
	}

	public static enum TimeComparator implements Optionable<FindOption> {
		Older(FindOption.timeOlder) {
			@Override
			public boolean accept(File file, TimeType timeType, Date time) {
				return timeType.getTime(file) <= time.getTime();
			}
		},
		Newer(FindOption.timeNewer) {
			@Override
			public boolean accept(File file, TimeType timeType, Date time) {
				return timeType.getTime(file) >= time.getTime();
			}
		};
		abstract public boolean accept(File file, TimeType timeType, Date time);

		private final FindOption option;

		private TimeComparator(FindOption option) {
			this.option = option;
		}

		@Override
		public FindOption getOption() {
			return option;
		}
	}

	private final Date time;
	private final TimeType timeType;;
	private final TimeComparator comparator;

	public TimeFilter(Date time, FindOptions options) {
		this(time,
				OptionableUtil.findFirstEnumByOptionInSet(TimeType.class, options, TimeType.Modified),
				OptionableUtil.findFirstEnumByOptionInSet(TimeComparator.class, options, TimeComparator.Newer));
	}

	public TimeFilter(Date time, TimeType timeType, TimeComparator comparator) {
		this.time = time;
		this.timeType = timeType;
		this.comparator = comparator;
	}

	@Override
	public boolean accept(File file) {
		return comparator.accept(file, timeType, time);
	}

}
