buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
    }
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven("https://kotlin.bintray.com/ktor")
        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
    }

    version = "0.0.2"
}
