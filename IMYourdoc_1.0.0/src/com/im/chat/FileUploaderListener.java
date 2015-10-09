package com.im.chat;

public interface FileUploaderListener
{
		public void fileUploadingCompleted(String response,FileUploadIngThread thread);
		public void fileUploadingProgress(FileUploadIngThread thread,long progress);
		public void fileUploadingFailed(FileUploadIngThread thread);

}