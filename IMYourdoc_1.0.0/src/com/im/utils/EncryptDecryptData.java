package com.im.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class EncryptDecryptData {

		public String encodeStringData(String value) {
			
			try {
				value = new String(Base64.encodeBase64(value.getBytes()));
				return value;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		
		public String encodeIntegerData(long value){
			String encodedText;
			try {
				encodedText = new String(Base64.encodeBase64(String.valueOf(value).getBytes()));
				return encodedText;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
			
		}
		
		
	public String decodeStringData(String value) {
			
			try {
				value = new String(Base64.decodeBase64(value.getBytes()));
				return value;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
	}
	public long decodeIntegerData(String value){
		long encodedText;
		try {
			encodedText = Long.parseLong(String.valueOf(Base64.decodeBase64(value.getBytes())));
			return encodedText;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	 public static BufferedImage decodeToImage(String imageString) {

	        BufferedImage image = null;
	        byte[] imageByte;
	        try {
	        	Base64 decoder = new Base64();
	            imageByte = decoder.decode(Base64.decodeBase64(imageString.getBytes()));
	            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
	            image = ImageIO.read(bis);
	            bis.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return image;
	    }
	 public static BufferedImage stringToImage(String imageString)    {
	        //string to ByteArrayInputStream
	        BufferedImage bImage = null;
	        ByteArrayInputStream bais = new ByteArrayInputStream(imageString.getBytes());
	        try {
	            bImage = ImageIO.read(bais);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }

	        return bImage;
	    }

	 public static String encodeToString(BufferedImage image, String type) {
	        String imageString = null;
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();

	        try {
	        	BufferedImage compressedImage = CompressThumbNail.CompressFile(image);
	            ImageIO.write(compressedImage, type, bos);
	            byte[] imageBytes = bos.toByteArray();
	            imageString =  new String(Base64.encodeBase64(imageBytes));
	            bos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return imageString;
	    }
}
