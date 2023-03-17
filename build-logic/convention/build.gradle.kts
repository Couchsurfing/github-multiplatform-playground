plugins {
  `kotlin-dsl`
}

group = "com.nicolasmilliard.githubcontributormultiplatform.buildlogic"

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.ksp.gradlePlugin)
  implementation(libs.check.dep.updates.gradlePlugin)
}

gradlePlugin {
  plugins {
    register("androidApplicationCompose") {
      id = "githubcontributormultiplatform.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidApplication") {
      id = "githubcontributormultiplatform.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidApplicationJacoco") {
      id = "githubcontributormultiplatform.android.application.jacoco"
      implementationClass = "AndroidApplicationJacocoConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "githubcontributormultiplatform.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("androidLibrary") {
      id = "githubcontributormultiplatform.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = "githubcontributormultiplatform.android.feature"
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("androidLibraryJacoco") {
      id = "githubcontributormultiplatform.android.library.jacoco"
      implementationClass = "AndroidLibraryJacocoConventionPlugin"
    }
    register("androidTest") {
      id = "githubcontributormultiplatform.android.test"
      implementationClass = "AndroidTestConventionPlugin"
    }
    register("androidFlavors") {
      id = "githubcontributormultiplatform.android.application.flavors"
      implementationClass = "AndroidApplicationFlavorsConventionPlugin"
    }
    register("multiPlatformLibrary") {
      id = "githubcontributormultiplatform.multiplatform.library"
      implementationClass = "MultiplatformLibraryConventionPlugin"
    }
    register("checkDepUpdates") {
      id = "githubcontributormultiplatform.check.updates"
      implementationClass = "CheckDepUpdatesConventionPlugin"
    }
  }
}
