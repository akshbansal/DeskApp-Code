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

public class DeviceManagementJSON {
		static JSONObject jsonObject, jsonResponse, jsonObject3;
		static String getData = "";
		static String message = "";
		static String err_code="";
		ResourceBundle rb;
		
		public String getDeviceList() {
			HttpClient httpClient = HttpClientBuilder.create().build();
			rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
			String urlToPostData = rb.getString("http_url_login");
			System.out.println(urlToPostData);
			jsonObject = new JSONObject();
			try {
				jsonObject.put("method", rb.getString("showUserDevice"));
				jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);

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
		
		public String getDeviceManagement(String deviceId, String status) {
			HttpClient httpClient = HttpClientBuilder.create().build();
			rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
			String urlToPostData = rb.getString("http_url_login");
			System.out.println(urlToPostData);
			jsonObject = new JSONObject();
			try {
				jsonObject.put("method", rb.getString("manageDevice"));
				jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);

				jsonObject.put("device_id", deviceId);
				jsonObject.put("status", status);
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
