package com.im.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.im.common.Footer;
import com.im.common.LightScrollPane;
import com.im.common.TopPanel;
import com.im.patientscreens.PatientRegister;
import com.im.physicianscreens.PhysicianRegister;
import com.im.staffscreens.StaffRegister;
import com.im.utils.Constants;
import com.im.utils.HospitalRelatedList;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;


public class UserTypeDialog extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton buttonPhysician;
	JButton buttonPatient;
	JButton buttonStaff;
	JButton infoPatient;
	JButton infoPhysician;
	JButton infoStaff;
	Box hboxPatient;
	Box hboxPhysician;
	Box hboxStaff;
	JButton maxButton;
	JButton minButton;
	Box infoPhysicianBox;
	Box infoStaffBox;
	Box infoPatientBox;
	JFrame thisFrame;
	JPanel infoPhysicianPanel;
	JPanel infoStaffPanel;
	JPanel infoPatientPanel;
	JLabel infoPhysicianLabel;
	JLabel infoStaffLabel;
	JLabel infoPatientLabel;
	Frame parent;
	Point point = new Point();
	boolean resizing = false;
	public UserTypeDialog(Frame parent) {
//		super(parent);
		Constants.IS_DIALOG = true;
		thisFrame = this;
		this.parent = parent ;
		parent.setEnabled(false);
		setUndecorated(true);
		toFront();
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY,1,2,0));
		Constants.PARENT = parent;
		try {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

				@Override
				protected Void doInBackground() throws Exception {
					HospitalRelatedList hospitalRelatedList = new HospitalRelatedList();
					try{
					if(Constants.PRACTISE_LIST==null)
						Constants.PRACTISE_LIST = hospitalRelatedList.getPractiseListResponse();
					if(Constants.DESIGNATION_LIST ==null)
						Constants.DESIGNATION_LIST = hospitalRelatedList.getDesignationListResponse();
					if(Constants.JOB_TITLE_LIST ==null)
						Constants.JOB_TITLE_LIST = hospitalRelatedList.getTitleListResponse();
					}
					catch(Exception e){
						System.out.println(e);
					}
					return null;
				}
				
			};
			worker.execute();
			initUI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initUI() throws IOException {
		
		hboxPatient =Box.createHorizontalBox();
		hboxPhysician = Box.createHorizontalBox();
		hboxStaff = Box.createHorizontalBox();
		infoPatientBox = Box.createHorizontalBox();
		infoPhysicianBox = Box.createHorizontalBox();
		infoStaffBox = Box.createHorizontalBox();
		
		infoPatientPanel = new JPanel();
		infoPhysicianPanel = new JPanel();
		infoStaffPanel = new JPanel();
		
		
		 infoPhysicianLabel = new JLabel();
		 infoPatientLabel = new JLabel();
		 infoStaffLabel = new JLabel();
		 
		 
		 infoPhysicianLabel.setText("<html><center>Please use ‘Provider’ registration option if you are <br>health care provider and have an NPI number.</center></html>");
		 infoPatientLabel.setText("<html><center>Please use ‘Patient’ registration option <br> if you are health care consumer.</center></html>");
		 infoStaffLabel.setText("<html><center>Please use ‘Staff’ registration option if you are health <br>care provider and do not have an NPI number.</center></html>");
		 
		 infoPhysicianLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		 infoPatientLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		 infoStaffLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		 
		 infoPhysicianLabel.setForeground(Color.lightGray);
		 infoPatientLabel.setForeground(Color.lightGray);
		 infoStaffLabel.setForeground(Color.lightGray);
		 
		 
		 infoPhysicianBox.setOpaque(true);
		 infoPhysicianBox.setBackground(null);
		 infoPhysicianBox.add(Box.createVerticalStrut(40));
		 infoPhysicianBox.add(infoPhysicianLabel,BorderLayout.CENTER);
		 
		
		 infoStaffBox.setOpaque(true);
		 infoStaffBox.setBackground(null);
		 infoStaffBox.add(Box.createVerticalStrut(40));
		 infoStaffBox.add(infoStaffLabel,BorderLayout.CENTER);
		
		 
		 infoPatientBox.setOpaque(true);
		 infoPatientBox.setBackground(null);
		 infoPatientBox.add(Box.createVerticalStrut(40));
		 infoPatientBox.add(infoPatientLabel,BorderLayout.CENTER);
		 
		
		 infoPatientPanel.setOpaque(true);
		 infoPatientPanel.setBackground(null);
		 infoPatientPanel.add(infoPatientBox,BorderLayout.CENTER);
		 infoPatientPanel.setVisible(false);
		 
		 infoPhysicianPanel.setOpaque(true);
		 infoPhysicianPanel.setBackground(null);
		 infoPhysicianPanel.add(infoPhysicianBox,BorderLayout.CENTER);
		 infoPhysicianPanel.setVisible(false);
		 
		 infoStaffPanel.setOpaque(true);
		 infoStaffPanel.setBackground(null);
		 infoStaffPanel.add(infoStaffBox,BorderLayout.CENTER);
		 infoStaffPanel.setVisible(false);
		
		 JLabel topIcon = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage()).getScaledInstance(150,200, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		 /*
		  * JLabel labelPhysician = new JLabel(new ImageIcon("images/physicianRegistration.png"));
			JLabel labelPatient = new JLabel(new ImageIcon("images/patientRegistration.png"));
			JLabel labelStaff = new JLabel(new ImageIcon("images/staffRegistration.png"));
		*/
		buttonPhysician = new JButton(new ImageIcon(getClass().getResource("/images/register_on1.png")));
		buttonPatient = new JButton(new ImageIcon(getClass().getResource("/images/register_on1.png")));
		buttonStaff = new JButton(new ImageIcon(getClass().getResource("/images/register_on1.png")));
		
		infoPatient = new JButton(new ImageIcon(getClass().getResource("/images/info_on1.png")));
		infoPhysician = new JButton(new ImageIcon(getClass().getResource("/images/info_on1.png")));
		infoStaff = new JButton(new ImageIcon(getClass().getResource("/images/info_on1.png")));
		
//		labelPhysician.setIcon(new ImageIcon("images/register_on.png"));
		buttonPatient.setText("Patient Registration");
		buttonPhysician.setText("Provider Registration");
		buttonStaff.setText("Staff Registration");
		
		buttonPhysician.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				buttonPhysician.setSelected(true);
				buttonPatient.setSelected(false);
				buttonStaff.setSelected(false);
				
				infoPhysician.setSelected(false);
				infoPatient.setSelected(false);
				infoStaff.setSelected(false);
				buttonPhysician.setSelectedIcon(new ImageIcon(getClass().getResource("/images/register_over1.png")));
				PhysicianRegister physicianRegister = new PhysicianRegister(thisFrame);
				physicianRegister.setVisible(true);
				
				
//				hboxPhysician.setBackground(Color.decode("#9CCD21"));
//				hboxStaff.setBackground(Color.decode("#AFB1B4"));
//				hboxPatient.setBackground(Color.decode("#AFB1B4"));
			}
		});
		buttonPatient.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				buttonPatient.setSelected(true);
				buttonPhysician.setSelected(false);
				buttonStaff.setSelected(false);
				infoPhysician.setSelected(false);
				infoPatient.setSelected(false);
				infoStaff.setSelected(false);
				buttonPatient.setSelectedIcon(new ImageIcon(getClass().getResource("/images/register_over1.png")));
