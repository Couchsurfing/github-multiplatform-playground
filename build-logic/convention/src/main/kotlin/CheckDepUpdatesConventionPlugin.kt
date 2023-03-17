import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class CheckDepUpdatesConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply("com.github.ben-manes.versions")

      tasks.withType(DependencyUpdatesTask::class.java) {
        rejectVersionIf {
          isNonStable(candidate.version)
        }
        checkForGradleUpdate = true
        outputFormatter = "json"
        outputDir = "build/reports"
        reportfileName = "dependencyUpdates"
      }
    }
  }

  fun isNonStable(version: String): Boolean {
    val unStableKeyword =
      listOf("-ALPHA", "-BETA", "-RC", "-DEV").any { version.toUpperCase().contains(it) }
    return unStableKeyword
  }
}
