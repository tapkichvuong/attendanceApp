package com.atc.team10.attendancetracking.external.ui.page

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.atc.team10.attendancetracking.R
import com.atc.team10.attendancetracking.databinding.PageFaceDetectionBinding
import com.atc.team10.attendancetracking.external.controller.FaceDetectionController
import com.atc.team10.attendancetracking.utils.AppConstant.BundleKey.SESSION_ID
import com.atc.team10.attendancetracking.utils.AppExt.bytesToKB
import com.atc.team10.attendancetracking.utils.AppExt.disable
import com.atc.team10.attendancetracking.utils.AppExt.enable
import com.atc.team10.attendancetracking.utils.AppExt.getPreviousFragment
import com.atc.team10.attendancetracking.utils.AppExt.onClick
import com.atc.team10.attendancetracking.utils.AppExt.onClickSafely
import com.atc.team10.attendancetracking.utils.AppExt.sendLog
import com.atc.team10.attendancetracking.utils.AppExt.setupOnBackPressedCallback
import com.atc.team10.attendancetracking.utils.AppExt.showShortToast
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutionException


class FaceDetectionPage : PageFragment() {
    override val controller by viewModels<FaceDetectionController>()
    private lateinit var binding: PageFaceDetectionBinding
    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun getLayoutId() = R.layout.page_face_detection
    override fun initView(rootView: View, isRestore: Boolean) {
        binding = PageFaceDetectionBinding.bind(rootView)
        controller.sessionId = arguments?.getLong(SESSION_ID) ?: -1L
        binding.btnAttendance.disable()
        binding.root.onClickSafely {}
        binding.btnAttendance.onClick {
            takePicture()
        }
        onBackPressedCallback = requireActivity().setupOnBackPressedCallback {
            val currentPage = requireActivity().getPreviousFragment()
            if (currentPage is ViewSessionPage) {
                requireActivity().supportFragmentManager.popBackStack()
                currentPage.refresh()
            }
        }
        binding.ivBack.onClick {
            onBackPressedCallback.handleOnBackPressed()
        }
        initObserver()
    }

    private fun initObserver() {
        controller.isLoading.observe(viewLifecycleOwner) {
            showDialogLoading(it, "Verifying...")
        }
        /**
         * finish if success in here
         * otherwise, must have another image
         */
        controller.isJoined.observe(viewLifecycleOwner) {
            // Toast.makeText(requireContext(), "Is Join: $it", Toast.LENGTH_SHORT).show()
            if (controller.isFirstObserver) {
                controller.isFirstObserver = false
            } else {
                if (it) {
                    requireContext().showShortToast("Checked in successfully.")
                    onBackPressedCallback.handleOnBackPressed()
                } else {
                    requireContext().showShortToast("Verification failed, please try again.")
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.take_picture)
        binding.btnAttendance.enable()
    }

    private fun startCamera() {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
        preview.setSurfaceProvider(binding.cameraView.surfaceProvider)
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
        camera = cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageCapture)
    }

    private fun takePicture() {
        // Create a file to save the image
        // Create a file to save the image
        val outputDirectory = requireContext().externalCacheDir
        var photoFile = File(outputDirectory, System.currentTimeMillis().toString() + ".jpg")

        // Configure the output options for the ImageCapture use case

        // Configure the output options for the ImageCapture use case
        val outputFileOptions = OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: OutputFileResults) {
                    // Image capture successful, handle the saved image
                    // You can display the captured image or perform further actions here
                    playShutterSound()
                    sendLog("face detection - picture saved")

                    controller.imageFile = photoFile
                    controller.joinSession()
                    sendLog("face detection size: ${photoFile.length().bytesToKB()}KB")
                }

                override fun onError(exception: ImageCaptureException) {
                    // Image capture failed, handle the error
                    Toast.makeText(requireContext(), "Error capturing picture", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun playShutterSound() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        } else {
            mediaPlayer.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        onBackPressedCallback.remove()
    }
}