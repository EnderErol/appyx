package com.bumble.appyx.transitionmodel.spotlight

import com.bumble.appyx.interactions.core.model.transition.BaseTransitionModel
import com.bumble.appyx.interactions.core.NavElement
import com.bumble.appyx.interactions.core.asElement
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel.State
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel.State.ElementState.DESTROYED
import com.bumble.appyx.transitionmodel.spotlight.SpotlightModel.State.ElementState.STANDARD

class SpotlightModel<InteractionTarget : Any>(
    items: List<InteractionTarget>,
    initialActiveIndex: Float = 0f
//    savedStateMap: SavedStateMap?,
//    key: String = KEY_NAV_MODEL,
//    backPressHandler: BackPressHandlerStrategy<InteractionTarget, State> = GoToDefault(
//        initialActiveIndex
//    ),
//    operationStrategy: OperationStrategy<InteractionTarget, State> = ExecuteImmediately(),
//    screenResolver: OnScreenStateResolver<State> = SpotlightOnScreenResolver
) : BaseTransitionModel<InteractionTarget, State<InteractionTarget>>(
//    backPressHandler = backPressHandler,
//    operationStrategy = operationStrategy,
//    screenResolver = screenResolver,
//    finalState = null,
//    savedStateMap = savedStateMap,
//    key = key
) {

    data class State<InteractionTarget>(
        val positions: List<Position<InteractionTarget>>,
        val activeIndex: Float
    ) {
        data class Position<InteractionTarget>(
            val elements: Map<NavElement<InteractionTarget>, ElementState> = mapOf()
        )

        enum class ElementState {
            CREATED, STANDARD, DESTROYED
        }

        fun hasPrevious(): Boolean =
            activeIndex >= 1

        fun hasNext(): Boolean =
            activeIndex <= positions.lastIndex - 1
    }

    override val initialState: State<InteractionTarget> =
        State(
            positions = items.map {
                State.Position(
                    elements = mapOf(it.asElement() to STANDARD)
                )
            },
            activeIndex = initialActiveIndex
        )

    override fun State<InteractionTarget>.removeDestroyedElement(navElement: NavElement<InteractionTarget>): State<InteractionTarget> {
        val newPositions = positions.map { position ->
            val newElements = position
                .elements
                .filterNot { mapEntry ->
                    mapEntry.key == navElement && mapEntry.value == DESTROYED
                }

            position.copy(elements = newElements)
        }
        return copy(positions = newPositions)
    }

    override fun State<InteractionTarget>.removeDestroyedElements(): State<InteractionTarget>  {
        val newPositions = positions.map { position ->
            val newElements = position
                .elements
                .filterNot { mapEntry ->
                    mapEntry.value == DESTROYED
                }

            position.copy(elements = newElements)
        }
        return copy(positions = newPositions)
    }

    override fun State<InteractionTarget>.availableElements(): Set<NavElement<InteractionTarget>> =
        positions
            .flatMap { it.elements.entries }
            .filter { it.value != DESTROYED }
            .map { it.key }
            .toSet()

    override fun State<InteractionTarget>.destroyedElements(): Set<NavElement<InteractionTarget>> =
        positions
            .flatMap { it.elements.entries }
            .filter { it.value == DESTROYED }
            .map { it.key }
            .toSet()
}
