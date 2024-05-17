package com.mapcok.ui.splash

import androidx.lifecycle.lifecycleScope
import com.mapcok.ui.main.MainActivity
import com.mapcok.R
import com.mapcok.databinding.ActivitySplashBinding
import com.mapcok.ui.base.BaseActivity
import com.mapcok.ui.login.LoginActivity
import com.mapcok.ui.util.firebaseAuthCheck
import com.mapcok.ui.util.initGoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

  override fun init() {
    lifecycleScope.launch {
      delay(600L)
      firebaseAuthCheck(
        complete = {
          initGoActivity(this@SplashActivity, MainActivity::class.java)
        },
        cancel = {
          initGoActivity(this@SplashActivity, LoginActivity::class.java)
        }
      )
    }
  }
}
