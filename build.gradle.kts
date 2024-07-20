plugins {
    java // Tell gradle this is a java project.
    id("io.github.goooler.shadow") version "8.1.8" // Import shadow plugin for dependency shading.
    eclipse // Import eclipse plugin for IDE integration.
    kotlin("jvm") version "1.9.23" // Import kotlin jvm plugin for kotlin/java integration.
}

java {
    // Declare java version.
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "net.trueog.template-og" // Declare bundle identifier.
version = "1.0" // Declare plugin version (will be in .jar).
val apiVersion = "1.19" // Declare minecraft server target version.

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "version" to version,
        "apiVersion" to apiVersion
    )

    inputs.properties(props) // Indicates to rerun if version changes.

    filesMatching("plugin.yml") {
        expand(props)
    }
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://repo.purpurmc.org/snapshots") // Import the PurpurMC Maven Repository.
    }
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT") // Declare purpur API version to be packaged.
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.2.3") // Import MiniPlaceholders API.

    implementation(project(":libs:Utilities-OG"))
    implementation(project(":libs:GxUI"))
    implementation(project(":libs:DiamondBank-OG"))
}

tasks.withType<AbstractArchiveTask>().configureEach { // Ensure reproducible builds.
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.shadowJar {
    exclude("io.github.miniplaceholders.*") // Exclude the MiniPlaceholders package from being shadowed.
    minimize()
}

tasks.jar {
    dependsOn(tasks.shadowJar)
    archiveClassifier.set("part")
}

tasks.shadowJar {
    archiveClassifier.set("") // Use empty string instead of null
    from("LICENSE") { // Copies license file.
        into("/") // Sets destination for license file within the completed .jar.
    }
}

tasks.jar {
    dependsOn("shadowJar") // Ensures shadowJar gets run.
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.encoding = "UTF-8" // Use UTF-8 encoding universally.
    options.isFork = true
}

kotlin {
    jvmToolchain(17) // Declare kotlin jvm toolchain version.
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Declare JDK version.
        vendor = JvmVendorSpec.GRAAL_VM // Declare JDK distribution.
    }
}
