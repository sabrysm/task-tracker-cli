plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.sabrysm.task_tracker_cli"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")

    // Lombok for generating boilerplate code
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "org.sabrysm.task_tracker_cli.Main"
        )
    }
}

tasks {
    // Configure the shadowJar task
    shadowJar {
        archiveClassifier.set("") // Optional: Remove "-all" suffix
        manifest {
            attributes(
                "Main-Class" to "org.sabrysm.task_tracker_cli.Main"
            )
        }
    }

    // Ensure shadowJar runs as part of the build process
    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }
}
