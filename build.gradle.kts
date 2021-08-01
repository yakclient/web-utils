plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.5.20-RC"
    id("org.springframework.boot") version "2.5.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.javamodularity.moduleplugin") version "1.8.7"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    id("maven-publish")
}
group = "net.yakclient"
version = "1.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-hateoas") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-web")
    }
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.21")
}


tasks.wrapper {
    gradleVersion = "7.1.1"
}

tasks.shadowJar {
    enabled = false
}

tasks.jar {
    this.archiveClassifier.set("")
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.javamodularity.moduleplugin")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
    }
}
