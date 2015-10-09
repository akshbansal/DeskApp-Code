package com.im.json;

import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.im.bo.PatientRegisterBO;
import com.im.bo.PhysicianRegisterBO;
import com.im.bo.StaffRegisterBO;
import com.im.bo.UserProfileBO;
import com.im.utils.Constants;
import com.im.utils.FindMACAddress;

public class UserProfileJSON {
	JSONObject jsonObject, jsonResponse, jsonObject3;
	String getData = "";
	String message = "";
	String err_code="";
	ResourceBundle rb;
	FindMACAddress macAddress;
	
	public UserProfileJSON() {
	}

	public String getUserProfile(String to_user_name,String login_token) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();

		try {
			jsonObject.put("method", rb.getString("viewProfileByAdmin"));
			jsonObject.put("to_user_name", to_user_name);
			if(login_token.equals("")&& Constants.IS_ADMIN_LOGIN==true){
				jsonObject.put("login_token", Constants.ADMIN_LOGIN_TOKEN);
			}
			else{
				jsonObject.put("login_token", login_token);
			}

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
	
	public String deleteUserProfile(String to_user_name) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();

		try {
			jsonObject.put("method", rb.getString("deleteUser"));

			jsonObject.put("to_user_name", to_user_name);
			jsonObject.put("login_token", Constants.ADMIN_LOGIN_TOKEN);

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
	
	public String getLoginUserProfile(String login_token) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("viewLoginUserProfile"));

			jsonObject.put("login_token", login_token);

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
	
	public String updateUserProfileByAdmin(String login_token,PatientRegisterBO patientRegisterBO) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("updateUserProfileByAdmin"));
			jsonObject.put("login_token", login_token);
			jsonObject.put("user_name", patientRegisterBO.getUsername());
			jsonObject.put("first_name", patientRegisterBO.getFirst_name());
			jsonObject.put("last_name", patientRegisterBO.getLast_name());
			jsonObject.put("security_question", patientRegisterBO.getSecurity_question());
			jsonObject.put("security_answer", patientRegisterBO.getSecurity_answer());
			jsonObject.put("privacy_enabled", patientRegisterBO.getPrivacy_enabled());
			jsonObject.put("zip", patientRegisterBO.getZip());
			jsonObject.put("phone", patientRegisterBO.getPhone());
			jsonObject.put("email", patientRegisterBO.getEmail());

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
	public String updateUserEmailId(String login_token,String EmailID) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("updateEmail"));
			jsonObject.put("login_token", login_token);
			jsonObject.put("new_email", EmailID);

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
	public String updateLoginUserProfile(String login_token,UserProfileBO userProfileBo) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("updateUserProfile"));
			jsonObject.put("login_token", login_token);
			jsonObject.put("user_name", userProfileBo.getUsername());
			jsonObject.put("first_name", userProfileBo.getName().split(" ")[0]);
			jsonObject.put("last_name", userProfileBo.getName().split(" ")[1]==null?"":userProfileBo.getName().split(" ")[1]);
			jsonObject.put("security_question", userProfileBo.getSeq_ques());
			jsonObject.put("security_answer", userProfileBo.getSeq_ans());
			jsonObject.put("privacy_enabled", userProfileBo.getPrivacy_enabled());
			jsonObject.put("zip", userProfileBo.getZip());
			jsonObject.put("phone", userProfileBo.getPhoneNo());
			jsonObject.put("email", userProfileBo.getEmailId());
			jsonObject.put("primary_hosp_id", userProfileBo.getPrimary_hosp_id());
//			jsonObject.put("other_hospitals", userProfileBo.getSec_hospital_selected()==null?"":userProfileBo.getSec_hospital_selected());
			jsonObject.put("practice_type", userProfileBo.getPractise_type()==null?"":userProfileBo.getPractise_type());
			jsonObject.put("job_title", userProfileBo.getJobTitle()==null?"":userProfileBo.getJobTitle());
			jsonObject.put("designation", userProfileBo.getDesignation()==null?"":userProfileBo.getDesignation());
			

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
	
	public String updateUserProfileByAdmin(String login_token,PhysicianRegisterBO physicianRegisterBO) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("updateUserProfileByAdmin"));
			jsonObject.put("login_token", login_token);
			jsonObject.put("user_name", physicianRegisterBO.getUsername());
			jsonObject.put("first_name", physicianRegisterBO.getFirst_name());
			jsonObject.put("last_name", physicianRegisterBO.getLast_name());
			jsonObject.put("security_question", physicianRegisterBO.getSecurity_question());
			jsonObject.put("security_answer", physicianRegisterBO.getSecurity_answer());
			jsonObject.put("privacy_enabled", physicianRegisterBO.getPrivacy_enabled());
			jsonObject.put("zip", physicianRegisterBO.getZip());
			jsonObject.put("phone", physicianRegisterBO.getPhone());
			jsonObject.put("email", physicianRegisterBO.getEmail());
			jsonObject.put("practice_type", physicianRegisterBO.getPractice_type());
			jsonObject.put("other_hospitals", physicianRegisterBO.getOther_hospitals());
			jsonObject.put("designation", physicianRegisterBO.getDesignation());
			jsonObject.put("job_title", physicianRegisterBO.getJobTitle());

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
	public String updateUserProfileByAdmin(String login_token,StaffRegisterBO staffRegisterBo) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("updateUserProfileByAdmin"));
			jsonObject.put("login_token", login_token);
			jsonObject.put("user_name", staffRegisterBo.getUsername());
			jsonObject.put("first_name", staffRegisterBo.getFirst_name());
			jsonObject.put("last_name", staffRegisterBo.getLast_name());
			jsonObject.put("security_question", staffRegisterBo.getSecurity_question());
			jsonObject.put("security_answer", staffRegisterBo.getSecurity_answer());
			jsonObject.put("privacy_enabled", staffRegisterBo.getPrivacy_enabled());
			jsonObject.put("zip", staffRegisterBo.getZip());
			jsonObject.put("phone", staffRegisterBo.getPhone());
			jsonObject.put("email", staffRegisterBo.getEmail());
			jsonObject.put("practice_type", staffRegisterBo.getPractiseType());
			jsonObject.put("designation", staffRegisterBo.getDesignation());
			jsonObject.put("job_title", staffRegisterBo.getJobTitle());

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