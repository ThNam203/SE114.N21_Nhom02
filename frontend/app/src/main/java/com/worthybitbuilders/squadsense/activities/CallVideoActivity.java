//package com.worthybitbuilders.squadsense.activities;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.worthybitbuilders.squadsense.R;
//import com.worthybitbuilders.squadsense.databinding.ActivityCallVideoBinding;
//
//import org.webrtc.AudioSource;
//import org.webrtc.AudioTrack;
//import org.webrtc.Camera2Enumerator;
//import org.webrtc.CameraEnumerator;
//import org.webrtc.EglBase;
//import org.webrtc.MediaConstraints;
//import org.webrtc.PeerConnection;
//import org.webrtc.PeerConnectionFactory;
//import org.webrtc.SurfaceTextureHelper;
//import org.webrtc.VideoCapturer;
//import org.webrtc.VideoSource;
//import org.webrtc.VideoTrack;
//
//import java.util.Objects;
//
//public class CallVideoActivity extends AppCompatActivity {
//    private static final int PERMISSIONS_REQUEST_CODE = 100;
//    public static final int VIDEO_RESOLUTION_WIDTH = 1280;
//    public static final int VIDEO_RESOLUTION_HEIGHT = 720;
//    public static final int FPS = 30;
//    private EglBase rootEglBase;
//    private MediaConstraints audioConstraints;
//    private MediaConstraints videoConstraints;
//    private MediaConstraints sdpConstraints;
//    private VideoSource videoSource;
//    private VideoTrack localVideoTrack;
//    private VideoTrack videoTrackFromCamera;
//    private AudioSource audioSource;
//    private AudioTrack localAudioTrack;
//    private SurfaceTextureHelper surfaceTextureHelper;
//    private ActivityCallVideoBinding binding;
//    private PeerConnectionFactory peerConnectionFactory;
//    private PeerConnection peerConnection;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityCallVideoBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        Objects.requireNonNull(getSupportActionBar()).hide();
//
//        checkPermissionsAndAccessCamera();
//    }
//
//    private void checkPermissionsAndAccessCamera() {
//        if (checkCameraPermissions()) {
//            initializeSurfaceView();
//            accessUserMedia();
//        } else {
//            requestCameraPermissions();
//        }
//    }
//
//    private boolean checkCameraPermissions() {
//        int cameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
//        int audioPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
//        return cameraPermission == PackageManager.PERMISSION_GRANTED && audioPermission == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestCameraPermissions() {
//        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
//        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSIONS_REQUEST_CODE) {
//            boolean allPermissionsGranted = true;
//            for (int result : grantResults) {
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    allPermissionsGranted = false;
//                    break;
//                }
//            }
//
//            if (allPermissionsGranted) {
//                // Proceed with accessing getUserMedia
//                accessUserMedia();
//            } else {
//                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
//                finish();
//            }
//        }
//    }
//
//    private void accessUserMedia() {
//        MediaConstraints videoConstraints = new MediaConstraints();
//        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"));
//        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"));
//
//        // Create a VideoCapturer
//        VideoCapturer videoCapturer = createCameraCapturer();
//
//        // Create a VideoSource
//        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
//
//        // Create a VideoTrack
//        VideoTrack localVideoTrack = peerConnectionFactory.createVideoTrack("video_track", videoSource);
//        localVideoTrack.addSink(binding.videoRenderer);
//
//        // Create an AudioSource
//        AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
//
//        // Create an AudioTrack
//        AudioTrack localAudioTrack = peerConnectionFactory.createAudioTrack("audio_track", audioSource);
//
//        // Use localVideoTrack and localAudioTrack as needed
//    }
//
//    private VideoCapturer createVideoCapturer() {
//        CameraEnumerator enumerator;
//        enumerator = new Camera2Enumerator(this);
//        final String[] deviceNames = enumerator.getDeviceNames();
//        String TAG = "CAMERAMAN";
//        // First, try to find front facing camera
//        Log.d(TAG, "Looking for front facing cameras.");
//        for (String deviceName : deviceNames) {
//            if (enumerator.isFrontFacing(deviceName)) {
//                Log.d(TAG, "Creating front facing camera capturer.");
//                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
//                if (videoCapturer != null) {
//                    return videoCapturer;
//                }
//            }
//        }
//        // Front facing camera not found, try something else
//        Log.d(TAG, "Looking for other cameras.");
//        for (String deviceName : deviceNames) {
//            if (!enumerator.isFrontFacing(deviceName)) {
//                Log.d(TAG, "Creating other camera capturer.");
//                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
//                if (videoCapturer != null) {
//                    return videoCapturer;
//                }
//            }
//        }
//
//        Toast.makeText(this, "Unable to find any cameras", Toast.LENGTH_LONG).show();
//        finish();
//        return null;
//    }
//
//    private void initializeSurfaceView() {
//        rootEglBase = EglBase.create();
//        binding.viewRenderer.init(rootEglBase.getEglBaseContext(), null);
//        binding.viewRenderer.setEnableHardwareScaler(true);
//        binding.viewRenderer.setMirror(true);
//    }
//
//    private void initializePeerConnectionFactory() {
//        PeerConnectionFactory.initialize(
//                PeerConnectionFactory
//                        .InitializationOptions
//                        .builder(this)
//                        .createInitializationOptions()
//        );
//
//        peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory();
//    }
//
//    private void createVideoTrackFromCameraAndShowIt() {
//        audioConstraints = new MediaConstraints();
//        VideoCapturer videoCapturer = createVideoCapturer();
//        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer);
//        videoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, FPS);
//
//        videoTrackFromCamera = factory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
//        videoTrackFromCamera.setEnabled(true);
//        videoTrackFromCamera.addRenderer(new VideoRenderer(binding.surfaceView));
//
//        //create an AudioSource instance
//        audioSource = factory.createAudioSource(audioConstraints);
//        localAudioTrack = factory.createAudioTrack("101", audioSource);
//
//    }
//}