/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.nicolasmilliard.githubcontributormultiplatform.configureAndroid
import com.nicolasmilliard.githubcontributormultiplatform.configureAndroidTarget
import com.nicolasmilliard.githubcontributormultiplatform.configureFlavors
import com.nicolasmilliard.githubcontributormultiplatform.configureGradleManagedDevices
import com.nicolasmilliard.githubcontributormultiplatform.configurePrintApksTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class MultiplatformLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.multiplatform")
      }

      extensions.configure<LibraryExtension> {
        configureAndroid(this, false)
        defaultConfig.targetSdk = 33
        configureFlavors(this)
        configureGradleManagedDevices(this)
      }
      extensions.configure<LibraryAndroidComponentsExtension> {
        configurePrintApksTask(this)
      }

      extensions.configure<KotlinMultiplatformExtension> {
        configureAndroidTarget(this)
      }
      extensions.configure<KotlinProjectExtension> {
        sourceSets.all {
          languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
      }
    }
  }
}
