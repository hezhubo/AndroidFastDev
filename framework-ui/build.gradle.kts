
android {
    namespace = "com.hezb.framework.ui"
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

    api(Config.Libs.recyclerview)
    api(Config.Libs.swiperefreshlayout)
}