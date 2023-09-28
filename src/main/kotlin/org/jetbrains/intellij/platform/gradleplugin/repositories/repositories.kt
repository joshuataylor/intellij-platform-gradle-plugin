// Copyright 2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package org.jetbrains.intellij.platform.gradleplugin.repositories

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.newInstance
import org.jetbrains.intellij.platform.gradleplugin.BuildFeature
import org.jetbrains.intellij.platform.gradleplugin.IntelliJPluginConstants.INTELLIJ_PLATFORM_REPOSITORY_SETTINGS_NAME

internal typealias ArtifactRepositoryAction = (ArtifactRepository.() -> Unit)

interface IntelliJPlatformRepositorySettings {

    val useCacheRedirector: Property<Boolean>
}

internal fun RepositoryHandler.applyIntelliJPlatformSettings(objects: ObjectFactory, providers: ProviderFactory) {
    val settings = objects.newInstance(IntelliJPlatformRepositorySettings::class)

    settings.useCacheRedirector.convention(BuildFeature.USE_CACHE_REDIRECTOR.getValue(providers))

    (this as ExtensionAware).extensions.add(INTELLIJ_PLATFORM_REPOSITORY_SETTINGS_NAME, settings)
}

internal val RepositoryHandler.intellijPlatformRepositorySettings: IntelliJPlatformRepositorySettings
    get() = (this as ExtensionAware).extensions.getByType<IntelliJPlatformRepositorySettings>()

internal typealias Action = (MavenArtifactRepository.() -> Unit)

internal fun RepositoryHandler.customRepository(
    name: String,
    url: String,
    urlWithCacheRedirector: String,
    settings: IntelliJPlatformRepositorySettings = intellijPlatformRepositorySettings,
    action: Action = {},
) = maven(settings.useCacheRedirector.map { cached ->
    when (cached) {
        true -> urlWithCacheRedirector
        false -> url
    }
}) {
    this.name = name
    action(this)
}
