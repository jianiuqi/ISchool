package com.ibm.ischool.util;

import java.util.Timer;
import java.util.TimerTask;

import com.ibm.ischool.ui.view.HoloCircularProgressBar;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;

public class PlayerUtils implements OnBufferingUpdateListener, OnCompletionListener, MediaPlayer.OnPreparedListener {

	public MediaPlayer mMediaPlayer;
	private HoloCircularProgressBar mProgressbar;
	private Timer mTimer = new Timer();
	private OnPlayListener mListener;

	public PlayerUtils(HoloCircularProgressBar progressbar) {
		mProgressbar = progressbar;
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mTimer.schedule(mTimerTask, 0, 1);
	}

	/**
	 * 设置音频源并缓冲准备播放
	 * @param url
	 */
	public void setUrlPrepare(String url){
		try {
			mMediaPlayer.reset();  
			mMediaPlayer.setDataSource(url);
			mMediaPlayer.prepareAsync();
		} catch (Exception e) {
			
		}
	}
	
	public void setOnPlayListener(OnPlayListener listener){
		this.mListener = listener;
	}
	
	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mMediaPlayer == null)
				return;
			try {
				if (mMediaPlayer.isPlaying()) {
					handleProgress.sendEmptyMessage(0);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			if (mMediaPlayer != null) {
				float position = mMediaPlayer.getCurrentPosition();
				float duration = mMediaPlayer.getDuration();

				if (duration > 0) {
					mProgressbar.setProgress(position / duration);
				}
			}
		};
	};

	/**
	 * 播放前调用setUrlPrepare
	 */
	public void play() {
		mMediaPlayer.start();
	}

	public void pause() {
		mMediaPlayer.pause();
	}

	public void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	@Override
	/**
	 * 通过onPrepared播放
	 */
	public void onPrepared(MediaPlayer arg0) {
		if (mListener != null) {
			mListener.onPlayReady();
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		mProgressbar.setProgress(0);
		if (mListener != null) {
			mListener.onPlayFinish();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		
	}
	
	public interface OnPlayListener {
		
		/**
		 * 可以开始播放
		 */
		void onPlayReady();
		
		/**
		 * 语音播放结束
		 */
		void onPlayFinish();
	}
}
