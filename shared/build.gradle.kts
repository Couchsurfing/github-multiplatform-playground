plugins {
  id("githubcontributormultiplatform.multiplatform.library")
  id("com.google.devtools.ksp")
  id("com.rickclephas.kmp.nativecoroutines")
  id("app.cash.molecule")
  id("org.jetbrains.compose")
  id("com.apollographql.apollo3")
}

kotlin {
  android()
  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64(),
  ).forEach {
    it.binaries.framework {
      baseName = "shared"
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(libs.kotlinx.coroutine.core)
        api(libs.kotlinx.collections.immutable)

        api(libs.kotlininject.runtime)

        api(compose.runtime)
//        api(compose.foundation)
//        api(compose.material3)
        api(libs.apollo.runtime)

        api(libs.kermit)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val androidMain by getting {
      dependencies {
        api(libs.androidx.lifecycle.viewmodel)
        api(compose.ui)
      }
    }
    val androidUnitTest by getting
    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)
    }
    val iosX64Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    val iosTest by creating {
      dependsOn(commonTest)
      iosX64Test.dependsOn(this)
      iosArm64Test.dependsOn(this)
      iosSimulatorArm64Test.dependsOn(this)
    }
  }
}

dependencies {
  add("kspIosSimulatorArm64", libs.kotlininject.compiler.ksp)
}

ksp {
  arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

android {
  namespace = "com.nicolasmilliard.githubcontributormultiplatform.shared"
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}

apollo {
  service("github") {
    packageName.set("com.nicolasmilliard.githubcontributormultiplatform.github")
    srcDir("src/commonMain/graphql/github")
  }
}

//kotlin.sourceSets.commonMain {
//  kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
//}
//
//tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
//  if (name != "kspCommonMainKotlinMetadata") {
//    dependsOn("kspCommonMainKotlinMetadata")
//  }
//}

