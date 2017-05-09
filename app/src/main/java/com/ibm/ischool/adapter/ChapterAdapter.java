package com.ibm.ischool.adapter;

import java.util.List;

import com.ibm.ischool.R;
import com.ibm.ischool.base.Constant;
import com.ibm.ischool.entity.ChapterEntity;
import com.ibm.ischool.ui.activity.LessonGridActivity_;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChapterAdapter extends RecyclingPagerAdapter{

	private List<ChapterEntity> mEntities;
	
	private Context mContext;
	
	public ChapterAdapter(Context context,List<ChapterEntity> entities){
		this.mContext = context;
		this.mEntities = entities;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		ViewHolder viewHolder = null;
		OnClick onClick = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.chapter_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.lessonNum = (TextView) convertView.findViewById(R.id.lesson_num);
			viewHolder.lessonTip = (TextView) convertView.findViewById(R.id.lesson_tip);
			viewHolder.startLesson = (Button) convertView.findViewById(R.id.start_lesson_btn);
			viewHolder.lessonProgress = (ProgressBar) convertView.findViewById(R.id.lesson_progress);
			viewHolder.lockMaskLayout = (RelativeLayout) convertView.findViewById(R.id.lock_mask_rl);
			viewHolder.currentNeedStar = (TextView) convertView.findViewById(R.id.current_star_number);
			onClick = new OnClick();
			viewHolder.startLesson.setOnClickListener(onClick);
			convertView.setTag(viewHolder);
			convertView.setTag(viewHolder.startLesson.getId(), onClick);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
			onClick = (OnClick) convertView.getTag(viewHolder.startLesson.getId());
		}
		onClick.setPosition(position);
		ChapterEntity entity = mEntities.get(position);
		viewHolder.lessonNum.setText(entity.getChapterNum());
		viewHolder.lessonTip.setText(entity.getChapterTip());
		viewHolder.lessonProgress.setMax(Constant.TOTAL_LESSON * 3);
		viewHolder.lessonProgress.setProgress(entity.getChapterStar());
		int needStar = (int) (position*Constant.TOTAL_LESSON * 3 * 0.6);
		if (entity.getLevelStar() > needStar) {
			viewHolder.currentNeedStar.setText(needStar + "/" + needStar);
		}else {
			viewHolder.currentNeedStar.setText(entity.getLevelStar() + "/" + needStar);
		}
		if (position == 0) {
			viewHolder.startLesson.setClickable(true);
			viewHolder.lockMaskLayout.setVisibility(View.GONE);
		}else {
			if (entity.getLevelStar() < needStar) {
				viewHolder.startLesson.setClickable(false);
				viewHolder.lockMaskLayout.setVisibility(View.VISIBLE);
			}else {
				viewHolder.startLesson.setClickable(true);
				viewHolder.lockMaskLayout.setVisibility(View.GONE);
			}
		}
		// 区分闯关和跟读模式
		if (entity.getChapterMode() == Constant.PASS_MODE) {
			viewHolder.startLesson.setText("闯关");
		}else {
			viewHolder.startLesson.setText("跟读");
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return mEntities.size();
	}
	
	static class ViewHolder{
		TextView lessonNum;
		TextView lessonTip;
		TextView currentNeedStar;
		Button startLesson;
		ProgressBar lessonProgress;
		RelativeLayout lockMaskLayout;
	}
	
	private class OnClick implements OnClickListener {

		private int position;
		
		public void setPosition(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, LessonGridActivity_.class);
			intent.putExtra(ChapterEntity.TAG, mEntities.get(position));
			mContext.startActivity(intent);
		}
		
	}
}
