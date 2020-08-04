import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("application")
    id("kotlin")
}

application {
    mainClassName = "de.stuermerbenjamin.facegallery.ApplicationKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation(project(":shared"))

    // persistence
    implementation("org.jetbrains.exposed:exposed-core:0.24.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.24.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.24.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.24.1")
    implementation("mysql:mysql-connector-java:8.0.20")
    implementation("com.h2database:h2:1.4.200")
    implementation("org.xerial:sqlite-jdbc:3.21.0.1")

    // tensorflow
    implementation("org.tensorflow:tensorflow:1.15.0")

    // openCV
    implementation("org.openpnp:opencv:3.4.2-0")
}
