// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package org.jetbrains.intellij.platform.gradleplugin.model

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "ivy-module")
data class IvyModule(

    @set:XmlAttribute
    var version: String = "2.0",

    @set:XmlElement
    var info: IvyModuleInfo? = null,

    @set:XmlElement(name = "conf")
    @set:XmlElementWrapper
    var configurations: List<IvyModuleConfiguration> = mutableListOf(),

    @set:XmlElement(name = "artifact")
    @set:XmlElementWrapper
    var publications: List<IvyModulePublication> = mutableListOf(),
)

data class IvyModuleConfiguration(

    @set:XmlAttribute
    var name: String? = null,

    @set:XmlAttribute
    var visibility: String? = null,
)

data class IvyModuleInfo(

    @set:XmlAttribute
    var organisation: String? = null,

    @set:XmlAttribute
    var module: String? = null,

    @set:XmlAttribute
    var revision: String? = null,

    @set:XmlAttribute
    var publication: String? = null,
)

data class IvyModulePublication(
    @set:XmlAttribute
    var name: String? = null,

    @set:XmlAttribute
    var type: String? = null,

    @set:XmlAttribute
    var ext: String? = null,

    @set:XmlAttribute
    var conf: String? = null,
)
