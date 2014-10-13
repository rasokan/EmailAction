package com.learn.mail.EmailAction;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSFileSource implements DataSource {

	private Path filePath;
	private FileSystem fileSystem;

	public HDFSFileSource(String filePath, Configuration conf,
			FileSystem fileSystem) {
		try {
			this.fileSystem = FileSystem.get(conf);
			this.filePath = new Path(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return "application/octet-stream";
	}

	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub

		byte[] byteArray = null;

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(fileSystem.open(filePath)));

		StringBuffer stringReader = new StringBuffer();

		String tempValue = null;

		while ((tempValue = bufferedReader.readLine()) != null) {

			stringReader = stringReader.append(tempValue);

		}

		byteArray = stringReader.toString().getBytes();

		return new ByteArrayInputStream(byteArray);

	}

	public String getName() {
		// TODO Auto-generated method stub
		return "Audit.txt";
	}

	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		// return fileSystem.create(filePath);
		return null;

	}

}
