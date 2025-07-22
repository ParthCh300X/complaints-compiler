package udemy.appdev.complaintscompiler


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayReportsScreen(
    navController: NavController,
    adminViewModel: AdminViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val complaints by adminViewModel.complaints.collectAsState()
    val selectedComplaint = remember { mutableStateOf<Complaints?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showDialog = remember { mutableStateOf(false) }


    val categoryList = listOf(
        "Plumbing", "Electrical", "Road Damage", "Garbage Collection",
        "Street Lights", "Water Supply", "Internet Issues", "Noise Complaint",
        "Safety Concern", "Park Maintenance", "Tree Cutting", "Other"
    )


    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2C2B5E),
            Color(0xFF1D1B4C),
            Color(0xFF11224D)
        )
    )

    // ✅ Show Bottom Sheet if a complaint is selected
    selectedComplaint.value?.let { complaint ->
        val formattedTime = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            .format(Date(complaint.timestamp))
        val statusColor = when (complaint.status.lowercase()) {
            "pending" -> Color.Red
            "in-progress" -> Color.Yellow
            "finished" -> Color.Green
            else -> Color.Gray
        }

        ModalBottomSheet(
            onDismissRequest = { selectedComplaint.value = null },
            sheetState = sheetState,
            containerColor = Color(0xFF1A1A2E)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                val statusOptions = listOf("pending", "in-progress", "finished")

                val expanded = remember { mutableStateOf(false) }
                val selectedStatus = remember { mutableStateOf(complaint.status) }

                val categoryList = listOf(
                    "Plumbing", "Electrical", "Road Damage", "Garbage Collection",
                    "Street Lights", "Water Supply", "Internet Issues", "Noise Complaint",
                    "Safety Concern", "Park Maintenance", "Tree Cutting", "Other"
                )
                var expandedC = remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = !expanded.value }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedStatus.value,
                        onValueChange = {},
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.uppercase()) },
                                onClick = {
                                    selectedStatus.value = status
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }

                val updatedTitle = remember { mutableStateOf(complaint.problemTitle) }

                TextField(
                    value = updatedTitle.value,
                    onValueChange = { updatedTitle.value = it },
                    label = { Text("\uD83D\uDD16 Title") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        containerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                val updatedCategory = remember { mutableStateOf(complaint.category) }

                ExposedDropdownMenuBox(
                    expanded = expandedC.value,
                    onExpandedChange = { expandedC.value = !expandedC.value }) {
                    TextField(
                        value = updatedCategory.value,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text(
                                text = "\uD83D\uDCC2 Category"
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedC.value) },

                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expandedC.value,
                        onDismissRequest = { expandedC.value = false }) {
                        categoryList.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    updatedCategory.value = category
                                    expandedC.value = false
                                })
                        }

                    }
                }

                val updatedLocation = remember { mutableStateOf(complaint.locationText) }

                TextField(
                    value = updatedLocation.value,
                    onValueChange = { updatedLocation.value = it },
                    label = { Text("\uD83D\uDCCD Location") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        containerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("🕒 $formattedTime", color = Color.LightGray)
                Text("📌 ${complaint.status.uppercase()}", color = statusColor)

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    TextButton(onClick = {
                        val updated = complaint.copy(
                            problemTitle = updatedTitle.value,
                            category = updatedCategory.value,
                            locationText = updatedLocation.value,
                            status = selectedStatus.value // from dropdown
                        )
                        adminViewModel.updateComplaint(complaint.id, updated)
                        selectedComplaint.value = null // close bottom sheet
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Cyan)
                        Text("Save", color = Color.Cyan)
                    }
                    TextButton(onClick = {
                        showDialog.value = true
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        Text("Delete", color = Color.Red)
                    }
                }
            }
        }
    }
    if (showDialog.value && selectedComplaint.value != null) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text("Confirm Delete", color = Color.White)
            },
            text = {
                Text("Are you sure you want to delete this complaint?", color = Color.White)
            },
            confirmButton = {
                TextButton(onClick = {
                    adminViewModel.deleteComplaint(selectedComplaint.value!!)
                    showDialog.value = false
                    selectedComplaint.value = null

                }) {
                    Text("DELETE", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF1A1A2E),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1A1A2E))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Admin Home Button
                    TextButton(
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("admin")
                        }
                    ) {
                        Text(text = "Admin Home 🏠", color = Color.White)
                    }

                    // Developer Info Button
                    TextButton(
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("dev_info")
                        }
                    ) {
                        Text(text = "Developer Info ⚙", color = Color.White)
                    }

                    // Logout Button
                    TextButton(
                        onClick = {
                            scope.launch { drawerState.close() }
                            authViewModel.logout()
                            navController.navigate("login")
                        }
                    ) {
                        Text("Logout ⏻", color = Color.Red)
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Welcome Admin 🛡") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
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

            val selectedCategories = remember { mutableStateOf(setOf<String>()) }
            val expandedF = remember { mutableStateOf(false) }

            val filteredComplaints = if (selectedCategories.value.isEmpty()) {
                complaints
            } else {
                complaints.filter { it.category in selectedCategories.value }
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // Filter Toggle Button
                Button(
                    onClick = { expandedF.value = !expandedF.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Sort via Category")
                }

                // Multi-Select Dropdown Menu
                DropdownMenu(
                    expanded = expandedF.value,
                    onDismissRequest = { expandedF.value = false },
                    modifier = Modifier.background(Color(0xFF1A1A2E))
                ) {
                    categoryList.forEach { category ->
                        val isSelected = selectedCategories.value.contains(category)
                        DropdownMenuItem(
                            text = {
                                Row {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.Green else Color.Gray
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(category, color = Color.White)
                                }
                            },
                            onClick = {
                                selectedCategories.value =
                                    if (isSelected)
                                        selectedCategories.value - category
                                    else
                                        selectedCategories.value + category
                            }
                        )
                    }
                }

                // Show selected filters and Reset
                if (selectedCategories.value.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Filtering: ${selectedCategories.value.joinToString()}",
                            color = Color.White,
                            modifier = Modifier.alignByBaseline()
                        )
                        TextButton(onClick = {
                            selectedCategories.value = emptySet()
                        }) {
                            Text("Reset Filters", color = Color.Cyan)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Complaint List
                LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
                    items(filteredComplaints) { complaint ->
                        val formattedTime =
                            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
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
                                .padding(vertical = 8.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        selectedComplaint.value = complaint
                                    })
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2B5E)),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("🔖 Title: ${complaint.problemTitle}", color = Color.White)
                                Text("📂 Category: ${complaint.category}", color = Color.White)
                                Text("📝 Description: ${complaint.description}", color = Color.White)
                                Text("📍 Location: ${complaint.locationText}", color = Color.White)
                                Text("🕒 Time: $formattedTime", color = Color.LightGray)
                                Text(
                                    text = "📌 Status: ${complaint.status.uppercase()}",
                                    color = statusColor,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}