package com.openclassroom.vitesse.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.Modifier
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassroom.vitesse.data.BitmapLoader
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CandidateImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun imageDisplaysCorrectlyWithMockedBitmap() {
        val fakeBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

        val mockLoader = object : BitmapLoader {
            override fun loadBitmap(photoUriString: String, context: Context): Bitmap? {
                return fakeBitmap
            }
        }

        composeTestRule.setContent {
            CandidateImage(
                photoUriString = "any_string",
                modifier = Modifier,
                bitmapLoader = mockLoader
            )
        }

        composeTestRule.onNodeWithContentDescription("candidate picture")
            .assertIsDisplayed()
    }

    @Test
    fun imageDisplaysPlaceholderWhenUriIsEmpty() {
        composeTestRule.setContent {
            CandidateImage(
                photoUriString = "",
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithContentDescription("No image")
            .assertIsDisplayed()
    }
}
