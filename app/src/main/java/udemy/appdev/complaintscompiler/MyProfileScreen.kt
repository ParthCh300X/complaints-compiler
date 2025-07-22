package udemy.appdev.complaintscompiler

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(navController: NavController, userViewModel: UserViewModel = viewModel(), authViewModel: AuthViewModel = viewModel()) {

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "No Email"
    val uid = user?.uid ?: "No UID"
    val name = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("") }
    val isFirstTime = remember { mutableStateOf(true) }

    val complaints by userViewModel.userComplaints.collectAsState()

    val totalCount = complaints.size
    val pendingCount = complaints.count{it.status == "pending"}
    val inProgressCount = complaints.count{it.status == "in-progress"}
    val finishedCount = complaints.count{it.status == "finished"}


    LaunchedEffect(uid) {
        val doc = FirebaseFirestore.getInstance().collection("users").document(uid).get().await()
        val savedName = doc.getString("name")
        val savedGender = doc.getString("gender")

        if (savedName != null && savedGender != null) {
            name.value = savedName
            gender.value = savedGender
            isFirstTime.value = false // fix typo here too
        }
    }


    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2C2B5E),
            Color(0xFF1D1B4C),
            Color(0xFF11224D)
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Profile \uD83D\uDC64") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "back",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2C2B5E),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF1A1A2E)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isFirstTime.value) {
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it},
                        label = { Text(text = "Name \uD83D\uDCDB") },
                        placeholder = { Text("Your Name") },
                        shape = CutCornerShape(12.dp),
                        colors = commonTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Select Gender", color = Color.White)

                    Row {
                        RadioButton(
                            selected = gender.value == "M", onClick = { gender.value = "M" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Cyan)
                        )
                        Text(
                            text = "Male ♂\uFE0F",
                            color = Color.Blue,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        RadioButton(
                            selected = gender.value == "F", onClick = { gender.value = "F" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Cyan)
                        )
                        Text(
                            text = "Female ♀\uFE0F",
                            color = Color.Blue,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                    }

                    Button(
                        onClick = {
                            val userData = mapOf(
                                "name" to name.value,
                                "gender" to gender.value
                            )
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(uid)
                                .set(userData)
                                .addOnSuccessListener { isFirstTime.value = false }
                        },
                        enabled = name.value.isNotBlank() && gender.value.isNotBlank(),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Save", color = Color.White)

                    }
                } else {
                    val maleAv = listOf(R.drawable.avatar1, R.drawable.avatarm2, R.drawable.avatarm3)
                    val femaleAv = listOf(R.drawable.avatar2, R.drawable.avatarf2, R.drawable.avatarf3)
                    val avId = remember{
                        if(gender.value == "M"){
                            maleAv.random()
                        }
                        else{
                            femaleAv.random()
                        }
                    }
                    // Show profile info if already saved
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar based on gender
                        Image(
                            painter = painterResource(id = avId),
                            contentDescription = "avatar",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Name \uD83D\uDCC7: ${name.value}", color = Color.White)
                        Text("Gender: ${if (gender.value == "M") "Male \uD83D\uDC68" else "Female \uD83D\uDC69"}", color = Color.White)
                        Text("Email \uD83D\uDCE7: $email", color = Color.White)
                        Text("U\uD83C\uDD94: $uid", color = Color.White)

                        Spacer(modifier = Modifier.height(16.dp))


                        Text("Your Complaint Stats: ", color = Color.White, modifier = Modifier.padding(top = 24.dp))

                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .background(Color(0xFF2C2B5E))
                                .clip(RoundedCornerShape(12.dp))
                                .padding(16.dp)

                        ) {
                            Text(text = "Total Complaints Filed \uD83D\uDDD2\uFE0F: ${totalCount}", color = Color.White)
                            Text(text = "Pending Complaints ⌛: ${pendingCount}", color = Color.Red)
                            Text(text = "In-progress Complaints \uD83D\uDD04: ${inProgressCount}", color = Color.Yellow)
                            Text(
                                text = "Complaints finished ✅: $finishedCount",
                                color = Color(0xFF66FF99) // or try 0xFF00CC99
                            )                        }
                    }
            }    }
        }
    }
}

@Composable
private fun commonTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        disabledTextColor = Color.LightGray,
        cursorColor = Color.White,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.LightGray
    )
}