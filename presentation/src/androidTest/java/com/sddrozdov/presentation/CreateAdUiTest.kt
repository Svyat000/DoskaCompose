package com.sddrozdov.presentation

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sddrozdov.presentation.screens.createAdScreens.CategoryGrid
import com.sddrozdov.presentation.screens.createAdScreens.SelectCategoryView
import com.sddrozdov.presentation.states.createAd.Category
import com.sddrozdov.presentation.states.createAd.CreateAdStates
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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

    @Test
    fun testSelectCategoryView_ErrorState() {
        val errorMessage = "Test error"
        val testState = CreateAdStates(error = errorMessage)

        rule.setContent {
            SelectCategoryView(
                state = testState,
                onEvent = {},
                onNavigateTo = {}
            )
        }

        rule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun testSelectCategoryView_SuccessState() {
        val categories = listOf(
            Category(id = 1, name = "Category 1"),
            Category(id = 2, name = "Category 2")
        )
        val testState = CreateAdStates(categories = categories)

        rule.setContent {
            SelectCategoryView(
                state = testState,
                onEvent = {},
                onNavigateTo = {}
            )
        }

        rule.onNodeWithText("Category 1").assertExists()
        rule.onNodeWithText("Category 2").assertExists()
    }

    @Test
    fun testNextButton_DisabledWhenNoCategorySelected() {
        rule.setContent {
            SelectCategoryView(
                state = CreateAdStates(),
                onEvent = {},
                onNavigateTo = {}
            )
        }

        rule.onNodeWithText("Далее").assertIsNotEnabled()
    }

    @Test
    fun testNextButton_EnabledWhenCategorySelected() {
        val testState = CreateAdStates(selectedCategoryId = 1L)

        rule.setContent {
            SelectCategoryView(
                state = testState,
                onEvent = {},
                onNavigateTo = {}
            )
        }

        rule.onNodeWithText("Далее").assertIsEnabled()
    }

    @Test
    fun testCategoryGrid_DisplaysAllCategories() {
        val categories = List(10) { index ->
            Category(id = index, name = "Category $index")
        }

        rule.setContent {
            CategoryGrid(
                categories = categories,
                selectedCategoryId = null,
                onCategorySelected = {}
            )
        }

        categories.forEach { category ->
            rule.onNodeWithText(category.name).assertExists()
        }
    }

}
