package com.openclassroom.vitesse.utils

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.Modifier
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CandidateImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun imageDisplaysCorrectlyWithValidUri() {
        val fakePhotoUri = "file:///fake/path/to/image.jpg"

        composeTestRule.setContent {
            CandidateImage(photoUriString = fakePhotoUri, modifier = Modifier)
        }

        composeTestRule.onNodeWithContentDescription("candidate picture").assertIsDisplayed()
    }

    @Test
    fun imageDisplaysPlaceholderWhenUriIsEmpty() {
        composeTestRule.setContent {
            CandidateImage(photoUriString = "", modifier = Modifier)
        }

        composeTestRule.onNodeWithContentDescription("No image").assertIsDisplayed()
    }
}
