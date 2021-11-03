/*
 *   Jagr - SourceGrade.org
 *   Copyright (C) 2021 Alexander Staeding
 *   Copyright (C) 2021 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
      sign(extensions.getByType<PublishingExtension>().publications)
    }
  }
}
