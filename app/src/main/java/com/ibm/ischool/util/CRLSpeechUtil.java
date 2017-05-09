package com.ibm.ischool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private OnResultListener listener;
	// AudioName裸音频数据文件 ，麦克风
	private String mAudioName = "";
	// NewAudioName可播放的音频文件
	private String mNewAudioName = "";
	
	private CRLSpeechUtil() {

	}

	public synchronized static CRLSpeechUtil getInstance() {
		if (mInstance == null)
			mInstance = new CRLSpeechUtil();
		return mInstance;
	}

	public void setListener(OnResultListener listener){
		this.listener = listener;
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

	private void stopRecording() {
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
		// 获取音频文件路径
		mAudioName = AudioFileUtils.getRawFilePath();
		mNewAudioName = AudioFileUtils.getWavFilePath();
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
			copyWaveFile(mAudioName, mNewAudioName);// 给裸数据加上头文件
		}
	}

	boolean stopFinished;

	private void writeDateTOBuffer(AudioBuffer audioBuffer) {

		stopFinished = false;
		byte[] audiodata = new byte[bufferSizeInBytes];
		int readsize = 0;
		FileOutputStream fos = null;
		try {
			File file = new File(mAudioName);
			if (file.exists()) {
				file.delete();
			}
			fos = new FileOutputStream(file);// 建立一个可存取字节的文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			while (isRecord == true) {
				readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
				if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
					audioBuffer.add(audiodata, readsize);
					// 保存文件
					if (fos != null) {
						fos.write(audiodata);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			audioBuffer.addFinish();
			stopFinished = true;
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}// 关闭写入流
			}
		}
	}

	class WebSocketThread implements Runnable {

		@Override
		public void run() {
			WebSocketClient client = null;
			String url = "ws://9.186.52.218:8082/voicetracking/api/decode";
			final boolean interim = false;
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
							System.out.println(jsonObject.toString());
							String state = null;
							try {
								state = jsonObject.getString("state");
							} catch (Exception e) {

							}
							if (state == null) {
								if (jsonObject.getBoolean("final")) {
									if (!StringUtils.isEmpty(jsonObject.getString("results"))) {
										
									}
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
	
	// 这里得到可播放的音频文件
	private void copyWaveFile(String inFilename, String outFilename) {
		FileInputStream in = null;
		FileOutputStream out = null;
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = AudioFileUtils.AUDIO_SAMPLE_RATE;
		int channels = 1;
		long byteRate = 16 * AudioFileUtils.AUDIO_SAMPLE_RATE * channels / 8;
		byte[] data = new byte[bufferSizeInBytes];
		try {
			in = new FileInputStream(inFilename);
			out = new FileOutputStream(outFilename);
			totalAudioLen = in.getChannel().size();
			totalDataLen = totalAudioLen + 36;
			WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
			while (in.read(data) != -1) {
				out.write(data);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。 为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav
	 * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有 自己特有的头文件。
	 */
	private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
			int channels, long byteRate) throws IOException {
		byte[] header = new byte[44];
		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8); // block align
		header[33] = 0;
		header[34] = 16; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
		out.write(header, 0, 44);
	}
}
