package com.hgs.ui;
import com.hgs.ui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;


/**************************************************************************
 * 
 * @author made by yi 2014-7-16
 * copyright (C)2014-2024 cmyy-inc.All Rights Reserved
 **************************************************************************/

public class GuideActivity extends FragmentActivity{
	private Fragment[] mFragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		mFragments = new Fragment[4];
		mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.farment_main);   
		mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.farment_search); 
		mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.farment_order); 
		mFragments[3] = getSupportFragmentManager().findFragmentById(R.id.farment_my);     
		getSupportFragmentManager().beginTransaction().
		hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]).show(mFragments[0]).commit();		
	}
	
	public void mainClick(View view){
		getSupportFragmentManager().beginTransaction().hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]).show(mFragments[0]).commit();
	}
	public void phoneClick(View view){
		getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[2]).hide(mFragments[3]).show(mFragments[1]).commit();
	}
	public void accessoryClick(View view){
		getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[3]).show(mFragments[2]).commit();
	}
	public void cartClick(View view){
		getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).show(mFragments[3]).commit();
	}
	
}
