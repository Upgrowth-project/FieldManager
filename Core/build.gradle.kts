import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
}

group = "ru.honfate.upgrowth"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test-junit"))
}

repositories {
    jcenter()
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}