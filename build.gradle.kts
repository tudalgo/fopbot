import org.jetbrains.dokka.gradle.DokkaTask
import org.sourcegrade.fopbot.script.FOPBotPublishPlugin

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    kotlin("jvm") version "1.9.0"
    `java-library`
    alias(libs.plugins.style)
    alias(libs.plugins.dokka)
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
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        jdkVersion.set(17)
    }
}
