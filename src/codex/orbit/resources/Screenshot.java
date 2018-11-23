package codex.orbit.resources;

import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

public class Screenshot extends Thread {
	private ByteBuffer data;
	private int width, height;
	
	public Screenshot() {
		this.width = Display.getWidth();
		this.height = Display.getHeight();
		data = BufferUtils.createByteBuffer(width * height * 4);
		glReadBuffer(GL_FRONT);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, data);
		start();
	}
	
	public void run() {
		Calendar cal = Calendar.getInstance();
		String year = "" + cal.get(Calendar.YEAR);
		String month = "" + cal.get(Calendar.MONTH);
		if (month.length() < 2)
			month = "0" + month;
		String day = "" + cal.get(Calendar.DAY_OF_MONTH);
		if (day.length() < 2)
			day = "0" + day;
		String hour = "" + cal.get(Calendar.HOUR_OF_DAY);
		if (hour.length() < 2)
			hour = "0" + hour;
		String minute = "" + cal.get(Calendar.MINUTE);
		if (minute.length() < 2)
			minute = "0" + minute;
		String second = "" + cal.get(Calendar.SECOND);
		if (second.length() < 2)
			second = "0" + second;
		File f = new File("screenshots/screenshot_" + year + "-" + month + "-" + day + "_" + hour + "." + minute + "." + second + ".png");
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int r = data.get() & 0xFF;
				int g = data.get() & 0xFF;
				int b = data.get() & 0xFF;
				int a = data.get() & 0xFF;
				
				int argb = (a << 24) | (r << 16) | (g << 8) | b;
				pixels[x + (height - y - 1) * width] = argb;
			}
		}
		try {
			f.createNewFile();
			ImageIO.write(img, "PNG", f);
			System.out.println("Image: " + f.getName() + " saved!");
		} catch (IOException ex) {
			System.err.println("Could not save image!");
			ex.printStackTrace();
		}
	}
}
