#!/bin/bash

(cd default.input && find .) > find_currentDir.output
(cd default.input && find ./) > find_currentDirWithDotSlash.output
(cd default.input/folder && find ../) > find_parentPath.output
(cd default.input/mydir/SUBDIR && find ../../) > find_parentOfParentPath.output
(cd default.input/mydir && find ../mydir) > find_parentBackToChild.output

(cd default.input && find mydir) > find_specifiedDir.output
(cd default.input && find mydir/SUBDIR) > find_specifiedSubDir.output
(cd default.input && find ./mydir) > find_specifiedDirWithDotSlash.output
(cd default.input && find ./mydir/SUBDIR) > find_specifiedSubDirWithDotSlash.output

(find /dev/null) > find_absoluteDir.output

(cd default.input && find . -type f) > find_typeFile.output
(cd default.input && find . -type d) > find_typeDir.output

(cd default.input && find . -type f -name a.txt) > find_typeFileWithName.output
(cd default.input && find . -name bb.*) > find_withNameWildcard.output
(cd default.input && find . -name bb.t*) > find_withNameWildcard2.output

(cd default.input && find . -type f -iname bb.txt) > find_typeFileWithNameIgnoreCase.output
(cd default.input && find . -iname bb.t*) > find_withNameWildcardIgnoreCae.output

(cd default.input && find -E . -regex "a.*\.txt") > find_regexWithNoMatchesAsIsMatchingWholePath.output
(cd default.input && find -E . -regex ".*a\.txt") > find_regexSimple.output

(cd default.input && find . -type f -size +1k) > find_sizesLargerThanOneK.output
(cd default.input && find . -type f -size -1k) > find_sizesSmallerThanOneK.output
(cd default.input && find . -type f -name a.* -size -1k) > find_sizesSmallerThanOneKWithGivenName.output







