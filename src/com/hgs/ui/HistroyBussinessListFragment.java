package com.hgs.ui;

import com.hgs.ui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**************************************************************************
 * 
 * @author made by yi 2014-7-16
 * copyright (C)2014-2024 cmyy-inc.All Rights Reserved
 * 历史+收藏 店家页面
 **************************************************************************/

public class HistroyBussinessListFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_histroy, null);
	 	
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	
}
