package com.hgs.adapter;

import java.util.ArrayList;

import com.hgs.ui.GuideActivity;
import com.hgs.ui.R;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class SplashAdapter extends PagerAdapter {
    private ArrayList<View> views;
    private Activity mActivity;
    public SplashAdapter (ArrayList<View> views, Activity activity){
        this.views = views;
        mActivity = activity;
    }
       
	/**
	 * 获得当前界面数
	 */
	@Override
	public int getCount() {
		 if (views != null) {
             return views.size();
         }      
         return 0;
	}

	/**
	 * 初始化position位置的界面
	 */
    @Override
    public Object instantiateItem(View view, int position) {
       
        ((ViewPager) view).addView(views.get(position), 0);
        if ( position == views.size() - 1) {
        	//为开始体验按钮设置响应
        	Button startButton = (Button) view.findViewById(R.id.btn_start);
        	startButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity,GuideActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }

            });
        }
        return views.get(position);
    }
    
    /**
	 * 判断是否由对象生成界面
	 */
	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return (view == arg1);
	}

	/**
	 * 销毁position位置的界面
	 */
    @Override
    public void destroyItem(View view, int position, Object arg2) {
        ((ViewPager) view).removeView(views.get(position));       
    }
}
