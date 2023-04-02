
android {
    compileSdk = Config.compileSdkVersion
    namespace = "com.hezb.fastdev.demo"

    defaultConfig {
        applicationId = "com.hezb.fastdev.demo"
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = Config.sourceCompatibility
        targetCompatibility = Config.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = Config.jvmTargetVersion
    }

    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(project(":framework-base"))
    implementation(project(":framework-network"))
    implementation(project(":framework-ui"))
    implementation(project(":framework-image"))

//    implementation(Config.Libs.kotlin_stdlib)
//    implementation(Config.Libs.core_ktx)
//    implementation(Config.Libs.appcompat)
    implementation(Config.Libs.material)
    implementation(Config.Libs.constraintlayout)
    implementation(Config.Libs.gson)

    testImplementation(Config.Libs.junit)
    androidTestImplementation(Config.Libs.androidx_test_ext)
    androidTestImplementation(Config.Libs.androidx_test_espresso)
}