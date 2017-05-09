package com.ibm.ischool.dao;

import com.ibm.ischool.entity.ChapterEntity;
import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.entity.LevelEntity;
import com.ibm.ischool.util.DBUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LessonDao {

	private DBUtils mDbUtils;
	
	public LessonDao(Context context){
		this.mDbUtils = new DBUtils(context);
	}
	
	/**
	 * 添加课程记录
	 * @param entity
	 */
	public void addLesson(LessonEntity entity){
		SQLiteDatabase database = this.mDbUtils.getWritableDatabase();
		Object[] data = new Object[5];
		data[0] = entity.getLevelId();
		data[1] = entity.getChapterId();
		data[2] = entity.getLessonId();
		data[3] = entity.getLessonScore();
		data[4] = entity.getLessonStar();
		Object[] deleteData = new Object[3];
		deleteData[0] = entity.getLevelId();
		deleteData[1] = entity.getChapterId();
		deleteData[2] = entity.getLessonId();
		database.execSQL("delete from lesson_detail where levelId=? and chapterId=? and lessonId=?", deleteData);
		database.execSQL("insert into lesson_detail(levelId,chapterId,lessonId,lessonScore,lessonStar) values(?,?,?,?,?)", data);
		database.close();
	}
	
	public void replaceLesson(LessonEntity entity){
		SQLiteDatabase database = this.mDbUtils.getWritableDatabase();
		Object[] data = new Object[5];
		data[0] = entity.getLevelId();
		data[1] = entity.getChapterId();
		data[2] = entity.getLessonId();
		data[3] = entity.getLessonScore();
		data[4] = entity.getLessonStar();
		database.execSQL("replace into lesson_detail(levelId,chapterId,lessonId,lessonScore,lessonStar) values(?,?,?,?,?)", data);
		database.close();
	}
	
	/**
	 * 修改课程得分信息
	* @param entity
	*/
	public void updateLessonInfo(LessonEntity entity) {
		SQLiteDatabase database = this.mDbUtils.getWritableDatabase();
		Object[] arrayOfObject = new Object[2];
		arrayOfObject[0] = entity.getLessonScore();
		arrayOfObject[1] = entity.getLessonStar();
		arrayOfObject[2] = entity.getLevelId();
		arrayOfObject[3] = entity.getChapterId();
		arrayOfObject[4] = entity.getLessonId();
		database.execSQL("update lesson_detail set lessonScore=?,lessonStar=? where levelId=? and chapterId=? and lessonId=?", arrayOfObject);
		database.close();
	}
	
	/**
	 * 获取Level的星星个数和总分数
	 * @param entity
	 * @return
	 */
	public LevelEntity getLevelScore(LevelEntity entity){
		SQLiteDatabase database = this.mDbUtils.getWritableDatabase();
		Cursor cursor = database.rawQuery("select lessonScore, lessonStar from lesson_detail " +
		  		"where levelId=?", new String[]{entity.getLevelId()});
		int levelScore = 0;
		int levelStar = 0;
		while (cursor.moveToNext()) {
			levelScore += cursor.getInt(cursor.getColumnIndex("lessonScore"));
			levelStar += cursor.getInt(cursor.getColumnIndex("lessonStar"));
		}
		entity.setLevelScore(levelScore);
		entity.setLevelStar(levelStar);
		database.close();
		return entity;
	}
	
	/**
	 * 获取Chapter的星星个数和总分数
	 * @param entity
	 * @return
	 */
	public ChapterEntity getChapterScore(ChapterEntity entity){
		SQLiteDatabase database = this.mDbUtils.getWritableDatabase();
		Cursor cursor = database.rawQuery("select lessonScore, lessonStar from lesson_detail " +
		  		"where levelId=? and chapterId=?", new String[]{entity.getLevelId(), entity.getChapterId()});
		int chapterScore = 0;
		int chapterStar = 0;
		while (cursor.moveToNext()) {
			chapterScore += cursor.getInt(cursor.getColumnIndex("lessonScore"));
			chapterStar += cursor.getInt(cursor.getColumnIndex("lessonStar"));
		}
		entity.setChapterScore(chapterScore);
		entity.setChapterStar(chapterStar);
		database.close();
		return entity;
	}
	
	/**
	 * 获取每个Lesson获得的分数
	 * @param entity
	 * @return
	 */
	public LessonEntity getLessonScore(LessonEntity entity){
		SQLiteDatabase database = this.mDbUtils.getWritableDatabase();
		Cursor cursor = database.rawQuery("select lessonScore, lessonStar from lesson_detail " +
		  		"where levelId=? and chapterId=? and lessonId=?", new String[]{entity.getLevelId(), entity.getChapterId(), entity.getLessonId()});
		int lessonScore = 0;
		int lessonStar = 0;
		while (cursor.moveToNext()) {
			lessonScore += cursor.getInt(cursor.getColumnIndex("lessonScore"));
			lessonStar += cursor.getInt(cursor.getColumnIndex("lessonStar"));
		}
		entity.setLessonScore(lessonScore);
		entity.setLessonStar(lessonStar);
		database.close();
		return entity;
	}
	
	public void deleteLessonInfo(){
		SQLiteDatabase database = this.mDbUtils.getWritableDatabase();
		database.execSQL("delete from lesson_detail");
		database.execSQL("update sqlite_sequence SET seq = 0 where name ='lesson_detail'");
		database.close();
	}
}
