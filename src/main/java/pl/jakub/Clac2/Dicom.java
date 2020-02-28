package pl.jakub.Clac2;

import java.awt.Image;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;

public class Dicom {
	

	static BufferedImage createBufferedImgdFromDICOMfile(File dicomFile) {
		Raster raster = null;
		System.out.println("Input: " + dicomFile.getName());

		// Open the DICOM file and get its pixel data
		try {
			Iterator iter = ImageIO.getImageReadersByFormatName("DICOM");
			ImageReader reader = (ImageReader) iter.next();
			DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
			ImageInputStream iis = ImageIO.createImageInputStream(dicomFile);
			reader.setInput(iis, false);
			// Returns a new Raster (rectangular array of pixels) containing the raw pixel
			// data from the image stream

			raster = reader.readRaster(0, param);
			if (raster == null)
				System.out.println("Error: couldn't read Dicom image!");
			iis.close();
		} catch (Exception e) {
			System.out.println("Error: couldn't read dicom image! " + e.getMessage());
			e.printStackTrace();
		}
		return get16bitBuffImage(raster);
	}

	private static BufferedImage get16bitBuffImage(Raster raster) {
		short[] pixels = ((DataBufferUShort) raster.getDataBuffer()).getData();
		ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 16 },
				false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);
		DataBufferUShort db = new DataBufferUShort(pixels, pixels.length);
		WritableRaster outRaster = Raster.createInterleavedRaster(db, raster.getWidth(), raster.getHeight(),
				raster.getWidth(), 1, new int[1], null);
		return new BufferedImage(colorModel, outRaster, false, null);
	}

	public static ArrayList<Integer> getPixels(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
//		int rgb;
//		int r;
//		int g;
//		int b;
//		int gray;
		ArrayList<Integer> result = new ArrayList();

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
//				rgb = image.getRGB(col, row);
//				r = (rgb >> 16) & 0xFF;
//				g = (rgb >> 8) & 0xFF;
//				b = (rgb & 0xFF);
//				gray = (r + g + b) / 3;
//				result.add(gray);
				result.add(image.getRGB(col, row));
			}
		}

		return result;

	}
	
	public static File dcmToJpeg(File dcmFile) {
		BufferedImage myJpegImage = null;
		File jpgImg = new File("C:\\Users\\Acer\\Documents\\EclipseProjects\\Clac2\\Jpeg\\test.jpg");
		
		Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("DICOM");
		while(iterator.hasNext()) {
			
			ImageReader imagerReader = (ImageReader) iterator.next();
			DicomImageReadParam dicomImageReadParam = (DicomImageReadParam) imagerReader.getDefaultReadParam();
			try {
				ImageInputStream iis = ImageIO.createImageInputStream(dcmFile);
				imagerReader.setInput(iis,false);
				myJpegImage = imagerReader.read(0, dicomImageReadParam);
				iis.close();
				if(myJpegImage == null) {
					System.out.println("Couldnt read image");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(jpgImg));
			ImageIO.write(myJpegImage, "jpeg", outputStream);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Complete");
		return jpgImg;
	}
	
	static public Image getImageFromArray(ArrayList <Integer> list, int width, int height) {
		int[] ret = new int[list.size()];
		for(int i = 0 ; i < ret.length ; i++) {
			ret[i] = list.get(i).intValue();
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = (WritableRaster) image.getData();
		raster.setPixels(0, 0, width, height, ret);
		
		
		try {
			File file = new File("C:\\Users\\Acer\\Documents\\EclipseProjects\\Clac2\\Jpeg\\reCreated.jpg");
			ImageIO.write(image, "jpg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Writenn sucesfully");
		return image;
	}
}
