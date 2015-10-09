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

public class SendInvitationJSON {
	JSONObject jsonObject, jsonResponse, jsonObject3;
	String getData = "";
	 String message = "";
	 String err_code="";
	ResourceBundle rb;
	FindMACAddress macAddress;
	
	public SendInvitationJSON() {
	}

	public String sendInvite(String username) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();

		try {
			jsonObject.put("method", rb.getString("sendInvite"));

			jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);
			jsonObject.put("to_user", username);


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
	public String removeUser(String username) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();

		try {
			jsonObject.put("method", rb.getString("removeUser"));

			jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);
			jsonObject.put("user_name", username);


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
	public String getPendingRequests() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("showRequests"));
			jsonObject.put("login_token",Constants.loggedinuserInfo.login_token);

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
	public String respondToInvitation(String username,String status) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();

		try {
			jsonObject.put("method", rb.getString("respondToInvitation"));

			jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);
			jsonObject.put("user_name", username);

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

	public String sendNotification(String msgId) {
		  HttpClient httpClient = HttpClientBuilder.create().build();
		  rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		  String urlToPostData = rb.getString("http_url_login");
		  jsonObject = new JSONObject();

		  try {
		   jsonObject.put("method", rb.getString("sendNotification"));
		   jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);
		   jsonObject.put("message_id", msgId);
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
	
	public String getDesktopNotifications() {
		  HttpClient httpClient = HttpClientBuilder.create().build();
		  rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		  String urlToPostData = rb.getString("http_url_login");
		  jsonObject = new JSONObject();

		  try {
		   jsonObject.put("method", rb.getString("deskNotification"));
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
	public String getRequestCount() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("getRequestCount"));
			jsonObject.put("login_token",
					Constants.loggedinuserInfo.login_token);
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpPost httpPost = new HttpPost(urlToPostData);
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			getData = EntityUtils.toString(httpResponse.getEntity());
			if (Constants.showConsole)
			System.out.println("the getting data IS " + "jo "
					+ jsonObject.toString() + "\n" + " response " + getData);
		} catch (Exception e) {
		}
		return getData;
	}
	public String SubmitMessage(String messageId, String roomJid,String messageType) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("submitChatMessage"));
			jsonObject.put("login_token",
					Constants.loggedinuserInfo.login_token);
			jsonObject.put("message_id", messageId);
			jsonObject.put("roomjid", roomJid);
			jsonObject.put("message_type", messageType);
			System.out.println(roomJid);
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpPost httpPost = new HttpPost(urlToPostData);
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			getData = EntityUtils.toString(httpResponse.getEntity());
			if (Constants.showConsole)

				System.out
						.println("the getting data IS " + "jo "
								+ jsonObject.toString() + "\n" + " response "
								+ getData);
		} catch (Exception e) {
		}
		return getData;
	}
	
	public String updateMessageReceivers(String messageId, String recieverJId,String senderJid) {
		  HttpClient httpClient = HttpClientBuilder.create().build();
		  rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		  String urlToPostData = rb.getString("http_url_login");
		  jsonObject = new JSONObject();
		  try {
		   jsonObject.put("method", rb.getString("updateMessageReceivers"));
		   jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);
		   jsonObject.put("message_id", messageId);
		   jsonObject.put("roomjid", recieverJId);
		   jsonObject.put("sender_jid", senderJid);
		   StringEntity stringEntity = new StringEntity(jsonObject.toString());
		   HttpPost httpPost = new HttpPost(urlToPostData);
		   httpPost.setEntity(stringEntity);
		   HttpResponse httpResponse = httpClient.execute(httpPost);
		   getData = EntityUtils.toString(httpResponse.getEntity());
		   if(Constants.showConsole) 
			   
			   System.out.println("the getting data IS " + "jo "
		   + jsonObject.toString() + "\n"+" response " + getData);
		  } catch (Exception e) {
		  }
		  return getData;
		 }
	public String ReportOnCloseConversation(String jid) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("closeConversation"));
			jsonObject.put("login_token",
					Constants.loggedinuserInfo.login_token);
			jsonObject.put("jid", jid);
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpPost httpPost = new HttpPost(urlToPostData);
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			getData = EntityUtils.toString(httpResponse.getEntity());
			if (Constants.showConsole)

				System.out
						.println("the getting data IS " + "jo "
								+ jsonObject.toString() + "\n" + " response "
								+ getData);
		} catch (Exception e) {
		}
		return getData;
	}
	public String getRoomMembers(String jid) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		jsonObject = new JSONObject();
		try {
			jsonObject.put("method", rb.getString("roomMembers"));
			jsonObject.put("login_token",
					Constants.loggedinuserInfo.login_token);
			jsonObject.put("roomJID", jid);
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			HttpPost httpPost = new HttpPost(urlToPostData);
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			getData = EntityUtils.toString(httpResponse.getEntity());
			if (Constants.showConsole)

				System.out
						.println("the getting data IS " + "jo "
								+ jsonObject.toString() + "\n" + " response "
								+ getData);
		} catch (Exception e) {
		}
		return getData;
	}
}
