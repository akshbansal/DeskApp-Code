package com.im.json;

public interface HTTPReqestHandler {

	public void didFinish(HTTPRequestThreads thread,String response);
	public void didFailed(HTTPRequestThreads thread,Exception e);
	public void didRetry(HTTPRequestThreads thread);
}
