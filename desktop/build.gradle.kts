apply(plugin = "java")

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/java/")
    }
    named("test") {
        java.srcDir("src/test/java/")
    }
}

tasks.register<JavaExec>("run") {
    group = "application"
    description = "Runs the game"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.gdx.game.desktop.DesktopLauncher")
}

tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Packages the game and all its dependencies into an executable JAR"
    archiveBaseName.set("GdxGame")
    archiveClassifier.set("all")

    manifest {
        attributes["Main-Class"] = "com.gdx.game.desktop.DesktopLauncher"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets["main"].output)
    from(project(":core").sourceSets["main"].output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}