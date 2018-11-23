package codex.orbit.entity;

import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Sprite extends Entity
{
	private int displaylist;
	private boolean hasTexture;
	
	private float widthR, heightR;
	
	protected float rotation;
	
	public Sprite(float w, float h) {
		this(0, 0, w, h);
	}
	public Sprite(float x, float y, float wr, float hr) {
		this(x, y, wr, hr, null);
	}
	public Sprite(float x, float y, float wr, float hr, Texture tex) {
		this(x, y, wr, hr, tex, 0);
	}
	public Sprite(float x, float y, float wr, float hr, Texture tex, float r) {
		super(x, y);
		widthR = wr;
		heightR = hr;
		rotation = r;
		setTexture(tex);
	}
	
	private void createDisplaylist(Texture tex) {
		if (tex == null)
			return;
		displaylist = glGenLists(1);
		glNewList(displaylist, GL_COMPILE);
		{
			glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0, 1);
				glVertex2f(-1, -1);
				
				glTexCoord2f(1, 1);
				glVertex2f(1, -1);
				
				glTexCoord2f(1, 0);
				glVertex2f(1, 1);
				
				glTexCoord2f(0, 0);
				glVertex2f(-1, 1);
			}
			glEnd();
		}
		glEndList();
	}
	
	public void update(float delta) {
	}
	public void draw() {
		draw(1, 1, 1);
	}
	public void draw(float r, float g, float b) {
		draw(r, g, b, 1);
	}
	public void draw(float r, float g, float b, float a) {
		if (!hasTexture)
			return;
		glPushMatrix();
		glTranslatef(x, y, 0);
		glScalef(widthR, heightR, 1);
		glRotatef(rotation, 0, 0, 1);
		glColor4f(r, g, b, a);
		glCallList(displaylist);
		glPopMatrix();
	}
	
	public void setTexture(Texture tex) {
		if (hasTexture)
			glDeleteLists(displaylist, 1);
		if (tex == null) {
			hasTexture = false;
		} else {
			hasTexture = true;
			createDisplaylist(tex);
		}
	}
	
	public float getWidthRadius() {
		return widthR;
	}
	public void setWidthRadius(float wr) {
		widthR = wr;
	}
	
	public float getHeightRadius() {
		return heightR;
	}
	public void setHeightRadius(float hr) {
		heightR = hr;
	}
	
	public float getRotation() {
		return rotation;
	}
	public void setRotation(float r) {
		rotation = r % 360;
	}
}
