group = "net.yakclient"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation(group = "org.slf4j", name = "slf4j-api")//, version: "1.7.30"
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
