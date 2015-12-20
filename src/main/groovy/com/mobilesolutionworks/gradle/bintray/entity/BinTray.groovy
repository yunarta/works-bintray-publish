package com.mobilesolutionworks.gradle.bintray.entity

import org.gradle.api.Named

/**
 * Created by yunarta on 19/12/15.
 */
class BinTray implements Named {

    String repo

    String name

    String license = 'Apache-2.0'

    String versionDesc = ''

    boolean publish = true

    void repo(String repo) {
        this.repo = repo
    }

    void name(String name) {
        this.name = name
    }

    void license(String license) {
        this.license = license
    }

    void versionDesc(String versionDesc) {
        this.versionDesc = versionDesc
    }

    void publish(boolean publish) {
        this.publish = publish
    }
}
