plugins {
  `java-library`
  `maven-publish`
}

group = "org.sourcegrade"
version = "0.1.0-SNAPSHOT"

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
      artifactId = "fopbot"
    }
  }
}

java {
  withSourcesJar()
  withJavadocJar()
}
