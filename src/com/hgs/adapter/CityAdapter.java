package com.hgs.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;




import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.hgs.ui.R;

public class CityAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
	private LayoutInflater inflater;
	ArrayList<String> dataList;
	//用来存放每一轮分组的第一个item的位置。
	private int[] sectionIndices;
	//用来存放每一个分组要展现的数据 也就是目录标题
	private String[] sectionHeaders; 

	public CityAdapter(Context context, ArrayList<String> outDataList) {
		inflater = LayoutInflater.from(context);
		this.dataList = outDataList;
		sectionIndices = getSectionIndices();  
        sectionHeaders = getSectionHeaders(); 
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//菜单项视图
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_city,parent, false);
		}
		TextView txt = (TextView) convertView.findViewById(R.id.text_city);
		txt.setText(dataList.get(position));
		return convertView;
	}

	//用来存放每一轮分组的第一个item的位置。
	private int[] getSectionIndices() {
        int[] sections = new int[2];  
        sections[0] = 1;  

        return sections; 
	}
	
	//用来存放每一个分组要展现的数据 也就是目录标题
	private String[] getSectionHeaders() {  
        String[] sectionHeaders = new String[2];  
        sectionHeaders[0] = "当前定位"; 
        sectionHeaders[1] = "已开通城市"; 
        return sectionHeaders;  
    }  
  

	@Override
	public Object[] getSections() {
		return sectionHeaders; 
	}

	@Override
	public long getHeaderId(int position) {
		//根据字符串列表顺序 进行分类
		if(position == 0)
		{
			return 0;
		}
		else
		{
			return 1;
		}	
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < sectionIndices.length; i++) {
			if (position < sectionIndices[i]) {
				return i - 1;
			}
		}
		return sectionIndices.length - 1;
	}

	@Override
	public int getPositionForSection(int section) {
		if (section >= sectionIndices.length) {
			section = sectionIndices.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return sectionIndices[section];
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		//菜单分类项视图
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_menu_header, parent, false);
		}
		TextView txt = (TextView) convertView.findViewById(R.id.text1);
		if(position == 0)
		{
			txt.setText(sectionHeaders[0]);
		}
		else
		{
			txt.setText(sectionHeaders[1]);
		}
		
		return convertView;
	}

	public void clear() {
		dataList = new ArrayList<String>();
		sectionHeaders = new String[0];
		sectionIndices = new int[0];
		notifyDataSetChanged();
	}

	public void restore(ArrayList<String> outDataList) {
		this.dataList = outDataList;
		sectionIndices = getSectionIndices();
		sectionHeaders = getSectionHeaders(); 
		notifyDataSetChanged();
	}
}
