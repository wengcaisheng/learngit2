package com.hgs.ui;

import java.util.ArrayList;
import java.util.List;







import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hgs.adapter.ImageListAdapter;
import com.hgs.bean.Merchs;
import com.hgs.ui.R;
import com.hgs.utils.JsonParser;
import com.hgs.utils.Utility;


/**************************************************************************
 * 
 * @author made by yi 2014-7-16
 * copyright 
 * 主页
 **************************************************************************/

public class MainFragment extends Fragment{
	private long exitTime=0;
	private GridView gv_type;
	private ArrayList<Integer>types;
	private static final int MESSAGE_INIT_DATA_SUCCESS=1;
	
	private RelativeLayout    mAddressRelative;//地址栏
	private TextView          mAddressText;  //所在城市
	private ImageView         mTakeaway;
	private ImageView         localFruit;  //本地水果
	private ImageView         inportFruit;  //进口水果
	private ImageView         center_action;  //活动专区
	private ImageView         foretaste;//我要试吃
	
	
	private ImageView         iv_search;
	private WebView           mWebView;
	
	
	private List<Merchs> merchsList=null;  //商品信息
	private ImageListAdapter merchAdapter=null;   //商品信息适配器
	public static final String path = "http://192.168.2.99:8080/TuanShoppingServer/mypack/merchsAction_getAllMerchsList";
	private TextView tv_merchs_info;      //商品信息
	private ListView lv_like_shop;         //列表
	
