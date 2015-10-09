package com.im.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.prompt.PromptSupport;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.im.bo.SearchUserBO;
import com.im.common.Footer;
import com.im.common.ISearchField;
import com.im.common.LightScrollPane;
import com.im.common.SearchField;
import com.im.common.TopPanel;
import com.im.utils.Constants;
import com.im.utils.HospitalRelatedList;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;

public class SearchUser extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Frame parent;
	int count=0;
	private static ResourceBundle rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
	private JPanel mainPanel;
	private JTextField textSearchName;
	private JTextField textPracticeType;
	private JTextField textHospitalName;
	private JTextField textPICNO;
	private JButton loadMore;
	private static String hospitalId="";
	private static String practiceTypeSelected="";
	Box panel;
	String userType;
	private static boolean fetchingListPrimary = false;
	private static boolean fetchingListSecondary = false;
	Frame thisParent;
	private static String searchingName;
	Point point = new Point();
	boolean resizing = false;
	private JLabel labelRequired;
	public SearchUser(Frame parent,String userType) {
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
//		super(parent);
		Constants.IS_DIALOG = true;
		this.userType = userType;
		this.parent = parent ;
		thisParent = this;
		parent.setEnabled(false);
		setUndecorated(true);
		toFront();
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY,1,2,0));
		try {
			initUI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*public SearchUser()
	{
		setUndecorated(true);
		toFront();
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY,1,2,0));
		try {
			initUI("providers");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	private void initUI() throws IOException {
		labelRequired = new JLabel("*Any one field is required.");
		loadMore = new JButton("Load More");
		loadMore.setBackground(Color.decode("#9CCD21"));
		JPanel panelLoadMore = new JPanel();
		panelLoadMore.setOpaque(true);
		panelLoadMore.setBackground(null);
		panelLoadMore.add(loadMore,BorderLayout.WEST);
		
		loadMore.addActionListener(new LoadMoreListener(userType));
		loadMore.setPreferredSize(new Dimension(150,50));
		loadMore.setForeground(Color.white.brighter());
		BufferedImage master;
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_over.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			loadMore.setIcon(new ImageIcon(scaled));
			loadMore.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
			loadMore.setForeground(Color.white);
			Util.setTransparentBtn(loadMore);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}		
		loadMore.setVisible(false);
		mainPanel = new JPanel();
		mainPanel.add(panel());
		mainPanel.setOpaque(true);
		mainPanel.setBackground(Color.white);
		LightScrollPane scrollPane = new LightScrollPane(mainPanel);
		Box vbox = Box.createVerticalBox();
		add(TopPanel.topButtonPanelForDialog(this,(JFrame) parent),BorderLayout.NORTH);
		
		vbox.add(scrollPane,BorderLayout.CENTER);
		//add(new Footer().commonLowerBox(), BorderLayout.SOUTH);
		vbox.add(panelLoadMore,BorderLayout.SOUTH);
		add(vbox,BorderLayout.CENTER);
		setIconImage(new ImageIcon(getClass().getResource("/images/logoicon.png")).getImage());
		int x = (Constants.SCREEN_SIZE.width)/8;
		int y = (Constants.SCREEN_SIZE.height)/8;
		setBounds(x,y,Constants.SCREEN_SIZE.width/2,Constants.SCREEN_SIZE.height/2);
		setMinimumSize(new Dimension((int) (Math.round(Constants.SCREEN_SIZE.width * 0.40)), 
				(int) (Math.round(Constants.SCREEN_SIZE.height * 0.60))));
		setResizable(false);
		setTitle("Search User");
		setLocationRelativeTo(getParent());
		getContentPane().setBackground(Color.white);
		getContentPane().addMouseListener(new MouseAdapter() {
		      public void mousePressed(MouseEvent e) {
		        resizing = getCursor().equals(Cursor.getDefaultCursor())? false:true;
		        if(!e.isMetaDown()){
		          point.x = e.getX();
		          point.y = e.getY();
		        }
		      }
		    });
		getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
		      public void mouseDragged(MouseEvent e) {
		        if(resizing){
		          Point pt = e.getPoint();
		          setSize(getWidth()+pt.x - point.x,getHeight());
		          point.x = pt.x;
		        }
		        else if(!e.isMetaDown()){
		        Point p = getLocation();
		        setLocation(p.x + e.getX() - point.x,
		            p.y + e.getY() - point.y);
		        }
		      }
		    });
	}
