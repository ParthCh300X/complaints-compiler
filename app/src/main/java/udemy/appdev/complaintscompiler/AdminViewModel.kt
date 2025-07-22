package udemy.appdev.complaintscompiler

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminViewModel : ViewModel()
{
    private val db = FirebaseFirestore.getInstance()

    private val _complaints = MutableStateFlow<List<Complaints>>(emptyList())

    val complaints: StateFlow<List<Complaints>> = _complaints.asStateFlow()

    private var listener: ListenerRegistration?= null

    init {
        listener = db.collection("complaints").addSnapshotListener { snapshot,
                                                                     error ->
            if (error != null) {
                Log.e(
                    "AdminViewModel",
                    "Error fetching complaints",
                    error
                )
                return@addSnapshotListener
            }
            val complaintList = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Complaints::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            _complaints.value = complaintList
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    fun deleteComplaint(complaint: Complaints){
        db.collection("complaints")
            .document(complaint.id)
            .delete()
            .addOnSuccessListener { Log.d("AdminViewModel", "Complaint Deleted Successfully") }
            .addOnFailureListener{e -> Log.e("AdminViewModel", "Failed to delete complaint", e)}
    }

    fun updateComplaint(complaintId: String, updatedComplaint: Complaints) {
        val updateMap = mapOf(
            "problemTitle" to updatedComplaint.problemTitle,
            "category" to updatedComplaint.category,
            "locationText" to updatedComplaint.locationText,
            "status" to updatedComplaint.status,
            "timestamp" to updatedComplaint.timestamp,
            "userId" to updatedComplaint.userId
        )

        db.collection("complaints")
            .document(complaintId)
            .update(updateMap)
            .addOnSuccessListener {
                Log.d("AdminViewModel", "Complaint updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("AdminViewModel", "Error updating complaint", e)
            }
    }

}
