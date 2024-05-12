buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }

    repositories {
        google()
        mavenCentral()
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.android.application") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}