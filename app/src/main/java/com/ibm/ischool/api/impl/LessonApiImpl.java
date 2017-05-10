package com.ibm.ischool.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.ischool.api.ILessonApi;
import com.ibm.ischool.api.IResponseApi;
import com.ibm.ischool.dao.LessonDao;
import com.ibm.ischool.entity.ChapterEntity;
import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.entity.WordEntity;
import com.ibm.ischool.util.Base64Util;
import com.ibm.ischool.util.FileUtils;
import com.ibm.ischool.util.HttpUtils;
import com.ibm.ischool.util.ResourceUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import cz.msebera.android.httpclient.Header;

public class LessonApiImpl implements ILessonApi{

	LessonDao mDao;
	Context mContext;
	int count = 1;
	
	public LessonApiImpl(Context context) {
		this.mContext = context;
		this.mDao = new LessonDao(mContext);
	}
	
	@Override
	public void getLessonsChapterId(final ChapterEntity chapterEntity, final IResponseApi<List<LessonEntity>> api) {
		RequestParams params = new RequestParams();
		HttpUtils.httpAsyncGet("/lesson?chapterId=1&mode=" + chapterEntity.getChapterMode(), params, new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
				List<LessonEntity> entities = new ArrayList<LessonEntity>();
				try {
					for (int i = 0; i < array.length(); i++) {
						LessonEntity entity = new LessonEntity();
						JSONObject object = array.getJSONObject(i);
						entity.setLessonId(object.getString("id"));
						entity.setLessonContent(object.getString("lessonContent"));
						entity.setLessonStar(object.getInt("lessonStar"));
						entity.setLessonNum(object.getString("lessonNum"));
						entity.setChapterId(chapterEntity.getChapterId());
						entity.setTranslation(object.getString("translation"));
						entity.setUrl(object.getString("url"));
						entity.setLevelId(chapterEntity.getLevelId());
						entities.add(mDao.getLessonScore(entity));
					}
					api.onSuccess(entities);
				} catch (Exception e) {
					api.onFailure("数据转换失败");
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				api.onFailure(responseString);
			}
		});
	}

	@Override
	public void uploadRecordFile(String lessonId, String filePath, final IResponseApi<List<WordEntity>> api) {
		RequestParams params = new RequestParams();
		try {
			// 当有file时,content-type为multipart/form-data,
			//当无file时为application/x-www-form-urlencoded(可直接用request.getParameter)
			//application/x-www-form-urlencoded，不能用于文件上传； 只有使用了multipart/form-data，才能完整的传递文件数据
			params.put("file", Base64Util.encode(FileUtils.getBytes(filePath)));
			params.put("lessonId", lessonId);
			HttpUtils.httpAsyncPost("/upload", params, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject objectTest) {
					try {
						JSONObject object = null;
						if (count == 1) {
							object = new JSONObject(ResourceUtils.geFileFromAssets(mContext, "data1.json"));
						}else if (count == 2) {
							object = new JSONObject(ResourceUtils.geFileFromAssets(mContext, "data2.json"));
						}else {
							object = objectTest;
						}
						List<WordEntity> entities = new ArrayList<WordEntity>();
						JSONArray scoreArray = object.getJSONArray("score");
						JSONArray silenceArray = object.getJSONArray("silence");
						for (int i = 0; i < scoreArray.length(); i++) {
							WordEntity entity = new WordEntity();
							entity.setScore(scoreArray.getDouble(i));
							entity.setSilence(silenceArray.getString(i));
							entities.add(entity);
						}
						count ++;
						api.onSuccess(entities);
					} catch (Exception e) {
						api.onFailure("数据转换失败");
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					api.onFailure(responseString);
				}
			});
		} catch (Exception e) {
			
		}
	}

}
