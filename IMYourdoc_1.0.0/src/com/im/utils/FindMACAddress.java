package com.im.utils;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 
 * @author Megha
 * <pre>
 * 	Calculating the device id which is the mac address of the desktop system
 * </pre>
 *
 */
public class FindMACAddress {
	StringBuilder sb;
	InetAddress ip;
	public String DeviceIDOfSystem(){
		 
		
		try {
			sb = new StringBuilder();
			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip);
	 
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			if(network!=null){
				byte[] mac = network.getHardwareAddress();
		 
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
				}
			}
			else
			{
				sb.append(ip);
			}
			if(Constants.showConsole) System.out.println("Current MAC address :"+sb.toString());
	 
		} catch (UnknownHostException e) {
	 
			e.printStackTrace();
	 
		} catch (SocketException e){
	 
			e.printStackTrace();
	 
		}
		return sb.toString();	
	   }

}

 
 
   
