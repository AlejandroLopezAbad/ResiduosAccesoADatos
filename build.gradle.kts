import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
    kotlin("plugin.serialization") version "1.7.10"
}

group = "es.AR"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    // https://mavenlibs.com/maven/dependency/org.jetbrains.kotlin/kdoc
   // implementation ("org.jetbrains.kotlin:kdoc:0.12.1230")

    implementation("com.fasterxml.jackson.core:jackson-core:2.14.0-rc1")
    implementation("org.jdom:jdom:2.0.2")
    implementation("org.jetbrains.kotlinx:dataframe:0.8.1")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin:4.0.0")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:2.4.0")
    implementation("io.github.pdvrieze.xmlutil:core-jvm:0.84.3")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.84.3")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}