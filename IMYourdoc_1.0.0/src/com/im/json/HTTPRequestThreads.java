package com.im.json;


import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import com.im.utils.Constants;
import com.im.utils.Util;

class Spinner extends JDialog
{
	public JLabel Textlabel;
	private JLabel IconLb;
	
	
	public Spinner() {
		super();
		IconLb=new JLabel();
		Textlabel=new JLabel();
		this.setLayout(new FlowLayout());
		
		this.add(IconLb);
		this.add(Textlabel);
	
		
		IconLb.setIcon(new ImageIcon(Util.class
				.getResource(Constants.IMAGE_PATH + "/"
						+ "ajax-loader.gif")));
		
		Color transparent=new Color(28, 28, 28, 255);
	
		this.setBackground(transparent);
		Textlabel.setBackground(transparent);
		IconLb.setBackground(transparent);
		//this.IconLb.setBounds(0, , 200, 200);
		//this.Textlabel.setBounds(0, 200, 200, 20);
		//Textlabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT)
		//this.setOpacity((float) 0.70);
		this.setUndecorated(true);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
		
		super.setBounds(x, y, width, height);
	}
}
public class HTTPRequestThreads extends Thread {

	JSONObject requestContent;
	HTTPReqestHandler requestHandler;
	static ExecutorService backgroundPool=Executors.newFixedThreadPool(1);
	static ExecutorService forgroundPool=Executors.newFixedThreadPool(5);
	Boolean retry=false;
	private ResourceBundle rb;
	Boolean showSpinner;
	Spinner spinner=new Spinner();
	public HTTPRequestThreads(JSONObject requestContent,HTTPReqestHandler listner,Boolean showSpinner) {
		// TODO Auto-generated constructor stub
		this.requestContent=requestContent;
		this.requestHandler=listner;
		this.showSpinner=showSpinner;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		spinner.setBounds(screenSize.width/2-200, screenSize.height/2-220, 200, 250);
		spinner.setAlwaysOnTop(true);
		//spinner.setResizable(false);
		
		if(showSpinner==false)
		{
			setPriority(NORM_PRIORITY);
			backgroundPool.submit(this);
		}
		else
		{
			setPriority(MAX_PRIORITY);
			forgroundPool.submit(this);
		}
		//start();
	}

	public HTTPRequestThreads(JSONObject requestContent,HTTPReqestHandler listner,Boolean showSpinner,Boolean retry) {
		// TODO Auto-generated constructor stub
		this.requestContent=requestContent;
		this.requestHandler=listner;
		this.showSpinner=showSpinner;
		this.retry=retry;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		spinner.setBounds(screenSize.width/2-250, screenSize.height/2-250, 250, 250);
		spinner.setAlwaysOnTop(true);
	//	spinner.setResizable(false);
		//start();
	}
	@Override
	public void run() {
		
		if(showSpinner)
		{
			spinner.Textlabel.setText("Loading ....");
			spinner.show();
			
		}
		if(retry)
		{
			if(showSpinner)
			{
				spinner.Textlabel.setText("Retrying ....");
				
				
			}
			if(this.requestHandler!=null)
			{
				this.requestHandler.didRetry(this);
			}
		}
		try{
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		String urlToPostData = rb.getString("http_url_login");
		int CONNECTION_TIMEOUT = 10 * 1000; // timeout in millis
		RequestConfig requestConfig = RequestConfig.custom()
		    .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
		    .setConnectTimeout(CONNECTION_TIMEOUT)
		    .setSocketTimeout(CONNECTION_TIMEOUT)
		    .build();
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		StringEntity stringEntity = new StringEntity(requestContent.toString());
		HttpPost httpPost = new HttpPost(urlToPostData);
		//httpPost.setConfig(RequestConfig.custom().setConnectTimeout(10000));
		httpPost.setConfig(requestConfig);
		
		httpPost.setEntity(stringEntity);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		String response = EntityUtils.toString(httpResponse.getEntity());
//		System.out.println("response:--"+response);
		if(this.requestHandler!=null)
		{
			this.requestHandler.didFinish(this, response);
			if(showSpinner)
			{
				spinner.Textlabel.setText("Finished ....");
				spinner.hide();
			}
		}
		
		}
		catch(Exception e)
		{
			if(showSpinner)
			{
				spinner.hide();
			}
			e.printStackTrace();
			if(this.retry==false)
			{
				HTTPRequestThreads thread=new HTTPRequestThreads(requestContent, requestHandler, showSpinner,retry);
				Constants.apiIsConnected  = true;
			}
			else
			{
				if(this.requestHandler!=null)
				{
					spinner.Textlabel.setText("Failed ....");
					this.requestHandler.didFailed(this, e);
					Constants.apiIsConnected  = false;
				}
			}
			
			
			
		}
		super.run();
	}

}
