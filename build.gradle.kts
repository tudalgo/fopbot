import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URI

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    alias(libs.plugins.style)
    alias(libs.plugins.dokka)
    id("fopbot-publish")
    id("fopbot-sign")
}

buildscript {
    dependencies {
        classpath(libs.dokkaBase)
    }
}

group = "org.tudalgo"
version = file("version").readLines().first()

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.slf4jSimple)
    implementation(libs.annotations)
    implementation(libs.batik)
    implementation(libs.imageio.batik)
    implementation(libs.jSystemThemeDetector)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    dokkaPlugin(libs.dokkaKotlinAsJavaPlugin)
    dokkaPlugin(libs.dokkaVersioningPlugin)
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set("fopbot")
    dokkaSourceSets.configureEach {
        includes.from("Module.md")
        jdkVersion.set(21)
        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl.set(URI("https://github.com/tudalgo/fopbot/tree/master/src").toURL())
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
