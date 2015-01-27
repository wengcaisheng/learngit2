package com.hgs.ui;

import com.hgs.ui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**************************************************************************
 * 
 * @author made by yi 2014-7-16
 * copyright (C)2014-2024 cmyy-inc.All Rights Reserved
 * 买单
 **************************************************************************/

public class PayFragment extends Fragment{
	private RelativeLayout    mRecordRelative;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pay, null);
		initViewMsg(view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	void initViewMsg(View view)
	{
		mRecordRelative = (RelativeLayout)view.findViewById(R.id.relativelayout_record);
		mRecordRelative.setOnClickListener(new Button.OnClickListener()
		{
		    @Override
		     public void onClick(View arg0) {

	          	}
		 });
	}
	
}
