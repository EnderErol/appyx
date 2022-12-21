package com.bumble.appyx.interop.coroutines.connectable

import com.bumble.appyx.core.plugin.NodeLifecycleAware
import kotlinx.coroutines.flow.Flow

interface Connectable<Input, Output> : NodeLifecycleAware {
    val input: Flow<Input>
    val output: Flow<Output>
}
