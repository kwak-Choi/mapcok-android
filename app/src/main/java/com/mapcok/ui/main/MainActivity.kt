package com.mapcok.ui.main

import MyPostViewModel
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navHostFragment: NavHostFragment
    private val ACCESS_FINE_LOCATION = 1000
    private val photoViewModel: MyPostViewModel by viewModels()


    // 권한 체크
//    private fun permissionCheck() {
//        val preference = getPreferences(MODE_PRIVATE)
//        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck!", true)
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // 권한이 없는 상태
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            ) {
//                // 권한 거절
//                val builder = AlertDialog.Builder(this)
//                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
//                builder.setPositiveButton("확인") { dialog, which ->
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION
//                    )
//                }
//                builder.setNegativeButton("취소") { dialog, which ->
//
//                }
//                builder.show()
//            } else {
//                if (isFirstCheck) {
//                    // 최초 권한 요청
//                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION
//                    )
//                } else {
//                    val builder = AlertDialog.Builder(this)
//                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
//                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
//                        val intent = Intent(
//                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                            Uri.parse("package:$packageName")
//                        )
//                        startActivity(intent)
//                    }
//                    builder.setNegativeButton("취소") { dialog, which ->
//
//                    }
//                    builder.show()
//                }
//            }
//        } else {
//
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == ACCESS_FINE_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "위치 권한이 승인되었습니다", Toast.LENGTH_SHORT).show()
//
//            } else {
//                Toast.makeText(this, "위치 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()
//
//            }
//        }
//    }

    fun onClickListener(view: View) {
        val tag = view.getTag() as MyPagePhoto
        photoViewModel.setSelectedPhoto(tag)
        val newFragment = MyPostFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_main, newFragment)
            .addToBackStack(null)
            .commit()
    }

    //GPS 확인
    private fun checkLocationService(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun init() {
//        if (checkLocationService()) {
//            permissionCheck()
//        }
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
}