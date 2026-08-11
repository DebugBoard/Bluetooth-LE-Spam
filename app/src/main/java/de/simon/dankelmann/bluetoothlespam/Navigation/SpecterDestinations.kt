package de.simon.dankelmann.bluetoothlespam.Navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

/** Route constants for the navigation-compose `NavHost` in `MainActivity` (plan §6 step 4). */
object SpecterDestinations {
    const val START = "start"
    const val ADVERTISEMENT_COLLECTION = "advertisementCollection"
    const val SPAM_DETECTOR = "spamDetector"
    const val ADVERTISEMENT = "advertisement"
    const val PREFERENCES = "preferences"
    const val DEVICE_SELECTOR = "deviceSelector"
    const val GROUP_EDITOR = "groupEditor"
    const val MANAGE_QUICK_START = "manageQuickStart"
}

/** Navigates to a top-level tab, preserving each tab's own back stack/scroll state. Shared by
 * FloatingNavBar taps and MainActivity's tab-swipe gesture so both go through the same options. */
fun NavController.navigateToTopLevelTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
