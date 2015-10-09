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

public class UserListJson {
		static JSONObject jsonObject, jsonResponse, jsonObject3;
		static String getData = "";
		static String message = "";
		static String err_code="";
		ResourceBundle rb;
		
		public String getHospitalUsersList(String login_token,String user_type) {
			HttpClient httpClient = HttpClientBuilder.create().build();
			rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
			String urlToPostData = rb.getString("http_url_login");
			System.out.println(urlToPostData);
			jsonObject = new JSONObject();
			try {
				jsonObject.put("method", rb.getString("registeredUserList"));
				jsonObject.put("login_token", login_token);
				jsonObject.put("user_type", user_type);
				StringEntity stringEntity = new StringEntity(jsonObject.toString());
				HttpPost httpPost = new HttpPost(urlToPostData);
				httpPost.setEntity(stringEntity);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				getData = EntityUtils.toString(httpResponse.getEntity());
				if(Constants.showConsole) System.out.println("the getting data IS jo "+ 
						jsonObject.toString() + "\n"+" response " + getData);
			} catch (Exception e) {
			}
			return getData;
		}
}