//				hboxPatient.setBackground(Color.decode("#9CCD21"));
//				hboxPhysician.setBackground(Color.decode("#AFB1B4"));
//				hboxStaff.setBackground(Color.decode("#AFB1B4"));
				PatientRegister patientRegister = new PatientRegister(thisFrame);
				patientRegister.setVisible(true);
			}
		});
		buttonStaff.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				buttonStaff.setSelected(true);
				buttonPhysician.setSelected(false);
				buttonPatient.setSelected(false);
				infoPhysician.setSelected(false);
				infoPatient.setSelected(false);
				infoStaff.setSelected(false);
				buttonStaff.setSelectedIcon(new ImageIcon(getClass().getResource("/images/register_over1.png")));
//				hboxStaff.setBackground(Color.decode("#9CCD21"));
//				hboxPhysician.setBackground(Color.decode("#AFB1B4"));
//				hboxPatient.setBackground(Color.decode("#AFB1B4"));
				StaffRegister staffRegister = new StaffRegister(thisFrame);
				staffRegister.setVisible(true);
			}
		});
		
		
		infoPatient.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				buttonPatient.setSelected(false);
				buttonPhysician.setSelected(false);
				buttonStaff.setSelected(false);
				
				infoPatient.setSelected(true);
				infoPhysician.setSelected(false);
				infoStaff.setSelected(false);
				infoPatient.setSelectedIcon(new ImageIcon(getClass().getResource("/images/info_over1.png")));
				
				if(!infoPatientPanel.isVisible()){
					infoPatientPanel.setVisible(true);
					infoPhysicianPanel.setVisible(false);
					infoStaffPanel.setVisible(false);
				}
				else
				{
					infoPatientPanel.setVisible(false);
				}
