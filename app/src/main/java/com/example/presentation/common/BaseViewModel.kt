package com.example.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Enterprise BaseViewModel implementing Model-View-Intent (MVI) and Clean Architecture principles.
 *
 * @param State Immutable UI state extending [UiState]
 * @param Event User or lifecycle actions extending [UiEvent]
 * @param Effect Single-shot side effects extending [UiEffect] (e.g. Snackbars, Navigation, Audio alerts)
 */
abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    // Thread-safe backing state
    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    // Current state snapshot accessor
    val currentState: State
        get() = _uiState.value

    // Single-shot side-effect channel
    private val _uiEffect: Channel<Effect> = Channel(Channel.BUFFERED)
    val uiEffect: Flow<Effect> = _uiEffect.receiveAsFlow()

    // Event entrypoint from Composable UI
    abstract fun onEvent(event: Event)

    /**
     * Atomically mutate current UI state.
     */
    protected fun setState(reducer: State.() -> State) {
        _uiState.value = currentState.reducer()
    }

    /**
     * Dispatch a single-shot effect to the UI channel.
     */
    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
