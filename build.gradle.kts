plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.springframework.boot") version "4.0.1"
    id("io.freefair.lombok") version "9.1.0"
}

group = "org.aren_rend"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(platform("org.springframework.boot:spring-boot-dependencies:4.0.1"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly ("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.aren_rend.Launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

javafx {
    version = "25.0.1"
    modules("javafx.graphics", "javafx.fxml", "javafx.controls")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("Spending_App.jar")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs(
        "--enable-native-access=ALL-UNNAMED"
    )
}