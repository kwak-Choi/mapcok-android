package com.mapcok.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapcok.R
import com.mapcok.ui.util.getSizeY

abstract class BaseBottomSheetFragment<T : ViewDataBinding>(private val layoutResId: Int, private val percent : Int) :
  BottomSheetDialogFragment() {
  private var _binding: T? = null
  val binding get() = _binding!!



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NORMAL, R.style.DialogStyle)
  }



  override fun onStart() {
    super.onStart()
    setupRatio(dialog as BottomSheetDialog)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
    binding.lifecycleOwner = viewLifecycleOwner

    return binding.root
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViewCreated()
  }


  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  abstract fun initViewCreated()

  fun getBottomSheetDialogDefaultHeight(percetage: Int): Int {
    return getSizeY(requireActivity()) * percetage / 100
  }

  fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
    val bottomSheet =
      bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
    val layoutParams = bottomSheet.layoutParams
    layoutParams.height = getBottomSheetDialogDefaultHeight(percent)
    bottomSheet.layoutParams = layoutParams
    behavior.apply {
      state = BottomSheetBehavior.STATE_EXPANDED
      isDraggable = false
    }
  }



}