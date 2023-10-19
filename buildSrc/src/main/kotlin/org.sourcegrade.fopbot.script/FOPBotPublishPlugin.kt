package org.sourcegrade.fopbot.script

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import java.net.URI

class FOPBotPublishPlugin : Plugin<Project> {
  override fun apply(target: Project) = target.afterEvaluate { configure() }
  private fun Project.configure() {
    apply<JavaBasePlugin>()
    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()
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
      publications {
        create<MavenPublication>("maven") {
          from(components["java"])
          pom {
            artifactId = "fopbot"
            name.set("FOPBot")
            description.set("A small environment with robot agents used for teaching programming basics")
            url.set("https://www.sourcegrade.org")
            scm {
              url.set("https://github.com/FOP-2022/FOPBot")
              connection.set("scm:git:https://github.com/FOP-2022/FOPBot.git")
              developerConnection.set("scm:git:https://github.com/FOP-2022/FOPBot.git")
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
              developer {
                id.set("alexstaeding")
                name.set("Alexander Staeding")
              }
            }
          }
        }
      }
    }
    extensions.configure<SigningExtension> {
      //sign(extensions.getByType<PublishingExtension>().publications)
    }
  }
}
