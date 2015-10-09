package com.im.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.im.utils.Constants;


public class HospitalListJSON {
	static JSONObject jsonObject, jsonResponse, jsonObject3;
	static String getData = "";
	static String message = "";
	static String err_code="";
	ResourceBundle rb;
	
	public String getHospitalList(String name) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		System.out.println(urlToPostData);
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("hospital_list"));
			jsonObject.put("name", name);

			StringEntity stringEntity = new StringEntity(jsonObject.toString());

			HttpPost httpPost = new HttpPost(urlToPostData);

			httpPost.setEntity(stringEntity);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			getData = EntityUtils.toString(httpResponse.getEntity());

			 System.out.println("the getting data IS " + "jo "

			+ jsonObject.toString() + "\n"+" response " + getData);

		} catch (Exception e) {
		}
		return getData;
	}
	
	
}
