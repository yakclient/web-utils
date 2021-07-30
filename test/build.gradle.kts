group = "net.yakclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation "org.jetbrains.kotlin:kotlin-stdlib"
    implementation(project (":"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation(group = "org.slf4j", name = "slf4j-api")//, version: "1.7.30"
}

tasks.test {
    useJUnitPlatform()
}

//test {
//    useJUnitPlatform()
//}