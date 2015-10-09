package com.im.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


public class CompressThumbNail  {
	private static final int IMG_WIDTH = 100;
	private static final int IMG_HEIGHT = 100;

	public static BufferedImage CompressFile(BufferedImage original){
		
	try{
		//File imageFile = new File(fileUrl);
		//InputStream is = new FileInputStream(imageFile);
	

		float quality = 0.5f;

		// create a BufferedImage as the result of decoding the supplied InputStream
		BufferedImage image = original;

		// get all image writers for JPG format
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

		if (!writers.hasNext())
			throw new IllegalStateException("No writers found");

		ImageWriter writer = (ImageWriter) writers.next();
	

		ImageWriteParam param = writer.getDefaultWriteParam();

		// compress to a given quality
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);

		// appends a complete image stream containing a single image and
	    //associated stream and image metadata and thumbnails to the output
		//writer.write(null, new IIOImage(image, null, null), param);
				
		
		BufferedImage originalImage = image;
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			
		BufferedImage resizeImageJpg = resizeImage(originalImage, type);
//		ImageIO.write(resizeImageJpg, "jpg", new File("D:\\a231.jpg")); 
//			
//		BufferedImage resizeImagePng = resizeImage(originalImage, type);
//		ImageIO.write(resizeImagePng, "png", new File("D:\\a231.jpg")); 
//			
//		BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type);
//		ImageIO.write(resizeImageHintJpg, "jpg", new File("D:\\a231.jpg")); 
//			
//		BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
//		ImageIO.write(resizeImageHintPng, "png", new File("D:\\a231.jpg")); 
		
		
	
		
		return resizeImageJpg;
		
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
		
    }
	
    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	
	g.dispose();
		
	return resizedImage;
    }
	
    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){
		
	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	g.dispose();	
	g.setComposite(AlphaComposite.Src);
	
	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.setRenderingHint(RenderingHints.KEY_RENDERING,
	RenderingHints.VALUE_RENDER_QUALITY);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	RenderingHints.VALUE_ANTIALIAS_ON);
	
	
	return resizedImage;
    }	
}