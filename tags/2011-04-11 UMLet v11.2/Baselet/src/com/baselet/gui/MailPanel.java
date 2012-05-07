package com.baselet.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.io.DiagramFileHandler;

public class MailPanel extends JPanel {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private static final long serialVersionUID = 1L;

	/**
	 * Some int and String
	 */

	private final int paddingTop = 1;
	private final int paddingBottom = 1;
	private final int outerPaddingLeft = 15;
	private final int outerPaddingRight = 15;
	private final int halfHorizontalDividerSpace = 2;
	private final int verticalDividerSpace = 10;

	/**
	 * Components
	 */

	private GridBagLayout layout = new GridBagLayout();

	private JLabel lb_from = new JLabel("From:");
	private JTextField tf_from = new JTextField();
	private JLink lnk_smtpInfo = new JLink(Program.WEBSITE + "/smtp.htm", "What is SMTP?");

	private JLabel lb_smtp = new JLabel("SMTP:");
	private JTextField tf_smtp = new JTextField();
	private JCheckBox cb_smtp_auth = new JCheckBox();

	private JLabel lb_smtpUser = new JLabel("User:");
	private JTextField tf_smtpUser = new JTextField();

	private JLabel lb_smtpPW = new JLabel("PW:");
	private JPasswordField pf_smtpPW = new JPasswordField();
	private JCheckBox cb_pwSave = new JCheckBox();

	private JLabel lb_to = new JLabel("To:");
	private JTextField tf_to = new JTextField();

	private JLabel lb_cc = new JLabel("CC:");
	private JTextField tf_cc = new JTextField();

	private JLabel lb_bcc = new JLabel("BCC:");
	private JTextField tf_bcc = new JTextField();

	private JLabel lb_subject = new JLabel("Subject:");
	private JTextField tf_subject = new JTextField();

	private JTextArea ta_text = new JTextArea(5, 5);
	JScrollPane sp_text = new JScrollPane(ta_text);

	private JCheckBox cb_attachXml = new JCheckBox();
	private JCheckBox cb_attachGif = new JCheckBox();
	private JCheckBox cb_attachPdf = new JCheckBox();

	private JButton bt_send = new JButton("Send");
	private JButton bt_cancel = new JButton("Cancel");

	private JPanel panel_attachments = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel panel_attachmentsWithButton = new JPanel(layout);

	// the padding between lines is different for the labels and text components of the grid bag layout
	private Insets paddingLeftLabel = new Insets(paddingTop, outerPaddingLeft, paddingBottom, halfHorizontalDividerSpace);
	private Insets paddingMessagebox = new Insets(paddingTop, outerPaddingLeft, paddingBottom, outerPaddingRight);
	private Insets paddingText = new Insets(paddingTop, halfHorizontalDividerSpace, paddingBottom, outerPaddingRight);
	private Insets paddingCheckbox = new Insets(paddingTop - 2, halfHorizontalDividerSpace, paddingBottom - 2, outerPaddingRight);
	private Insets paddingRightLabel = new Insets(paddingTop, halfHorizontalDividerSpace, paddingBottom, halfHorizontalDividerSpace);
	private Insets noPadding = new Insets(0, 0, 0, 0);

	// the label doesn't get any additional space. it's always as short as possible
	private double noWeight = 0;
	private double fullWeight = 1;
	private double leftWeight = 0.75;
	private double rightWeight = 0.25;

	// the constraint int to fill the width
	private int fillWidth = GridBagConstraints.HORIZONTAL;
	private int fillBoth = GridBagConstraints.BOTH;

