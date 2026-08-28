package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppTier
import com.example.data.model.LicenseKeyValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Image Identifier", appName)
  }

  @Test
  fun `validate mathematical license key generation and verification`() {
    val lifetimeKey = LicenseKeyValidator.generateValidKey(AppTier.LIFETIME)
    val verifiedTier = LicenseKeyValidator.validateKey(lifetimeKey)
    assertEquals(AppTier.LIFETIME, verifiedTier)

    val proKey = LicenseKeyValidator.generateValidKey(AppTier.PRO)
    assertEquals(AppTier.PRO, LicenseKeyValidator.validateKey(proKey))

    val supporterKey = LicenseKeyValidator.generateValidKey(AppTier.SUPPORTER)
    assertEquals(AppTier.SUPPORTER, LicenseKeyValidator.validateKey(supporterKey))

    // Invalid key
    assertNull(LicenseKeyValidator.validateKey("INVALID_KEY1"))
  }
}
