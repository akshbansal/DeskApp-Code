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

public class AuthenticateJSON {
	JSONObject jsonObject, jsonResponse, jsonObject3;
	String getData = "";
	 String message = "";
	 String err_code="";
	ResourceBundle rb;
	FindMACAddress macAddress;
	
	public AuthenticateJSON() {
	}

	public String doAuthentication() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("login_method"));
			jsonObject.put("user_name", Constants.loggedinuserInfo.username);
			jsonObject.put("password", Constants.loggedinuserInfo.password);
			jsonObject.put("device_id", Constants.loggedinuserInfo.device_id);
			jsonObject.put("device_type", Constants.loggedinuserInfo.device_type);

			StringEntity stringEntity = new StringEntity(jsonObject.toString());

			HttpPost httpPost = new HttpPost(urlToPostData);
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			getData = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("the getting data IS " + "jo " + jsonObject.toString() + "\n" + " response " + getData);
		} catch (Exception e) {
		}
		return getData;
	}
	public String doConfigureAccount(String username,String userPin,String password, String securityQues, String securityAns) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("configureProfile"));
			jsonObject.put("user_name", username);
			jsonObject.put("user_pin", userPin);
			jsonObject.put("password",password); ;
			jsonObject.put("security_qus", securityQues);
			jsonObject.put("security_ans", securityAns);
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
	
	public String submitFeedBack(String comment) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();

		try {
			jsonObject.put("method", rb.getString("feedback"));
			jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);
			jsonObject.put("comment", comment);
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpPost httpPost = new HttpPost(urlToPostData);
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			getData = EntityUtils.toString(httpResponse.getEntity());
			if(Constants.showConsole) System.out.println("the getting data IS " + "jo " + jsonObject.toString() + "\n"+" response " + getData);
		} catch (Exception e) {
		}
		return getData;
	}
	public String checkUserAvailability(String username) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("checkUserAvailable"));
			jsonObject.put("user_name",username);
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
	/*public static void main(String[] args) throws JSONException {
		Authenticate login = new Authenticate();
		String response = login.doAuthentication();
		System.out.println();
		jsonResponse = new JSONObject(response);

		err_code = jsonResponse.getString("err-code");

		message=jsonResponse.getString("message");
		System.out.println(message.toUpperCase());
	}*/
}
