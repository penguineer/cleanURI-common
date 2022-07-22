# cleanURI - Common

> Common definitions and functions for the cleanURI service.

## Include library

The library can be included with [jitpack](https://jitpack.io/) with two additions to the project's POM file. 

Adding the jitpack repository:
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Adding the library:
```xml
<dependency>
  <groupId>com.github.penguineer</groupId>
  <artifactId>cleanURI-common</artifactId>
  <version>main-HEAD</version>
</dependency>
```
Replace the `version` tag with the desired release.

Please refer to the [jitpack documentation](https://jitpack.io/) on using alternative build systems.


## Deployment

### Development

This project uses the [Micronaut Framework](https://micronaut.io/).

Version numbers are determined with [jgitver](https://jgitver.github.io/).
Please check your [IDE settings](https://jgitver.github.io/#_ides_usage) to avoid problems, as there are still some unresolved issues.
If you encounter a project version `0` there is an issue with the jgitver generator.


## Maintainers

* Stefan Haun ([@penguineer](https://github.com/penguineer))


## Contributing

PRs are welcome!

If possible, please stick to the following guidelines:

* Keep PRs reasonably small and their scope limited to a feature or module within the code.
* If a large change is planned, it is best to open a feature request issue first, then link subsequent PRs to this issue, so that the PRs move the code towards the intended feature.


## License

[MIT](LICENSE.txt) © 2022 Stefan Haun and contributors
