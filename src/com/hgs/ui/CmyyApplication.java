package com.hgs.ui;

import java.util.LinkedList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import android.app.Activity;
import android.app.Application;

public class CmyyApplication extends Application{
	private List<Activity> mActivityList=new LinkedList<Activity>();
	@Override
	public void onCreate() {
		    super.onCreate();
		    SDKInitializer.initialize(this);  //百度地图初始化
	}
	
	//为系统能够完全退出
	 public void addActivity(Activity activity){
			 mActivityList.add(activity);
    }
	 public void exit(){
			for(Activity activity:mActivityList)
			{
			   if(!activity.isFinishing())
			   {
				   activity.finish();
			   }
		    }
			int id=android.os.Process.myPid();
			if(id!=0){
				 android.os.Process.killProcess(id);
			}
	 }
}
