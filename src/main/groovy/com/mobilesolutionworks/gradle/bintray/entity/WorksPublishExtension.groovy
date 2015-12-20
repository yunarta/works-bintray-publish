package com.mobilesolutionworks.gradle.bintray.entity

import org.gradle.api.Action

/**
 * Created by yunarta on 19/12/15.
 */
class WorksPublishExtension {

    String siteUrl

    String gitUrl

    BinTray bintray

    Developer developer

    Artifact artifact

    WorksPublishExtension() {
        bintray = new BinTray()
        developer = new Developer()
        artifact = new Artifact()
    }

    void bintray(Action<? super BinTray> action) {
        action.execute(bintray)
    }

    def developer(Action<? super Developer> action) {
        action.execute(developer)
    }

    def artifact(Action<? super Artifact> action) {
        action.execute(artifact)
    }
}