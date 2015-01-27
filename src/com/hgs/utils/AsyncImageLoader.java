package com.hgs.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
/**************************************************************************
 * 
 * @author made by yi 2014-5-8
 * copyright (C)2014-2024 cmyy-inc.All Rights Reserved
 **************************************************************************/
/*运行java程序时，有时为了提高访问速度会将某些对象存放在内存中。但是在内存吃紧的时候我们往往需要释放一些长期没有用的对象来得到更多的内存空间。
java中给我们提供了软引用这个类java.lang.ref.SoftReference<T>。

一个要引用的对象不直接引用实际的对象，而是引用java中一个特定的类SoftReference，再由SoftReference类去引用要实际引用的对象。

SoftReference类引用的对象在java运行时内存吃紧的时候会适当的回收，这样来释放内存。

*/
public class AsyncImageLoader {
	private Map<String, SoftReference<Bitmap>> imageCaches = null;
	private FileUtil fileUtil;
	private AsyncHttpClient mImageLoader;

	public AsyncImageLoader(Context context) {
		imageCaches = new HashMap<String, SoftReference<Bitmap>>();
		fileUtil = new FileUtil(context);
		mImageLoader = new AsyncHttpClient();
	}

	public boolean DisplayImage(ImageView imageView, String imageUrl, String path/*缓存目录*/) {
		 String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		 String filepath = fileUtil.getAbsolutePath() + "/" + path + filename;
		// 先从软引用中找
		if (imageCaches.containsKey(imageUrl)) {
			SoftReference<Bitmap> reference = imageCaches.get(imageUrl);
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
				return true;
			}
		}

		// 从文件中找
		if (fileUtil.isBitmapExists(fileUtil.getAbsolutePath() + "/" + path,filename)) {
			Bitmap bitmap = BitmapFactory.decodeFile(filepath);
			// 重新加入到内存软引用中
			imageCaches.put(imageUrl, new SoftReference<Bitmap>(bitmap));
			imageView.setImageBitmap(bitmap);
			return true;
		}

		// 软引用和文件中都没有再从网络下载
		if (imageUrl != null && !imageUrl.equals("")) 
		{		 // statusCode:状态返回码，headers:头部请求信息，responseBody返回结果
			mImageLoader.get(imageUrl,new myBinaryHttpResponseHandler(imageView,imageUrl,filepath) 
			{
				 @Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] binaryData, Throwable error) {
					 Log.e("AsyncImageLoader", "网络加载图片失败"+error.toString());
				}

				@Override
				 public void onSuccess(byte[] arg0) 
				 {
					    InputStream input = new ByteArrayInputStream(arg0); 
					    Bitmap bitmap = BitmapFactory.decodeStream(input);
						if (bitmap != null)
						{
							// 加入到软引用中
							imageCaches.put(getmImageUrl(), new SoftReference<Bitmap>(
									bitmap));
							// 缓存到文件系统
							fileUtil.saveBitmap(getmFilepath(), bitmap);
							getmImageView().setImageBitmap(bitmap);
				        }	
			       }
			  });			
		   }
		
		   return true;
	}
	
	class myBinaryHttpResponseHandler extends BinaryHttpResponseHandler
	{
		private ImageView mImageView;
		private String    mImageUrl;
		private String    mFilepath;
		
		public myBinaryHttpResponseHandler(ImageView imageView,String imageUrl,String filepath)
		{
			mImageView = imageView;
			mImageUrl = imageUrl;
			mFilepath = filepath;
		}
		public ImageView getmImageView() {
			return mImageView;
		}
		public void setmImageView(ImageView mImageView) {
			this.mImageView = mImageView;
		}
		public String getmImageUrl() {
			return mImageUrl;
		}
		public void setmImageUrl(String mImageUrl) {
			this.mImageUrl = mImageUrl;
		}
		public String getmFilepath() {
			return mFilepath;
		}
		public void setmFilepath(String mFilepath) {
			this.mFilepath = mFilepath;
		}	
	}

}
