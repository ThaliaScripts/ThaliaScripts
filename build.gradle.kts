import gg.essential.gradle.util.makeConfigurationForInternalDependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("gg.essential.loom") version "0.10.0.+"
    id("gg.essential.defaults") version "0.1.10"
    java
    idea
}

val modVersion = "0.0.1"
val modGroup = "dev.bozho"
val modBaseName = "ThaliaScripts"
version = modVersion
group = modGroup
base.archivesName.set(modBaseName)

loom {
    forge {
        mixinConfig("thaliascripts.mixins.json")
    }
    mixin {
        defaultRefmapName.set("thaliascripts.mixins.refmap.json")
    }
    launchConfigs {
        getByName("client") {
            property("mixin.debug.verbose", "true")
            property("mixin.debug.export", "true")
            property("mixin.dumpTargetOnFailure", "true")
            arg("--mixin", "thaliascripts.mixins.json")
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
    maven("https://jitpack.io")
    maven("https://repo.essential.gg/repository/maven-public")
}

val internal = makeConfigurationForInternalDependencies {
    relocate("com.google", "thaliascripts.google")
}

val embed by configurations.creating
configurations.implementation.get().extendsFrom(embed)

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly("org.spongepowered:mixin:0.8.5")

    embed("org.jgrapht:jgrapht-core:1.3.1")
}

sourceSets {
    main {
        output.setResourcesDir(file("${buildDir}/classes/kotlin/main"))
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion", "1.8.9")

        filesMatching("mcmod.info") {
            expand(mapOf("version" to project.version, "mcversion" to "1.8.9"))
        }
        dependsOn(compileJava)
    }
    named<Jar>("jar") {
        from(embed.files.map { zipTree(it) })

        exclude("**/module-info.class")
        exclude("**/package-info.class")

        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "ThaliaScripts",
                    "ForceLoadAsMod" to true,
                    "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                    "MixinConfigs" to "thaliascripts.mixins.json",
                    "ModSide" to "CLIENT",
                )
            )
        }
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn", "-Xjvm-default=all", "-Xrelease=8", "-Xbackend-threads=0")
        }
    }
}

kotlin {
    jvmToolchain {
        check(this is JavaToolchainSpec)
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}