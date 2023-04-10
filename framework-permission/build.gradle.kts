
android {
    namespace = "com.hezb.framework.permission"
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        minSdk = Config.minSdkVersion
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
    implementation(Config.Libs.appcompat)
    implementation(Config.Libs.lifecycle_runtime_ktx)
}