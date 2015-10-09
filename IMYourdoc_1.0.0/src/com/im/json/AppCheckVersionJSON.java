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

public class AppCheckVersionJSON {
	JSONObject jsonObject, jsonResponse, jsonObject3;
	String getData = "";
	 String message = "";
	 String err_code="";
	ResourceBundle rb;
	FindMACAddress macAddress;
	
	public AppCheckVersionJSON() {
	}

	public String checkAppVersion(String app_version) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("checkAppVersion"));
			jsonObject.put("app_type", "Desktop");
			jsonObject.put("app_version", app_version);
			if(Constants.loggedinuserInfo.login_token!=null && !Constants.loggedinuserInfo.login_token.equals(""))
				jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);

			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpPost httpPost = new HttpPost(urlToPostData);
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			getData = EntityUtils.toString(httpResponse.getEntity());
			if(Constants.showConsole) System.out.println("the getting data IS " + "jo " + jsonObject.toString() + "\n" + " response " + getData);
		} catch (Exception e) {
		}
		return getData;
	}
	
}
