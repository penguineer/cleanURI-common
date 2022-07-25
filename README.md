# cleanURI - Common

> Common definitions and functions for the cleanURI service.


## Deployment

### Including the library from GitHub

The library is hosted on GitHub and access is set up for projects using it.

GitHub packages require authentication. Please refer to 
[Working with the Apache Maven registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
for the proper setup.

In the POM.xml, two sections must be included.

The GitHub repository:
```xml
  <repositories>
    <!-- link to maven central here -->
    <repository>
      <id>github</id>
      <name>GitHub penguineer Apache Maven Packages</name>
      <url>https://maven.pkg.github.com/penguineer/maven-packages</url>
    </repository>
  </repositories>
```

The dependency:
```xml
<dependency>
    <groupId>com.penguineering.cleanuri</groupId>
    <artifactId>common</artifactId>
    <version>version</version>
    <scope>compile</scope>
</dependency>
```
Replace the `version` tag with the desired release.


This method is used by the GitHub actions that create Docker Images of the various cleanURI components.

### Including the library from Local installation

You can check out the library as a local working copy and then build and install with:
```bash
mvn build package
mnv install
```

Only the dependency needs to be added to your POM:
```xml
<dependency>
    <groupId>com.penguineering.cleanuri</groupId>
    <artifactId>common</artifactId>
    <version>version</version>
    <scope>compile</scope>
</dependency>
```
Replace the `version` tag with the desired release.

### Development

This project uses the [Micronaut Framework](https://micronaut.io/).

Version numbers are determined with [jgitver](https://jgitver.github.io/).
Please check your [IDE settings](https://jgitver.github.io/#_ides_usage) to avoid problems, as there are still some unresolved issues.
If you encounter a project version `0` there is an issue with the jgitver generator.

If access to the GitHub packages repository is not available, please check *Including the library from Local installation* on how to make the artifact available locally. 


## Maintainers

* Stefan Haun ([@penguineer](https://github.com/penguineer))


## Contributing

PRs are welcome!

If possible, please stick to the following guidelines:

* Keep PRs reasonably small and their scope limited to a feature or module within the code.
* If a large change is planned, it is best to open a feature request issue first, then link subsequent PRs to this issue, so that the PRs move the code towards the intended feature.


## License

[MIT](LICENSE.txt) © 2022 Stefan Haun and contributors
