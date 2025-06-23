plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "v1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.json:json:20250107")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.capstone.mtgwizard.Main"
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveFileName.set("MTGWizard-all.jar")
    manifest {
        attributes["Main-Class"] = "org.capstone.mtgwizard.Main"
    }
}

tasks.test {
    useJUnitPlatform()
    maxHeapSize = "1G"

    if (System.getenv("CI") == "true") {
        // These tests rely on local machine databases
        filter {
            excludeTestsMatching("org.capstone.mtgwizard.unit.GetCriteriaTest")
            excludeTestsMatching("org.capstone.mtgwizard.integration.QueryDatabaseTest")
            excludeTestsMatching("org.capstone.mtgwizard.integration.QueryByUuidTest")
            excludeTestsMatching("org.capstone.mtgwizard.integration.InventoryServiceTest")
        }
    }
}