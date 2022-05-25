import org.sourcegrade.fopbot.script.FOPBotPublishPlugin

plugins {
    `java-library`
    id("org.sourcegrade.style") version "2.0.0"
}

group = "org.sourcegrade"
version = "0.3.1-HEADLESS"

apply<FOPBotPublishPlugin>()

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
