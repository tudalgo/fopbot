import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.sourcegrade.fopbot.script.FOPBotPublishPlugin
import java.net.URL

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    kotlin("jvm") version "1.9.0"
    `java-library`
    alias(libs.plugins.style)
    alias(libs.plugins.dokka)
}

buildscript {
    dependencies {
        classpath(libs.dokkaBase)
    }
}

group = "org.tudalgo"
version = file("version").readLines().first()

apply<FOPBotPublishPlugin>()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.annotations)
    dokkaPlugin(libs.dokkaKotlinAsJavaPlugin)
    dokkaPlugin(libs.dokkaVersioningPlugin)
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set("FOPBot")
    dokkaSourceSets.configureEach {
        includes.from("Module.md")
        jdkVersion.set(17)
        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl.set(URL("https://github.com/tudalgo/fopbot/tree/master/src"))
            remoteLineSuffix.set("#L")
        }
        perPackageOption {
            reportUndocumented.set(true)
            documentedVisibilities.set(setOf(Visibility.PUBLIC, Visibility.PROTECTED))
        }
    }
}

tasks.dokkaHtml {
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customAssets = listOf(file("logo-icon.svg"))
        footerMessage = "(c) 2023 tudalgo.org"
    }
}
