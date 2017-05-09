package com.ibm.ischool.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.ischool.base.Constant;
import com.ibm.ischool.util.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class WordEntity implements Parcelable{

	/**
	 * 单词序号
	 */
	private int index;
	
	/**
	 * 单词评分
	 */
	private double score;
	
	/**
	 * 单词开始帧
	 */
	private int beg;
	
	/**
	 * 单词结束帧
	 */
	private int end;
	
	/**
	 * 单词是否有停顿
	 */
	private String silence;
	
	/**
	 * 单词内容
	 */
	private String word;
	
	/**
	 * 是否警告(整个word大于0.2且syllabels中有一个小于0.2时警告)
	 */
	private boolean iswarning = false;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getBeg() {
		return beg;
	}

	public void setBeg(int beg) {
		this.beg = beg;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getSilence() {
		return silence;
	}

	public void setSilence(String silence) {
		this.silence = silence;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public boolean isIswarning() {
		return iswarning;
	}

	public void setIswarning(boolean iswarning) {
		this.iswarning = iswarning;
	}

	public WordEntity() {
		super();
	}
	
	public WordEntity(Parcel in){
		index = in.readInt();
		score = in.readDouble();
		beg = in.readInt();
		end = in.readInt();
		silence = in.readString();
		word = in.readString();
		iswarning = in.readByte() != 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(index);
		dest.writeDouble(score);
		dest.writeInt(beg);
		dest.writeInt(end);
		dest.writeString(silence);
		dest.writeString(word);
		dest.writeByte((byte) (iswarning ? 1 : 0));
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<WordEntity> CREATOR
	   = new Creator<WordEntity>() {
		
		@Override
		public WordEntity[] newArray(int size) {
			return new WordEntity[size];
		}
		
		@Override
		public WordEntity createFromParcel(Parcel source) {
			return new WordEntity(source);
		}
	};

	/**
	 * 获取课程所得的分数和星星数
	 * @return int[0]为分数; int[1]为获得的星星数
	 */
	public static int[] getLessonScoreAndStar(List<WordEntity> entities) {
		int[] lesson_score_star = new int[2];
		/* 对的个数
		double rightWords = 0;
		for (int i = 0; i < entities.size(); i++) {
			WordEntity wordEntity = entities.get(i);
			if (wordEntity.getScore() >= Constant.STANDARD_SCORE) {
				rightWords += 1;
			}
		}
		*/
		// 总分除以总个数
		double totalScore = 0;
		for (WordEntity entity : entities) {
			totalScore += entity.getScore();
		}
		DecimalFormat format = new DecimalFormat("##0.00");
		int score = (int) (Double.valueOf(format.format(totalScore/entities.size()))*100);
		lesson_score_star[0] = score;
		
		if (score >= 30 && score < 60) {
			lesson_score_star[1] = 1;
		}else if(score >= 60 && score < 90){
			lesson_score_star[1] = 2;
		}else if(score >= 90){
			lesson_score_star[1] = 3;
		}
		
		return lesson_score_star;
	}
	
	/**
	 * 解析返回的结果
	 * @param content
	 * @return
	 */
	public static List<WordEntity> getWordEntities(String content) {
		List<WordEntity> entities = new ArrayList<WordEntity>();
		try {
			JSONObject resultObj = new JSONObject(content);
			JSONArray wordsArray = resultObj.getJSONArray("words");
			boolean isSilence = false;
			for (int i = 0; i < wordsArray.length(); i++) {
				JSONObject wordObj = wordsArray.getJSONObject(i);
				if(wordObj.getInt("start") == 0 && 
						StringUtils.isEquals(wordObj.getString("word"), "<eps>")){
					continue;
				}else if (wordObj.getInt("start")!=0 && 
						StringUtils.isEquals(wordObj.getString("word"), "<eps>")){
					isSilence = true;
				}else {
					WordEntity entity = new WordEntity();
					entity.setBeg(wordObj.getInt("start"));
					entity.setWord(wordObj.getString("word"));
					entity.setScore(wordObj.getDouble("score"));
					if (isSilence) {
						entity.setSilence("Y");
						isSilence = false;
					}
					//单词总分大于标准分 且 音节小于标准分设置为警告
					if (entity.getScore() > Constant.STANDARD_SCORE) {
						JSONArray syllabelsArray = wordObj.getJSONArray("syllabels");
						for (int j = 0; j < syllabelsArray.length(); j++) {
							JSONObject syllabel = syllabelsArray.getJSONObject(j);
							if (syllabel.getDouble("score") < Constant.STANDARD_SYLLABELS) {
								entity.setIswarning(true);
							}
						}
					}
					entities.add(entity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entities;
	}
}
