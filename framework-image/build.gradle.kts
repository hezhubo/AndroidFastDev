
android {
    namespace = "com.hezb.framework.image"
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        minSdk = Config.minSdkVersion

        consumerProguardFiles("consumer-rules.pro")
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

    implementation(Config.Libs.glide)
    implementation(Config.Libs.glide_transformations)
}