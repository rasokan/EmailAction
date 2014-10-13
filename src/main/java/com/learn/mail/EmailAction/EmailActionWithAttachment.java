package com.learn.mail.EmailAction;

import static java.lang.System.err;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class EmailActionWithAttachment {

	public static void main(String args[]) {

		final Configuration conf = new Configuration();

		// final String attachedFilePath =
		// "hdfs://localhost:54310/home/ashok/Desktop/hadoop/data/file/u.item";

		final String attachedFilePath = "hdfs://localhost:54310/home/ashok/Desktop/hadoop/data/file/u.item";

		FileSystem fileSystem;
		try {

			conf.addResource(new Path("/opt/hadoop-1.2.1/conf/core-site.xml"));
			conf.addResource(new Path("/opt/hadoop-1.2.1/conf/hdfs-site.xml"));

			fileSystem = FileSystem.get(conf);

			// final Path attachedFileFSPath = new Path(attachedFilePath);

			// conf.set("fs.defaultFS",
			// "hdfs://localhost:54310/home/ashok/Desktop/hadoop/data/file");

			sendMailWithAttachment(attachedFilePath, conf, fileSystem);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean sendMailWithAttachment(String attachedFilePath,
			Configuration conf, FileSystem fileSystem) {

		String host = "smtp.gmail.com";
		final Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		String to = "rasokantest@gmail.com";
		String from = "rasokantest@gmail.com";
		final String username = "rasokantest@gmail.com";// change accordingly
		final String password = "XXXXXXXXX";// change accordingly

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject("Mail sent with Attachment");
			message.setText("Mail sent with Attachment");

			BodyPart messageBodyPart = new MimeBodyPart();

			Multipart multipart = new MimeMultipart();

			// multipart.addBodyPart(messageBodyPart);

			// DataSource source = new HDFSFileSource(attachedFilePath, conf,
			// fileSystem);

			// messageBodyPart.setDataHandler(new DataHandler(source));

			messageBodyPart.setFileName("Audit");

			String auditContent = getInputStream(attachedFilePath, conf,
					fileSystem);

			messageBodyPart.setContent(auditContent, "text/plain");

			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			Transport.send(message);

			System.out.println("Sent Successfuly!!!");

		} catch (final Exception e) {
			err.println("ERROR in Sending Mail : " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static String getInputStream(String attachedFilePath,
			Configuration conf, FileSystem fileSystem) throws IOException {
		// TODO Auto-generated method stub

		Path filePath = new Path(attachedFilePath);
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(fileSystem.open(filePath)));

		StringBuffer stringReader = new StringBuffer();

		String tempValue = null;

		while ((tempValue = bufferedReader.readLine()) != null) {

			stringReader = stringReader.append(tempValue).append("\n");
			

		}

		System.out.println(stringReader.toString());

		bufferedReader.close();

		return stringReader.toString();

	}

}
