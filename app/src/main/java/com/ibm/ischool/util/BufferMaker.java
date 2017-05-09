package com.ibm.ischool.util;

import java.util.ArrayList;

public class BufferMaker {
	public final static int CAPCITY = 1024*4;
	private byte[] mBuffer = new byte[CAPCITY];
	private int mSize;

	public ArrayList<byte[]> makeBuffer(byte[] buffer, int len) {
		ArrayList<byte[]> result = new ArrayList<byte[]>(4);
		int begin = 0;
		int left = len;
		while (left > 0) {
			if (left <= CAPCITY - mSize) {
				int size = left;
				System.arraycopy(buffer, begin, mBuffer, mSize, size);
				mSize += size;
				begin += size;
				left -= size;
			} else {
				int size = CAPCITY - mSize;
				System.arraycopy(buffer, begin, mBuffer, mSize, size);
				result.add(mBuffer.clone());
				mSize = 0;
				begin += size;
				left -= size;
			}
		}
		return result;
	}

	public byte[] flush() {
		if (mSize > 0) {
			byte[] result = new byte[mSize];
			System.arraycopy(mBuffer, 0, result, 0, mSize);
			return result;
		}
		return null;
	}
}