	private int headerHeight; //头高度
    private int lastHeaderPadding;//最后一次调用Move Header的Padding
    private boolean isBack;  //从release转到null
    private int headerState=DONE;
    static final private int RELEASE_To_REFRESH = 0; // 释放刷新:一直下拉屏幕时显示
	static final private int PULL_To_REFRESH = 1; // 正在刷新：放开屏幕后显示
	static final private int REFRESHING = 2; // 正在刷新
	static final private int DONE = 3;
	
	
	private ScrollView sc;
	private LinearLayout globleLayout;
	private LinearLayout header;
	private Animation anim;
	private ImageView iv_anim_first;
	private ImageView iv_header_fresh_anim;
	private TextView tv_text;
	private AnimationDrawable ad;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, null);
	 	header=(LinearLayout) inflater.inflate(R.layout.first_header, null);
		initViewMsg(view);
	//	new Thread(new InitDataTask()).start();
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//	 	header=(LinearLayout) inflater.inflate(R.layout.first_header, null);
		new Thread(new InitDataTask()).start();
	}
		
	void initViewMsg(View view)
	{
		//加载默认地址
		mAddressText = (TextView)view.findViewById(R.id.txt_address);
		
		mAddressRelative = (RelativeLayout)view.findViewById(R.id.relativelayout_location);
		mAddressRelative.setOnClickListener(new Button.OnClickListener()
		{
		    @Override
		     public void onClick(View arg0) {
			      Intent intent = new Intent(getActivity(),CityActivity.class);
			      startActivityForResult(intent,0);
	          	}
		 });
		
		
		//初始化中间商品信息控件
		tv_merchs_info=(TextView)view.findViewById(R.id.tv_load_info);//正在努力加载一栏
		lv_like_shop=(ListView)view.findViewById(R.id.lv_like_shop);//滑动栏
		//ScrollView
		sc=(ScrollView)view.findViewById(R.id.sv_first_sc);
		//整体布局
		globleLayout=(LinearLayout)view.findViewById(R.id.globleLayout);
		//头部
		tv_text=(TextView)header.findViewById(R.id.tv_first_refresh_text);
        iv_header_fresh_anim=(ImageView)header.findViewById(R.id.iv_header_anim);
		iv_header_fresh_anim.setBackgroundResource(R.drawable.frame);
		ad=(AnimationDrawable)iv_header_fresh_anim.getBackground();
		//头部动画
		anim=AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
		//动画应用到的控件
		iv_anim_first=(ImageView)header.findViewById(R.id.iv_first_refresh);
		//计算头部高度
		measureView(header);
		headerHeight=header.getMeasuredHeight();
		lastHeaderPadding=(-1*headerHeight);
		header.setPadding(10, lastHeaderPadding, 0, 20);
		header.invalidate();
		
		
		//添加头部布局
		globleLayout.addView(header,0);
		anim.setFillAfter(true);//动画结束后保持动画
		//为ScrollView绑定监听器
		sc.setOnTouchListener(new View.OnTouchListener() {
			private int beginY;
			
					
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
				case MotionEvent.ACTION_MOVE:
				/**
				 * sc.getScrollY == 0 scrollview 滑动到头了 lastHeaderPadding >
				 * (-1*headerHeight) 表示header还没完全隐藏起来时 headerState !=
				 * REFRESHING正在刷新时
				 */
				if((sc.getScrollY()==0||lastHeaderPadding>(-1*headerHeight))
					&&headerState!=REFRESHING){
					//拿到滑动的Y轴距离
					int interval=(int)(event.getY()-beginY);
					//是向下滑动而不是向上滑动
					if(interval>0){
						interval=interval/2;//下滑阻力
						lastHeaderPadding=interval+(-1*headerHeight);
						header.setPadding(10, lastHeaderPadding, 0, 20);
						if(lastHeaderPadding>0)
						{
							//txView.setText("我要刷新咯");
							headerState=RELEASE_To_REFRESH;
							//是否已更新UI
							if(!isBack){
								isBack=true;//到Release状态，如果往回滑动到pull则启动动画
								changeHeaderViewByState();
								
							}
						}else{
								headerState=PULL_To_REFRESH;
								changeHeaderViewByState();
								// txView.setText("看到我了哦");
								// sc.scrollTo(0, headerPadding);
							}
						}
										
				}
				break;
					case MotionEvent.ACTION_DOWN:
						//加下滑阻力与实际滑动距离的差（大概值）
						beginY=(int)((int)event.getY()+sc.getScrollY()*1.5);
						break;
					case MotionEvent.ACTION_UP:
						if(headerState!=REFRESHING){
							switch(headerState){
							case DONE:
								//什么也不干
								break;
							case PULL_To_REFRESH:
								headerState=DONE;
								lastHeaderPadding=-1*headerHeight;
								header.setPadding(10, lastHeaderPadding, 0, 0);
								changeHeaderViewByState();
								break;
							case RELEASE_To_REFRESH:
								isBack=false;   //准备开始刷新，此时将不会往回滑动
								headerState=REFRESHING;
								changeHeaderViewByState();
								onRefresh();
								break;
							default:
								break;
							}
								
							}
						break;
						}
				// 如果Header是完全被隐藏的则让ScrollView正常滑动，让事件继续否则的话就阻断事件		
				if (lastHeaderPadding > (-1 * headerHeight)
						&& headerState != REFRESHING) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		
		
		//本地水果
		localFruit = (ImageView)view.findViewById(R.id.img_local_fruit);
		localFruit.setOnClickListener(new Button.OnClickListener()
		{
		    @Override
		     public void onClick(View arg0) {
				    Intent intent = new Intent(getActivity().getBaseContext(),TakeAwayActivity.class);
				    startActivity(intent);
	          	}
		 });
		//进口水果
		inportFruit=(ImageView)view.findViewById(R.id.img_import_fruit);
		inportFruit.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(getActivity().getBaseContext(),ImportFruitActivity.class);
			    startActivity(intent);
			}
			
		});
		//活动专区
		center_action=(ImageView)view.findViewById(R.id.img_group);
		center_action.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity().getBaseContext(),Center_action.class);
			    startActivity(intent);
			}
			
		});
		//我要试吃
		foretaste=(ImageView)view.findViewById(R.id.img_taste);
		foretaste.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity().getBaseContext(),tasteActivity.class);
			    startActivity(intent);
			}
			
		});
	/*	
		mWebView = (WebView)view.findViewById(R.id.webview);
		mWebView.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		    	view.loadUrl(url);
		        return true;
		    }
		});
		mWebView.loadUrl("http://www.baidu.com");   */
	}

	private void onRefresh() {
		AsyncTask<Void, Void, Void> execute = new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				onRefreshComplete();
			}

		}.execute();
	}

	private void onRefreshComplete() {
		// TODO Auto-generated method stub
		headerState=DONE;
		changeHeaderViewByState();
	}
	private void changeHeaderViewByState() {
		switch (headerState) {
		case PULL_To_REFRESH:
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) { // 向上送
				isBack = false;
				// 开启动画
				iv_anim_first.startAnimation(anim);
				ad.start();
				tv_text.setText("下拉刷新");
			}
			tv_text.setText("下拉刷新");
			break;
		case RELEASE_To_REFRESH: // 向下拖：这里只有右边的进度动画
			iv_anim_first.setVisibility(View.VISIBLE);
			iv_header_fresh_anim.setVisibility(View.VISIBLE);
			tv_text.setVisibility(View.VISIBLE);
			iv_anim_first.startAnimation(anim); // 右边的进度动画
			tv_text.setText("松手刷新");
			break;
		case REFRESHING:
			lastHeaderPadding = 0;
			header.setPadding(10, lastHeaderPadding, 0, 20);
			header.invalidate();
			iv_header_fresh_anim.setVisibility(View.VISIBLE);
			iv_anim_first.setVisibility(View.VISIBLE);
			tv_text.setText("载入中...");
			ad.start();
			break;
		case DONE: // 向上送
			lastHeaderPadding = -1 * headerHeight;
			header.setPadding(10, lastHeaderPadding, 0, 20);
			header.invalidate();
			iv_header_fresh_anim.setVisibility(View.GONE);
			tv_text.setText("下拉刷新");
			break;
		default:
			break;
		}
	}
	private void measureView(View childView) {
		android.view.ViewGroup.LayoutParams p = childView.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int height = p.height;
		int childHeightSpec;
		if (height > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		childView.measure(childWidthSpec, childHeightSpec);
	}
	/**
	 * 多线程查询商品数据
	 */
	class InitDataTask implements Runnable {
		@Override
		public void run() {
//			merchsList = new ArrayList<Merchs>();
			Log.d("geek", "开启线程");
			// 加载数据:接受服务端的数据
			merchsList = JsonParser.parse(path);
			Log.d("geek", "大小：" + merchsList.size());
			// 查询完了将数据给适配器
			handler.sendEmptyMessage(MESSAGE_INIT_DATA_SUCCESS);
		}

	}
	private Handler handler=new Handler(){
		@SuppressWarnings("unused")
		public void dispathMessage(android.os.Message msg)
		{
			switch(msg.what){
			case MESSAGE_INIT_DATA_SUCCESS:
				//绑定数据到适配器
				merchAdapter=new ImageListAdapter(getActivity().getBaseContext(),merchsList);
				lv_like_shop.setAdapter(merchAdapter);
				//加载完成，隐藏动画
				tv_merchs_info.setVisibility(View.GONE);
				Utility.setListViewHeightBasedOnChildren(lv_like_shop);
				break;
			}
		}
	};
	

}
