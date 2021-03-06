# Works - Bintray Publish Plugin

A shorter version of Bintray Publish plugin. And the publish.properties file
is missing, the plugin will not load the required bintray and maven plugin thus
this will make gradle build faster

**build.gradle**
```groovy
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath 'com.mobilesolutionworks:works-bintray-publish:1.0.0'
    }
}

apply plugin: 'com.mobilesolutionworks.bintray-publish'

worksPublish {
    bintray {
        repo 'maven'
        name group + ':' + 'works-bintray-publish'

        license 'Apache-2.0'
        versionDesc ''
    }

    developer {
        id 'yunarta'
        name 'Yunarta Kartawahyudi'
        email 'yunarta.kartawahyudi@gmail.com'
    }

    artifact {
        javadoc true
    }

    siteUrl 'https://github.com/yunarta/works-bintray-publish'
    gitUrl 'https://github.com/yunarta/works-bintray-publish.git'
}
```

In order for the publish function to works, it requires "publish.properties"
in the root project directory.

**publish.properties**
```
user=yunarta-kartawahyudi
api.key=API_KEY
repo=snapshot
```

The repo here will override the repo in bintray { }, useful for automated build

