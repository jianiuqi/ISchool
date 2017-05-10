package com.ibm.ischool.util;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class CRLSpeechUtil {

	private static CRLSpeechUtil mInstance;
	private int audioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;
	private static int sampleRateInHz = 16000;
	//设置音频的录制声道，CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
	private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	private int bufferSizeInBytes = 0;
	private AudioRecord audioRecord;
	private boolean isRecord = false;
    private OnResultListener mListener;

	private CRLSpeechUtil() {

	}

	public synchronized static CRLSpeechUtil getInstance() {
		if (mInstance == null)
			mInstance = new CRLSpeechUtil();
		return mInstance;
	}

	public void setListener(OnResultListener listener){
		this.mListener = listener;
	}
	
	public void startRecog() {
		new Thread(new WebSocketThread()).start();
	}
	
	private void startRecording(WebSocketClient wc) {
		creatAudioRecord();
		audioRecord.startRecording();
		isRecord = true;
		AudioBuffer mAudioBuffer = new AudioBuffer(true);
		new Thread(new AudioRecordThread(mAudioBuffer)).start();
		new Thread(new SendBufferThread(mAudioBuffer, wc)).start();
	}

	public void stopRecording() {
		if (audioRecord != null) {
			isRecord = false;
			while (stopFinished == false) {
				try {
					Thread.sleep(20);
				} catch (Exception e) {
				}
			}
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
		}
	}

	private void creatAudioRecord() {
		bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
		audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
	}

	class AudioRecordThread implements Runnable {
		AudioBuffer audioBuffer;

		public AudioRecordThread(AudioBuffer _audioBuffer) {
			this.audioBuffer = _audioBuffer;
		}

		@Override
		public void run() {
			writeDateTOBuffer(audioBuffer);
		}
	}

	boolean stopFinished;

	private void writeDateTOBuffer(AudioBuffer audioBuffer) {

		stopFinished = false;
		byte[] audiodata = new byte[bufferSizeInBytes];
		int readsize = 0;
		try {
			while (isRecord == true) {
				readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
				if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
					audioBuffer.add(audiodata, readsize);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			audioBuffer.addFinish();
			stopFinished = true;
		}
	}

	class WebSocketThread implements Runnable {

		@Override
		public void run() {
			WebSocketClient client = null;
			String url = "ws://9.186.52.218:8082/voicetracking/api/decode";
			final boolean interim = true;
			final boolean vad = false;
			try {
				client = new WebSocketClient(new URI(url), new Draft_17()) {
					@Override
					public void onOpen(ServerHandshake handshakedata) {
						try {
							Thread.sleep(500);// 休眠一分钟
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						this.send("{\"action\": \"start\", \"format\": \"wave\", \"interim_results\": " + interim
								+ ", \"vad\": " + vad + "}");
					}

					@Override
					public void onMessage(String message) {
						try {
							JSONObject jsonObject = new JSONObject(message);
							String state = null;
							try {
								state = jsonObject.getString("state");
							} catch (Exception e) {

							}
							if (state == null) {
								String recogResult = jsonObject.getString("results");
								if (!StringUtils.isEmpty(recogResult)){
                                    mListener.onSuccess(recogResult);
								}
								return;
							}
							if (state != null && state.equals("listening")) {
								startRecording(this);
							} else if (state != null && state.equals("stopped")) {
								stopRecording();
								this.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Exception ex) {
						
					}

					@Override
					public void onClose(int code, String reason, boolean remote) {

					}
				};
				client.connectBlocking();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class SendBufferThread implements Runnable {
		AudioBuffer audioBuffer;
		WebSocketClient wc;

		public SendBufferThread(AudioBuffer _audioBuffer, WebSocketClient _wc) {
			this.audioBuffer = _audioBuffer;
			this.wc = _wc;
		}

		@Override
		public void run() {
			byte[] buffer;
			try {
				while ((buffer = audioBuffer.poll()) != null || isRecord) {
					wc.send(buffer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				audioBuffer.reset();
				audioBuffer = null;
				wc.send("{\"action\": \"stop\"}");
			}
		}
	}

	public interface OnResultListener {
		void onSuccess(String content);
		void onFailure();
	}
	
}
