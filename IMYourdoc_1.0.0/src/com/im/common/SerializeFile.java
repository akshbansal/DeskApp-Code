package com.im.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.im.bo.RosterVCardBo;
import com.im.utils.Constants;

public class SerializeFile {
	
	public static void SerializeObject(List<RosterVCardBo> object)
	{
		try
		  {
			   File destDir = FileUtils.getUserDirectory();
			   String finalPath = destDir.getPath()+"/imyourdoc/"+"GroupsDetails_"+Constants.loggedinuserInfo.username.split("@")[0]+".im";
			   FileOutputStream fileOut = new FileOutputStream(finalPath,true);
			   ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			   outStream.writeObject(object);
			   outStream.close();
			   fileOut.close();
		  }
		  catch(IOException i)
		  {
			   i.printStackTrace();
	      }
	}
	


}
