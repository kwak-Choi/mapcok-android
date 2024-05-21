package com.mapcok.ui.mypost

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapcok.R
import timber.log.Timber

class MyPostMenuFragment : BottomSheetDialogFragment() {

    private val myPostViewModel: MyPostViewModel by activityViewModels()

    private var editlistener: OnEditOptionClickListener? = null

    private var dellistener : OnDeleteOptionClickListener?=null

    interface OnEditOptionClickListener {
        fun onEditOptionClicked()
    }

    interface OnDeleteOptionClickListener{
        fun OnDeleteOptionClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditOptionClickListener) {
            editlistener = context
        } else {
            throw RuntimeException("$context must implement OnEditOptionClickListener")
        }
        if(context is OnDeleteOptionClickListener){
            dellistener = context
        }else {
            throw RuntimeException("$context must implement OnDeleteOptionClickListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            bottomSheet.setBackgroundResource(android.R.color.transparent)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_post_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editOption = view.findViewById<LinearLayout>(R.id.edit_option)
        val deleteOption = view.findViewById<LinearLayout>(R.id.delete_option)

        editOption.setOnClickListener {
            Timber.d("수정")
            editlistener?.onEditOptionClicked()
            dismiss()
        }

        deleteOption.setOnClickListener {
            Timber.d("삭제")
            dellistener?.OnDeleteOptionClicked()
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        editlistener = null
        dellistener = null
    }
}
