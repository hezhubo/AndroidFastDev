
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
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

dependencies {
    implementation(project(":framework-base"))
    implementation(Config.Libs.appcompat)

    api(Config.Libs.recyclerview)
    api(Config.Libs.swiperefreshlayout)
}