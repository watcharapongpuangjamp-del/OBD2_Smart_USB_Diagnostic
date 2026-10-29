package com.example.presentation.common

/**
 * Marker interface for all MVI UI state data classes.
 * Represents the immutable snapshot of a screen's UI at any given moment.
 */
interface UiState

/**
 * Marker interface for all user intentions and UI events sent to ViewModels.
 */
interface UiEvent

/**
 * Marker interface for single-shot, ephemeral UI effects (e.g. toasts, navigation, sound alerts).
 * These are consumed once by the UI layer and not replayed on screen rotation.
 */
interface UiEffect
