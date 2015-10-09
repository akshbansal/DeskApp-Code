package com.im.chat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.im.utils.Constants;
 
/**
 * This program demonstrates a usage of the MultipartUtility class.
 * @author megha
 *
 */
public class MultipartFileUploader {
 
    public static void main(String[] args) {
        String charset = "UTF-8";
        File uploadFile1 = new File("d:/d.jpg");
        File uploadFile2 = new File(" ");
        String requestURL = "http://localhost:8080/FileUploadSpringMVC/uploadFile.do";
 
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
             
            multipart.addFormField("description", "Cool Pictures");
            multipart.addFormField("keywords", "Java,upload,Spring");
             
            multipart.addFilePart("fileUpload", uploadFile1);
            multipart.addFilePart("fileUpload", uploadFile2);
 
            List<String> response = multipart.finish();
             
            if(Constants.showConsole) if(Constants.showConsole) System.out.println("SERVER REPLIED:");
             
            for (String line : response) {
            	if(Constants.showConsole) System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}