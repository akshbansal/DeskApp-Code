package com.im.chat;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.im.utils.Constants;


 class CustomMultiPartEntity extends MultipartEntity

{
 
    private final ProgressListener listener;
 
    public CustomMultiPartEntity(final ProgressListener listener) {
        super();
        this.listener = listener;
    }
 
    public CustomMultiPartEntity(final HttpMultipartMode mode,
            final ProgressListener listener) {
        super(mode);
        this.listener = listener;
    }
 
    public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary,
            final Charset charset, final ProgressListener listener) {
        super(mode, boundary, charset);
        this.listener = listener;
    }
 
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener));
    }
 
 
 
    public static class CountingOutputStream extends FilterOutputStream {
 
        private final ProgressListener listener;
        private long transferred;
 
        public CountingOutputStream(final OutputStream out,
                final ProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }
 
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }
 
        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }
}
 
  interface ProgressListener {
     void transferred(long num);
 }
public class FileUploadIngThread extends Thread {

	public File uploadingFile;
	public long totalsize;
	
	Object info;
	public FileUploaderListener listner;
	static ExecutorService threadPool=Executors.newFixedThreadPool(2);
	 public FileUploadIngThread(File file,Object Info,FileUploaderListener handler) {
		  // TODO Auto-generated constructor stub
		  uploadingFile=file;
		  this.setPriority(MAX_PRIORITY);
		  this.listner=handler;
		  this.info=Info;
		  //this.start();
		  threadPool.submit(this);
		 }

	@Override
	public void run() {
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost post=new HttpPost("https://api.imyourdoc.com/serviceFiles.php");
		
		CustomMultiPartEntity postBody=new CustomMultiPartEntity(new ProgressListener() {
			
			public void transferred(long num) {
				if(listner!=null)
				{
					listner.fileUploadingProgress(FileUploadIngThread.this, num);
				}
			}
		});
		try {
			postBody.addPart("login_token", new StringBody(Constants.loggedinuserInfo.login_token));
			postBody.addPart("method", new StringBody("uploadFile"));
			postBody.addPart("type", new StringBody("image"));
			FileBody fileBody = new FileBody(this.uploadingFile, "application/octect-stream");
			postBody.addPart("upload", fileBody);
			post.setEntity(postBody);
			totalsize=postBody.getContentLength();
			HttpResponse httpResponse;
			try {
				httpResponse = httpClient.execute(post);
			
			String getData = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("the getting data IS " + "jo "

			+ postBody.toString() + "\n" + " response " + getData);
			
			if(this.listner!=null)
			{
				this.listner.fileUploadingCompleted(getData, FileUploadIngThread.this);
			}
			
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				this.listner.fileUploadingFailed(FileUploadIngThread.this);
				e1.printStackTrace();
			} 
		}catch(Exception e)
		{
			e.printStackTrace();
			this.listner.fileUploadingFailed(this);
		}
		
		super.run();
	}

}
