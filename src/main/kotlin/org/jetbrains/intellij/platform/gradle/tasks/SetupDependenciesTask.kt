// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package org.jetbrains.intellij.platform.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.jetbrains.intellij.platform.gradle.IntelliJPluginConstants.PLUGIN_GROUP_NAME
import org.jetbrains.intellij.platform.gradle.IntelliJPluginConstants.Tasks
import org.jetbrains.intellij.platform.gradle.error
import org.jetbrains.intellij.platform.gradle.logCategory

/**
 * TODO: Provide valid URL
 */
private const val message = "Task is scheduled for removal, see: [SDK Docs link]"

/**
 * A deprecated method for setting up IntelliJ Platform dependencies.
 *
 * The `setupDependencies` task was automatically added to the ["After Sync" Gradle trigger](https://www.jetbrains.com/help/idea/work-with-gradle-tasks.html#config_triggers_gradle) to make the IntelliJ SDK dependency available for IntelliJ IDEA right after the Gradle synchronization.
 * With IntelliJ Platform Gradle Plugin 2.0 release, this method is no longer needed as the native Gradle dependencies resolution is in use.
 *
 * To remove any references to this task, call the "Tasks Activation" action and remove the `setupDependencies` entry from the "After Sync" group.
 *
 * TODO: Link to SDK Docs
 */
@DisableCachingByDefault(because = "No output state to track")
@Deprecated(message = message)
abstract class SetupDependenciesTask : DefaultTask() {

    private val context = logCategory()

    init {
        group = PLUGIN_GROUP_NAME
        description = message
    }

    @TaskAction
    fun setupDependencies() {
        error(context, message)
    }

    companion object {
        fun register(project: Project) =
            project.configureTask<SetupDependenciesTask>(Tasks.SETUP_DEPENDENCIES)
    }
}