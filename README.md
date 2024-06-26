<div align="center">
    <img src="logo-icon.svg" align="float" width="25%" alt="jagr logo">
    <h1>FoPBot</h1>
</div>

A small environment with robot agents used for teaching programming basics.

You can finde the documentation [here](https://tudalgo.github.io/fopbot/index.html).

# Setup
## Release
This project is available on Maven Central. You can use it by adding the following code to your `build.gradle.kts` file:
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.tudalgo.fopbot:<version>")
}
```
## Snapshot
The snapshot version is available on Sonatype. You can use it by adding the following code to your `build.gradle.kts` file:
```kotlin
repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("org.tudalgo.fopbot:<version>-SNAPSHOT")
}
```
## Build from source
To build the project from source, you can execute the `publishToMavenLocal` task. This will build the project and publish it to your local maven repository.
```shell
./gradlew publishToMavenLocal
```
After that, you can use the project in your other projects by adding the following code to your `build.gradle.kts` file:
```kotlin
repositories {
    mavenLocal()
}

dependencies {
    implementation("org.tudalgo.fopbot:<version>")
}
```
