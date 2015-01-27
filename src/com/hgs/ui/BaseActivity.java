package com.hgs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

/**************************************************************************
 * 
 * @author made by yi 2014-7-16
 * copyright (C)2014-2024 cmyy-inc.All Rights Reserved
 **************************************************************************/

public class BaseActivity extends Activity{
	public CmyyApplication mApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (CmyyApplication) getApplication();
		mApplication.addActivity(this);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) { 
	    	finish();
	        return true;
	    } 
	    return super.onKeyDown(keyCode, event);
	}
	
}
