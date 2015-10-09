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
import com.im.utils.Constants;
import com.im.utils.FindMACAddress;

public class StaffRegistrationJSON {
	static JSONObject jsonObject, jsonResponse;
	static String getData = "";
	static String message = "";
	static String err_code="";
	ResourceBundle rb;
	FindMACAddress macAddress;
	StaffRegisterBO physicianBO;
	public StaffRegistrationJSON(StaffRegisterBO bo){
		this.physicianBO = bo;	
	}
	
	public String doPatientRegistration() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			
			if(Constants.IS_ADMIN_LOGIN == true){
				jsonObject.put("login_token", Constants.ADMIN_LOGIN_TOKEN);
				jsonObject.put("method", rb.getString("admin_registeration"));
				jsonObject.put("user_type", "Staff");
			}
			else{
				jsonObject.put("method", rb.getString("staff_registration"));
				
			}
			jsonObject.put("user_name", physicianBO.getUsername());
			jsonObject.put("password", physicianBO.getPassword());
			jsonObject.put("pin", physicianBO.getPin());
			jsonObject.put("device_id", physicianBO.getDevice_id());
			jsonObject.put("email", physicianBO.getEmail());
			jsonObject.put("first_name" , physicianBO.getFirst_name());
			jsonObject.put("last_name" , physicianBO.getLast_name());
			jsonObject.put("security_question" , physicianBO.getSecurity_question());
			jsonObject.put("security_answer" , physicianBO.getSecurity_answer());
			jsonObject.put("zip" , physicianBO.getZip());
			jsonObject.put("phone_no" , physicianBO.getPhone());
			jsonObject.put("practice_type" , physicianBO.getPractiseType());
			jsonObject.put("designation" , physicianBO.getDesignation());
			jsonObject.put("job_title" , physicianBO.getJobTitle());
			if(Constants.IS_ADMIN_LOGIN == false)
			{
				jsonObject.put("primary_hosp_id" , physicianBO.getPrimary_hosp_id());
			}
			jsonObject.put("other_hospitals" , physicianBO.getSecondary_hosp_id());

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
