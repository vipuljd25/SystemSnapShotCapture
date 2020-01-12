package com.snap.common.util;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class JavaMail {
	private static final Logger logger = Logger.getLogger(JavaMail.class.getName());
	public static void sendMail(List<String> files, String folderName, String toAddress, String path) throws Exception {
		logger.debug("inside sendMail");
		String host = "smtp.gmail.com";
		String Password = "9406233224kar";
		String from = "kartikeyarajvaidya1786@gmail.com";
		// Get system properties
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", Password);
		props.put("mail.smtp.port", 587);
		props.put("mail.smtps.auth", true);

		Session session = Session.getInstance(props, null);

		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(from));

		InternetAddress to1 = new InternetAddress();
		to1 = new InternetAddress(toAddress);
		message.setRecipient(Message.RecipientType.TO, to1);
		message.setSubject("JavaMail Attachment");
		
		Multipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText("Here's the file");
		DataSource source1 = new FileDataSource(path);
		messageBodyPart.setDataHandler(new DataHandler(source1));
		messageBodyPart.setFileName(folderName);
		multipart.addBodyPart(messageBodyPart);
		
		for (String filePath : files) {
			BodyPart messageBodyPart1 = new MimeBodyPart();
			DataSource source = new FileDataSource(filePath);
			File file = new File(filePath);
			String fileName = file.getName();
			messageBodyPart1.setDataHandler(new DataHandler(source));
			messageBodyPart1.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart1);
		}

		message.setContent(multipart);

		try {
			Transport tr = session.getTransport("smtps");
			tr.connect(host, from, Password);
			tr.sendMessage(message, message.getAllRecipients());
			System.out.println("Mail Sent Successfully");
			tr.close();

		} catch (SendFailedException sfe) {

			logger.error("Exception occured..", sfe);
		}
	}
}
