package udemy.appdev.complaintscompiler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintFormScreenWithScaffold(
    navController: NavController,
    viewModel: ComplaintViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📝 Submit Complaint") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Back", tint = Color.Red)
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
        ComplaintFormScreen(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            onSubmit = {
                viewModel.submitComplaint(
                    onSuccess = {
                        Toast.makeText(context, "✅ Complaint submitted!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onError = { error ->
                        Toast.makeText(context, "❌ $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }
}


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ComplaintFormScreen(
        modifier: Modifier = Modifier,
        viewModel: ComplaintViewModel,
        onSubmit: () -> Unit
    ) {
        val state = viewModel.state
        val categoryList = listOf(
            "Plumbing", "Electrical", "Road Damage", "Garbage Collection",
            "Street Lights", "Water Supply", "Internet Issues", "Noise Complaint",
            "Safety Concern", "Park Maintenance", "Tree Cutting", "Other"
        )

        var expandedW by remember { mutableStateOf(false) }

        val gradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF2C2B5E),
                Color(0xFF1D1B4C),
                Color(0xFF11224D)
            )
        )
        val context = LocalContext.current

        val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context)}
        DisposableEffect(Unit){
            onDispose {
                speechRecognizer.destroy()
            }
        }
        val isListening = remember { mutableStateOf(false) }

        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = {
                    isGranted ->
                if(isGranted){
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    }
                    speechRecognizer.setRecognitionListener(object : RecognitionListener{
                        override fun onResults(results: Bundle?) {
                            val spokenText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                                ?.firstOrNull() ?: ""
                            viewModel.onDescriptionChange(viewModel.state.description + " " + spokenText)
                            isListening.value = false
                        }
                        override fun onReadyForSpeech(params: Bundle?) {
                            isListening.value = true
                        }
                        override fun onBeginningOfSpeech() {}
                        override fun onRmsChanged(rmsdB: Float) {}
                        override fun onBufferReceived(buffer: ByteArray?) {}
                        override fun onEndOfSpeech() {
                            isListening.value = false
                        }
                        override fun onError(error: Int) {
                            isListening.value = false
                        }
                        override fun onPartialResults(partialResults: Bundle?) {}
                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })

                    speechRecognizer.startListening(intent)
                }
            }
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(brush = gradient)
                .border(1.dp, Color(0xFF7C4DFF), CutCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📝 Fill the form to submit your complaint",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = state.problemTitle,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("🔖 Title", color = Color.White) },
                placeholder = { Text("e.g. Water Leakage") },
                modifier = Modifier.fillMaxWidth(),
                shape = CutCornerShape(12.dp),
                colors = commonTextFieldColors()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    label = { Text("📝 Description", color = Color.White) },
                    placeholder = { Text("Describe your issue") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = CutCornerShape(12.dp),
                    colors = commonTextFieldColors()
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { launcher.launch(android.Manifest.permission.RECORD_AUDIO) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Mic",
                            tint = if (isListening.value) Color.Green else Color.White
                        )
                    }

                    if (isListening.value) {
                        Text(
                            text = "🎙 Listening...",
                            color = Color.Green,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expandedW,
                onExpandedChange = { expandedW = !expandedW }
            ) {
                OutlinedTextField(
                    value = state.category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Category 📂", color = Color.White) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedW) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = CutCornerShape(12.dp),
                    colors = commonTextFieldColors()
                )
                ExposedDropdownMenu(expanded = expandedW, onDismissRequest = { expandedW = false }) {
                    categoryList.forEach { category ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                viewModel.onCategoryChange(category)
                                expandedW = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.locationText,
                onValueChange = { viewModel.onLocationChange(it) },
                label = { Text("📍 Location", color = Color.White) },
                placeholder = { Text("Where is the issue?") },
                modifier = Modifier.fillMaxWidth(),
                shape = CutCornerShape(12.dp),
                colors = commonTextFieldColors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF)),
                enabled = !state.isSubmitting
            ) {
                Text(if (state.isSubmitting) "Submitting..." else "🚀 Submit", color = Color.White, fontSize = 16.sp)
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

