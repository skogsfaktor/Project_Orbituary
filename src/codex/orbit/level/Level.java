package codex.orbit.level;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import codex.orbit.resources.Textures;

public class Level
{
	private final float widthR, heightR;
	private int roomList;
	private int backgroundList;
	
	private double angle;
	
	public Level(float wr, float hr) {
		widthR = wr;
		heightR = hr;
		
		generate();
	}
	
	private void generate() {
		final Texture wall, floor, background, galaxy;
		try {
			wall = Textures.loadTexture("wall", "environment/wall.png");
			floor = Textures.loadTexture("floor", "environment/floor.png");
			background = Textures.loadTexture("background", "environment/background.png");
			galaxy = Textures.loadTexture("galaxy", "environment/backgrounds/galaxy_" + (new Random().nextInt(4)+1) + ".jpg");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Vector3f[][][] colors = new Vector3f[2][2][2];
		for (int z = 0; z < 2; z++)
			for (int y = 0; y < 2; y++)
				for (int x = 0; x < 2; x++)
					colors[x][y][z] = newColor();
					
		int depth = -1;
		roomList = glGenLists(1);
		glNewList(roomList, GL_COMPILE);
		{
			float darkness = 0.5f;
			Vector3f color;
			/**
			 *  LEFT
			 *  &
			 *  RIGHT WALL
			 */
			{
				int repeat = 1;
				glBindTexture(GL_TEXTURE_2D, wall.getTextureID());
				glBegin(GL_QUADS);
				{
//					System.out.println(widthR + ", " + heightR);
					color = colors[0][1][0];
					glColor3f(color.x, color.y, color.z);
					glTexCoord2f(0, 0);
					glVertex3f(-widthR, heightR, 0);
					
					color = colors[0][0][0];
					glColor3f(color.x, color.y, color.z);
					glTexCoord2f(0, repeat);
					glVertex3f(-widthR, -heightR, 0);
					
					color = colors[0][0][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(repeat, repeat);
					glVertex3f(-widthR, -heightR, depth);
					
					color = colors[0][1][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(repeat, 0);
					glVertex3f(-widthR, heightR, depth);
				}
				glEnd();
				
				glBegin(GL_QUADS);
				{
					color = colors[1][0][0];
					glColor3f(color.x, color.y, color.z);
					glTexCoord2f(0, repeat);
					glVertex3f(widthR, -heightR, 0);
					
					color = colors[1][1][0];
					glColor3f(color.x, color.y, color.z);
					glTexCoord2f(0, 0);
					glVertex3f(widthR, heightR, 0);
					
					color = colors[1][1][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(repeat, 0);
					glVertex3f(widthR, heightR, depth);
					
					color = colors[1][0][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(repeat, repeat);
					glVertex3f(widthR, -heightR, depth);
				}
				glEnd();
			}
			
			/**
			 *  FLOOR
			 *  &
			 *  ROOF
			 */
			{
				int repeat = 1;
				glBindTexture(GL_TEXTURE_2D, floor.getTextureID());
				glBegin(GL_QUADS);
				{
					color = colors[0][0][0];
					glColor3f(color.x, color.y, color.z);
					glTexCoord2f(0, repeat);
					glVertex3f(-widthR, -heightR, 0);
					
					color = colors[1][0][0];
					glColor3f(color.x, color.y, color.z);
					glTexCoord2f(repeat, repeat);
					glVertex3f(widthR, -heightR, 0);
					
					color = colors[1][0][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(repeat, 0);
					glVertex3f(widthR, -heightR, depth);
					
					color = colors[0][0][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(0, 0);
					glVertex3f(-widthR, -heightR, depth);
				}
				glEnd();
				
				glBegin(GL_QUADS);
				{
					color = colors[1][1][0];
					glColor3f(color.x, color.y, color.z);
					glTexCoord2f(repeat, repeat);
					glVertex3f(widthR, heightR, 0);
					
					color = colors[0][1][0];
					glColor3f(color.x, color.y, color.z);
					glTexCoord2f(0, repeat);
					glVertex3f(-widthR, heightR, 0);
					
					color = colors[0][1][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(0, 0);
					glVertex3f(-widthR, heightR, depth);
					
					color = colors[1][1][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(repeat, 0);
					glVertex3f(widthR, heightR, depth);
				}
				glEnd();
			}
			
			/**
			 *  END WALL
			 */
			{
				glBindTexture(GL_TEXTURE_2D, background.getTextureID());
				glBegin(GL_QUADS);
				{
					color = colors[0][1][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(0, 0);
					glVertex3f(-widthR, heightR, depth);
					
					color = colors[0][0][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(0, 1);
					glVertex3f(-widthR, -heightR, depth);
					
					color = colors[1][0][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(1, 1);
					glVertex3f(widthR, -heightR, depth);
					
					color = colors[1][1][1];
					glColor3f(color.x * darkness, color.y * darkness, color.z * darkness);
					glTexCoord2f(1, 0);
					glVertex3f(widthR, heightR, depth);
				}
				glEnd();
			}
		}
		glEndList();
		
		backgroundList = glGenLists(1);
		glNewList(backgroundList, GL_COMPILE);
		{
			/**
			 *  BACKGROUND
			 */
			glColor3f(1, 1, 1);
			glBindTexture(GL_TEXTURE_2D, galaxy.getTextureID());
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0, 0);
				glVertex3f(-widthR, heightR, depth);
				
				glTexCoord2f(0, 1);
				glVertex3f(-widthR, -heightR, depth);
				
				glTexCoord2f(1, 1);
				glVertex3f(widthR, -heightR, depth);
				
				glTexCoord2f(1, 0);
				glVertex3f(widthR, heightR, depth);
			}
			glEnd();
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		glEndList();
	}
	private Vector3f newColor() {
		Random rnd = new Random();
		
		float h = rnd.nextFloat();
		float s = rnd.nextFloat() * 0.2f + 0.8f;
		float b = rnd.nextFloat() * 0.4f + 0.6f;
		b = s = 1;
		
		Color col = Color.getHSBColor(h, s, b);
		return new Vector3f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f);
	}
	public void finalize() throws Throwable {
		glDeleteLists(roomList, 1);
		glDeleteLists(backgroundList, 1);
		super.finalize();
	}
	
	public void draw() {
		glPushMatrix();
		{
			double rotRadius = 0.2;
			double x = Math.cos(Math.toRadians(angle)) * (widthR / 2.0) * rotRadius;
			double y = Math.sin(Math.toRadians(angle)) * (heightR / 2.0) * rotRadius;
			angle += 0.25;
			glScaled(1 + rotRadius, 1 + rotRadius, 1);
			glTranslated(x, y, 0);
			glCallList(backgroundList);
		}
		glPopMatrix();
		glCallList(roomList);
	}
}
