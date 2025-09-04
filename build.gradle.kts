plugins {
    id("java")
    application
}

application {
    mainClass = "com.itmo.Main"
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.itmo.Main"
        )
    }
}

group = "com.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jfree:jfreechart:1.5.6")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}