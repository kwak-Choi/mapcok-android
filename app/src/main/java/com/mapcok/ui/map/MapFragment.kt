package com.mapcok.ui.map

import android.os.Bundle
import android.view.View
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.mapcok.R
import com.mapcok.databinding.FragmentMapBinding
import com.mapcok.ui.base.BaseFragment
import java.lang.Exception

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {
    private lateinit var mapView:MapView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(p0: Exception?) {
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
                binding.mapView.addView(mapView)
            }
        })


    }
    override fun initView() {


    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }
}
