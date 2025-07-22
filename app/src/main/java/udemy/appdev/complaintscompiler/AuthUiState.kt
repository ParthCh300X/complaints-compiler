package udemy.appdev.complaintscompiler

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isSubmitting: Boolean=false
)