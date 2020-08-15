# CoreLib

A library containing shared code for all of [my mods](https://modrepo.de/minecraft).

## Usage

This library should be packed into the mods jar file.
Here is an example using [shadow](https://github.com/johnrengelman/shadow):

*build.gradle*
``` groovy
buildscript {
    repositories {
        ...
        mavenCentral()
    }
    dependencies {
        ...
        classpath group: 'com.github.jengelman.gradle.plugins', name: 'shadow', version: '4.0.4'
    }
}
...
apply plugin: 'com.github.johnrengelman.shadow'
...
repositories {
    maven {
        name = "henkelmax.public"
        url = 'https://maven.maxhenkel.de/repository/public'
    }
    mavenLocal()
}
...
dependencies {
    ...
    shadow fg.deobf("de.maxhenkel.corelib:corelib:${minecraft_version}-${corelib_version}:api")
    runtimeOnly fg.deobf("de.maxhenkel.corelib:corelib:${minecraft_version}-${corelib_version}")
    runtimeOnly fg.deobf("de.maxhenkel.corelib:corelib:${minecraft_version}-${corelib_version}:javadoc")
}
...
artifacts {
    ...
    archives shadowJar
}
...
shadowJar {
    project.configurations.shadow.setTransitive(true);
    configurations = [project.configurations.shadow]
    classifier = ""
    relocate 'de.maxhenkel.corelib', "de.maxhenkel.${mod_id}.corelib"
}

reobf {
    shadowJar {}
}
```

It is very important to relocate the corelib package, to avoid conflicts with other mods using this library.