//				hboxPhysician.setBackground(Color.decode("#9CCD21"));
//				hboxStaff.setBackground(Color.decode("#AFB1B4"));
//				hboxPatient.setBackground(Color.decode("#AFB1B4"));
			}
		});
		infoPhysician.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				buttonPatient.setSelected(false);
				buttonPhysician.setSelected(false);
				buttonStaff.setSelected(false);
				infoPhysician.setSelected(true);
				infoPatient.setSelected(false);
				infoStaff.setSelected(false);
				infoPhysician.setSelectedIcon(new ImageIcon(getClass().getResource("/images/info_over1.png")));
				
				if(!infoPhysicianPanel.isVisible()){
					infoPhysicianPanel.setVisible(true);
					infoPatientPanel.setVisible(false);
					infoStaffPanel.setVisible(false);
				}
				else
				{
					infoPhysicianPanel.setVisible(false);
				}
				
				
//				hboxPatient.setBackground(Color.decode("#9CCD21"));
//				hboxPhysician.setBackground(Color.decode("#AFB1B4"));
//				hboxStaff.setBackground(Color.decode("#AFB1B4"));
			}
		});
		infoStaff.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				buttonStaff.setSelected(false);
				buttonPhysician.setSelected(false);
				buttonPatient.setSelected(false);
				infoPhysician.setSelected(false);
				infoPatient.setSelected(false);
				infoStaff.setSelected(true);
				infoStaff.setSelectedIcon(new ImageIcon(getClass().getResource("/images/info_over1.png")));
//				hboxStaff.setBackground(Color.decode("#9CCD21"));
//				hboxPhysician.setBackground(Color.decode("#AFB1B4"));
//				hboxPatient.setBackground(Color.decode("#AFB1B4"));
				
				
				if(!infoStaffPanel.isVisible()){
					infoStaffPanel.setVisible(true);
					infoPhysicianPanel.setVisible(false);
					infoPatientPanel.setVisible(false);
				}
				else
				{
					infoStaffPanel.setVisible(false);
				}
			}
		});
		
		Util.setTransparentBtn(buttonPatient);
		Util.setTransparentBtn(buttonPhysician);
		Util.setTransparentBtn(buttonStaff);
		
		Util.setTransparentBtn(infoPatient);
		Util.setTransparentBtn(infoPhysician);
		Util.setTransparentBtn(infoStaff);
		
		
		buttonPhysician.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 24));
		buttonPhysician.setForeground(Color.white);
		
		buttonPatient.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 24));
		buttonPatient.setForeground(Color.white);
		
		buttonStaff.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 24));
		buttonStaff.setForeground(Color.white);
		
		buttonPhysician.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		buttonPatient.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		buttonStaff.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		
		hboxPhysician.add(buttonPhysician);
		hboxPhysician.add(infoPhysician);
		hboxPhysician.setOpaque(true);
		
