plugins {
  `java-library`
  id("net.kyori.indra") version "2.0.6"
  id("net.kyori.indra.publishing.sonatype") version "2.0.6"
  signing
}

group = "org.sourcegrade"
version = "0.1.0-SNAPSHOT"

signing {
  useGpgCmd()
  sign(configurations.archives.get())
}
indra {
  javaVersions {
    target(11)
  }
  mitLicense()
  github("FOP-2022", "FOPBot")
  configurePublications {
    pom {
      artifactId = "fopbot"
      description.set("A small environment with robot agents used for teaching programming basics")
      developers {
        developer {
          id.set("alexstaeding")
          name.set("Alexander Staeding")
        }
      }
    }
  }
}

indraSonatype {
  useAlternateSonatypeOSSHost("s01")
}
