package com.maxdejesus.noisenix.ui.submitSample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.chibde.visualizer.BarVisualizer
import com.maxdejesus.noisenix.R
import kotlinx.coroutines.delay

interface AudioManagerInput {
    fun startSound()
    fun stopSound()
    fun restartSound()
}

class AudioManager(private val context: Context) : AudioManagerInput {
    private var mediaPlayer: MediaPlayer? = null

    override fun startSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.sample_audio)
        }
        mediaPlayer?.seekTo(0)
        mediaPlayer?.start()
    }

    override fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun restartSound() {
        mediaPlayer?.seekTo(0)
        mediaPlayer?.start()
    }

    fun getAudioSessionId(): Int {
        return mediaPlayer?.audioSessionId ?: -1
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}

@Composable
fun SubmitSampleOverlay(onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        // Semi-transparent background overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            val context = LocalContext.current

            var hasAudioPermission by remember {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                )
            }
            var hasLocationPermission by remember {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                )
            }

            val requestAudioPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                hasAudioPermission = granted
            }

            val requestLocationPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                hasLocationPermission = granted
            }

            // Try requesting both permissions if not granted
            LaunchedEffect(Unit) {
                if (!hasAudioPermission) {
                    requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                if (!hasLocationPermission) {
                    requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            val allPermissionsGranted = hasAudioPermission && hasLocationPermission

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 10% top space
                Spacer(modifier = Modifier.weight(0.10f))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(0.50f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    if (allPermissionsGranted) {
                        // Permissions granted, proceed
                        AudioPlaybackContent(onClose = onClose)
                    } else {
                        // Permissions not fully granted yet
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Requesting audio & location permissions...",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (!hasLocationPermission) {
                                // If location not granted, offer a retry
                                Button(onClick = {
                                    requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }) {
                                    Text("Grant Location Permission")
                                }
                            }
                        }
                    }
                }

                // 40% bottom space
                Spacer(modifier = Modifier.weight(0.40f))
            }
        }
    }
}

@Composable
private fun AudioPlaybackContent(onClose: () -> Unit) {
    val context = LocalContext.current
    val audioManager = remember { AudioManager(context) }
    var remainingTime by remember { mutableStateOf(30) }
    var audioSessionId by remember { mutableStateOf(-1) }

    LaunchedEffect(Unit) {
        audioManager.startSound()
        val sessionId = audioManager.getAudioSessionId()
        if (sessionId != -1) {
            audioSessionId = sessionId
        }

        while (remainingTime > 0 && audioManager.isPlaying()) {
            delay(1000)
            remainingTime--
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioManager.stopSound()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recording Sample...",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        if (audioSessionId != -1) {
            AndroidView(
                factory = { ctx ->
                    BarVisualizer(ctx).apply {
                        setColor(ContextCompat.getColor(ctx, R.color.purple_200))
                        setDensity(70f)
                        setPlayer(audioSessionId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Initializing audio...")
            }
        }

        Text(
            text = "Time Left: $remainingTime s",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = {
                audioManager.restartSound()
                remainingTime = 30
            }) {
                Text("Restart")
            }
            Button(
                onClick = {
                    audioManager.stopSound()
                    onClose()
                },
                enabled = remainingTime == 0
            ) {
                Text("Submit")
            }
        }
    }
}