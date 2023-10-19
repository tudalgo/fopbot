import java.net.URI

plugins {
    java
    `maven-publish`
}

extensions.configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
}

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            credentials {
                username = project.findProperty("sonatypeUsername") as? String
                password = project.findProperty("sonatypePassword") as? String
            }
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots"
            url = URI(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }
    publications.register<MavenPublication>("maven") {
        from(components["java"])
        pom {
            artifactId = "fopbot"
            name.set("FoPBot")
            description.set("A small environment with robot agents used for teaching programming basics")
            url.set("https://tudalgo.github.io/fopbot/")
            scm {
                url.set("https://github.com/tudalgo/fopbot")
                connection.set("scm:git:https://github.com/tudalgo/fopbot.git")
                developerConnection.set("scm:git:https://github.com/tudalgo/fopbot.git")
            }
            licenses {
                license {
                    name.set("The MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("lr32")
                }
                rootProject.file("authors").readLines()
                    .asSequence()
                    .map { it.split(",") }
                    .forEach { (_id, _name) -> developer { id.set(_id); name.set(_name) } }
            }
        }
    }
}
