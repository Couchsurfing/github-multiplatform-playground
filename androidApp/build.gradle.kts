import com.nicolasmilliard.githubcontributormultiplatform.NiaBuildType
import org.jetbrains.compose.ExperimentalComposeLibrary
import java.io.FileInputStream
import java.util.Properties

plugins {
  id("githubcontributormultiplatform.android.application")
  id("githubcontributormultiplatform.android.application.compose")
  id("githubcontributormultiplatform.android.application.flavors")
  id("org.jetbrains.compose")
  id("com.google.devtools.ksp")
}

val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(FileInputStream(apikeyPropertiesFile))

android {
  namespace = "com.nicolasmilliard.githubcontributormultiplatform.android"
  defaultConfig {
    applicationId = "com.nicolasmilliard.githubcontributormultiplatform.android"
    versionCode = 1
    versionName = "1.0"

    buildConfigField("String", "GITHUB_KEY", apikeyProperties["GITHUB_KEY"].toString())
  }

  buildTypes {
    val debug by getting {
      applicationIdSuffix = NiaBuildType.DEBUG.applicationIdSuffix
    }
    getByName("release") {
      isMinifyEnabled = false
      applicationIdSuffix = NiaBuildType.RELEASE.applicationIdSuffix
    }
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}

@OptIn(ExperimentalComposeLibrary::class)
dependencies {
  implementation(project(":shared"))
  implementation(compose.ui)
  implementation(compose.ui)
  implementation(compose.preview)
  implementation(compose.material3)
  implementation(libs.androidx.activity.compose)
  implementation(libs.coil)

  ksp(libs.kotlininject.compiler.ksp)

  debugImplementation(compose.uiTooling)
//  debugImplementation(libs.androidx.compose.ui.testManifest)

  androidTestImplementation(compose.uiTestJUnit4)
}


// Otherwise KSP fails
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
  kotlinOptions {
    jvmTarget = "11"
  }
}
