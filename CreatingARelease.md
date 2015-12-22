# Setup Required for doing a Release #
  1. Ensure you can deploy to the Maven Central repository from your workspace, see: [Deploying via Nexus Guide](http://central.sonatype.org/pages/ossrh-guide.html) for details.

# Preparing the Release #
  1. Checkout the latest version of aic-expresso into a clean workspace.
  1. Ensure all tests pass and there are no compiler warnings.
  1. Ensure the version number in the pom.xml file is updated appropriately.
  1. Commit changes to Subversion

# Publish the Release #
  1. Run the following command from your aic-expresso workspace directory:
```
mvn -P sonatype-oss-release deploy -DskipTests=true
```
> > // **Note:** the -DskipTests is used to bypass running the integration/stress tests.<br>
<blockquote>// <b>Note:</b> if you would also like to skip the unit tests (not recommended) add the flag -DskipUnitTests as well, e.g.:<br>
<pre><code>mvn -P sonatype-oss-release deploy -DskipTests=true -DskipUnitTests=true<br>
</code></pre>
</blockquote><ol><li>Close the Staged artifacts and then Release them to the Maven Central Repository, by following the Releasing to Central section from <a href='http://central.sonatype.org/pages/ossrh-guide.html'>the guide for deploying via Nexus</a> from the <a href='https://oss.sonatype.org/'>Nexus Server</a>
</li><li>Tag the release under Subversion.<br>
</li><li>Create a release page for the released version and add to the <a href='ReleaseHistory.md'>release history wiki page</a> (in reverse chronological order).<br>
</li><li>Ensure the Wiki Pages relating to usage are updated to take into account any changes to the APIs.<br>
</li><li><a href='DeployingDemoApp.md'>Deploy an updated demo application</a> if necessary.</li></ol>

<h1>Useful Resources</h1>
<ul><li><a href='https://oss.sonatype.org/'>Sonatype/Nexus Open Source Repository</a>
</li><li><a href='http://search.maven.org/'>The Maven Central Repository</a>
</li><li><a href='http://www.sonatype.com/books/mvnref-book/reference/public-book.html'>The Complete Maven Reference</a>