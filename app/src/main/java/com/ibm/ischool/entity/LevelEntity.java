package com.ibm.ischool.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class LevelEntity  implements Parcelable{

	public static final String TAG = LevelEntity.class.getSimpleName();
	
	private String levelId;
	
	private int levelScore;
	
	private int levelStar;
	
	private String levelTip;

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public int getLevelScore() {
		return levelScore;
	}

	public void setLevelScore(int levelScore) {
		this.levelScore = levelScore;
	}

	public int getLevelStar() {
		return levelStar;
	}

	public void setLevelStar(int levelStar) {
		this.levelStar = levelStar;
	}

	public String getLevelTip() {
		return levelTip;
	}

	public void setLevelTip(String levelTip) {
		this.levelTip = levelTip;
	}
	
	public LevelEntity() {
		super();
	}
	
	public LevelEntity(Parcel in){
		levelId = in.readString();
		levelScore = in.readInt();
		levelStar = in.readInt();
		levelTip = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(levelId);
		dest.writeInt(levelScore);
		dest.writeInt(levelStar);
		dest.writeString(levelTip);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<LevelEntity> CREATOR
	   = new Creator<LevelEntity>() {
		
		@Override
		public LevelEntity[] newArray(int size) {
			return new LevelEntity[size];
		}
		
		@Override
		public LevelEntity createFromParcel(Parcel source) {
			return new LevelEntity(source);
		}
	};
}