//		hboxPhysician.setBackground(Color.decode("#AFB1B4"));
//		hboxStaff.setBackground(Color.decode("#AFB1B4"));
//		hboxPatient.setBackground(Color.decode("#AFB1B4"));
		hboxStaff.add(buttonStaff);
		hboxStaff.add(infoStaff);
		hboxPatient.add(buttonPatient);
		hboxPatient.add(infoPatient);
		
//		hbox1.add(physicianPanel);
		hboxPatient.setOpaque(true);
//		hbox2.add(staffPanel);
		hboxPhysician.setOpaque(true);
		//hboxPhysician.setBackground(null);
//		hbox3.add(patientPanel);
		hboxStaff.setOpaque(true);
		Box panel = Box.createVerticalBox();
		JPanel mainPanel = new JPanel();
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(null);
		logoPanel.setOpaque(true);
		logoPanel.add(topIcon,BorderLayout.CENTER);
		panel.setOpaque(true);
		panel.setBackground(Color.white);
		panel.add(logoPanel,BorderLayout.CENTER);
		panel.add(Box.createVerticalStrut(30));
		panel.add(hboxPatient,BorderLayout.CENTER);
		panel.add(Box.createVerticalStrut(10));
		panel.add(infoPatientPanel,BorderLayout.CENTER);
		panel.add(hboxPhysician,BorderLayout.CENTER);
		panel.add(Box.createVerticalStrut(10));
		panel.add(infoPhysicianPanel,BorderLayout.CENTER);
		panel.add(hboxStaff,BorderLayout.CENTER);
		panel.add(infoStaffPanel,BorderLayout.CENTER);
//		panel.add(Box.createVerticalStrut(50));
//		JScrollPane sc = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		sc.setOpaque(true);
//		sc.setBackground(null);
//		sc.setWheelScrollingEnabled(true);
//		sc.setBorder(null);
		//createLayout(sc);
		mainPanel.add(panel);
		mainPanel.setOpaque(true);
		mainPanel.setBackground(Color.white);
		LightScrollPane scrollPane = new LightScrollPane(mainPanel);
		add(TopPanel.topButtonPanelForDialog(this,(JFrame) parent),BorderLayout.NORTH);
		add(scrollPane,BorderLayout.CENTER);
		//add(new Footer().loginLowerBox(), BorderLayout.SOUTH);
//		scrollPane.setMinimumSize(new Dimension(680, 100));
		setIconImage(new ImageIcon(getClass().getResource("/images/logoicon.png")).getImage());
		  int x = (Constants.SCREEN_SIZE.width)/8;
			 int y = (Constants.SCREEN_SIZE.height)/8;
			  setBounds(x,y,Constants.SCREEN_SIZE.width/2,Constants.SCREEN_SIZE.height/2);
		    setMinimumSize(new Dimension((int) (Math.round(Constants.SCREEN_SIZE.width * 0.60)), (int) (Math.round(Constants.SCREEN_SIZE.height * 0.70))));
		setResizable(false);
		/*labelPhysician.addMouseListener(new OpenPhysicianDialogListener());
		labelStaff.addMouseListener(new OpenStaffDialogListener());
		labelPatient.addMouseListener(new OpenPatientDialogListener());*/
//		setModalityType(ModalityType.TOOLKIT_MODAL);
		setTitle("Signup Type");
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


/* final class OpenPhysicianDialogListener implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		RegistrationDialog registerDialog = new RegistrationDialog(null, "Provider");
		Constants.OPENED_REGISTER_DIALOG = registerDialog;
		registerDialog.setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
 final class OpenStaffDialogListener implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		RegistrationDialog registerDialog = new RegistrationDialog(null, "Staff");
		Constants.OPENED_REGISTER_DIALOG = registerDialog;
		registerDialog.setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
 final class OpenPatientDialogListener implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		RegistrationDialog registerDialog = new RegistrationDialog(null, "Patient");
		Constants.OPENED_REGISTER_DIALOG = registerDialog;
		registerDialog.setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}*/
	
}