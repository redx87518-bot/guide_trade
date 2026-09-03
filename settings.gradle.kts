pluginManagement {
    repositories {
        google()
        maven("http://127.0.0.1:8080/maven2/")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        maven("http://127.0.0.1:8080/maven2/")
    }
}

rootProject.name = "Guide Trade"
include(":app")
