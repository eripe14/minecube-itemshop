import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("xyz.jpenilla.run-paper") version "2.0.1"
}

group = "com.eripe14"
version = "1.0.0"

repositories {
    mavenCentral()
    gradlePluginPortal()

    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    // Spigot api
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")

    // Kyori Adventure
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")

    // LiteCommands
    implementation("dev.rollczi.litecommands:bukkit-adventure:2.8.0")

    // cdn
    implementation("net.dzikoysk:cdn:1.14.3")

    // TriumphGui
    implementation("dev.triumphteam:triumph-gui:3.1.4")

    // panda-utilities
    implementation("org.panda-lang:panda-utilities:0.5.2-alpha")

    // Ormlite jdbc
    compileOnly("com.j256.ormlite:ormlite-jdbc:6.1")

    // Hikari
    compileOnly("com.zaxxer:HikariCP:5.0.1")

    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.2")

    // LuckPerms
    compileOnly("net.luckperms:api:5.4")

    // Caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.1")

    // Vault
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}

bukkit {
    main = "com.eripe14.itemshop.ItemShopPlugin"
    apiVersion = "1.13"
    prefix = "ItemShop"
    name = "ItemShop"
    version = "${project.version}"
    depend = listOf("PlaceholderAPI", "LuckPerms")
    libraries = listOf(
        "org.postgresql:postgresql:42.5.0",
        "com.h2database:h2:2.1.214",
        "com.j256.ormlite:ormlite-jdbc:6.1",
        "com.zaxxer:HikariCP:5.0.1",
        "org.mariadb.jdbc:mariadb-java-client:3.0.7"
    )
}

tasks {
    runServer {
        minecraftVersion("1.19.3")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("Itemshop v${project.version} (MC 1.19.x).jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "META-INF/**",
        "javax/**"
    )

    val prefix = "com.eripe14.itemshop.libs"
    listOf(
        "panda",
        "org.panda_lang",
        "net.dzikoysk",
        "dev.rollczi",
        "net.kyori",
        "dev.triumphteam",
        "com.github.ben-manes.caffeine"
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}