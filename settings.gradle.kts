pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
    plugins {
        id("gg.essential.defaults")// version "0.10.0.2"
        //id("gg.essential.multi-version")
    }
}