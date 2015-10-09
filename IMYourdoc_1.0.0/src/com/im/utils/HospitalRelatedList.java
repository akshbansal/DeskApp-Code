package com.im.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.DeviceDetailBO;
import com.im.bo.HospitalBO;
import com.im.bo.PendingRequestBO;
import com.im.bo.SearchUserBO;
import com.im.bo.UserProfileBO;
import com.im.json.DeviceManagementJSON;
import com.im.json.HospitalListJSON;
import com.im.json.PractiseTypeJSON;
import com.im.json.SearchUserJSON;
import com.im.json.SendInvitationJSON;
import com.im.json.UserListJson;

public class HospitalRelatedList {
	JSONObject jsonResponse;
	String err_code;
	JSONArray obj;
	ArrayList<HospitalBO> map;
	ArrayList<String> practiseTypeList;
	ArrayList<String> designationList;
	ArrayList<String> titleList;
	ArrayList<UserProfileBO> userList;
	String response;
	public HospitalRelatedList(){
		 response = new PractiseTypeJSON().getPractiseTypeList();
	}
	public HospitalRelatedList(String search){
	}
	public ArrayList<HospitalBO> getHospitalListResponse(String name) throws JSONException{
		String response = new HospitalListJSON().getHospitalList(name);
		System.out.println();
		jsonResponse = new JSONObject(response);
		err_code = jsonResponse.getString("err-code");
		obj = jsonResponse.getJSONArray("hospitals_list");
		map = new ArrayList<HospitalBO>();
		if(obj!=null){
		for(int i=0;i<obj.length();i++){
			map.add(new HospitalBO(obj.getJSONArray(i).get(0).toString(), obj.getJSONArray(i).get(1).toString(),obj.getJSONArray(i).get(2).toString()));
			}
		}
		return map;
	}
	public String getHospitalSelectedID(String name) throws JSONException{
		String split[] = name.split(", ");
		String response = new HospitalListJSON().getHospitalList(split[0]);
		System.out.println();
		String hospitalId="";
		jsonResponse = new JSONObject(response);
		
		err_code = jsonResponse.getString("err-code");
		obj = jsonResponse.getJSONArray("hospitals_list");
		map = new ArrayList<HospitalBO>();
		if(obj!=null){
		for(int i=0;i<obj.length();i++){
				if(obj.length()==1){
					hospitalId =  obj.getJSONArray(i).get(0).toString();
				}
				else if(obj.getJSONArray(i).get(1).toString().equalsIgnoreCase(split[0]) && obj.getJSONArray(i).get(2).toString().equalsIgnoreCase(split[1]))
				{
					hospitalId =  obj.getJSONArray(i).get(0).toString();
				}
			}
		}
		return hospitalId;
	}
	public ArrayList<String> getPractiseListResponse() throws JSONException{
		
		System.out.println();
		jsonResponse = new JSONObject(response);
		err_code = jsonResponse.getString("err-code");
		obj = jsonResponse.getJSONArray("practice_typeslist");
		practiseTypeList = new ArrayList<String>();
		for(int i=0;i<obj.length();i++){
			practiseTypeList.add((String) obj.get(i));
		}
		return practiseTypeList;
	}
	public ArrayList<String> getDesignationListResponse() throws JSONException{
		jsonResponse = new JSONObject(response);
		err_code = jsonResponse.getString("err-code");
		obj = jsonResponse.getJSONArray("designation_list");
		designationList = new ArrayList<String>();
		for(int i=0;i<obj.length();i++){
			designationList.add((String) obj.get(i));
		}
		return designationList;
	}
	public ArrayList<String> getTitleListResponse() throws JSONException{
		jsonResponse = new JSONObject(response);
		err_code = jsonResponse.getString("err-code");
		obj = jsonResponse.getJSONArray("job_title_list");
		titleList = new ArrayList<String>();
		for(int i=0;i<obj.length();i++){
			titleList.add((String) obj.get(i));
		}
		return titleList;
	}
	public List<UserProfileBO> getUserList(String login_token,String user_type) throws JSONException{
		String response = new UserListJson().getHospitalUsersList(login_token, user_type);
		jsonResponse = new JSONObject(response);
		err_code = jsonResponse.getString("err-code");
		if(jsonResponse!=null){
			obj = jsonResponse.getJSONArray("user_list");
			userList = new ArrayList<UserProfileBO>();
			for(int i=0;i<obj.length();i++){
	//			user_id":"40","user_name":"jcjcjcjvj","email":"hcjcjc@jcigg.com","user_status":"Active","name":null,"phone":null
				userList.add(new UserProfileBO(obj.getJSONObject(i).get("name").toString(), obj.getJSONObject(i).getString("user_name"), 
						obj.getJSONObject(i).get("phone").toString(),obj.getJSONObject(i).getString("email"),
						user_type, obj.getJSONObject(i).getString("user_status")));
			}
			return userList;
		}
		else 
			return null;
	
	}
	public ArrayList<SearchUserBO> searchUserList(String name,String userType,String npi,String PracticeType,String hospitalId,int count) throws JSONException{
		String response="";
		int hasMore=0;
		ArrayList<SearchUserBO> userList;
		if(userType.equalsIgnoreCase("physician")||userType.equalsIgnoreCase("staff")){
			response = new SearchUserJSON().doSearchUser(name, userType, npi, PracticeType, hospitalId,count);
		}else{
			response = new SearchUserJSON().doSearchUser(name, userType, "","","",count);
		}
		jsonResponse = new JSONObject(response);
		
		if(jsonResponse!=null){
			err_code = jsonResponse.getString("err-code");
			if(err_code.equals("1")){
				hasMore = jsonResponse.getInt("has_more");
			obj = jsonResponse.getJSONArray("users");
			 userList = new ArrayList<SearchUserBO>();
			for(int i=0;i<obj.length();i++){
				SearchUserBO bo = new SearchUserBO();
				bo.setFirstName(obj.getJSONObject(i).get("first_name").toString());
				bo.setUserName(obj.getJSONObject(i).get("username").toString());
				bo.setLastName(obj.getJSONObject(i).get("last_name").toString());
				bo.setImageURL(obj.getJSONObject(i).get("image_url").toString());
				bo.setUid(obj.getJSONObject(i).get("uid").toString());
				if(userType.equalsIgnoreCase("providers")||userType.equalsIgnoreCase("staff"))
				{
					bo.setNpi(obj.getJSONObject(i).get("NPI").toString()==null?"":obj.getJSONObject(i).get("NPI").toString());
					bo.setJobTitle(obj.getJSONObject(i).get("job_title").toString()==null?"":obj.getJSONObject(i).get("job_title").toString());
					bo.setDesignation(obj.getJSONObject(i).get("designation").toString()==null?"":obj.getJSONObject(i).get("designation").toString());
				}
				
	//			user_id":"40","user_name":"jcjcjcjvj","email":"hcjcjc@jcigg.com","user_status":"Active","name":null,"phone":null
				userList.add(bo);
				}
			if(hasMore!=0)
				Constants.HAS_MORE = hasMore;
			else
				Constants.HAS_MORE = 0;
			return userList;
			}
			
		}
		return null;
		
	}
	public ArrayList<PendingRequestBO> getPendingRequestsResponse() throws JSONException {
		// TODO Auto-generated method stub
		String response = new SendInvitationJSON().getPendingRequests();
		PendingRequestBO requestBo;
		jsonResponse = new JSONObject(response);
		err_code = jsonResponse.getString("err-code");
		if(jsonResponse!=null){
			obj = jsonResponse.getJSONArray("pending");
		ArrayList<PendingRequestBO>	pendingList = new ArrayList<PendingRequestBO>();
			for(int i=0;i<obj.length();i++){
				requestBo = new PendingRequestBO();
				requestBo.setUser_name(obj.getJSONObject(i).getString("user_name"));
				requestBo.setUser_type(obj.getJSONObject(i).getString("user_type"));
				requestBo.setStatus(obj.getJSONObject(i).getString("status"));
				requestBo.setFlagged(obj.getJSONObject(i).getBoolean("flagged"));
				requestBo.setType(obj.getJSONObject(i).getString("type"));
				pendingList.add(requestBo);
	//			user_id":"40","user_name":"jcjcjcjvj","email":"hcjcjc@jcigg.com","user_status":"Active","name":null,"phone":null
			}
			return pendingList;
		}
		else 
			return null;
	
	}
	public ArrayList<PendingRequestBO> getDeclinedRequestsResponse() throws JSONException {
		// TODO Auto-generated method stub
		String response = new SendInvitationJSON().getPendingRequests();
		PendingRequestBO requestBo;
		jsonResponse = new JSONObject(response);
		err_code = jsonResponse.getString("err-code");
		if(jsonResponse!=null){
			obj = jsonResponse.getJSONArray("decline");
		ArrayList<PendingRequestBO>	pendingList = new ArrayList<PendingRequestBO>();
			for(int i=0;i<obj.length();i++){
				requestBo = new PendingRequestBO();
				requestBo.setUser_name(obj.getJSONObject(i).getString("user_name"));
				requestBo.setUser_type(obj.getJSONObject(i).getString("user_type"));
				requestBo.setStatus(obj.getJSONObject(i).getString("status"));
				requestBo.setFlagged(obj.getJSONObject(i).getBoolean("flagged"));
				requestBo.setType(obj.getJSONObject(i).getString("type"));
				pendingList.add(requestBo);
	//			user_id":"40","user_name":"jcjcjcjvj","email":"hcjcjc@jcigg.com","user_status":"Active","name":null,"phone":null
			}
			return pendingList;
		}
		else 
			return null;
	
	}
	public ArrayList<DeviceDetailBO> getDeviceList() throws JSONException {
		// TODO Auto-generated method stub
		String response = new DeviceManagementJSON().getDeviceList();
		jsonResponse = new JSONObject(response);
		err_code = jsonResponse.getString("err-code");
		if(jsonResponse!=null){
			obj = jsonResponse.getJSONArray("device_list");
		ArrayList<DeviceDetailBO>	deviceList = new ArrayList<DeviceDetailBO>();
			for(int i=0;i<obj.length();i++){
				
				DeviceDetailBO bo = new DeviceDetailBO();
				bo.setDevice_id(obj.getJSONArray(i).get(0).toString());
				bo.setDevice_name(obj.getJSONArray(i).get(1).toString());
				bo.setStatus(obj.getJSONArray(i).get(2).toString());
				deviceList.add(bo);
			}
			return deviceList;
		}
		else 
			return null;
	
	}
}
