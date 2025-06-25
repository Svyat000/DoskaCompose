package com.sddrozdov.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sddrozdov.presentation.screens.createAdScreens.SelectCategoryView
import com.sddrozdov.presentation.states.createAd.CreateAdStates

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.sddrozdov.presentation.test", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class SelectCategoryViewTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testSelectCategoryView_LoadingState() {
        val testState = CreateAdStates(isLoading = true)

        rule.setContent {
            SelectCategoryView(
                state = testState,
                onEvent = {},
                onNavigateTo = {}
            )
        }

        rule.onNodeWithTag("LoadingIndicator").assertExists()
    }
}