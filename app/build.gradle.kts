plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "net.tglt.android.fatlauncher"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        val plainName = "FatLauncher"
        setProperty("archivesBaseName", "$plainName-v$versionName($versionCode)")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
    implementation("com.willowtreeapps:fuzzywuzzy-kotlin-jvm:0.9.0")
    implementation("dev.kdrag0n:colorkt:1.0.5")

    implementation("io.posidon:android.launcherUtils:22.0")
//    implementation("io.posidon:android.loader:22.0")
    implementation("io.posidon:android.libduckduckgo:22.0")
    implementation("io.posidon:android.convenienceLib:22.0")
    implementation("com.github.zaguragit:ksugar:fb31214198")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    testImplementation("junit:junit:4.13.2")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}
