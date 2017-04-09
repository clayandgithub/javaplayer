package com.clayoverwind.javaplayer.model;

import java.util.Random;

public class SingleDanMu {
	public static final int MAX_LINE_COUNT = 20;

	private static final Random RANDOM = new Random(System.currentTimeMillis());
	
	private long mStartTime;
	
	private int mType;
	
	private String mContent;

	private String mColorHexStr;

	private int mLineNo;
	
	public SingleDanMu(String attrInfo, String content) {
		String[] attrs = attrInfo.split(",");
		mStartTime = Math.round(Double.parseDouble(attrs[0])* 1000);
		mColorHexStr = attrs[6];
		mLineNo = RANDOM.nextInt(MAX_LINE_COUNT);
		mType = Integer.parseInt(attrs[1]);
		mContent = content;
	}

	public long getStartTime() {
		return mStartTime;
	}

	public void setStartTime(long mStartTime) {
		this.mStartTime = mStartTime;
	}

	public int getType() {
		return mType;
	}

	public void setType(int mType) {
		this.mType = mType;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String mContent) {
		this.mContent = mContent;
	}

	public int getLineNo() {
		return mLineNo;
	}

	public String getColorHexStr() {
		return mColorHexStr;
	}
}
