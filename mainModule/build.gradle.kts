plugins {
    kotlin("jvm") version "1.0.0"
}

group = "ru.honfate.upgrowth"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}
dependencies {
    implementation(kotlin("stdlib"))
}
repositories {
    mavenCentral()
}