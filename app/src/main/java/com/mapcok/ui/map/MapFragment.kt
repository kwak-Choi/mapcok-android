package com.mapcok.ui.map

import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.mapcok.R
import com.mapcok.databinding.FragmentMapBinding
import com.mapcok.ui.base.BaseFragment

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {
    override fun initView() {
        binding.mapView.start(MapLifeCycleCallback()){
            o
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }
}
