package udemy.appdev.complaintscompiler

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DeveloperInfoScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A2E))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Developer Info \uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        containerColor = Color(0xFF1A1A2E)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlipCardSimplified()
        }
    }
}

@Composable
fun FlipCardSimplified() {
    val isFlipped = remember { mutableStateOf(false) }
    val rotationY by animateFloatAsState(
        targetValue = if (isFlipped.value) 180f else 0f,
        animationSpec = tween(500),
        label = "cardRotation"
    )
    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
            .height(420.dp)
            .padding(horizontal = 24.dp)
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 8 * density
            }
            .clickable { isFlipped.value = !isFlipped.value }
    ) {
        // Front
        if (rotationY <= 90f) {
            FrontCardSimple()
        } else {
            Box(modifier = Modifier.graphicsLayer { this.rotationY = 180f }) {
                BackCardSimple()
            }
        }
    }
}


@Composable
fun FrontCardSimple() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1F1B4D), Color(0xFF2C2B5E), Color(0xFF1A1A2E))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                "👨‍💻 Parth Chaudhary",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "🛠 Android Developer | 🧪 Kotlin & Jetpack Compose | 🔥 ML Enthusiast",
                color = Color(0xFF80DEEA),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Tech Stack \uD83D\uDD27: Jetpack Compose, Kotlin, MVVM, Material3, Firebase Backend",
                fontSize = 14.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                listOf("💻", "📱", "🎨", "🧠", "🚀", "🧪", "🔥").forEach {
                    Text(it, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }
        }
    }
}

@Composable
fun BackCardSimple() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF11224D), Color(0xFF1A1A2E))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.dev),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(18.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Why I built Complaints Compiler \uD83D\uDEE0\uFE0F:",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight =FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Inspired by grassroots realities, this app envisions a centralized and transparent complaint system bridging citizens and administration.\n\n" +
                    "Born from rural roots, it empowers everyday voices through structured, accountable, and accessible tech.", color = Color(0xFFCCCCCC), fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Email \uD83D\uDCE7: parthch3008@gmail.com",
                color = Color(0xFF80DEEA),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}