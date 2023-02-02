plugins {
    id("org.jetbrains.intellij") version "1.11.0"
    kotlin("jvm") version "1.6.10"
    java
}

group = "pers.wjx.plugin.demo"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("org.freemarker", "freemarker", "2.3.28")
}

intellij {
    version.set("2021.3")
    plugins.set(
        listOf(
            "com.intellij.java",
            "yaml"
        )
    )
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        version.set("${project.version}")
        sinceBuild.set("203")
    }
}