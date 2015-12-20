package com.mobilesolutionworks.gradle.bintray.entity

/**
 * Created by yunarta on 20/12/15.
 */
class Artifact {

    boolean javadoc = false

    void javadoc(boolean javadoc) {
        this.javadoc = javadoc
    }
}
