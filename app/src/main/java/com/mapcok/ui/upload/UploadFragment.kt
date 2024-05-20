package com.mapcok.ui.upload

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapcok.R
import com.mapcok.data.model.param.UserPhotoParam
import com.mapcok.databinding.FragmentUploadBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
import com.mapcok.ui.util.SingletonUtil
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

private const val TAG = "UploadFragment_싸피"

@AndroidEntryPoint
class UploadFragment : BaseFragment<FragmentUploadBinding>(R.layout.fragment_upload) {

    private val args: UploadFragmentArgs by navArgs()
    private val uploadPhotoViewModel: UploadPhotoViewModel by activityViewModels()

    override fun initView() {


        hideBottomNavigation()
        binding.backMap.setOnClickListener {
            findNavController().navigate(R.id.action_uploadFragment_to_mapFragment)
        }

        val imageUriString = args.imagePath
        val imageUri = Uri.parse(imageUriString)
        binding.setmyimage.setImageURI(imageUri)

        Log.d(TAG, "initView: 진짜로? ${uploadPhotoViewModel.location.value}")

        binding.saveBtn.setOnClickListener {

            Log.d(TAG, "initView: ㅠㅗ토ㅠㅏ람스 입문1차")
            Log.d(TAG, "initView: ${SingletonUtil.user}")
            val userId = SingletonUtil.user?.id ?: return@setOnClickListener
            Log.d(TAG, "initView: ㅠㅗ토ㅠㅏ람스 입문1차..")
            uploadPhotoViewModel.location.value?.let { location ->
                val (latitude, longitude) = location
                val photoParam = createUserPhotoParam(userId, imageUri, latitude, longitude)
                Log.d(TAG, "initView: 입문 2차")
                if (photoParam != null) {
                    uploadPhotoViewModel.addPhoto(photoParam)
                }
            }
        }

        uploadPhotoViewModel.photoAddedSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Log.d(TAG, "initView: 업로드 성")
                Toast.makeText(requireContext(), "사진 업로드 성공", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "initView: 업도르 실")
                Toast.makeText(requireContext(), "사진 업로드 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUserPhotoParam(userId: Int, imageUri: Uri, latitude: Double, longitude: Double): UserPhotoParam? {
        val context = requireContext()
        val file = getFileFromUri(context, imageUri) ?: return null

        val requestFile = RequestBody.create((context.contentResolver.getType(imageUri) ?: "image/*").toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("imageFile", file.name, requestFile)

        return UserPhotoParam(userId, body, latitude, longitude)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val filePathColumn = arrayOf(android.provider.MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) }

        cursor?.close()
        return filePath?.let { File(it) }
    }

    override fun onPause() {
        super.onPause()
        showBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
    }

    private fun hideBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility = View.VISIBLE
    }
}
