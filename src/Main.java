import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

	// Returns the 3 different color channels of the input image in an ArrayList
	public static ArrayList<BufferedImage> subBands(BufferedImage img) {

		BufferedImage red = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		BufferedImage green = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		BufferedImage blue = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int pixel = img.getRGB(x, y);
				int redP = (pixel >> 16) & 0xFF;
				int greenP = (pixel >> 8) & 0xFF;
				int blueP = (pixel & 0xFF);

				red.setRGB(x, y, redP);
				green.setRGB(x, y, greenP);
				blue.setRGB(x, y, blueP);
			}
		}

		ArrayList<BufferedImage> result = new ArrayList<BufferedImage>();
		result.add(red);
		result.add(green);
		result.add(blue);
		return result;
	}

	// Applies the average (median) filter on the passed image, and returns a
	// new image (does not mutate the original input)
	public static BufferedImage averageFilter(BufferedImage img) throws IOException {
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

				int sum = 0;
				if (x - 1 > 0 && y - 1 > 0)
					sum += img.getRGB(x - 1, y - 1);
				if (x - 1 > 0)
					sum += img.getRGB(x - 1, y);
				if (x - 1 > 0 && y + 1 < img.getHeight())
					sum += img.getRGB(x - 1, y + 1);
				if (y - 1 > 0)
					sum += img.getRGB(x, y - 1);
				if (true)
					sum += img.getRGB(x, y);
				if (y + 1 < img.getHeight())
					sum += img.getRGB(x, y + 1);
				if (x + 1 < img.getWidth() && y - 1 > 0)
					sum += img.getRGB(x + 1, y - 1);
				if (x + 1 < img.getWidth())
					sum += img.getRGB(x + 1, y);
				if (x + 1 < img.getWidth() && y + 1 < img.getHeight())
					sum += img.getRGB(x + 1, y + 1);

				result.setRGB(x, y, sum / 9);
			}
		}
		return result;
	}

		// Applies the sharpening filter on a passed image, and returns a new image
	// (does not mutate the original image)
	public static BufferedImage sharpenFilter(BufferedImage img) throws IOException {

		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

				int sum = 0;
				if (x - 1 > 0 && y - 1 > 0)
					sum -= (img.getRGB(x - 1, y - 1) & 0x000000FF);
				if (x - 1 > 0)
					sum -= (img.getRGB(x - 1, y) & 0x000000FF);
				if (x - 1 > 0 && y + 1 < img.getHeight())
					sum -= (img.getRGB(x - 1, y + 1) & 0x000000FF);
				if (y - 1 > 0)
					sum -= (img.getRGB(x, y - 1) & 0x000000FF);
				if (true)
					sum += (9 * (img.getRGB(x, y) & 0x000000FF));
				if (y + 1 < img.getHeight())
					sum -= (img.getRGB(x, y + 1) & 0x000000FF);
				if (x + 1 < img.getWidth() && y - 1 > 0)
					sum -= (img.getRGB(x + 1, y - 1) & 0x000000FF);
				if (x + 1 < img.getWidth())
					sum -= (img.getRGB(x + 1, y) & 0x000000FF);
				if (x + 1 < img.getWidth() && y + 1 < img.getHeight())
					sum -= (img.getRGB(x + 1, y + 1) & 0x000000FF);

				if (sum > 255)
					sum = 255;
				else if (sum < 0)
					sum = 0;

				result.setRGB(x, y, sum);
			}
		}

		return result;
	}

	// The "int c" is an index that is hard-coded for the 3 different gamma
	// functions in the assignment
	public static BufferedImage gamma(BufferedImage img, int c) {

		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int pixel = img.getRGB(x, y);
				pixel = pixel & 0x000000FF;

				int newPixel;
				switch (c) {

				case 0:
					newPixel = pixel + 50;
					break;
				case 1:
					newPixel = pixel * pixel;
					break;
				case 2:
					newPixel = (int) Math.sqrt((double) pixel);
					break;
				default:
					newPixel = 0;
					break;
				}
				// Handling overflow
				if (newPixel > 255)
					newPixel = 255;
				else if (newPixel < 0)
					newPixel = 0;
				res.setRGB(x, y, newPixel);
			}
		}

		return res;
	}

	// Displays a given image in a JPanel with the title passed as a string
	// argument
	public static void showImage(String title, BufferedImage img) {
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(img.getWidth(), img.getHeight());
		JLabel label = new JLabel(title);
		frame.add(label);
		frame.getContentPane().add(new JLabel(new ImageIcon(img)));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws IOException {
		String fileName = "bin/input.png";
		// Loading the original image into 3 different color channels
		ArrayList<BufferedImage> list = subBands(ImageIO.read(new File(fileName)));
		// Displaying the original image
		showImage("Original", ImageIO.read(new File(fileName)));

		// EX1, applying the average filter separately on each sub-band.
		BufferedImage red = averageFilter(list.get(0));
		BufferedImage green = averageFilter(list.get(1));
		BufferedImage blue = averageFilter(list.get(2));

		BufferedImage result = new BufferedImage(red.getWidth(), red.getHeight(), red.getType());
		// Combining the different color channels into one image (the same exact
		// procedure is done for the rest of the assignment)
		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				int newRGB = 0xFF000000 | (red.getRGB(x, y) << 16) | (green.getRGB(x, y) << 8) | blue.getRGB(x, y);
				result.setRGB(x, y, newRGB);
			}
		}
		showImage("Average", result);

		/* -------------------------------------------------- */
		// Applying the uniform sharpening filter on each color sub-band and
		// then
		// combining them altogether, and displaying the end result
		BufferedImage result2 = new BufferedImage(red.getWidth(), red.getHeight(), red.getType());
		red = sharpenFilter(list.get(0));
		green = sharpenFilter(list.get(1));
		blue = sharpenFilter(list.get(2));

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {

				int newRGB = 0xFF000000 | (red.getRGB(x, y) << 16) | (green.getRGB(x, y) << 8) | blue.getRGB(x, y);
				result2.setRGB(x, y, newRGB);
			}
		}
		showImage("Sharpened", result2);
	}
}
