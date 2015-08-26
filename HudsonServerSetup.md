# Introduction #

**Server is hosted at by Ben Warner (unix4j developer)**Server is located at DNS voodoo.homedns.org

# Files / Directories #
| /etc/init.d/hudson | Holds the hudson deamon details |
|:-------------------|:--------------------------------|
| /usr/java/jdk1.7.0\_07 | JDK                             |
| /var/lib/hudson/maven/slavebundle/bundled-maven | Hudson bundled Maven 3          |
| /var/lib/hudson/.gnupg | gpg public and private keys used for signing jars |


# Sonatype details #
| URL | https://issues.sonatype.org/secure/Dashboard.jspa |
|:----|:--------------------------------------------------|
| Username | benjwarner                                        |
| Password | yRMoo8fW                                          |


# Maven setup #
The following must be added to settings.xml to access the sonatype snapshots server.
```
  <servers>
    <server>
      <id>sonatype-nexus-snapshots</id>
      <username>benjwarner</username>
      <password>yRMoo8fW</password>
    </server>
    <server>
      <id>sonatype-nexus-staging</id>
      <username>benjwarner</username>
      <password>yRMoo8fW</password>
    </server>
  </servers>
```

# Jar signing #
See this reference https://docs.sonatype.org/display/Repository/How+To+Generate+PGP+Signatures+With+Maven

Here is the process for creating and publishing a public key

```
ben@voodoo-macbook ~/IdeaProjects/unix4j
$gpg --gen-key
gpg (GnuPG/MacGPG2) 2.0.18; Copyright (C) 2011 Free Software Foundation, Inc.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

Please select what kind of key you want:
   (1) RSA and RSA (default)
   (2) DSA and Elgamal
   (3) DSA (sign only)
   (4) RSA (sign only)
Your selection? 
RSA keys may be between 1024 and 4096 bits long.
What keysize do you want? (2048) 
Requested keysize is 2048 bits   
Please specify how long the key should be valid.
         0 = key does not expire
      <n>  = key expires in n days
      <n>w = key expires in n weeks
      <n>m = key expires in n months
      <n>y = key expires in n years
Key is valid for? (0) 
Key does not expire at all
Is this correct? (y/N) y
                        
GnuPG needs to construct a user ID to identify your key.

Real name: unix4j
Email address: bjwarner@gmail.com
Comment:                         
You selected this USER-ID:
    "unix4j <bjwarner@gmail.com>"

Change (N)ame, (C)omment, (E)mail or (O)kay/(Q)uit? o
You need a Passphrase to protect your secret key.    

Passphrase:
VlQMAj50

We need to generate a lot of random bytes. It is a good idea to perform
some other action (type on the keyboard, move the mouse, utilize the
disks) during the prime generation; this gives the random number
generator a better chance to gain enough entropy.
We need to generate a lot of random bytes. It is a good idea to perform
some other action (type on the keyboard, move the mouse, utilize the
disks) during the prime generation; this gives the random number
generator a better chance to gain enough entropy.
gpg: key A8A8A55A marked as ultimately trusted
public and secret key created and signed.

gpg: checking the trustdb
gpg: 3 marginal(s) needed, 1 complete(s) needed, PGP trust model
gpg: depth: 0  valid:   2  signed:   0  trust: 0-, 0q, 0n, 0m, 0f, 2u
gpg: next trustdb check due at 2015-08-18
pub   2048R/A8A8A55A 2013-01-15
      Key fingerprint = 7D36 5349 A638 56AA 2794  4767 A01B 9C37 A8A8 A55A
uid                  unix4j <bjwarner@gmail.com>
sub   2048R/9E856A44 2013-01-15


ben@voodoo-macbook ~/IdeaProjects/unix4j
$gpg --list-keys
/Users/ben/.gnupg/pubring.gpg
-----------------------------
pub   2048D/00D026C4 2010-08-19 [expires: 2015-08-18]
uid                  GPGTools Project Team (Official OpenPGP Key) <gpgtools-org@lists.gpgtools.org>
uid                  GPGMail Project Team (Official OpenPGP Key) <gpgmail-devel@lists.gpgmail.org>
sub   2048g/DBCBE671 2010-08-19 [expires: 2015-08-18]

pub   2048R/A8A8A55A 2013-01-15
uid                  unix4j <bjwarner@gmail.com>
sub   2048R/9E856A44 2013-01-15


ben@voodoo-macbook ~/IdeaProjects/unix4j
$gpg --list-secret-keys
/Users/ben/.gnupg/secring.gpg
-----------------------------
sec   2048R/A8A8A55A 2013-01-15
uid                  unix4j <bjwarner@gmail.com>
ssb   2048R/9E856A44 2013-01-15


ben@voodoo-macbook ~/IdeaProjects/unix4j
$gpg --keyserver hkp://pool.sks-keyservers.net --send-keys A8A8A55A
gpg: sending key A8A8A55A to hkp server pool.sks-keyservers.net
```