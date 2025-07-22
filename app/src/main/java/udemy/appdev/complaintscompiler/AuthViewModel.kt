package udemy.appdev.complaintscompiler

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel()
{
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _email = mutableStateOf("")
    val email : String get() = _email.value

    private val _password = mutableStateOf("")
    val password : String get() = _password.value

    var isAdmin = mutableStateOf(false)

    fun onEmailChange(newEmail:String){
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword:String){
        _password.value = newPassword
    }

    fun userLogin(email : String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    {
        if(email == "admin" && password == "123abc#$")
        {
            isAdmin.value = true
            onSuccess()
        }
        else{
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { isAdmin.value= false
                onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "Login Failed ⚠\uFE0F")
                }
        }
    }

    fun userSignup(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {onSuccess()}
            .addOnFailureListener {onFailure(it.message ?: "Signup Failed ⚠\uFE0F")}
    }

    fun logout(){
        auth.signOut()
    }
    fun currentUser() = auth.currentUser

    fun isLoggedIn() = auth.currentUser != null
}
