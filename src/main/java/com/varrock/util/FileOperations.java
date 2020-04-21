package com.varrock.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileOperations {

	public FileOperations() {
	}

	public static final byte[] readFile(File file) {
		try {
			int i = (int) file.length();
			byte abyte0[] = new byte[i];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			totalRead++;
			return abyte0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int totalRead = 0;
}