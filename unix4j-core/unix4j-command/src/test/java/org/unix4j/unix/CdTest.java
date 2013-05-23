package org.unix4j.unix;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.unix4j.Unix4j;

public class CdTest {

	@Test
	public void testCd() {
		final String userName = System.getProperty("user.name");
		final String userHome = System.getProperty("user.home");
		final String userDir = System.getProperty("user.dir");
		
		Assert.assertEquals(userHome, Unix4j.echo("$user.home").toStringResult());
		Assert.assertEquals(userDir, Unix4j.echo("$user.dir").toStringResult());
		Assert.assertEquals(userDir, Unix4j.echo("$pwd").toStringResult());
		Assert.assertEquals(userName, Unix4j.echo("$whoami").toStringResult());
		
		//now with cd
		Assert.assertEquals(userHome, Unix4j.cd(userHome).echo("$pwd").toStringResult());
		Assert.assertEquals(userHome, Unix4j.cd(new File(userHome)).echo("$pwd").toStringResult());
		Assert.assertEquals(userHome, Unix4j.cd().echo("$pwd").toStringResult());
		Assert.assertEquals(userHome, Unix4j.cd("$user.home").echo("$pwd").toStringResult());
	}

}
