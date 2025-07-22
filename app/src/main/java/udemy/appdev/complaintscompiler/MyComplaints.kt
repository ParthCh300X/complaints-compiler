package udemy.appdev.complaintscompiler

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyComplaints(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val complaints by userViewModel.userComplaints.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "My Complaints \uD83D\uDCCB") },
                navigationIcon = {
                                 IconButton(onClick = { navController.popBackStack() }) {
                                     Icon(imageVector = Icons.Default.Close, contentDescription = "close", tint = Color.Red)
                                 }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2C2B5E),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF1A1A2E)
    ) {
        innerPadding ->
        if(complaints.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No complaints submitted yet!!", color = Color.LightGray)
            }
        }
        else{
            LazyColumn(modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)){
                items(complaints) {
                    complaint -> ComplaintCard(complaint)
                }
            }
        }
    }
}
@Composable
fun ComplaintCard(complaint: Complaints) {
    val formattedTime = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        .format(Date(complaint.timestamp))
    val statusColor = when (complaint.status.lowercase()) {
        "pending" -> Color.Red
        "in-progress" -> Color.Yellow
        "finished" -> Color.Green
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2B5E)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🔖 Title: ${complaint.problemTitle}", color = Color.White)
            Text("📂 Category: ${complaint.category}", color = Color.White)
            Text("📍 Location: ${complaint.locationText}", color = Color.White)
            Text("🕒 Time: $formattedTime", color = Color.LightGray)
            Text(
                "📌 Status: ${complaint.status.uppercase()}",
                color = statusColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
