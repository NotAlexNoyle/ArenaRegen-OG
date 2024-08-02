import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java // Tell gradle this is a java project.
    id("io.github.goooler.shadow") version "8.1.8" // Import shadow plugin for dependency shading.
    eclipse // Import eclipse plugin for IDE integration.
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "me.realized.de"
version = "1.4"
val apiVersion = "1.19"

tasks.named<ProcessResources>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    val props = mapOf(
        "version" to version,
        "apiVersion" to apiVersion
    )

    inputs.properties(props)

    filesMatching("plugin.yml") {
        expand(props)
    }

    from(sourceSets.main.get().resources.srcDirs) {
        include("**/*.yml")
        filter<ReplaceTokens>("tokens" to mapOf("VERSION" to project.version.toString()))
    }
}

repositories {
    mavenCentral()
    
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    
    maven {
        url = uri("https://repo.purpurmc.org/snapshots")
    }
    
    maven {
        url = uri("https://jitpack.io")
    }
    
    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
    
    maven {
        url = uri("file://${System.getProperty("user.home")}/.m2/repository")
    }
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.19.4-R0.1-SNAPSHOT")
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.2.3")
    implementation("com.github.Realizedd.Duels:duels-api:3.4.1") // Import/Shade Duels API.
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.8") // Import WorldGuard API.
    compileOnly("io.github.miniplaceholders:miniplaceholders-kotlin-ext:2.2.3") // Import MiniPlaceholders API helper.

    // Shade remapped APIs into final jar.
    implementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    implementation("org.spigotmc:spigot:1.19.4-R0.1-SNAPSHOT")
}

tasks.withType<ShadowJar> {
    destinationDirectory.set(file("$rootDir/out/"))

    val archiveName = parent?.name?.replace("Parent", "") + '-' + project.version + ".jar"
    archiveFileName.set(archiveName)

    from(project.configurations.runtimeClasspath.get().asFileTree)
    exclude("io.github.miniplaceholders.*")
    minimize()
}

tasks.jar {
    dependsOn(tasks.shadowJar)
    archiveClassifier.set("part")
    from("LICENSE") {
        into("/")
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.build {
    dependsOn(tasks.getByName("shadowJar"))
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Xlint:deprecation")
    options.encoding = "UTF-8"
    options.isFork = true
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.GRAAL_VM
    }
}
