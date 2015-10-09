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
import com.im.utils.Constants;
import com.im.utils.FindMACAddress;

public class PatientRegistrationJSON {
	static JSONObject jsonObject, jsonResponse, jsonObject3;
	static String getData = "";
	static String message = "";
	static String err_code="";
	ResourceBundle rb;
	FindMACAddress macAddress;
	PatientRegisterBO patientBo;
	public PatientRegistrationJSON(PatientRegisterBO patientBo){
		this.patientBo = patientBo;	
	}
	
	public String doPatientRegistration() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			System.out.println(patientBo.getFirst_name()+">>>"+ patientBo.getUsername());
			
			if(Constants.IS_ADMIN_LOGIN == true){
				jsonObject.put("login_token", Constants.ADMIN_LOGIN_TOKEN);
				jsonObject.put("method", rb.getString("admin_registeration"));
				jsonObject.put("user_type", "Patient");
			}
			else{
				jsonObject.put("method", rb.getString("patient_registration"));
			}
			jsonObject.put("user_name", patientBo.getUsername());
			jsonObject.put("password", patientBo.getPassword());
			jsonObject.put("pin", patientBo.getPin());
			jsonObject.put("device_id", patientBo.getDevice_id());
			jsonObject.put("email", patientBo.getEmail());
			jsonObject.put("first_name" , patientBo.getFirst_name());
			jsonObject.put("last_name" , patientBo.getLast_name());
			jsonObject.put("security_question" , patientBo.getSecurity_question());
			jsonObject.put("security_answer" , patientBo.getSecurity_answer());
			jsonObject.put("zip" , patientBo.getZip());
			jsonObject.put("phone_no" , patientBo.getPhone());

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
