package com.im.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.json.JSONArray;
import org.json.JSONObject;

import com.im.bo.HospitalBO;
import com.im.bo.UserProfileBO;
import com.im.json.UserProfileJSON;

public class UserProfile {
	UserProfileJSON userProfile = new UserProfileJSON();
	JSONObject jsonResponse;
	String err_code="";
	String message ="";
	
	public UserProfileBO viewLoginUserProfile(String login_token){
		
		String response = userProfile.getLoginUserProfile(login_token);
		UserProfileBO userProfileBo = new UserProfileBO();
		System.out.println();
		try{
			jsonResponse = new JSONObject(response);
			err_code = jsonResponse.getString("err-code");
			message=jsonResponse.getString("message");
			if(err_code.equals("1")){
				userProfileBo.setEmailId(jsonResponse.getString("email")==null?"":jsonResponse.getString("email"));
				userProfileBo.setName(jsonResponse.getString("first_name")+" "+jsonResponse.getString("last_name"));
				userProfileBo.setUserType(jsonResponse.getString("type"));
				//userProfileBo.setUsername(jsonResponse.getString("user_name"));
				userProfileBo.setPhoneNo(jsonResponse.getString("phone"));
				userProfileBo.setZip(jsonResponse.getString("zip"));
//				userProfileBo.setPic_no(jsonResponse.getString("pic_no"));
				/*{"err-code":"1","message":"User profile data.","email":"k@k.com","type":"Patient",
					"first_name":"Ali","last_name":"Khan","phone":"1234567890","zip":"83201","secu_ques":"",
					"secu_ans":"","privacy_enabled":"0","job_title":"","designation":"","session":"1",
					"practice_type":"","primary_hospital":{"name":"","city":"","hosp_id":""},"other_hospitals":[]}*/
				userProfileBo.setSeq_ques(jsonResponse.getString("secu_ques"));
				userProfileBo.setSeq_ans(jsonResponse.getString("secu_ans"));
				userProfileBo.setJobTitle(jsonResponse.getString("job_title"));
				userProfileBo.setDesignation(jsonResponse.getString("designation"));
				userProfileBo.setPrivacy_enabled(jsonResponse.getString("privacy_enabled"));
				userProfileBo.setSession(jsonResponse.getString("session"));
				userProfileBo.setPractise_type(jsonResponse.getString("practice_type"));
				JSONObject array = jsonResponse.getJSONObject("primary_hospital");
				System.out.println(array.getString("name"));
				userProfileBo.setPrimary_hospital(array.getString("name"));
				userProfileBo.setPrimary_hosp_id(array.getString("hosp_id"));
				ArrayList<HospitalBO> secondaryHospitals = new ArrayList<HospitalBO>();
				JSONArray obj = jsonResponse.getJSONArray("other_hospitals");
				if(obj.length()!=0){
					for(int i=0;i<obj.length();i++){
						//			user_id":"40","user_name":"jcjcjcjvj","email":"hcjcjc@jcigg.com","user_status":"Active","name":null,"phone":null
							secondaryHospitals.add(new HospitalBO(obj.getString(2), obj.getString(0), obj.getString(1)));
					}
					userProfileBo.setSecondary_hospital(secondaryHospitals);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return userProfileBo;
	}
}
