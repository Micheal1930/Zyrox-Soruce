package com.zyrox.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.zyrox.util.RandomUtility;

public class CaptchaTest {

	public static void main(String[] args) throws IOException {

		Cage cage = new GCage();
		String text = RandomUtility.randomString(4);
		BufferedImage image = cage.drawImage(text);
		
		ImageIO.write(image, "jpg", new File("test.jpg"));
		
	}
	
}