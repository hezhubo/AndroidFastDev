
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 阿里云镜像
        maven {
            setUrl("https://maven.aliyun.com/repository/public/")
        }
    }
}
rootProject.name = "AndroidFastDev"
include(":app")
include(":framework-base")
include(":framework-network")
include(":framework-ui")
include(":framework-image")
include(":framework-permission")
