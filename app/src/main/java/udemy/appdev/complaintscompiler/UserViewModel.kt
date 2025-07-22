package udemy.appdev.complaintscompiler

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _userComplaints = MutableStateFlow<List<Complaints>>(emptyList())
    val userComplaints: StateFlow<List<Complaints>> = _userComplaints.asStateFlow()

    init {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        db.collection("complaints")
            .whereEqualTo("userId", currentUserId) // 👈 Filter by logged-in user
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("UserViewModel", "Error fetching user complaints", error)
                    return@addSnapshotListener
                }

                val complaintList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Complaints::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                _userComplaints.value = complaintList
            }
    }
}