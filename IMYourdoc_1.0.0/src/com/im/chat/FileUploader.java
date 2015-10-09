package com.im.chat;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class FileUploader extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFileChooser filechooser=new JFileChooser();
	
	public FileUploader() throws HeadlessException {
		// TODO Auto-generated constructor stub
	
		filechooser.setBounds(10, 10, 200, 50);
		
		this.add(filechooser);
		
		filechooser.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			System.out.println("e"+e+filechooser.getSelectedFile());
			
			
			File file=filechooser.getSelectedFile();
			if(file!=null)
			{
				
				new FileUploadIngThread(file,file.getName(), new FileUploaderListener() {
					
					public void fileUploadingFailed(FileUploadIngThread thread) {
						// TODO Auto-generated method stub
						
					}
					
					public void fileUploadingCompleted(String response,
							FileUploadIngThread thread) {
						// TODO Auto-generated method stub
						System.out.println("Response ----"+response);
						
					}

					public void fileUploadingProgress(
							FileUploadIngThread thread, long progress) {
						System.out.println(" Name ---"+thread.info +" progress"+progress);
						
					}
				});
			
			}
			
							
			}
		});
	}

	public FileUploader(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	public FileUploader(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public FileUploader(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
	
	FileUploader fileUploader=new FileUploader();
	
	fileUploader.setBounds(0, 0, 500, 600);
	

	
	fileUploader.show();
	
	}

}
