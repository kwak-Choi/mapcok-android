package com.mapcok.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mapcok.R
import com.mapcok.databinding.ActivityMainBinding
import com.mapcok.ui.base.BaseActivity
import com.mapcok.ui.mypage.MyPagePhoto
import com.mapcok.ui.mypost.MyPostFragment
import com.mapcok.ui.mypost.MyPostMenuFragment
import com.mapcok.ui.mypost.MyPostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), MyPostMenuFragment.OnEditOptionClickListener,MyPostMenuFragment.OnDeleteOptionClickListener{

    private lateinit var navHostFragment: NavHostFragment


    override fun init() {
        initNavigation()
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

    override fun onEditOptionClicked() {
        val myPostFragment = supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.firstOrNull { it is MyPostFragment } as? MyPostFragment
        myPostFragment?.setupEditOption()
    }

    override fun OnDeleteOptionClicked() {
        val myPostFragment = supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.firstOrNull { it is MyPostFragment } as? MyPostFragment
        myPostFragment?.setupDeleteOption()
    }
}