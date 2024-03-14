// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package org.jetbrains.intellij.platform.gradle.resolvers.closestVersion

import org.gradle.api.GradleException
import org.jetbrains.intellij.platform.gradle.models.MavenMetadata
import org.jetbrains.intellij.platform.gradle.models.decode
import org.jetbrains.intellij.platform.gradle.resolvers.Resolver
import org.jetbrains.intellij.platform.gradle.utils.*
import java.net.URL

/**
 * Interface for resolving the closest [Version] of any entity to the provided value.
 *
 * @param url The URL where versions list is available for parsing
 */
abstract class ClosestVersionResolver(
    protected val url: URL,
) : Resolver<Version> {

    /**
     * Resolves the closest version to the provided [version] of the artifact available in Maven repository.
     *
     * @throws GradleException
     */
    @Throws(GradleException::class)
    protected fun inMaven(version: Version): Version {
        log.debug(message = "Resolving the $subject version closest to $version")
        return decode<MavenMetadata>(url)
            ?.versioning
            ?.versions
            .throwIfNull { GradleException("Cannot resolve the $subject version closest to $version") }
            .map { it.toVersion() }
            .filter { it <= version }
            .maxOf { it }
    }
}