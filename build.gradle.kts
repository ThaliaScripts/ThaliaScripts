import dev.architectury.pack200.java.Pack200Adapter

plugins {
    kotlin("jvm") version "1.6.21"
    id("gg.essential.loom")
    id("dev.architectury.architectury-pack200") version "0.1.3"
}

val modVersion = "0.0.1"
val modGroup = "dev.bozho"
val modBaseName = "ThaliaScripts"
version = modVersion
group = modGroup
base.archivesName.set(modBaseName)

loom {
    forge {
        pack200Provider.set(Pack200Adapter())
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
    maven("https://repo.polyfrost.cc/releases")
}

val embed by configurations.creating
configurations.implementation.get().extendsFrom(embed)

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    embed("org.spongepowered:mixin:0.8.5-SNAPSHOT")

    compileOnly("com.github.Minikloon:FSMgasm:-SNAPSHOT")
    compileOnly("cc.polyfrost:oneconfig-1.8.9-forge:0.1.0-alpha26")
    embed("cc.polyfrost:oneconfig-wrapper-1.8.9-forge:1.0.0-alpha6")
}

kotlin {
    jvmToolchain {
        check(this is JavaToolchainSpec)
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xjvm-default=all", "-Xrelease=8", "-Xbackend-threads=0")
        languageVersion = "1.6"
    }
}

tasks.jar {
    from(embed.files.map { zipTree(it) })

    manifest.attributes(mapOf(
        "ModSide" to "CLIENT",
        "Main-Class" to "dev.bozho.ThaliaScripts",
        "TweakOrder" to "0",
        "MixinConfigs" to "thaliascripts.mixins.json"
    ))
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mcversion", "1.8.9")

    filesMatching("mcmod.info") {
        expand(mapOf("version" to project.version, "mcversion" to "1.8.9"))
    }
    dependsOn(tasks.compileJava)
}
