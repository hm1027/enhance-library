package com.enhance.rtsp.rtp.packets

import com.enhance.rtsp.rtsp.RtpFrame


/**
 * Created by pedro on 7/11/18.
 */
interface AudioPacketCallback {
  fun onAudioFrameCreated(rtpFrame: RtpFrame)
}