plugins {
    id("java")
}

group = "org.example"
version = "v1.0.0"

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