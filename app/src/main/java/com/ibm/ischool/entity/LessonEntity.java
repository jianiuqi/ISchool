package com.ibm.ischool.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class LessonEntity implements Parcelable{

	public static final String TAG = LessonEntity.class.getSimpleName();
	
	/**
	 * level级别id
	 */
	private String levelId;
	
	/**
	 * 关卡Id
	 */
	private String chapterId;
	
	/**
	 * 句子Id
	 */
	private String lessonId;
	
	/**
	 * 句子编号
	 */
	private String lessonNum;
	
	/**
	 * 句子内容
	 */
	private String lessonContent;
	
	/**
	 * 翻译
	 */
	private String translation;
	
	/**
	 * 句子获得的分数
	 */
	private int lessonScore;
	
	/**
	 * 句子获得的分数
	 */
	private int lessonStar;

	/**
	 * 音频Url
	 */
	private String url;
	
	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public String getLessonNum() {
		return lessonNum;
	}

	public void setLessonNum(String lessonNum) {
		this.lessonNum = lessonNum;
	}

	public String getLessonContent() {
		return lessonContent;
	}

	public void setLessonContent(String lessonContent) {
		this.lessonContent = lessonContent;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public int getLessonScore() {
		return lessonScore;
	}

	public void setLessonScore(int lessonScore) {
		this.lessonScore = lessonScore;
	}

	public int getLessonStar() {
		return lessonStar;
	}

	public void setLessonStar(int lessonStar) {
		this.lessonStar = lessonStar;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LessonEntity() {
		super();
	}
	
	public LessonEntity(Parcel in){
		levelId = in.readString();
		chapterId = in.readString();
		lessonId = in.readString();
		lessonNum = in.readString();
		lessonContent = in.readString();
		translation = in.readString();
		lessonScore = in.readInt();
		lessonStar = in.readInt();
		url = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(levelId);
		dest.writeString(chapterId);
		dest.writeString(lessonId);
		dest.writeString(lessonNum);
		dest.writeString(lessonContent);
		dest.writeString(translation);
		dest.writeInt(lessonScore);
		dest.writeInt(lessonStar);
		dest.writeString(url);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<LessonEntity> CREATOR
	   = new Creator<LessonEntity>() {
		
		@Override
		public LessonEntity[] newArray(int size) {
			return new LessonEntity[size];
		}
		
		@Override
		public LessonEntity createFromParcel(Parcel source) {
			return new LessonEntity(source);
		}
	};
}
