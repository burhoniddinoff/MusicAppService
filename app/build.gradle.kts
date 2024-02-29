plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.musicplayerservice"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicplayerservice"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    /**
     *   Android Runtime Permission Library
     */
    implementation("com.nabinbhandari.android:permissions:3.8")

    /**
     *  Navigation component
     */
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.2")

    /**
     *   viewBinding
     */
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.3")

    /**
     * ViewModelProviders
     * */
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")

    /**
     *  lifeCycle
     */
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")

    /**
     *  kotlinx.coroutines
     */
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    /**
     * Better Logging In Android Using Timber
     **/
    implementation("com.jakewharton.timber:timber:5.0.1")


    implementation("de.hdodenhof:circleimageview:3.1.0")
}