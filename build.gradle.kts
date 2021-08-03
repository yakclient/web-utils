plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.5.20-RC"
    id("org.springframework.boot") version "2.5.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.javamodularity.moduleplugin") version "1.8.7"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("signing")
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.4.32"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}
group = "net.yakclient"
version = "1.3.1"
java.sourceCompatibility = JavaVersion.VERSION_11

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
task<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

task<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            pom {
                name.set("Web Utilities")
                description.set("A Kotlin library for all making spring web development easier!")
                url.set("https://github.com/yakclient/web-utils")

                packaging = "jar"

                developers {
                    developer {
                        id.set("Chestly")
                        name.set("Durgan McBroom")
                    }
                }

                licenses {
                    license {
                        name.set("GNU General Public License")
                        url.set("https://opensource.org/licenses/gpl-license")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/yakclient/web-utils")
                    developerConnection.set("scm:git:ssh://github.com:yakclient/web-utils.git")
                    url.set("https://github.com/yakclient/web-utils")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("mavenUsername") as String)
            password.set(project.findProperty("mavenPassword") as String)
        }
    }
}

signing {
    sign(publishing.publications["maven"])
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
