import org.gradle.api.JavaVersion

object Config {

    val agpVersion = "7.4.1"
    val kotlinVersion = "1.8.0"

    val compileSdkVersion = 31
    val minSdkVersion = 19
    val targetSdkVersion = 31

    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8

    val jvmTargetVersion = "1.8"

    object Plugins {
        val android_application = "com.android.application"
        val android_library = "com.android.library"
        val kotlin_android = "org.jetbrains.kotlin.android"
        val kotlin_kapt = "org.jetbrains.kotlin.kapt"
    }

    object Libs {
        val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}"
        val core_ktx = "androidx.core:core-ktx:1.7.0"
        val appcompat = "androidx.appcompat:appcompat:1.4.1"

        // 协程库
        val kotlinx_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4"
        val kotlinx_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
        val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1"
        val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
        val lifecycle_livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"

        val material = "com.google.android.material:material:1.5.0"
        val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.3"
        val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
        val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

        val retrofit2 = "com.squareup.retrofit2:retrofit:2.6.4" // 2.7.0开始最低要求API21以上
        val retrofit2_converter_gson = "com.squareup.retrofit2:converter-gson:2.6.4"
        val gson = "com.google.code.gson:gson:2.10.1"

        val glide = "com.github.bumptech.glide:glide:4.15.1"
        val glide_compiler = "com.github.bumptech.glide:compiler:4.15.1" // 自定义AppGlideModule时使用
        val glide_annotations = "com.github.bumptech.glide:annotations:4.15.1" // 自定义AppGlideModule时使用
        val glide_transformations = "jp.wasabeef:glide-transformations:4.2.0" // 4.3.0 minSdk=21

        val junit = "junit:junit:4.13.2"
        val androidx_test_ext = "androidx.test.ext:junit:1.1.3"
        val androidx_test_espresso = "androidx.test.espresso:espresso-core:3.4.0"

    }
}