package com.mapcok.ui.upload

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapcok.R
import com.mapcok.data.model.param.PostParam
import com.mapcok.databinding.FragmentUploadBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

import com.mapcok.ui.util.SingletonUtil
import timber.log.Timber

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
        Timber.d("확인 ${SingletonUtil.user}")
        val imageUriString = args.imagePath
        val imageUri = Uri.parse(imageUriString)
        Log.d(TAG, "initView: $imageUri, $imageUriString")
        binding.imgPost.setImageURI(imageUri)

        Log.d(TAG, "initView: 진짜로? ${uploadPhotoViewModel.location.value}")

        binding.saveBtn.setOnClickListener {


            val userId = SingletonUtil.user?.id
            val userData = SingletonUtil.user

            Log.d(TAG, "initView: ${userId} , ${userData}")
            if (userId == null || userData == null) {
                return@setOnClickListener
            }

            uploadPhotoViewModel.location.value?.let { location ->
                val (latitude, longitude) = location
                Log.d(TAG, "initView: $latitude, $longitude")
                val photoParam = createUserPhotoParam(userId, imageUri,latitude, longitude)

                Log.d(TAG, "initView: Photo Params initiation 2 - ${photoParam?.userId},${photoParam?.imageFile},${photoParam?.latitude},${photoParam?.longitude}")

                if (photoParam != null) {
                    uploadPhotoViewModel.registerPost(photoParam)
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

    private fun createUserPhotoParam(userId: Int, imageUri: Uri,latitude: Double, longitude: Double): PostParam? {
        val context = requireContext()
        val file = getFileFromUri(context, imageUri)
        if (file == null || !file.exists()) {
            Log.e(TAG, "createUserPhotoParam: File does not exist or cannot be retrieved")
            return null
        }
        val content = binding.etContent.text.toString()

        val contentType = context.contentResolver.getType(imageUri)
        if (contentType == null) {
            Log.e(TAG, "createUserPhotoParam: Content type is null")
            return null
        }

        Log.d(TAG, "createUserPhotoParam: File exists - ${file.absolutePath}")

        val requestFile = RequestBody.create(contentType.toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("imageFile", file.name, requestFile)

        Log.d(TAG, "createUserPhotoParam: RequestBody created - $body")

        return PostParam(userId, body, content, latitude, longitude)
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
