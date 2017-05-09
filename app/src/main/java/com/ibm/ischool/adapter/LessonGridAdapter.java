package com.ibm.ischool.adapter;

import java.util.List;

import com.ibm.ischool.R;
import com.ibm.ischool.base.Constant;
import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.ui.activity.LessonDetailActivity_;
import com.ibm.ischool.ui.activity.TrackingActivity_;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LessonGridAdapter extends BaseAdapter {

	private List<LessonEntity> mEntities;
	
	private Context mContext;

	private int mLearnMode;
	
	public LessonGridAdapter(Context context, List<LessonEntity> entities, int learnMode){
		this.mContext = context;
		this.mEntities = entities;
		this.mLearnMode = learnMode;
	}

	@Override
	public int getCount() {
		return mEntities.size();
	}
	
	@Override
	public Object getItem(int position) {
		return mEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		ViewHolder viewHolder = null;
		OnClick onClick = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lesson_grid_item_layout, null);
			viewHolder = new ViewHolder();
			onClick = new OnClick();
			viewHolder.lessonGridNum = (TextView) convertView.findViewById(R.id.lesson_detail_number);
			viewHolder.detailEntryLayout = (RelativeLayout) convertView.findViewById(R.id.detail_entry_layout);
			viewHolder.starGet1 = (ImageView) convertView.findViewById(R.id.star_get_1);
			viewHolder.starGet2 = (ImageView) convertView.findViewById(R.id.star_get_2);
			viewHolder.starGet3 = (ImageView) convertView.findViewById(R.id.star_get_3);
			viewHolder.detailEntryLayout.setOnClickListener(onClick);
			convertView.setTag(viewHolder);
			convertView.setTag(viewHolder.detailEntryLayout.getId(), onClick);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
			onClick = (OnClick) convertView.getTag(viewHolder.detailEntryLayout.getId());
		}
		onClick.setPosition(position);
		LessonEntity entity = mEntities.get(position);
		viewHolder.lessonGridNum.setText(entity.getLessonNum());
		int starNum = entity.getLessonStar();
		if (starNum == 0) {
			viewHolder.starGet1.setImageResource(R.drawable.icon_star_grey);
			viewHolder.starGet2.setImageResource(R.drawable.icon_star_grey);
			viewHolder.starGet3.setImageResource(R.drawable.icon_star_grey);
		}else if (starNum == 1) {
			viewHolder.starGet1.setImageResource(R.drawable.icon_star_get);
			viewHolder.starGet2.setImageResource(R.drawable.icon_star_grey);
			viewHolder.starGet3.setImageResource(R.drawable.icon_star_grey);
		}else if (starNum == 2) {
			viewHolder.starGet1.setImageResource(R.drawable.icon_star_get);
			viewHolder.starGet2.setImageResource(R.drawable.icon_star_get);
			viewHolder.starGet3.setImageResource(R.drawable.icon_star_grey);
		}else if (starNum >= 3) {
			viewHolder.starGet1.setImageResource(R.drawable.icon_star_get);
			viewHolder.starGet2.setImageResource(R.drawable.icon_star_get);
			viewHolder.starGet3.setImageResource(R.drawable.icon_star_get);
		}
		return convertView;
	}

	static class ViewHolder{
		TextView lessonGridNum;
		ImageView starGet1;
		ImageView starGet2;
		ImageView starGet3;
		RelativeLayout detailEntryLayout;
	}
	
	private class OnClick implements OnClickListener{

		private int position;
		
		public void setPosition(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			if (mLearnMode == Constant.PASS_MODE) {
				intent.setClass(mContext, LessonDetailActivity_.class);
			}else {
				intent.setClass(mContext, TrackingActivity_.class);
			}
			intent.putExtra(LessonEntity.TAG, mEntities.get(position));
			mContext.startActivity(intent);
		}
		
	}
}
