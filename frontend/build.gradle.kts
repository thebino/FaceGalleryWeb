import org.jetbrains.kotlin.config.KotlinCompilerVersion

val kotlin_version: String by project
val ktor_version: String by project
val logback_version: String by project

plugins {
    id("application")
    id("kotlin")
}

application {
    mainClassName = "de.stuermerbenjamin.facegallery.frontend.ApplicationKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    implementation(project(":shared"))

    // features
    implementation("io.ktor:ktor-freemarker:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    // http client features
    implementation("io.ktor:ktor-client-json:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")
}
