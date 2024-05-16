import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapcok.ui.mypage.MyPagePhoto

class MyPhotoViewModel : ViewModel() {
    val _selectedPhoto = MutableLiveData<MyPagePhoto>()
    val selectedPhoto: LiveData<MyPagePhoto>
        get() = _selectedPhoto

    fun setSelectedPhoto(photo: MyPagePhoto) {
        _selectedPhoto.value = photo
    }
}