/*	public static void main(String[] args) {
		SearchUser user = new SearchUser();
		user.setVisible(true);
	}*/
	private Box panel() {
		panel = Box.createVerticalBox();
		textSearchName = new JTextField(30);
		textPICNO = new JTextField(30);
		textPracticeType = new JTextField(30);
		textHospitalName = new JTextField(30);

		String serviceUrl = rb.getString("http_url_login");
		String hospitalListMethod = rb.getString("hospital_list");
		
		Box nameField = makeSearchField(textSearchName, "Enter Name here");
		Box practiceField = makeSearchField(textPracticeType, "Enter Practice Type here", true, serviceUrl, hospitalListMethod, "practice");
		Box hospitalField = makeSearchField(textHospitalName, "Enter Network Name here", true, serviceUrl, hospitalListMethod, "hospital");
		Box providerField = makeSearchField(textPICNO, "Enter Provider NPI ");
		
		final Map<String, String> primaryHospIds = new HashMap<String, String>();
		new SearchField().makeSearchField(textHospitalName, rb.getString("http_url_login"), rb.getString("hospital_list"), "hospitals_list",
			new ISearchField() {
				
				@Override
				public void onSelect(int index, String value) {
					if(Constants.showConsole) System.out.println("Selected Id-------:(" + index + ") " + primaryHospIds.get(value));
					//bo.setPrimary_hosp_id(primaryHospIds.get(value));
					hospitalId = primaryHospIds.get(value);
				}
				
				@Override
				public void loopStart() {
					primaryHospIds.clear();
				}
				
				@Override
				public String fieldValue(Map<String, String> map) {
					String display = map.get("name") + "," + map.get("city");
					primaryHospIds.put(display, map.get("hosp_id"));
					return display;
				}
			});
		ArrayList<String> practiseTypeList = Constants.PRACTISE_LIST;
		
		if (null != practiseTypeList)
			fetchPractiseTypeList(textPracticeType, practiseTypeList);
		
		if (userType.equals("physician") || userType.equalsIgnoreCase("staff")) {
			panel.add(labelRequired,BorderLayout.CENTER);
		}
		labelRequired.setForeground(Color.red);
		labelRequired.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		panel.add(Box.createVerticalStrut(40));
		panel.add(nameField);
		panel.add(Box.createVerticalStrut(20));
		if (userType.equals("physician") || userType.equalsIgnoreCase("staff")) {
			panel.add(practiceField);
			panel.add(Box.createVerticalStrut(20));
			panel.add(hospitalField);
		}
		panel.add(Box.createVerticalStrut(20));
		if (userType.equals("physician")) {
			panel.add(providerField);
		}
		panel.setOpaque(true);
		panel.setBackground(Color.white);
		panel.add(Box.createVerticalStrut(10));
		panel.add(getSearchBtn(),BorderLayout.CENTER);
		return panel;
	}
	
	private Box makeSearchField(JTextField field, String placeholder){
		return makeSearchField(field, placeholder, false, null, null, null);
	}
	
	private Box makeSearchField(JTextField field, String placeholder, boolean searchable, String URL, String method, final String searchResultKey) {
		Box finalBox = Box.createHorizontalBox();
		Box box = Box.createHorizontalBox();
		JLabel searchIcon = new JLabel(new ImageIcon(
			((new ImageIcon(getClass().getResource("/images/search.png"))).getImage()).getScaledInstance(20, 20,
				java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
		
		field.setBorder(BorderFactory.createEmptyBorder());
		field.setFont(new Font(Font.decode("CentraleSansRndBold").getFontName(), Font.PLAIN, 20));
		field.setOpaque(false);
		field.setBackground(Color.white);
		PromptSupport.setPrompt(placeholder, field);
		
		//if(searchable == true)
			//makeItSearchField(field, URL, method, searchResultKey);
			
		
		box.add(searchIcon);
		box.add(Box.createHorizontalStrut(10));
		box.add(field);		
		box.add(Box.createHorizontalStrut(10));
		box.setBorder(null);
		box.setBackground(Color.white);
		box.add(Box.createHorizontalStrut(10));
		finalBox.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		finalBox.add(Box.createHorizontalStrut(10));
		finalBox.add(box);
		finalBox.setPreferredSize(new Dimension(400, 40));
		finalBox.setOpaque(true);
		finalBox.setBackground(null);
		finalBox.add(Box.createHorizontalStrut(15));
		return finalBox;
	}
	
	private JButton getSearchBtn() {
		JButton searchButton = new JButton("Search User");
		
		searchButton.addActionListener(new SearchUserListener(userType));
		searchButton.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 24));
		getRootPane().setDefaultButton(searchButton);
		searchButton.setFocusable(true);
		
		searchButton.registerKeyboardAction(searchButton.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
		searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		searchButton.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		searchButton.setForeground(Color.white.brighter());
		BufferedImage master;
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			searchButton.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
			searchButton.setForeground(Color.white);
			searchButton.setIcon(new ImageIcon(scaled));
			Util.setTransparentBtn(searchButton);
		} catch (IOException e2) {
			
		}
			
		return searchButton;
	}
	
	private class SearchUserListener implements ActionListener{
		String userType;
		int i;
		JLabel label = new JLabel("No records found");
		private SearchUserListener(String userType){
			this.userType = userType;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(textSearchName.getText().trim().equals("") && textPICNO.getText().trim().equals("") 
					&& textPracticeType.getText().trim().equals("") && textHospitalName.getText().trim().equals("")){
				JOptionPane.showMessageDialog(getParent(), "Enter at least one field.");
			}else{
				Constants.loader.setVisible(true);
				thisParent.setEnabled(false);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					ArrayList<SearchUserBO> list;
					@Override
					protected Void doInBackground() throws Exception {
						try {
							Box vboxList = Box.createVerticalBox();
							JPanel panelInner = new JPanel();
								list = new HospitalRelatedList(
										"search").searchUserList(
										textSearchName.getText(), userType,
										textPICNO.getText(), practiceTypeSelected,
										hospitalId, count);
								
								if (list != null) {
									for (i = 0; i < list.size(); i++) {
										System.out.println(list.get(i)
												.getFirstName());
										final Box horBox = Box
												.createHorizontalBox();

										BufferedImage icon = Util
												.getProfileImg(list.get(i)
														.getUserName());
										if (icon == null) {
											icon = ImageIO
													.read(WelcomeUtils.class
															.getResource(Constants.IMAGE_PATH+"/"+ "default_pp.png"));

										}
										JLabel Profile = Util.combine(icon, false,
												60, 60);

										// BufferedImage rounded =
										// Util.makeRoundedCorner(icon, 100);
										JLabel name = new JLabel(list.get(i)
												.getFirstName()
												+ " "
												+ list.get(i).getLastName());
										name.setFont(new Font(Font.decode(
												"CentraleSansRndMedium")
												.getFontName(), Font.PLAIN, 18));
										JLabel username = new JLabel(", "
												+ list.get(i).getUserName());
										username.setFont(new Font(Font.decode(
												"CentraleSansRndMedium")

										.getFontName(), Font.PLAIN, 16));
										Box vbox = Box.createVerticalBox();
										JPanel namePanel = new JPanel();
										JPanel profilePanel = new JPanel();
										JPanel titlePanel = new JPanel();
										JLabel jobTitle = new JLabel(list.get(i)
												.getJobTitle() == null ? "" : list
												.get(i).getJobTitle());
										JLabel designation = new JLabel(list.get(i)
												.getDesignation() == null ? ""
												: list.get(i).getDesignation());
										name.setOpaque(true);
										name.setFont(new Font(Font.decode(
												"CentraleSansRndMedium")
												.getFontName(), Font.PLAIN, 18));
										name.setBackground(null);
										jobTitle.setOpaque(true);
										jobTitle.setFont(new Font(Font.decode(
												"CentraleSansRndMedium")
												.getFontName(), Font.PLAIN, 14));
										jobTitle.setBackground(null);
										designation.setOpaque(true);
										designation.setFont(new Font(Font.decode(
												"CentraleSansRndMedium")
												.getFontName(), Font.PLAIN, 14));
										designation.setBackground(null);
										namePanel.setOpaque(true);
										namePanel.setBackground(null);

										profilePanel.setOpaque(true);
										profilePanel.setBackground(null);
										profilePanel.add(Profile);

										titlePanel.setOpaque(true);
										titlePanel.setBackground(null);
										titlePanel.add(jobTitle);

										namePanel.add(name);
										namePanel.add(designation);
										vbox.add(namePanel);
										vbox.add(titlePanel);
										vbox.setPreferredSize(new Dimension(400, 40));
										vbox.setOpaque(true);
										horBox.add(profilePanel);
										horBox.setOpaque(true);
										horBox.setPreferredSize(new Dimension(400,
												80));
										horBox.setBackground(Color.white);
										horBox.setBorder(BorderFactory
												.createMatteBorder(0, 0, 1, 0,
														Color.LIGHT_GRAY));
										horBox.add(vbox);
										horBox.setName(list.get(i).getUserName());
										horBox.addMouseListener(new MouseListener() {

											@Override
											public void mouseReleased(
													MouseEvent arg0) {
											}

											@Override
											public void mousePressed(MouseEvent arg0) {
												horBox.setBackground(Color
														.decode("#F4F9E7"));
											}

											@Override
											public void mouseExited(MouseEvent arg0) {
												horBox.setBackground(Color.white);

											}

											@Override
											public void mouseEntered(MouseEvent arg0) {
												horBox.setBackground(Color
														.decode("#F4F9E7"));
											}

											@Override
											public void mouseClicked(MouseEvent arg0) {
												System.out
														.println("mouse clicked on userId : "
																+ horBox.getName());

												Constants.loader.setVisible(true);
												SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

													@Override
													protected Void doInBackground()
															throws Exception {
														AddUser userProfile = new AddUser(
																thisParent, horBox
																		.getName());
														thisParent.dispose();
														parent.setEnabled(true);
														userProfile
																.setVisible(true);
														Constants.loader
																.setVisible(false);
														return null;
													}

												};
												worker.execute();
												// getParent().setVisible(false);
											}
										});
										vboxList.add(horBox);
									}
									panelInner.setOpaque(true);
									panelInner.setBackground(null);
									vboxList.setOpaque(true);
									vboxList.setBackground(null);
									// panelInner.add(vboxList);
									if (Constants.HAS_MORE == 1) {
										loadMore.setVisible(true);
									}
									else
									{   
										loadMore.setVisible(false);
										textSearchName.setText("");
										textPICNO.setText("");
										practiceTypeSelected  = "";
										hospitalId = "";
									}
									panel.removeAll();
									labelRequired.setVisible(false);
									Constants.loader.setVisible(false);
									thisParent.setEnabled(true);
									panel.add(vboxList);
									panel.revalidate();
									panel.repaint();
									parent.toFront();
									toFront();
								} else {
									label.setForeground(Color.red);
									label.setFont(new Font(Font.decode(
											"CentraleSansRndMedium").getFontName(),
											Font.PLAIN, 18));
									Constants.loader.setVisible(false);
									thisParent.setEnabled(true);
									thisParent.toFront();
									panel.add(Box.createVerticalStrut(10));
									panel.add(label,BorderLayout.CENTER);
									panel.repaint();
									panel.revalidate();
									parent.toFront();
									toFront();
								}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						return null;
					}
					
				};
				worker.execute();
			}
			
		}
	}
	private class LoadMoreListener implements ActionListener{
		String userType;
		JLabel label = new JLabel("No records found");
		int i;
		private LoadMoreListener(String userType){
			this.userType = userType;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			Constants.loader.setVisible(true);
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				   @Override
				   protected Void doInBackground() throws Exception {
					   try {
							
							count = count+1;
							Box vboxList = Box.createVerticalBox();
							JPanel panelInner = new JPanel();
							final ArrayList<SearchUserBO> list = new HospitalRelatedList("search").searchUserList(textSearchName.getText(), userType,textPICNO.getText(),
									textPracticeType.getText(),hospitalId,count);
							if(list!=null){
								for(i=0;i<list.size();i++){
									System.out.println(list.get(i).getFirstName());
									final Box horBox = Box.createHorizontalBox();
									horBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
									BufferedImage icon = Util.getProfileImg(list.get(i).getUserName());
									if(icon == null){
										icon = ImageIO.read(getClass().getResource("/images/default_pp.png"));
									}
									JLabel Profile = Util.combine(icon, false, 50, 50);
									JLabel name = new JLabel(list.get(i).getFirstName()+" "+list.get(i).getLastName());
									name.setFont(new Font(Font.decode("CentraleSansRndMedium")
											.getFontName(), Font.PLAIN, 18));
									JLabel username = new JLabel(", "+list.get(i).getUserName());
									username.setFont(new Font(Font.decode("CentraleSansRndMedium")
											.getFontName(), Font.PLAIN, 16));
									
									Box vbox = Box.createVerticalBox();
									JPanel namePanel = new JPanel();
									JPanel profilePanel = new JPanel();
									JPanel titlePanel = new JPanel();
									JLabel jobTitle = new JLabel(list.get(i).getJobTitle()==null?"":list.get(i).getJobTitle());
									JLabel designation = new JLabel(list.get(i).getDesignation()==null?"":list.get(i).getDesignation());
									name.setOpaque(true);
									name.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
									name.setBackground(null);
									jobTitle.setOpaque(true);
									jobTitle.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 14));
									jobTitle.setBackground(null);
									designation.setOpaque(true);
									designation.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 14));
									designation.setBackground(null);
									namePanel.setOpaque(true);
									namePanel.setBackground(null);
									
									profilePanel.setOpaque(true);
									profilePanel.setBackground(null);
									profilePanel.add(Profile);
									
									titlePanel.setOpaque(true);
									titlePanel.setBackground(null);
									titlePanel.add(jobTitle);
									
									namePanel.add(name);
									namePanel.add(designation);
									vbox.add(namePanel);
									vbox.add(titlePanel);
									vbox.setPreferredSize(new Dimension(400, 40));
									vbox.setOpaque(true);
									horBox.add(profilePanel);
									horBox.setOpaque(true);
									horBox.setPreferredSize(new Dimension(400, 60));
									horBox.setBackground(Color.white);
									horBox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
									horBox.add(vbox);
									horBox.setName(list.get(i).getUserName());
									horBox.addMouseListener(new MouseListener() {
										
										@Override
										public void mouseReleased(MouseEvent arg0) {
										}
										
										@Override
										public void mousePressed(MouseEvent arg0) {
											horBox.setBackground(Color.decode("#F4F9E7"));
										}
										
										@Override
										public void mouseExited(MouseEvent arg0) {
											horBox.setBackground(Color.white);
											
										}
										
										@Override
										public void mouseEntered(MouseEvent arg0) {
											horBox.setBackground(Color.decode("#F4F9E7"));
										}
										
										@Override
										public void mouseClicked(MouseEvent arg0) {
											//if(Constants.showConsole) System.out.println("mouse clicked on userId : " + list.get(i).getUserName());
//											WelcomeUtils.openChatWindow(vCard, centerBox);
											AddUser userProfile = new AddUser(thisParent,horBox.getName());
											userProfile.setVisible(true);
										}
									});
									vboxList.add(horBox);
								}
								panelInner.setOpaque(true);
								panelInner.setBackground(null);
								vboxList.setOpaque(true);
								vboxList.setBackground(null);
								//panelInner.add(vboxList);
								panel.removeAll();
								Constants.loader.setVisible(false);
								panel.add(vboxList);
								panel.revalidate();
								panel.repaint();
							}
							else{
								Constants.loader.setVisible(false);
								label.setForeground(Color.red);
								label.setFont(new Font(Font.decode("CentraleSansRndMedium")
										.getFontName(), Font.PLAIN, 18));
								panel.add(Box.createVerticalStrut(10));
								panel.add(label);
								panel.repaint();
								panel.revalidate();
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					   return null;
					   
				   }
			};
			worker.execute();
			
		}
	}
	private static boolean isAdjusting(JComboBox cbInput) {
		if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) cbInput.getClientProperty("is_adjusting");
		}
		return false;
	}
	
	private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
		cbInput.putClientProperty("is_adjusting", adjusting);
	}
	
	private static void fetchPrimaryHospitalList(final JTextField txtInput) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(cbInput, false);
		cbInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isAdjusting(cbInput)) {
					if (cbInput.getSelectedItem() != null) {
						txtInput.setText(cbInput.getSelectedItem().toString());
					}
				}
			}
		});
		
		txtInput.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				setAdjusting(cbInput, true);
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (cbInput.isPopupVisible()) {
						e.setKeyCode(KeyEvent.VK_ENTER);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.setSource(cbInput);
					cbInput.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						txtInput.setText(cbInput.getSelectedItem().toString());
						cbInput.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cbInput.setPopupVisible(false);
				}
				setAdjusting(cbInput, false);
			}
		});
		txtInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent paramKeyEvent) {}
			
			@Override
			public void keyReleased(KeyEvent paramKeyEvent) {
				if (!fetchingListPrimary && isValidKey(paramKeyEvent.getKeyCode())) {
					updateList();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent paramKeyEvent) {}
			
			private void updateList() {
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					getHospitalList(input);
				}
			}
			
			private void getHospitalList(final String name) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							fetchingListPrimary = true;
							setAdjusting(cbInput, true);
							model.removeAllElements();
							cbInput.setPopupVisible(false);
							model.addElement("Loading...");
							cbInput.setPopupVisible(true);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("method", rb.getString("hospital_list"));
							jsonObject.put("name", name);
							String jsonResponse = Util.getHTTPResponseStr(rb.getString("http_url_login"), jsonObject.toString());
							if(Constants.showConsole) System.out.println("----->" + jsonResponse);
							
							Gson gson = new Gson();
							Map<String, List<Map<String, String>>> javaObj = gson.fromJson(jsonResponse, Map.class);
							List<Map<String, String>> hospitalsList = javaObj.get("hospitals_list");
							model.removeAllElements();
							cbInput.setPopupVisible(false);
							if (null != hospitalsList) {
								model.addElement(name);
								for (Map<String, String> map : hospitalsList) {
									model.addElement(map.get("name") + "," + map.get("city"));
								}
							}
							cbInput.setPopupVisible(model.getSize() > 0);
							setAdjusting(cbInput, false);
							txtInput.requestFocus();
							txtInput.setText("");
							fetchingListPrimary = false;
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				};
				Thread t = new Thread(runnable);
				t.start();
			}
			
			private boolean isValidKey(int key) {
				return ((key >= KeyEvent.VK_0) && (key <= KeyEvent.VK_CLOSE_BRACKET)) || (key <= KeyEvent.VK_BACK_SPACE)
					|| (key <= KeyEvent.VK_SPACE || (key <= KeyEvent.VK_DELETE));
			}
		});
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}
	
	public static void fetchSecondaryHospitalList(final JTextField txtInput) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		cbInput.setLightWeightPopupEnabled(true);
		
		setAdjusting(cbInput, false);
		cbInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isAdjusting(cbInput)) {
					if (cbInput.getSelectedItem() != null) {
						txtInput.setText(cbInput.getSelectedItem().toString());
					}
				}
			}
		});
		
		txtInput.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				setAdjusting(cbInput, true);
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (cbInput.isPopupVisible()) {
						e.setKeyCode(KeyEvent.VK_ENTER);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.setSource(cbInput);
					cbInput.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						txtInput.setText(cbInput.getSelectedItem().toString());
						cbInput.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cbInput.setPopupVisible(false);
				}
				setAdjusting(cbInput, false);
			}
		});
		txtInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent paramKeyEvent) {}
			
			@Override
			public void keyReleased(KeyEvent paramKeyEvent) {
				if (!fetchingListSecondary && isValidKey(paramKeyEvent.getKeyCode())) {
					updateList();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent paramKeyEvent) {}
			
			private void updateList() {
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					getHospitalList(input);
				}
			}
			
			private void getHospitalList(final String name) {
				final ResourceBundle rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							fetchingListSecondary = true;
							setAdjusting(cbInput, true);
							model.removeAllElements();
							cbInput.setPopupVisible(false);
							model.addElement("Loading...");
							cbInput.setPopupVisible(true);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("method", rb.getString("hospital_list"));
							jsonObject.put("name", name);
							String jsonResponse = Util.getHTTPResponseStr(rb.getString("http_url_login"), jsonObject.toString());
							if(Constants.showConsole) System.out.println("----->" + jsonResponse);
							
							Gson gson = new Gson();
							Map<String, List<Map<String, String>>> javaObj = gson.fromJson(jsonResponse, Map.class);
							List<Map<String, String>> hospitalsList = javaObj.get("hospitals_list");
							model.removeAllElements();
							cbInput.setPopupVisible(false);
							if (null != hospitalsList) {
								model.addElement(name);
								for (Map<String, String> map : hospitalsList) {
									model.addElement(map.get("name") + "," + map.get("city"));
								}
							}
							cbInput.setPopupVisible(model.getSize() > 0);
							setAdjusting(cbInput, false);
							txtInput.requestFocus();
							txtInput.setText("");
							fetchingListSecondary = false;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				Thread t = new Thread(runnable);
				t.start();
			}
			
			private boolean isValidKey(int key) {
				return ((key >= KeyEvent.VK_0) && (key <= KeyEvent.VK_CLOSE_BRACKET)) || (key <= KeyEvent.VK_BACK_SPACE)
					|| (key <= KeyEvent.VK_SPACE || (key <= KeyEvent.VK_DELETE));
			}
		});
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}
	
	public static void fetchPractiseTypeList(final JTextField txtInput, final ArrayList<String> items) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(cbInput, false);
		for (String item : items) {
			model.addElement(item);
		}
		cbInput.setSelectedItem(null);
		cbInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isAdjusting(cbInput)) {
					if (cbInput.getSelectedItem() != null) {
						txtInput.setText(cbInput.getSelectedItem().toString());
						practiceTypeSelected = cbInput.getSelectedItem().toString();
					}
				}
			}
		});
		
		txtInput.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				setAdjusting(cbInput, true);
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (cbInput.isPopupVisible()) {
						e.setKeyCode(KeyEvent.VK_ENTER);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.setSource(cbInput);
					cbInput.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						txtInput.setText(cbInput.getSelectedItem().toString());
						cbInput.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cbInput.setPopupVisible(false);
				}
				setAdjusting(cbInput, false);
			}
		});
		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}
			
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}
			
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}
			
			private void updateList() {
				setAdjusting(cbInput, true);
				model.removeAllElements();
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					for (String item : items) {
						if (item.toLowerCase().startsWith(input.toLowerCase())) {
							model.addElement(item);
						}
					}
				}
				cbInput.setPopupVisible(model.getSize() > 0);
				setAdjusting(cbInput, false);
			}
		});
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}
	
//	class HospitalJSONBO {
//		private String name;
//		private String city;
//		private String hospitalId;
//		
//		HospitalJSONBO(String name, String city, String hospitalId){
//			this.name = name;
//			this.city = city;
//			this.hospitalId = hospitalId;
//		}
//		public String getName() {
//			return name;
//		}
//		public void setName(String name) {
//			this.name = name;
//		}
//		
//		public String getCity() {
//			return city;
//		}
//		public void setCity(String city) {
//			this.city = city;
//		}
//		
//		public String getHospitalId() {
//			return hospitalId;
//		}
//		public void setHospitalId(String hospitalId) {
//			this.hospitalId = hospitalId;
//		}
//		
//		@Override
//		public String toString() {
//			return name + ", " + city;
//		}
//	}
}
