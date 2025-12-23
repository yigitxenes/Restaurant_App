plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.restaurantApp"
    compileSdk = 36 // Use your preferred SDK version

    defaultConfig {
        applicationId = "com.example.restaurant_api"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "/META-INF/spring.*",
                    "/META-INF/DEPENDENCIES",
                    "/META-INF/LICENSE*",
                    "/META-INF/NOTICE*",
                    "/META-INF/INDEX.LIST",
                    "META-INF/NOTICE.md",
                    "META-INF/LICENSE.md",
                    "META-INF/DISCLAIMER",
                    "META-INF/web-fragment.xml",
                    "META-INF/io.netty.versions.properties",
                    "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat",
                    "META-INF/spring-configuration-metadata.json",
                    "META-INF/additional-spring-configuration-metadata.json"
                )
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:3.0.0")


    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")


    implementation ("com.google.android.material:material:1.9.0")

}