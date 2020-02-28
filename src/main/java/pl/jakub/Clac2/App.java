package pl.jakub.Clac2;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		BasicConfigurator.configure();
		System.loadLibrary("clib_jiio");
		System.loadLibrary("clib_jiio_util");
		File file = new File("C:\\Users\\Acer\\Documents\\EclipseProjects\\Clac2\\DCM\\image-000190.dcm");
		File jpg = Dicom.dcmToJpeg(file);
		
		
		BufferedImage image = Dicom.createBufferedImgdFromDICOMfile(file);

		ArrayList <Integer> pixels = Dicom.getPixels(image);
		for(Integer element : pixels) {
			System.out.println(element);
		}
		Dicom.getImageFromArray(pixels, image.getHeight(), image.getWidth());
		pixels.sort(null);
		System.out.println(pixels.toString());
		System.out.println("Number of pixels: " + pixels.size());
		System.out.println("Complete");
	}
}
