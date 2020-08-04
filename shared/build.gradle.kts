val kotlin_version: String by project
val ktor_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    application
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.2.0")

    // features
    api("io.ktor:ktor-server-core:$ktor_version")
    api("io.ktor:ktor-server-netty:$ktor_version")
    api("io.ktor:ktor-auth:$ktor_version")
    api("io.ktor:ktor-jackson:$ktor_version")
    api("com.google.code.gson:gson:2.8.1")

    // logging
    api("ch.qos.logback:logback-classic:$logback_version")
    api("org.slf4j:slf4j-simple:1.7.26")

    testApi("io.ktor:ktor-server-tests:$ktor_version")
    testApi("io.ktor:ktor-server-test-host:$ktor_version ")
    testApi("io.rest-assured:rest-assured:4.2.0")
    testApi("org.assertj:assertj-core:3.15.0")
    testApi("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}
