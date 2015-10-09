package com.im.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.im.bo.RosterVCardBo;
import com.im.utils.Constants;

public class DeserialiseFile {
	public static List<RosterVCardBo> deserializeFile() {

		List<RosterVCardBo> vcardBoList = null;
		       try
		       {
		    	  File destDir = FileUtils.getUserDirectory();
		    	  String finalPath = destDir.getPath()+"/imyourdoc/"+"GroupsDetails.im_"+Constants.loggedinuserInfo.username.split("@")[0];
		          FileInputStream fileIn =new FileInputStream(finalPath);
		          ObjectInputStream in = new ObjectInputStream(fileIn);
		          vcardBoList = (ArrayList<RosterVCardBo>)in.readObject();
		          in.close();
		          fileIn.close();
		          return vcardBoList;
		       }catch(IOException i)
		       {
		         System.out.println(i.getMessage());
		          return null;
		       }catch(ClassNotFoundException c)
		       {
		    	   System.out.println(c.getMessage());
		          return null;
		       }
		}

}
