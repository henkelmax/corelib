# Corelib

A library containing shared code for all of [my mods](https://modrepo.de).

## Usage

This library should be packed into the mods jar file.
Here is an example using [shadow](https://github.com/johnrengelman/shadow):

*build.gradle*
``` groovy
plugins {
    id 'com.github.johnrengelman.shadow' version '8.1.1'
}
// ...
repositories {
    maven {
        name = "henkelmax.public"
        url = 'https://maven.maxhenkel.de/repository/public'
    }
    // ...
}
// ...
dependencies {
    // ...
    shadow fg.deobf("de.maxhenkel.corelib:corelib:${minecraft_version}-${corelib_version}:api")
    runtimeOnly fg.deobf("de.maxhenkel.corelib:corelib:${minecraft_version}-${corelib_version}")
    runtimeOnly fg.deobf("de.maxhenkel.corelib:corelib:${minecraft_version}-${corelib_version}:javadoc")
}
// ...
shadowJar {
    configurations = [project.configurations.shadow]
    archiveClassifier = ''
    relocate 'de.maxhenkel.corelib', "de.maxhenkel.${mod_id}.corelib"
}
shadowJar.dependsOn('reobfJar')

reobf {
    shadowJar {}
}
```

It is very important to relocate the corelib package, to avoid conflicts with other mods using this library.
