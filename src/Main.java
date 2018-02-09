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
		// Displaying the original image
		showImage("Original", ImageIO.read(new File(fileName)));
	}
}