	public MailPanel() {

		initAndFillComponents();

		setLayout(layout);
		setSize(new Dimension(0, Constants.mail_split_position));

		int line = 0;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), 0, line, 10, 1, fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, lb_to, 0, line, 1, 1, fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tf_to, 1, line, 1, 1, fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lb_from, 2, line, 1, 1, fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tf_from, 3, line, 1, 1, fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, lnk_smtpInfo, 4, line, 1, 1, fillWidth, noWeight, 0, paddingText);
		line++;
		addComponent(this, layout, lb_cc, 0, line, 1, 1, fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tf_cc, 1, line, 1, 1, fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lb_smtp, 2, line, 1, 1, fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tf_smtp, 3, line, 1, 1, fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, cb_smtp_auth, 4, line, 1, 1, fillWidth, noWeight, 0, paddingText);
		line++;
		addComponent(this, layout, lb_bcc, 0, line, 1, 1, fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tf_bcc, 1, line, 1, 1, fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lb_smtpUser, 2, line, 1, 1, fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, tf_smtpUser, 3, line, 1, 1, fillWidth, rightWeight, 0, paddingRightLabel);
		line++;
		addComponent(this, layout, lb_subject, 0, line, 1, 1, fillWidth, noWeight, 0, paddingLeftLabel);
		addComponent(this, layout, tf_subject, 1, line, 1, 1, fillWidth, leftWeight, 0, paddingText);
		addComponent(this, layout, lb_smtpPW, 2, line, 1, 1, fillWidth, noWeight, 0, paddingRightLabel);
		addComponent(this, layout, pf_smtpPW, 3, line, 1, 1, fillWidth, rightWeight, 0, paddingRightLabel);
		addComponent(this, layout, cb_pwSave, 4, line, 1, 1, fillWidth, noWeight, 0, paddingCheckbox);
		line++;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), 0, line, 10, 1, fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, sp_text, 0, line, 5, 1, fillBoth, leftWeight, 1, paddingMessagebox);
		line++;
		addComponent(this, layout, panel_attachmentsWithButton, 1, line, 5, 1, fillWidth, fullWeight, 0, noPadding);
		line++;
		addComponent(this, layout, Box.createRigidArea(new Dimension(0, verticalDividerSpace)), 0, line, 4, 1, fillWidth, fullWeight, 0, noPadding);

	}

	private void initAndFillComponents() {

		ta_text.setText(Constants.DEFAULT_MAILTEXT);

		cb_pwSave.setText("save in config");
		cb_attachXml.setText("attach " + Program.EXTENSION.toUpperCase());
		cb_attachGif.setText("attach GIF");
		cb_attachPdf.setText("attach PDF");
		cb_smtp_auth.setText("authentication");

		bt_send.addActionListener(new SendActionListener());
		bt_cancel.addActionListener(new CancelActionListener());
		cb_smtp_auth.addActionListener(new AuthentificationActionListener());

		// Set Tooltips
		String adressToolTip = "Separate multiple adresses with ','";
		cb_pwSave.setToolTipText("WARNING: The password is stored as plain text in " + Program.CONFIG_NAME);
		tf_from.setToolTipText(adressToolTip);
		tf_to.setToolTipText(adressToolTip);
		tf_cc.setToolTipText(adressToolTip);
		tf_bcc.setToolTipText(adressToolTip);

		// Fill Attachment Panel
		panel_attachments.add(cb_attachXml);
		panel_attachments.add(Box.createRigidArea(new Dimension(5, 0)));
		panel_attachments.add(cb_attachGif);
		panel_attachments.add(Box.createRigidArea(new Dimension(5, 0)));
		panel_attachments.add(cb_attachPdf);

		// Fill the superpanel which holds attachments and the send button
		addComponent(panel_attachmentsWithButton, layout, panel_attachments, 0, 0, 1, 1, fillWidth, fullWeight, 0, noPadding);
		addComponent(panel_attachmentsWithButton, layout, bt_send, 1, 0, 1, 1, fillWidth, fullWeight, 0, paddingText);
		addComponent(panel_attachmentsWithButton, layout, bt_cancel, 2, 0, 1, 1, fillWidth, fullWeight, 0, paddingText);

		setAllFonts();
		readConstants();
		checkVisibilityOfSmtpAuth();
	}

	private void sendMail() {

		/**
		 * Initialize some variables and objects
		 */

		String smtpHost = tf_smtp.getText();
		String smtpUser = tf_smtpUser.getText();
		String smtpPW = String.valueOf(pf_smtpPW.getPassword());
		String from = tf_from.getText();
		String[] to = removeWhitespaceAndSplitAt(tf_to.getText());
		String[] cc = removeWhitespaceAndSplitAt(tf_cc.getText());
		String[] bcc = removeWhitespaceAndSplitAt(tf_bcc.getText());
		String subject = tf_subject.getText();
		String text = ta_text.getText();
		boolean useSmtpAuthentication = false;
		File diagramXml = null;
		File diagramGif = null;
		File diagramPdf = null;
		int nrOfAttachments = 0;

		// Set SMTP Authentication if the user or password field isn't empty
		if (!smtpUser.isEmpty() || !smtpPW.isEmpty()) useSmtpAuthentication = true;

		// Create the temp diagrams to send
		try {
			final String diagramName = "diagram_" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date());
			DiagramFileHandler fileHandler = Main.getInstance().getDiagramHandler().getFileHandler();
			if (cb_attachXml.isSelected()) {
				nrOfAttachments++;
				diagramXml = fileHandler.doSaveTempDiagram(diagramName, Program.EXTENSION);
			}
			if (cb_attachGif.isSelected()) {
				nrOfAttachments++;
				diagramGif = fileHandler.doSaveTempDiagram(diagramName, "gif");
			}
			if (cb_attachPdf.isSelected()) {
				nrOfAttachments++;
				diagramPdf = fileHandler.doSaveTempDiagram(diagramName, "pdf");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "There has been an error with your diagram. Please make sure it's not empty.", "Diagram Error", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
			return;
		}

		/**
		 * Check if all necessary fields are filled
		 */

		String errorMsg = null;
		if (smtpHost.isEmpty()) errorMsg = "The SMTP field must not be empty";
		else if (from.isEmpty()) errorMsg = "The FROM field must not be empty";
		else if (to.length == 0) errorMsg = "The TO field must not be empty";

		if (errorMsg != null) {
			JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
			return;
		}

		/**
		 * Set up the mail
		 */

		try {
			// Get system properties and session
			Properties props = System.getProperties();
			Session session = Session.getInstance(props);

			// Define message and it's parts
			MimeMessage message = new MimeMessage(session);
			MimeBodyPart textPart = new MimeBodyPart();
			MimeBodyPart[] attachmentPart = new MimeBodyPart[nrOfAttachments];
			for (int i = 0; i < nrOfAttachments; i++)
				attachmentPart[i] = new MimeBodyPart();

			// Build multipart message
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(textPart);
			for (int i = 0; i < nrOfAttachments; i++)
				multipart.addBodyPart(attachmentPart[i]);
			message.setContent(multipart);

			/**
			 * Fill the message properties
			 */

			// Set the SMTP Host
			props.put("mail.smtp.host", smtpHost);

			// We want to close the connection immediately after sending
			props.put("mail.smtp.quitwait", "false");

			// We want to use encryption if needed
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.protocols", "SSLv3 TLSv1");

			// If authentication is needed we set it to true
			if (useSmtpAuthentication) props.put("mail.smtp.auth", "true");
			else props.put("mail.smtp.auth", "false");

			// Set all recipients of any kind (TO, CC, BCC)
			message.setFrom(new InternetAddress(from));
			for (int i = 0; i < to.length; i++) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
			}
			for (int i = 0; i < cc.length; i++) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
			}
			for (int i = 0; i < bcc.length; i++) {
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
			}

			// Set subject, text and attachment
			message.setSubject(subject);
			textPart.setText(text);

			int i = 0;
			if (cb_attachXml.isSelected()) attachmentPart[i++].attachFile(diagramXml);
			if (cb_attachGif.isSelected()) attachmentPart[i++].attachFile(diagramGif);
			if (cb_attachPdf.isSelected()) attachmentPart[i++].attachFile(diagramPdf);

			/**
			 * Send message (if no authentication is used, we use the short variant to send a mail
			 */

			if (useSmtpAuthentication) {
				Transport transport = session.getTransport("smtp");
				try {
					transport.connect(smtpHost, smtpUser, smtpPW);
					transport.sendMessage(message, message.getAllRecipients());
				}
				finally {
					transport.close();
				}
			}
			else { // No SMTP Authentication
				Transport.send(message);
			}

			closePanel();
		}

		catch (Exception e) {
			log.error(null, e);
			if (e instanceof MessagingException) { // SMTP Error
				JOptionPane.showMessageDialog(this, "There has been an error with your smtp server." + Constants.NEWLINE + "Please recheck your smtp server and login data.", "SMTP Error", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
			}
			else { // Other Error
				JOptionPane.showMessageDialog(this, "There has been an error sending your mail." + Constants.NEWLINE + "Please recheck your input data.", "Sending Error", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
			}
		}
		finally {
			if (diagramXml != null) diagramXml.delete();
			if (diagramGif != null) diagramGif.delete();
			if (diagramPdf != null) diagramPdf.delete();
		}
	}

	/**
	 * Adds a component to this panel
	 * 
	 * @param gbl
	 *            The GridBagLayout of this component
	 * @param c
	 *            The Component to add
	 * @param x
	 *            The x value of grid where the component starts
	 * @param y
	 *            The y value of grid where the component starts
	 * @param width
	 *            How many spaces of the grid's width will be used by the component
	 * @param height
	 *            How many spaces of the grid's height will be used by the component
	 * @param fill
	 *            If the component's display area is larger than the component's requested size this param determines whether and how to resize the component
	 * @param weightx
	 *            Specifies how to distribute extra horizontal space.
	 * @param weighty
	 *            Specifies how to distribute extra vertical space.
	 * @param insets
	 *            Specifies the external padding of the component (= minimum amount of space between the component and the edges of its display area)
	 */
	private void addComponent(JPanel panel, GridBagLayout gbl, Component c, int x, int y, int width, int height, int fill, double weightx, double weighty, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		gbl.setConstraints(c, gbc);
		panel.add(c);
	}

	private String[] removeWhitespaceAndSplitAt(String inputString) {
		if (inputString.isEmpty()) return new String[] {};
		String returnString = "";
		for (int i = 0; i < inputString.length(); i++) {
			if (inputString.charAt(i) != ' ') returnString += inputString.charAt(i);
		}
		return returnString.split(",");
	}

	private void storeConstants() {
		Constants.mail_smtp = tf_smtp.getText();
		Constants.mail_smtp_auth = cb_smtp_auth.isSelected();
		Constants.mail_smtp_user = tf_smtpUser.getText();
		Constants.mail_smtp_pw_store = cb_pwSave.isSelected();
		if (cb_pwSave.isSelected()) Constants.mail_smtp_pw = String.valueOf(pf_smtpPW.getPassword());
		else Constants.mail_smtp_pw = "";
		Constants.mail_from = tf_from.getText();
		Constants.mail_to = tf_to.getText();
		Constants.mail_cc = tf_cc.getText();
		Constants.mail_bcc = tf_bcc.getText();
		Constants.mail_xml = cb_attachXml.isSelected();
		Constants.mail_gif = cb_attachGif.isSelected();
		Constants.mail_pdf = cb_attachPdf.isSelected();
	}

	private void readConstants() {
		tf_smtp.setText(Constants.mail_smtp);
		cb_smtp_auth.setSelected(Constants.mail_smtp_auth);
		tf_smtpUser.setText(Constants.mail_smtp_user);
		cb_pwSave.setSelected(Constants.mail_smtp_pw_store);
		pf_smtpPW.setText(Constants.mail_smtp_pw);
		tf_from.setText(Constants.mail_from);
		tf_to.setText(Constants.mail_to);
		tf_cc.setText(Constants.mail_cc);
		tf_bcc.setText(Constants.mail_bcc);
		cb_attachXml.setSelected(Constants.mail_xml);
		cb_attachGif.setSelected(Constants.mail_gif);
		cb_attachPdf.setSelected(Constants.mail_pdf);
	}

	private void setAllFonts() {

		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		Font fontBold = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		Font fontSmallItalic = new Font(Font.SANS_SERIF, Font.ITALIC, 10);

		lb_smtp.setFont(fontBold);
		tf_smtp.setFont(font);
		lb_smtpUser.setFont(fontBold);
		tf_smtpUser.setFont(font);
		lb_smtpPW.setFont(fontBold);
		pf_smtpPW.setFont(font);
		lb_from.setFont(fontBold);
		tf_from.setFont(font);
		lb_to.setFont(fontBold);
		tf_to.setFont(font);
		lb_cc.setFont(fontBold);
		tf_cc.setFont(font);
		lb_bcc.setFont(fontBold);
		tf_bcc.setFont(font);
		lb_subject.setFont(fontBold);
		tf_subject.setFont(font);
		ta_text.setFont(font);
		cb_attachXml.setFont(fontBold);
		cb_attachGif.setFont(fontBold);
		cb_attachPdf.setFont(fontBold);
		lnk_smtpInfo.setFont(fontSmallItalic);
		cb_smtp_auth.setFont(fontSmallItalic);
		cb_pwSave.setFont(fontSmallItalic);
	}

	public void closePanel() {
		storeConstants();
		Constants.mail_split_position = (int) this.getSize().getHeight();
		Main.getInstance().getGUI().setMailPanelEnabled(false);
	}

	private class SendActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sendMail();
		}
	}

	private class CancelActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			closePanel();
		}
	}

	private void checkVisibilityOfSmtpAuth() {
		boolean val = cb_smtp_auth.isSelected();
		lb_smtpUser.setVisible(val);
		tf_smtpUser.setVisible(val);
		lb_smtpPW.setVisible(val);
		pf_smtpPW.setVisible(val);
		cb_pwSave.setVisible(val);
		if (!val) {
			tf_smtpUser.setText("");
			pf_smtpPW.setText("");
			cb_pwSave.setSelected(false);
		}
		repaint();
	}

	private class AuthentificationActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			checkVisibilityOfSmtpAuth();
		}
	}

}
