import org.sourcegrade.fopbot.script.FOPBotPublishPlugin

plugins {
    `java-library`
    id("org.sourcegrade.style") version "2.1.0"
}

group = "org.tudalgo"
version = file("version").readLines().first()

apply<FOPBotPublishPlugin>()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
