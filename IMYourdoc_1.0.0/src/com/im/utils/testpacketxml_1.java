package com.im.utils;




import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.jar.Attributes;

import org.w3c.dom.Document;

import javax.lang.model.element.Element;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent.EventType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.smackx.packet.MessageEvent;
import org.jivesoftware.smackx.packet.VCard;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;   

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class testpacketxml_1 extends Thread{

    private XMPPConnection xmppConnection;

    public void connect(String server, int port, String s) throws Exception {
        xmppConnection = new XMPPConnection(new ConnectionConfiguration(server, port,s));
        ProviderManager manager=ProviderManager.getInstance();
        
        manager.addIQProvider("list", "urn:xmpp:archive", ChatIQ.ChatIQProvider.getInstanse());
        manager.addIQProvider("chat", "urn:xmpp:archive", RetriveChatIQ_1.RetriveChatIQProvider.getInstanse());
       xmppConnection.connect();
        PacketFilter filter = new IQTypeFilter(IQ.Type.RESULT);
        System.out.println(filter);
		/*xmppConnection.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				System.out.println(packet.toXML());
				
				   String x1=packet.toXML();        
				       System.out.println(x1);
				       String[] tokens = x1.split("from");

				       for (String t : tokens)
				         System.out.println(t);
			}	
		}, filter);*/
    
        
       
//        PacketFilter filter = new IQTypeFilter(IQ.Type.SET); // or IQ.Type.GET etc. according to what you like to filter. 
//        System.out.println("djc");
//	    xmppConnection.addPacketListener(new PacketListener() { 
//	    	
//	        public void processPacket(Packet packet) {
//	        	System.out.println("djc");
//	            // HERE YOU PUT YOUR CODE TO HANDLE THE IQ MESSAGE
//	        	System.out.println(packet.toXML()); 
//	        	//packet.toXML();
//	        
//	        }
//	    }, filter);  
     
    }

    public void disconnect(){
        if(xmppConnection != null){
            xmppConnection.disconnect();
            interrupt();
        }
    }

    public  void login(String username, String password) throws Exception{
        connect("xmpp.imyourdoc.com", 5222, "imyourdoc.com");
        xmppConnection.login(username, password);
        System.out.println("Login successful");
  
      

    } 
        
    

    public void run(){
        try {
            login("harminder3@imyourdoc.com", "Mind@123");
            
            getConversationList("harmsing2@imyourdoc.com");
           
            getXmlForParsing();
            System.out.println("nott");
            displayBuddyList();
            displayPacket();
            listeningForMessages();
           
           // new getConversationThread(xmppConnection, "harmsing2@imyourdoc.com").start();
          
         
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   
	private void displayPacket() {
    	 final PacketFilter filter = new IQTypeFilter(IQ.Type.RESULT); // or IQ.Type.GET etc. according to what you like to filter. 
      
	    xmppConnection.addPacketListener(new PacketListener() { 
	    	
	        public void processPacket(Packet packet) {
	        
	        	 IQ iq=(IQ)packet;
	        	
	        
	        	
//	        	  try {
//	        	    IQ reply=handleIQ(iq);
//	        	    if (reply != null) {
//	        	      PacketDeliverer.deliver(reply);
//	        	    }
//	        	  }
//	        	 catch ( Exception e) {
//	        	    if (iq != null) {
//	        	      try {
//	        	        IQ response=IQ.createResultIQ(iq);
//	        	        response.setChildElement(iq.getChildElement().createCopy());
//	        	        response.setError(PacketError.Condition.not_authorized);
//	        	        sessionManager.getSession(iq.getFrom()).process(response);
//	        	      }
//	        	 catch (      Exception de) {
//	        	      
//	        	        sessionManager.getSession(iq.getFrom()).close();
//	        	      }
//	        	    }
//	        	  } 

	        	Message mes = (Message)packet;
	        	 System.out.println(mes.toString());
	        	// Log.w("***"+mes.toString()+"***","0");
//	        	 String from = packet.getFrom();
//	             String to = packet.getTo();
	      
	         System.out.println(mes);
	        	
	        	
	        	System.out.println("djc");
	            // HERE YOU PUT YOUR CODE TO HANDLE THE IQ MESSAGE
	        	System.out.println(packet.toXML()); 
	        	//packet.toXML();
	        
	        }
	    }, filter);  
		// TODO Auto-generated method stub
		
	}

	public void displayBuddyList()
    {
    Roster roster = xmppConnection.getRoster();
    Collection<RosterEntry> entries = roster.getEntries();
 
    System.out.println("\n\n" + entries.size() + " buddy(ies):");
    for(RosterEntry r:entries)
    {
    System.out.println(r.getUser());
    }
    }

    public static void main(String args[]) throws Exception {
    	testpacketxml_1 gtd = new testpacketxml_1();
    	  XMPPConnection.DEBUG_ENABLED = true;
    	  
        gtd.run();
       testpacketxml_1 mm=new testpacketxml_1();
       mm.getXmlForParsing();
    
       
      
       

           }

    public void listeningForMessages() throws JSONException, java.text.ParseException, ParserConfigurationException, SAXException {
        PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class));
       // System.out.println("filrte"+filter);
        PacketCollector collector = xmppConnection.createPacketCollector(filter);
       // System.out.println("coll"+collector);
        Packet packet = collector.nextResult();
        
   
        
                   
                if (packet instanceof Message) {
                    Message message = (Message) packet;
                    if (message != null && message.getBody() != null)
                    
                    System.out.println("Received message from "
                            + packet.getFrom() + " : "
                            + (message != null ? message.getBody() : "NULL"));
            }
        }

    public void getXmlForParsing()
    {
    	
    	PacketFilter filter = new IQTypeFilter(IQ.Type.RESULT);
    	System.out.println("tttt"+filter);
//    	PacketCollector collector = xmppConnection.createPacketCollector(filter);
//    	 Packet packet = collector.nextResult();
			xmppConnection.addPacketListener(new PacketListener() {
				
		         public void processPacket(Packet packet) {

				        
		        	 String childElemnt=((IQ) packet).getChildElementXML();
		        	 if(childElemnt.contains("harminder3@imyourdoc.com")){
		        	System.out.println("child"+childElemnt);
		        	
		        	//System.out.println("aaaa"+ packet.setPacketID("4aP02"));
		        /*	 String xml=packet.toXML();
					 try {
						 
						//parsexml(xml);
						
					} catch (XmlPullParserException | JSONException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
		        	 }
					
				
				}
//				@Override
//				public void processPacket(final Packet packet) {
//					
		         
//					final IQ paIq=new IQ() {
//						
//						@Override
//						public String getChildElementXML() {
//							String childElemnt=((IQ) packet).getChildElementXML();
//							System.out.println(childElemnt);
//							// TODO Auto-generated method stub
//					String xml=	packet.toXML();
//						 try {
//							parsexml(xml);
//							
//						} catch (XmlPullParserException | JSONException | IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//							return toXML();
//						}
//					};
//					// TODO Auto-generated method stub
////					 String xml=packet.toXML(); 
////					parsexml(xml);
//
//					
//				}
			
				private void parsexml(String xml) throws XmlPullParserException, JSONException, IOException {
					ArrayList<HashMap<String, String>> messageArray = new ArrayList<HashMap<String,String>>();
					 XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				        factory.setNamespaceAware(true);
				        XmlPullParser xpp = factory.newPullParser();
				        System.out.println("parser implementation class is "+xpp.getClass());
					
					
					  xpp.setInput( new StringReader (xml));
					  int eventType = xpp.getEventType();
					  while (eventType != XmlPullParser.END_DOCUMENT) 
					    {
					     if(eventType == XmlPullParser.START_DOCUMENT) 
					     {
					    	 
					     // System.out.println("Start document");
					     }
					     
					     else if(eventType == XmlPullParser.START_TAG) 
					     {
					    	System.out.println("Start tag "+xpp.getName());
					    	 if (xpp.getName().equals("iq")) {
					    		 
					    		
					    		 System.out.println("sabbd"+xpp.getText());
					    	 }
//			                       
//			                    }

					    	  /*if(xpp.getName().equals("chat"))
					    	  {
					    	   HashMap<String, String> convHash = new HashMap<String, String>();

					    	   if(isJSONValid(xpp.getAttributeValue("", "to")))
					    	   {
					    convHash.put("body", xpp.getAttributeValue("", "body"));

					    	    messageArray .add(convHash);  	  
					    	   }
					    	  }*/

					     }
					    	     else if(eventType == XmlPullParser.END_TAG) 
					    	     {
					    	      System.out.println("End tag "+xpp.getText());
					    	     
					    	     }
					    	     else if (eventType == XmlPullParser.START_DOCUMENT) {
					                 System.out.println("Start document: "  + xpp.getText());
					                 
					             }
					              
//					             else if (eventType == XmlPullParser.CDSECT) {
//					                 System.out.println("CDATA Section: " + xpp.getText());
//					             }
//					             else if (eventType == XmlPullParser.COMMENT) {
//					                 System.out.println("Comment: " + xpp.getText());
//					             }
//					             else if (eventType == XmlPullParser.DOCDECL) {
//					                 System.out.println("Document type declaration: " + xpp.getText());
//					             }
//					             else if (eventType == XmlPullParser.ENTITY_REF) {
//					                 System.out.println("Entity Reference: " + xpp.getText());
//					             }
//					             else if (eventType == XmlPullParser.IGNORABLE_WHITESPACE) {
//					                 System.out.println("Ignorable white space: " + xpp.getText());
//					             }
//					    	     else if(eventType == XmlPullParser.TEXT)
//					    	     {
//					    	    	 
//					    	      System.out.println("Texttttttt "+xpp.getText());
//
//					    	      HashMap<String, String> convHash = new HashMap<String, String>();
//					       					       }
					       eventType = xpp.next();
					      }

					      System.out.println("Messages Array "+messageArray);

					      for (int i = 0; i < messageArray.size(); i++) 
					      {
					       HashMap<String, String> messageHash1 = new HashMap<String, String>();

					       messageHash1 = messageArray.get(i);

					       String messageHash = messageHash1.toString();

					       JSONObject jsonObject1 = new JSONObject(messageHash);

					       JSONObject jsonObject = jsonObject1.getJSONObject("body");

					       String file_type = "chat";

					       String file_path = "";

					       String type2 = "Seen";

					       String display_name = "";

					       String messageBody = jsonObject.getString("content");

					       String timeStamp = jsonObject.getString("timestamp");

					       SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

					       SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					       try 
					       {
					        Date date = input.parse(timeStamp); 

					        timeStamp = output.format(date);
					       } 
					       catch (Exception e) 
					       {
					        e.printStackTrace();
					       }

					       String toID =  jsonObject.getString("to");

					       String fromID = jsonObject.getString("from");

					       boolean incoming = false;

					       if(toID.equals(getName()+"@imyourdoc.com"))
					       {
					    
					        incoming =  true;
					        

					      //  display_name = xml.getRosterInfo(fromID, "name");
					       }
					       else
					       {
					        incoming = false;

					       // display_name = xml.getRosterInfo(toID, "name");
					       }

					       if(!messageBody.contains("~$^^xxx*xxx~$^^"))

					       {
					        if(jsonObject.has("file_type"))
					        {
					         file_type = jsonObject.getString("file_type");

					         if(incoming == true)
					         {
					          if(file_type.equalsIgnoreCase("image"))
					          {
					           file_type= "FileGet";

					           type2 ="Display";
					          }
					          else if(file_type.equalsIgnoreCase("File"))
					          {
					           file_type= "file";

					           type2 ="Display";
					          }
					         }
					         else if(incoming == false)
					         {
					          if(file_type.equalsIgnoreCase("image"))
					          {
					           file_type= "FileSend";

					           type2 = "Seen";
					          }
					          else if(file_type.equalsIgnoreCase("File"))
					          {
					           file_type= "file_other";

					           type2 = "Seen";
					          }
					         }

					         file_path = jsonObject.getString("file_path");
					        }
					      }
//					       int holderForStartAndLength[] = new int[2];
//					       char ch[] = xpp.getTextCharacters(holderForStartAndLength);
//					        int start = holderForStartAndLength[0];
//					        int length = holderForStartAndLength[1];
//					        System.out.print("Characters:    \"");
//					        for (int i1 = start; i < start + length; i++) {
//					            switch (ch[i1]) {
//					                case '\\':
//					                    System.out.print("\\\\");
//					                    break;
//					                case '"':
//					                    System.out.print("\\\"");
//					                    break;
//					                case '\n':
//					                    System.out.print("\\n");
//					                    break;
//					                case '\r':
//					                    System.out.print("\\r");
//					                    break;
//					                case '\t':
//					                    System.out.print("\\t");
//					                    break;
//					                default:
//					                    System.out.print(ch[i1]);
//					                    break;
//					            }
//					        }
//					        System.out.print("\"\n");
					 
					     }
					    }
    	
    	
			}, filter);
    	
    	
    	
    }
    private void getConversationList(final String jid) {
    	
    	IQ packet=new IQ(){
    		
    	
		// TODO Auto-generated method stub
    	String xml=getChildElementXML();
    	

		@Override
		public String getChildElementXML() {
			// TODO Auto-generated method stub
			
			String xml="<list xmlns='urn:xmpp:archive' with='"+jid+"'> <set xmlns='http://jabber.org/protocol/rsm'> </set>   </list>";
			
			return xml; 
		}
    	};
    	packet.setPacketID("asd");
    	try {
			sleep(100);
			if(xmppConnection.isAuthenticated())
			{
			//	xmppConnection.sendPacket(packet);
		
			
				getConversationThread_1 thread1= new getConversationThread_1(xmppConnection, "harminder3@imyourdoc.com",new getConversationThread_1.getConversationListListner() {
				
				@Override
				public void didReciveIQ(ChatIQ iq, String Jid) {
					// TODO Auto-generated method stub
					System.out.println("Chat IQ"+iq);
				}
			});

getConversationThread_1 thread2= new getConversationThread_1(xmppConnection, "harminderstaff33@imyourdoc.com",new getConversationThread_1.getConversationListListner() {
	
	@Override
	public void didReciveIQ(ChatIQ iq, String Jid) {
		// TODO Auto-generated method stub
		System.out.println("Chat IQ"+iq);
	}
});

getConversationThread_1 thread3= new getConversationThread_1(xmppConnection, "harmsing3@imyourdoc.com",new getConversationThread_1.getConversationListListner() {
	
	@Override
	public void didReciveIQ(ChatIQ iq, String Jid) {
		// TODO Auto-generated method stub
		System.out.println("Chat IQ"+iq);
	}
});

getConversationThread_1 thread4= new getConversationThread_1(xmppConnection, "riclyp@imyourdoc.com",new getConversationThread_1.getConversationListListner() {
	
	@Override
	public void didReciveIQ(ChatIQ iq, String Jid) {
		// TODO Auto-generated method stub
		System.out.println("Chat IQ"+iq);
	}
});

getConversationThread_1 thread5= new getConversationThread_1(xmppConnection, "rickys@imyourdoc.com",new getConversationThread_1.getConversationListListner() {
	
	@Override
	public void didReciveIQ(ChatIQ iq, String Jid) {
		// TODO Auto-generated method stub
		System.out.println("Chat IQ"+iq);
	}
});
			
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}

	

  
//public void getConversationList(final String jid)
//{
//	
//	
//	IQ packet=new IQ() {
//		
//		@Override
//		public String getChildElementXML() {
//		//	String xml=getChildElementXML();
//		    
// 
//			
//			// TODO Auto-generated method stub
//		//	String xml="<<iq type='get' id='period1' <list xmlns='urn:xmpp:archive'  with='"+jid+"'start='1469-07-21T02:00:00Z' end='1479-07-21T04:00:00Z'><set xmlns='http://jabber.org/protocol/rsm'> <max>30</max></set> </list></iq>";
//		
//			//String xml="<list xmlns='urn:xmpp:archive'        with='"+jid+"'>    </list>";
//		//	System.out.println(xml);
////			PacketFilter filter = new IQTypeFilter(IQ.Type.GET); // or IQ.Type.GET etc. according to what you like to filter. 
////		
////		    xmppConnection.addPacketListener(new PacketListener() { 
////		    	
////		       
////	
//
////		        if ((xml != null) && (xml.length() != 0))
////		        {
////		        	
////		            StringReader saxReader = new StringReader(xml);
////		          //  Document document = saxReader.read(new StringReader(xml));
////		            System.out.println(saxReader);
////		         //   element = document.getRootElement();
////		        }
////			
////			String[] tokens = xml.split("=");
////
////			for (String t : tokens)
//			 // System.out.println(t);
//			
////			String[] tokens1 = xml.split("<chat with=");
////
////			for (String t1 : tokens1)
////			  System.out.println("from"+t1);
//			
//			//return xml;
//			
//			PacketFilter filter = new IQTypeFilter(IQ.Type.RESULT);
//	    System.out.println(filter);
//			xmppConnection.addPacketListener(new PacketListener() {
//				//String element=getChildElementXML();
//				
//				@Override
//				public void processPacket(Packet packet) {
//					
//												
//					System.out.println(packet.toXML());
//					
//				 String xml=packet.toXML();  
//			
//				   try {
//					parsexml(xml);
//				} catch (XmlPullParserException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//					
//					  
////					   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
////					   DocumentBuilder db = null;
////					   try {
////					       db = dbf.newDocumentBuilder();
////					       InputSource is = new InputSource(xml);
////					   
////					       is.setCharacterStream(new StringReader(xml));
////					       try {
////					           Document doc = db.parse(is);
////					           NodeList messagea = doc.getChildNodes();
////					           System.out.println(messagea);
////					           String message = doc.getDocumentElement().getTextContent();
////					           System.out.println(message);
////					       } catch (SAXException e) {
////					           // handle SAXException
////					       } catch (Exception e) {
////					           // handle IOException
////					       }
////					   } catch (ParserConfigurationException e1) {
////					       // handle ParserConfigurationException
////					   }
//				
//					   
//					    			      
//					
//				}
//
//				private void parsexml(String xml) throws XmlPullParserException, JSONException, IOException {
//					ArrayList<HashMap<String, String>> messageArray = new ArrayList<HashMap<String,String>>();
//					 XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//				        factory.setNamespaceAware(true);
//				        XmlPullParser xpp = factory.newPullParser();
//				        System.out.println("parser implementation class is "+xpp.getClass());
//					System.out.println("csn "+xpp);
//					String x1=xml;
//					
//					
//					
//					
//					  xpp.setInput( new StringReader (x1));
//					  int eventType = xpp.getEventType();
//					  while (eventType != XmlPullParser.END_DOCUMENT) 
//					    {
//					     if(eventType == XmlPullParser.START_DOCUMENT) 
//					     {
//					    	 
//					     // System.out.println("Start document");
//					     }
//					     
//					     else if(eventType == XmlPullParser.START_TAG) 
//					     {
//					    	System.out.println("Start tag "+xpp.getName());
//
//					    	  /*if(xpp.getName().equals("chat"))
//					    	  {
//					    	   HashMap<String, String> convHash = new HashMap<String, String>();
//
//					    	   if(isJSONValid(xpp.getAttributeValue("", "to")))
//					    	   {
//					    convHash.put("body", xpp.getAttributeValue("", "body"));
//
//					    	    messageArray .add(convHash);  	  
//					    	   }
//					    	  }*/
//
//					     }
//					    	     else if(eventType == XmlPullParser.END_TAG) 
//					    	     {
//					    	      System.out.println("End tag "+xpp.getText());
//					    	     }
//					    	     else if (eventType == XmlPullParser.START_DOCUMENT) {
//					                 System.out.println("Start document: "  + xpp.getText());
//					             }
//					              
//					             else if (eventType == XmlPullParser.CDSECT) {
//					                 System.out.println("CDATA Section: " + xpp.getText());
//					             }
//					             else if (eventType == XmlPullParser.COMMENT) {
//					                 System.out.println("Comment: " + xpp.getText());
//					             }
//					             else if (eventType == XmlPullParser.DOCDECL) {
//					                 System.out.println("Document type declaration: " + xpp.getText());
//					             }
//					             else if (eventType == XmlPullParser.ENTITY_REF) {
//					                 System.out.println("Entity Reference: " + xpp.getText());
//					             }
//					             else if (eventType == XmlPullParser.IGNORABLE_WHITESPACE) {
//					                 System.out.println("Ignorable white space: " + xpp.getText());
//					             }
//					    	     else if(eventType == XmlPullParser.TEXT)
//					    	     {
//					    	    	 
//					    	      System.out.println("Texttttttt "+xpp.getText());
//
//					    	      HashMap<String, String> convHash = new HashMap<String, String>();
//					       					       }
//					       eventType = xpp.next();
//					      }
//
//					      System.out.println("Messages Array "+messageArray);
//
//					      for (int i = 0; i < messageArray.size(); i++) 
//					      {
//					       HashMap<String, String> messageHash1 = new HashMap<String, String>();
//
//					       messageHash1 = messageArray.get(i);
//
//					       String messageHash = messageHash1.toString();
//
//					       JSONObject jsonObject1 = new JSONObject(messageHash);
//
//					       JSONObject jsonObject = jsonObject1.getJSONObject("body");
//
//					       String file_type = "chat";
//
//					       String file_path = "";
//
//					       String type2 = "Seen";
//
//					       String display_name = "";
//
//					       String messageBody = jsonObject.getString("content");
//
//					       String timeStamp = jsonObject.getString("timestamp");
//
//					       SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
//
//					       SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//					       try 
//					       {
//					        Date date = input.parse(timeStamp); 
//
//					        timeStamp = output.format(date);
//					       } 
//					       catch (Exception e) 
//					       {
//					        e.printStackTrace();
//					       }
//
//					       String toID =  jsonObject.getString("to");
//
//					       String fromID = jsonObject.getString("from");
//
//					       boolean incoming = false;
//
//					       if(toID.equals(getName()+"@imyourdoc.com"))
//					       {
//					    
//					        incoming =  true;
//					        
//
//					      //  display_name = xml.getRosterInfo(fromID, "name");
//					       }
//					       else
//					       {
//					        incoming = false;
//
//					       // display_name = xml.getRosterInfo(toID, "name");
//					       }
//
//					       if(!messageBody.contains("~$^^xxx*xxx~$^^"))
//
//					       {
//					        if(jsonObject.has("file_type"))
//					        {
//					         file_type = jsonObject.getString("file_type");
//
//					         if(incoming == true)
//					         {
//					          if(file_type.equalsIgnoreCase("image"))
//					          {
//					           file_type= "FileGet";
//
//					           type2 ="Display";
//					          }
//					          else if(file_type.equalsIgnoreCase("File"))
//					          {
//					           file_type= "file";
//
//					           type2 ="Display";
//					          }
//					         }
//					         else if(incoming == false)
//					         {
//					          if(file_type.equalsIgnoreCase("image"))
//					          {
//					           file_type= "FileSend";
//
//					           type2 = "Seen";
//					          }
//					          else if(file_type.equalsIgnoreCase("File"))
//					          {
//					           file_type= "file_other";
//
//					           type2 = "Seen";
//					          }
//					         }
//
//					         file_path = jsonObject.getString("file_path");
//					        }
//					      }
//					       int holderForStartAndLength[] = new int[2];
//					       char ch[] = xpp.getTextCharacters(holderForStartAndLength);
//					        int start = holderForStartAndLength[0];
//					        int length = holderForStartAndLength[1];
//					        System.out.print("Characters:    \"");
//					        for (int i1 = start; i < start + length; i++) {
//					            switch (ch[i1]) {
//					                case '\\':
//					                    System.out.print("\\\\");
//					                    break;
//					                case '"':
//					                    System.out.print("\\\"");
//					                    break;
//					                case '\n':
//					                    System.out.print("\\n");
//					                    break;
//					                case '\r':
//					                    System.out.print("\\r");
//					                    break;
//					                case '\t':
//					                    System.out.print("\\t");
//					                    break;
//					                default:
//					                    System.out.print(ch[i1]);
//					                    break;
//					            }
//					        }
//					        System.out.print("\"\n");
//					 
//					     }
//					    }
//				
//				 }
//			, filter);
//			return jid;
//			
//		
//			
//		}
//		
//		
//	};
//	
//	
//	
//	
//	xmppConnection.sendPacket(packet);
//
//
//    }
}




//  http://www.xmlpull.org/v1/download/unpacked/src/java/samples/MyXmlPullApp.java
//	http://www.xmlpull.org/v1/download/unpacked/doc/quick_intro.html











