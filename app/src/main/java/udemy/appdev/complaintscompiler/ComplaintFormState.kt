package udemy.appdev.complaintscompiler

data class ComplaintFormState(
    val problemTitle: String = "",
    val description: String = "",
    val category: String = "",
    val locationText: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
)