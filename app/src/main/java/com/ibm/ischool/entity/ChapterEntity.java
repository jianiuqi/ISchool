package com.ibm.ischool.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ChapterEntity implements Parcelable{

	public static final String TAG = ChapterEntity.class.getSimpleName();
	
	private String levelId;
	
	/**
	 * 关卡Id
	 */
	private String chapterId;
	
	/**
	 * 关卡编号
	 */
	private String chapterNum;
	
	/**
	 * 关卡提示
	 */
	private String chapterTip;
	
	/**
	 * 关卡获得的分数
	 */
	private int chapterScore;
	
	/**
	 * 关卡获得的星星数
	 */
	private int chapterStar;

	/**
	 * 当前Level获得的总星星数
	 */
	private int levelStar;

	/**
	 * 学习模式0闯关，1跟读
	 */
	private int chapterMode;

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public String getChapterNum() {
		return chapterNum;
	}

	public void setChapterNum(String chapterNum) {
		this.chapterNum = chapterNum;
	}

	public String getChapterTip() {
		return chapterTip;
	}

	public void setChapterTip(String chapterTip) {
		this.chapterTip = chapterTip;
	}

	public int getChapterScore() {
		return chapterScore;
	}

	public void setChapterScore(int chapterScore) {
		this.chapterScore = chapterScore;
	}

	public int getChapterStar() {
		return chapterStar;
	}

	public void setChapterStar(int chapterStar) {
		this.chapterStar = chapterStar;
	}

	public int getLevelStar() {
		return levelStar;
	}

	public void setLevelStar(int levelStar) {
		this.levelStar = levelStar;
	}

	public int getChapterMode() {
		return chapterMode;
	}

	public void setChapterMode(int chapterMode) {
		this.chapterMode = chapterMode;
	}

	public ChapterEntity() {
		super();
	}
	
	public ChapterEntity(Parcel in){
		levelId = in.readString();
		chapterId = in.readString();
		chapterNum = in.readString();
		chapterTip = in.readString();
		chapterScore = in.readInt();
		chapterStar = in.readInt();
		levelStar = in.readInt();
		chapterMode = in.readInt();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(levelId);
		dest.writeString(chapterId);
		dest.writeString(chapterNum);
		dest.writeString(chapterTip);
		dest.writeInt(chapterScore);
		dest.writeInt(chapterStar);
		dest.writeInt(levelStar);
		dest.writeInt(chapterMode);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<ChapterEntity> CREATOR
	   = new Creator<ChapterEntity>() {
		
		@Override
		public ChapterEntity[] newArray(int size) {
			return new ChapterEntity[size];
		}
		
		@Override
		public ChapterEntity createFromParcel(Parcel source) {
			return new ChapterEntity(source);
		}
	};
}
