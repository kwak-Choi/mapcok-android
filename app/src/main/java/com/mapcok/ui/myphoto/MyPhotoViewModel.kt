import android.provider.ContactsContract.Contacts.Photo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapcok.data.model.PostData

class MyPhotoViewModel : ViewModel() {
    val _selectedPhoto = MutableLiveData<PostData>()
    val selectedPhoto: LiveData<PostData>
        get() = _selectedPhoto

    fun setSelectedPhoto(photo: PostData) {
        _selectedPhoto.value = photo
    }
}
