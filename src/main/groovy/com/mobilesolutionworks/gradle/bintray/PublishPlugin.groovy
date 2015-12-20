package com.mobilesolutionworks.gradle.bintray

import com.mobilesolutionworks.gradle.bintray.entity.WorksPublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

/**
 * Created by yunarta on 19/12/15.
 */
class PublishPlugin implements Plugin<Project> {

    WorksPublishExtension worksPublish

    @Override
    void apply(Project project) {
        worksPublish = project.extensions.create('worksPublish', WorksPublishExtension)

        def File publishProp = [project.rootDir, 'publish.properties']
        if (!publishProp.exists()) {
            return
        }

        Properties properties = new Properties()
        publishProp.withInputStream { is ->
            properties.load(is)
        }

        boolean isAndroidArtifact = false
        if (project.extensions.findByName('android')) {
            isAndroidArtifact = true
        }

        project.pluginManager.apply('com.jfrog.bintray')
        project.pluginManager.apply('com.github.dcendents.android-maven')

        project.afterEvaluate {
            def defineRepo = properties.getProperty("repo")
            project.bintray {
                user = properties.getProperty("user")
                key = properties.getProperty("api.key")

                configurations = ['archives']
                publish = worksPublish.bintray.publish

                pkg {
                    repo = defineRepo != null ? defineRepo : worksPublish.bintray.repo
                    name = worksPublish.bintray.name

                    userOrg = user

                    websiteUrl = worksPublish.siteUrl
                    vcsUrl = worksPublish.gitUrl

                    licenses = [worksPublish.bintray.license]
                    publish = worksPublish.bintray.publish

                    publicDownloadNumbers = true

                    version {
                        name = project.version
                        desc = worksPublish.bintray.versionDesc

                        vcsTag = project.version
                    }
                }
            }

            project.install {
                repositories.mavenInstaller {
                    pom.project {
                        packaging isAndroidArtifact ? 'aar' : 'jar'

                        // Add your description here
                        name project.name
                        url worksPublish.siteUrl

                        // Set your license
                        licenses {
                            license {
                                name 'The Apache Software License, Version 2.0'
                                url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
                            }
                        }
                        developers {
                            developer {
                                id worksPublish.developer.id
                                name worksPublish.developer.name
                                email worksPublish.developer.email
                            }
                        }
                        scm {
                            connection worksPublish.gitUrl
                            developerConnection worksPublish.gitUrl
                            url worksPublish.siteUrl
                        }


                    }

                    if (isAndroidArtifact) {
                        pom.withXml {
                            def parentNode = asNode()
                            parentNode.appendNode('packaging', 'aar')
                        }
                    }
                }
            }

            def Task jar = null
            if (isAndroidArtifact) {
                jar = project.tasks.maybeCreate('jar', Jar.class).configure {
                    from 'build/intermediates/classes/release'
                }

                project.android.libraryVariants.all { variant ->
                    variant.outputs.each { output ->
                        def outputFile = output.outputFile
                        if (outputFile != null && outputFile.name.endsWith('.aar')) {
                            def fileName = "${project.archivesBaseName}-${project.version}.aar"
                            output.outputFile = [outputFile.parent, fileName] as File
                        }
                    }
                }
            } else {
                jar = project.tasks.maybeCreate('jar', Jar.class)
            }

            def sourcesJar = project.tasks.maybeCreate('sourcesJar', Jar.class).configure {
                if (isAndroidArtifact) {
                    from project.android.sourceSets.main.java.srcDirs
                    classifier = 'sources'
                } else {
                    from project.sourceSets.main.java.srcDirs
                    classifier = 'sources'
                }
            }

            def javadoc = project.tasks.maybeCreate('javadoc', Javadoc.class).configure {
                if (isAndroidArtifact) {
                    source = project.android.sourceSets.main.java.srcDirs
                    classpath += project.files(project.android.getBootClasspath().join(File.pathSeparator))
                } else {
                    source = project.sourceSets.main.java.srcDirs
                }
            }

            def javadocJar = project.tasks.maybeCreate('javadocJar', Jar.class).dependsOn(javadoc).configure {
                classifier = 'javadoc'
                from javadoc.destinationDir
            }

            project.artifacts {
                if (worksPublish.artifact.javadoc) {
                    archives javadocJar
                }

                archives sourcesJar
                archives jar
            }
        }
    }
}