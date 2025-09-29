package com.openclassroom.vitesse.utils

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.KeyboardType
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassroom.vitesse.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LabelledTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorMessageIsDisplayed_whenIsErrorTrue() {
        val errorMessage = "Champ obligatoire"
        composeTestRule.setContent {
            LabelledTextField(
                labelId = R.string.app_name,
                value = "",
                onValueChange = {},
                isError = true,
                errorMessage = errorMessage,
                keyboardType = KeyboardType.Text
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun noErrorMessage_whenIsErrorFalse() {
        composeTestRule.setContent {
            LabelledTextField(
                labelId = R.string.app_name,
                value = "",
                onValueChange = {},
                isError = false,
                errorMessage = "",
                keyboardType = KeyboardType.Text
            )
        }

        composeTestRule.onNode(hasText("")).assertExists()
        composeTestRule.onNodeWithText(R.string.app_name.toString()).assertExists()
        composeTestRule.onNodeWithText("Champ obligatoire").assertDoesNotExist()
    }
}
