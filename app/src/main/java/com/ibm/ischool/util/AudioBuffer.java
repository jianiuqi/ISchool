package com.ibm.ischool.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeoutException;

public class AudioBuffer {
	private final Lock lock = new ReentrantLock();
	private final Condition empty = lock.newCondition();
	private Queue<byte[]> mQueue = new LinkedList<byte[]>();
	private int mSize = 0;
	private int OverTime=Integer.MAX_VALUE;

	private BufferMaker mBufferMaker;

	private boolean mWaitMoreData;

	public AudioBuffer(boolean waitMoreData) {
		mBufferMaker = new BufferMaker();
		mWaitMoreData = waitMoreData;
	}

	public void setWaitMoreData(boolean wait) {
		lock.lock();
		mWaitMoreData = wait;
		try {
			if (!wait) empty.signal();
		} finally {
			lock.unlock();
		}
	}

	public boolean isWaitMoreData() {
		return mWaitMoreData;
	}

	private void offer(byte[] buffer) {
		lock.lock();
		try {
			boolean result = mQueue.offer(buffer);
			if (result) {
				mSize++;
			}
			empty.signal();
		} finally {
			lock.unlock();
		}
	}

	public byte[] poll() throws TimeoutException {
		byte[] buffer = null;
		lock.lock();
		try {
			if (mSize == 0 && mWaitMoreData) {
				if(!empty.await(OverTime, TimeUnit.MILLISECONDS))
				{
					throw new TimeoutException();
				}
			}
			buffer = mQueue.poll();
			if (buffer != null) {
				mSize--;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) 
		{
			throw e;
		}
		finally {
			lock.unlock();
		}
		return buffer;
	}

	public int size() {
		return mSize;
	}

	public void reset() {
		lock.lock();
		try {
			mQueue.clear();
			mSize = 0;
			mWaitMoreData = true;
		} finally {
			lock.unlock();
		}
	}

	public void add(byte[] pdata, int length) {
		ArrayList<byte[]> result = mBufferMaker.makeBuffer(pdata, length);
		int size = result.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				offer(result.get(i));
			}
		}
	}

	public void addFinish() {
		byte[] result = mBufferMaker.flush();
		if (result != null) offer(result);
		setWaitMoreData(false);
	}
	
	public void setTimeOut(int ms)
	{
		this.OverTime=ms;
	}
}
