package com.mapcok

import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kakao.sdk.common.KakaoSdk
import com.mapcok.databinding.ActivityMainBinding
import com.mapcok.ui.base.BaseActivity
import com.kakao.sdk.common.util.Utility

private const val TAG = "MainActivity"

class MainActivity : BaseActivity<ActivityMainBinding>(R    .layout.activity_main) {

    private lateinit var navHostFragment: NavHostFragment

    override fun init() {
        initNavigation()
        KakaoSdk.init(this, "{040d7aff43138a91fc66d8f6b4d573ea}")
        //KakaoMapSdk.init(this, "{040d7aff43138a91fc66d8f6b4d573ea}");
    }


    private fun initNavigation() { // 네비게이션 세팅
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        with(binding) {
            bottomNavigationMain.apply {
                setupWithNavController(navHostFragment.navController)
            }
        }

    }
}
