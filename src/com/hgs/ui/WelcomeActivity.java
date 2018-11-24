package com.hgs.ui;
import java.util.ArrayList;

import com.hgs.adapter.SplashAdapter;
import com.hgs.ui.R;
import com.hgs.utils.Common;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**************************************************************************
 * 
 * @author made by yi 2014-7-16
 * copyright (C)2014-2024 cmyy-inc.All Rights Reserved
 **************************************************************************/
public class WelcomeActivity extends Activity implements Runnable{
    private ImageView       mImageView;
    private ViewPager       mViewPager;
	private ArrayList<View> mViews;
        Private int test=10;
	private SplashAdapter   mVpAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharep = getSharedPreferences(Common.mSharedPreferencesName,MODE_APPEND);
		int bfirst = sharep.getInt("bfirst_start",0);
		if( bfirst != 1 )
		{
            //首次使用显示向导欢迎页
			setContentView(R.layout.activity_splash);	
			mViewPager = (ViewPager) findViewById(R.id.viewpager);
			LayoutInflater inflater = LayoutInflater.from(this);
			mViews = new ArrayList<View>();
			mViews.add(inflater.inflate(R.layout.splash_one, null));
			mViews.add(inflater.inflate(R.layout.splash_two, null));
			mViews.add(inflater.inflate(R.layout.splash_three, null));
			mViews.add(inflater.inflate(R.layout.splash_four, null));
			mViews.add(inflater.inflate(R.layout.splash_five, null));
			mVpAdapter = new SplashAdapter(mViews,this);
	        mViewPager.setAdapter(mVpAdapter);
	        
			Editor editor = sharep.edit();
			editor.putInt("bfirst_start",1);
			editor.commit();
		}
		else
		{
			setContentView(R.layout.activity_welcome);
			mImageView = (ImageView)findViewById(R.id.image_bg);
			mImageView.setImageDrawable(getResources().getDrawable(R.drawable.welcome_bg));
			new Thread(this).start();
		}
	}
	
	@Override
	public void run() {	
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//进入主界面
		startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
		finish();
	}
}
