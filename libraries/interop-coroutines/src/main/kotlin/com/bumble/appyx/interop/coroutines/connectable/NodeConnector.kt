package com.bumble.appyx.interop.coroutines.connectable

import androidx.lifecycle.Lifecycle
import com.bumble.appyx.core.lifecycle.subscribe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow

class NodeConnector<Input, Output>(
    override val input: MutableSharedFlow<Input> = MutableSharedFlow(),
) : Connectable<Input, Output> {

    override val output: MutableSharedFlow<Output> = MutableSharedFlow(
        replay = Int.MAX_VALUE
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(lifecycle: Lifecycle) {
        lifecycle.subscribe(onCreate = { output.resetReplayCache() })
    }

}
