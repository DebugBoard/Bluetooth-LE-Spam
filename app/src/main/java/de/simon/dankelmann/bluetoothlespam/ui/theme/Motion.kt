package de.simon.dankelmann.bluetoothlespam.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

/** Base duration (ms) for lightweight UI feedback animations (chevron rotation, fades, NavHost
 * screen transitions) before the user's Settings -> Animation Speed multiplier is applied. */
const val BaseAnimationDurationMillis = 220

/** User-configurable animation behavior (Settings -> Animations): [enabled] is a global on/off
 * switch, [speedMultiplier] scales every animation's duration when on (2x = twice as fast). */
data class AnimationSettings(val enabled: Boolean, val speedMultiplier: Float) {
    /** [base] scaled by [speedMultiplier], or 0 (instant) when [enabled] is false. */
    fun duration(base: Int = BaseAnimationDurationMillis): Int {
        if (!enabled) return 0
        return (base / speedMultiplier).toInt().coerceAtLeast(1)
    }
}

val LocalAnimationSettings = staticCompositionLocalOf { AnimationSettings(enabled = true, speedMultiplier = 1f) }
