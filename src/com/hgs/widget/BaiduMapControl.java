package com.hgs.widget;
import android.content.Context;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;



//百度地图版本号v2_3_5 定位版本号4.2 
public class BaiduMapControl {
	//百度地图
	public static boolean m_bKeyRight = true;
    private LocationClient mLocationClient;
    private Context mContext;
    private LocationClientOption mOption;
    
    //定位
    public void Location(Context context,BDLocationListener locationListener) {
    	mContext = context;
    	mOption = new LocationClientOption();
    	//定位模式：高精度,低功耗，仅设备
    	//高精度下会同时使用gps,wifi和基站定位,返回的是当前条件下精度最好的定位结果
    	//低功耗下仅使用网络定位，即wifi和基站定位，还回的是当前条件下最好的网络结果
    	//设备定位模式下，只使用gps进行定位，由于gps芯片锁定需要时间，首次定位速度会需要一定的时候
    	mOption.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        //坐标系
    	//gcj02 国测局加密经纬度坐标
    	//bd09ll 百度加密经纬度坐标
    	//bd09 百度加密魔卡托坐标
    	mOption.setCoorType("bd09ll");//返回的定位结果是城市地理位置名称，默认值gcj02
    	mOption.setScanSpan(60000);//设置发起定位请求的间隔时间 最低1秒
    	mOption.setIsNeedAddress(true);
    	mLocationClient = new LocationClient(mContext);
    	mLocationClient.registerLocationListener(locationListener);
    	mLocationClient.setLocOption(mOption);
    	mLocationClient.start();
    	
    }
    
    public void stopLocation()
    {
    	mLocationClient.stop();
    }
    
}
