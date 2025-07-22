package udemy.appdev.complaintscompiler


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class ComplaintViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var state by mutableStateOf(ComplaintFormState())
        private set

    // ---- Field update functions ----
    fun onTitleChange(newTitle: String) {
        state = state.copy(problemTitle = newTitle)
    }

    fun onDescriptionChange(newDescription: String) {
        state = state.copy(description = newDescription)
    }

    fun onCategoryChange(newCategory: String) {
        state = state.copy(category = newCategory)
    }

    fun onLocationChange(newLocation: String) {
        state = state.copy(locationText = newLocation)
    }

    fun clearForm() {
        state = ComplaintFormState()
    }

    fun submitComplaint(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (state.problemTitle.isBlank() || state.description.isBlank()
            || state.category.isBlank() || state.locationText.isBlank()
        ) {
            onError("All fields are required.")
            return
        }
        val currentUser = FirebaseAuth.getInstance().currentUser
        val complaint = Complaints(
            userId = currentUser?.uid?:"",
            problemTitle = state.problemTitle,
            description = state.description,
            category = state.category,
            locationText = state.locationText,
        )

        state = state.copy(isSubmitting = true, errorMessage = null)

        viewModelScope.launch {
            db.collection("complaints")
                .add(complaint)
                .addOnSuccessListener {
                    Log.d("Firestore", "Complaint Submitted successfully!")
                    state = state.copy(isSubmitting = false)
                    clearForm()
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error submitting complaint", e)
                    state = state.copy(
                        isSubmitting = false,
                        errorMessage = e.message ?: "Unknown error"
                    )
                    onError(e.message ?: "Unknown error")
                }
        }
    }
}