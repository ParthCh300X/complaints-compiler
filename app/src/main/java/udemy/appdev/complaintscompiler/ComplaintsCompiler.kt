package udemy.appdev.complaintscompiler

import android.app.Application
import com.google.firebase.FirebaseApp

class ComplaintsCompiler : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}