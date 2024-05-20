package com.mapcok.ui.splash

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mapcok.R
import com.mapcok.databinding.ActivitySplashBinding
import com.mapcok.ui.base.BaseActivity
import com.mapcok.ui.login.LoginActivity
import com.mapcok.ui.main.MainActivity
import com.mapcok.ui.util.firebaseAuthCheck
import com.mapcok.ui.util.initGoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

  private val splashViewModel: SplashViewModel by viewModels()

  override fun init() {
    observeLoginSuccess()
    checkUser()
  }

  private fun checkUser(){
    lifecycleScope.launch {
      delay(600L)
      firebaseAuthCheck(
        complete = {
          splashViewModel.getUserInfo(it)
        },
        cancel = {
          initGoActivity(this@SplashActivity, LoginActivity::class.java)
        }
      )
    }
  }

  private fun observeLoginSuccess() {
    splashViewModel.loginSuccess.observe(this) {
      if (it) {
        initGoActivity(this@SplashActivity, MainActivity::class.java)
      }else{
        initGoActivity(this@SplashActivity, LoginActivity::class.java)
      }
    }
  }
}
