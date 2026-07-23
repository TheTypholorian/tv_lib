plugins {
    kotlin("jvm") version "2.4.0"
}

group = "net.typho"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    jvmToolchain(8)
}

tasks.test {
    useJUnitPlatform()
}