package com.example.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.model.ScanMode
import com.example.ui.theme.LocalCustomColors
import java.util.concurrent.Executors

@Composable
fun CameraViewfinder(
    scanMode: ScanMode,
    zoomFactor: Float,
    isTorchOn: Boolean,
    isFrontCamera: Boolean,
    onZoomChanged: (Float) -> Unit,
    onCaptureReady: (captureAction: (onBitmapCaptured: (Bitmap) -> Unit) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val customColors = LocalCustomColors.current

    var camera by remember { mutableStateOf<Camera?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(isFrontCamera) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val capture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()
            imageCapture = capture

            val cameraSelector = if (isFrontCamera) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            try {
                cameraProvider.unbindAll()
                val boundCamera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    capture
                )
                camera = boundCamera
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    LaunchedEffect(isTorchOn, camera) {
        camera?.cameraControl?.enableTorch(isTorchOn)
    }

    LaunchedEffect(zoomFactor, camera) {
        camera?.cameraInfo?.zoomState?.value?.let { zoomState ->
            val ratio = (zoomFactor / 8.0f) * (zoomState.maxZoomRatio - zoomState.minZoomRatio) + zoomState.minZoomRatio
            camera?.cameraControl?.setZoomRatio(ratio.coerceIn(zoomState.minZoomRatio, zoomState.maxZoomRatio))
        }
    }

    DisposableEffect(Unit) {
        onCaptureReady { onBitmapCaptured ->
            val capture = imageCapture
            if (capture != null) {
                capture.takePicture(
                    cameraExecutor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            val bitmap = imageProxyToBitmap(image)
                            image.close()
                            ContextCompat.getMainExecutor(context).execute {
                                onBitmapCaptured(bitmap)
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            exception.printStackTrace()
                        }
                    }
                )
            } else {
                // Fallback bitmap generation if preview not attached
                val dummy = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
                val canvas = android.graphics.Canvas(dummy)
                canvas.drawColor(android.graphics.Color.DKGRAY)
                onBitmapCaptured(dummy)
            }
        }

        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    val newZoom = (zoomFactor * zoom).coerceIn(1.0f, 8.0f)
                    onZoomChanged(newZoom)
                }
            }
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // HUD Scan Line Animation for "Is It Fake?" Scanner mode
        if (scanMode == ScanMode.FAKE_CHECK) {
            val infiniteTransition = rememberInfiniteTransition(label = "hud_scan")
            val scanOffset by infiniteTransition.animateFloat(
                initialValue = 0.05f,
                targetValue = 0.95f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scan_line_anim"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, customColors.accent)
                    .background(customColors.accentGlow.copy(alpha = 0.08f))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val y = size.height * scanOffset
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                customColors.accent,
                                customColors.highlight,
                                customColors.accent,
                                Color.Transparent
                            )
                        ),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 6.dp.toPx()
                    )
                }
            }
        }
    }
}

private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val buffer = image.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    val matrix = Matrix()
    matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
