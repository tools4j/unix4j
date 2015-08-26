Below are the builds that are configured on the unix4j hudson build server.

Server is located here: https://voodoo.homedns.org:8443/

# unix4j-build #
This is the main build which is triggered by git check-ins.  On build/test errors, the committer is notified via email.

# unix4j-examples-build\_from-maven-central #
This runs the test(s) in the unix4j-examples project against the latest release in the maven central repo.

It should be used just after publishing a release through sonatype to the central repo.

# unix4j-examples-build\_from-sonatype-snapshot #
This runs the test(s) in the unix4j-examples project against the latest SNAPSHOT release in the sonatype snapsbot repository.

# unix4j-examples-build\_from-sonatype-staging #
This runs the test(s) in the unix4j-examples project against the latest  release in the sonatype staging repository, which is temporarily created when Closing a staged release in sonatype.
This is a parameterized build, and requires the user to enter the unique staging URL provided by sonatype, along with the version being tested.  (Unfortunately we could not find a way of automatically obtaining these two parameters.)

# x-unix4j-deploy-snapshot-to-sonatype #
This build can be run to push a snapshot to the sonatype SNAPSHOT repository located at: https://oss.sonatype.org/content/repositories/snapshots.  After pushing a SNAPSHOT to the repo, the build "unix4j-examples-build\_from-sonatype-snapshot" can be run to test the deployed SNAPSHOT.  This is a fairly harmless command in that no versions are updated, and the SNAPSHOT can be pushed multiple times.

# x-unix4j-release-to-sonatype #
Once the development team is ready to push a release to the maven repo, this build can be run.

It runs the maven commands:
  * release:clean
  * release:prepare
  * release:perform

This will
  * Run tests
  * Roll the SNAPSHOT version to the non-SNAPSHOT version.  e.g. 0.3-SNAPSHOT to 0.3.
  * Commit the poms with the updated versions.
  * Tag the Git repository with the version.
  * Roll the non-SNAPSHOT version to the next SNAPSHOT version.  e.g. 0.3 -> 0.4-SNAPSHOT.

**_For full details about deploying snapshots, and pushing a staged release, go to https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide_**