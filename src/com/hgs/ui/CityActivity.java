package com.hgs.ui;

import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.hgs.adapter.CityAdapter;
import com.hgs.ui.R;
import com.hgs.utils.Common;
import com.hgs.widget.BaiduMapControl;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

//城市界面
public class CityActivity extends BaseActivity implements OnItemClickListener{
	private BaiduMapControl            mBaiduMapControl;
	private RelativeLayout             mBackRelative;
	private StickyListHeadersListView  mCityList;
	private CityAdapter                mCityAdapter;
	private ArrayList<String>          mCityDataList;//已开通城市列表
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		initViewMsg();
	}
	
	void initViewMsg()
	{
		mBackRelative = (RelativeLayout)findViewById(R.id.relativelayout_back);
		mBackRelative.setOnClickListener(new Button.OnClickListener()
		{
		    @Override
		     public void onClick(View arg0) {
                   finish();
	          	}
		 });
		
		mBaiduMapControl = new BaiduMapControl();
	    mBaiduMapControl.Location(getApplicationContext(),new BDLocationListener()
	    {
			@Override
			public void onReceiveLocation(BDLocation location) {
				String city = location.getCity(); //获取城市
				//把当前城市写入配置缓存
				SharedPreferences sharep;
				Editor editor;
				sharep = getSharedPreferences(Common.mSharedPreferencesName,MODE_APPEND);
				editor = sharep.edit();
				editor.putString("city",city);	
				editor.putString("address",location.getAddrStr());	
				editor.commit();
				Toast.makeText(CityActivity.this, city, Toast.LENGTH_LONG).show();
				//开通服务城市
				mCityDataList = new ArrayList<String>();
				mCityDataList.add(city);
				mCityDataList.add("南昌");
				mCityDataList.add("北京");
				mCityDataList.add("上海");
				mCityDataList.add("广州");
				mCityDataList.add("深圳");
				mCityDataList.add("崇仁");
				
				mCityList = (StickyListHeadersListView) findViewById(R.id.list_city);
				mCityList.setOnItemClickListener(CityActivity.this);
				mCityAdapter = new CityAdapter(CityActivity.this, mCityDataList);
				mCityList.setAdapter(mCityAdapter);	
				mBaiduMapControl.stopLocation();
			}
 	
	    });  
	}
	
 	@Override
 	public void onItemClick(AdapterView<?> parent, View view, int position,
 			long id) {

 	}
}
