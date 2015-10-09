package com.im.json;


import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.im.utils.Constants;
import com.im.utils.FindMACAddress;

public class SearchUserJSON {
	static JSONObject jsonObject, jsonResponse;
	static String getData = "";
	static String message = "";
	static String err_code="";
	ResourceBundle rb;
	FindMACAddress macAddress;

	
	public String doSearchUser(String name,String user_type,String npi,String practice_type,String hosp_id,int count) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("searchUserList"));
			jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);
			jsonObject.put("name", name);
			jsonObject.put("user_type", user_type);
			jsonObject.put("npi", npi);
			jsonObject.put("practice_type", practice_type);
			jsonObject.put("hosp_id" , hosp_id);
			jsonObject.put("start" , count);

			StringEntity stringEntity = new StringEntity(jsonObject.toString());

			HttpPost httpPost = new HttpPost(urlToPostData);

			httpPost.setEntity(stringEntity);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			getData = EntityUtils.toString(httpResponse.getEntity());

			if(Constants.showConsole) System.out.println("the getting data IS " + "jo "

			+ jsonObject.toString() + "\n"+" response " + getData);

		} catch (Exception e) {
		}
		return getData;
	}


}
