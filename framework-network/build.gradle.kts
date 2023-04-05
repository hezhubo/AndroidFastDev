
android {
    namespace = "com.hezb.framework.network"
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
    implementation(project(":framework-base"))
    implementation(Config.Libs.core_ktx)

    implementation(Config.Libs.kotlinx_coroutines_core)
    implementation(Config.Libs.kotlinx_coroutines_android)
    implementation(Config.Libs.lifecycle_runtime_ktx)
    implementation(Config.Libs.lifecycle_viewmodel_ktx)
    implementation(Config.Libs.lifecycle_livedata_ktx)

    api(Config.Libs.retrofit2)
    implementation(Config.Libs.retrofit2_converter_gson)
    implementation(Config.Libs.eventbus)
}