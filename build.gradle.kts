plugins {
  id("githubcontributormultiplatform.check.updates")
  alias(libs.plugins.android.application) apply false

  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.kmp.nativecoroutines) apply false
  alias(libs.plugins.molecule) apply false
  alias(libs.plugins.jb.compose) apply false
  alias(libs.plugins.apollo) apply false
//    alias(libs.plugins.kotlin.serialization) apply false
//


}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
