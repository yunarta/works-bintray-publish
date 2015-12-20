package com.mobilesolutionworks.gradle.bintray.entity

/**
 * Created by yunarta on 19/12/15.
 */
class Developer {

    String id

    String name

    String email

    void id(String id) {
        this.id = id
    }

    void name(String name) {
        this.name = name
    }

    void email(String email) {
        this.email = email
    }
}
