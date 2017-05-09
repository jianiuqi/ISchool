package com.ibm.ischool.api;

import java.util.List;

import com.ibm.ischool.entity.ChapterEntity;
import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.entity.WordEntity;

public interface ILessonApi {

	/**
	 * 根据关卡获取课程列表
	 * @param chapterId
	 * @return
	 */
	void getLessonsChapterId(ChapterEntity chapterEntity, IResponseApi<List<LessonEntity>> api);
	
	/**
	 * 上传用户录音文件
	 * @param lessonId
	 * @param api
	 */
	void uploadRecordFile(String lessonId, String filePath, IResponseApi<List<WordEntity>> api);
}
