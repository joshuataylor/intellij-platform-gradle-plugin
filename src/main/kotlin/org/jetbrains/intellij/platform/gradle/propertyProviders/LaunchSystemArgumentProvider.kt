// Copyright 2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package org.jetbrains.intellij.platform.gradle.propertyProviders

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.process.CommandLineArgumentProvider
import org.jetbrains.intellij.platform.gradle.IntelliJPluginConstants.Sandbox
import org.jetbrains.intellij.platform.gradle.asPath
import org.jetbrains.intellij.platform.gradle.model.productInfo
import org.jetbrains.intellij.platform.gradle.resolveIdeHomeVariable
import kotlin.io.path.pathString

class LaunchSystemArgumentProvider(
    @InputFiles @PathSensitive(RELATIVE) val intellijPlatform: ConfigurableFileCollection,
    @InputDirectory @PathSensitive(RELATIVE) val sandboxDirectory: DirectoryProperty,
    private val requirePluginIds: List<String>,
) : CommandLineArgumentProvider {

    private val intellijPlatformPath
        get() = intellijPlatform.singleFile.toPath()

    private val currentLaunchProperties
        get() = intellijPlatformPath
            .productInfo()
            .currentLaunch
            .additionalJvmArguments
            .filter { it.startsWith("-D") }
            .map { it.resolveIdeHomeVariable(intellijPlatformPath) }

    private fun resolveInSandboxDirectory(directoryName: String) = sandboxDirectory.map {
        it.dir(directoryName).apply {
            asFile.mkdirs()
        }
    }.asPath.pathString

    override fun asArguments() = currentLaunchProperties + listOf(
        "-Didea.config.path=${resolveInSandboxDirectory(Sandbox.CONFIG)}",
        "-Didea.system.path=${resolveInSandboxDirectory(Sandbox.SYSTEM)}",
        "-Didea.log.path=${resolveInSandboxDirectory(Sandbox.LOG)}",
        "-Didea.plugins.path=${resolveInSandboxDirectory(Sandbox.PLUGINS)}",
        "-Didea.required.plugins.id=${requirePluginIds.joinToString(",")}",
    )
}