import org.sourcegrade.fopbot.script.FOPBotPublishPlugin

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    `java-library`
    alias(libs.plugins.style)
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
}
