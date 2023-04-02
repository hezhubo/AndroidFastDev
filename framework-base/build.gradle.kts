
android {
    namespace = "com.hezb.framework"
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        minSdk = Config.minSdkVersion
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = Config.sourceCompatibility
        targetCompatibility = Config.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = Config.jvmTargetVersion
    }
}

dependencies {
    implementation(Config.Libs.kotlin_stdlib)
    implementation(Config.Libs.core_ktx)
    implementation(Config.Libs.appcompat)
    implementation(Config.Libs.lifecycle_runtime_ktx)
}