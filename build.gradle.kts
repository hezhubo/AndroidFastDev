plugins {
    id(Config.Plugins.android_application) version Config.agpVersion apply false
    id(Config.Plugins.android_library) version Config.agpVersion apply false
    id(Config.Plugins.kotlin_android) version Config.kotlinVersion apply false
    id(Config.Plugins.kotlin_kapt) version Config.kotlinVersion apply false
}

subprojects {
    apply(plugin = Config.Plugins.kotlin_android)
    if (name == "app") {
        apply(plugin = Config.Plugins.android_application)
    } else {
        apply(plugin = Config.Plugins.android_library)
    }
}