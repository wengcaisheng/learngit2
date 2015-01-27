package com.hgs.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 向服务端发送请求,得到请求的数据
 * @author Administrator
 *
 */
public class HttpUtil {
public static String download(String uri) throws ClientProtocolException,IOException{
	String result=null;
	HttpClient client=new DefaultHttpClient();
	HttpPost post=new HttpPost(uri);
	HttpResponse response=client.execute(post);
	if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
		result=EntityUtils.toString(response.getEntity());
	}
	return result;
	
}

}
