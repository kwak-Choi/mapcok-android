package com.mapcok

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mapcok.databinding.ActivityMainBinding
import com.mapcok.ui.base.BaseActivity
import com.mapcok.ui.mypost.MyPostFragment
import com.mapcok.ui.mypost.MyPostMenuFragment
import com.mapcok.ui.mypost.MyPostViewModel


private const val TAG = "MainActivity_싸피"
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    MyPostMenuFragment.OnEditOptionClickListener, MyPostMenuFragment.OnDeleteOptionClickListener {

    private lateinit var navHostFragment: NavHostFragment
    private val ACCESS_FINE_LOCATION = 1000
    private val photoViewModel: MyPostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkLocationService()){
            permissionCheck()   
        }
    }


    // 권한 체크
    private fun permissionCheck() {
        val preference = getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절
                val builder = AlertDialog.Builder(this)
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {

        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "위치 권한이 승인되었습니다", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "위치 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()

            }
        }
    }



    //GPS 확인
    private fun checkLocationService(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun init() {
        initNavigation()

    }


    private fun initNavigation() { // 네비게이션 세팅
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        with(binding) {
            bottomNavigationMain.apply {
                bottomNavigationMain.itemIconTintList = null
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
