package com.enhance.encoder.input.video;

public interface CameraCallbacks {
  void onCameraChanged(boolean isFrontCamera);
  void onCameraError(String error);
}
