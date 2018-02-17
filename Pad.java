package Simon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

import javax.sound.midi.MidiChannel;

public class Pad extends Arc2D.Double
{
	private static final long serialVersionUID = 1L;
	
	private static int numObjects = 0;
	private final int id = numObjects;
	
	private Color c;
	private boolean isDarker = false;
	
	int noteNumber;
	
	public Pad(int x, int y, int w, int h, int startA, int extentA, int type, Color c, int noteNumber)
	{
		super(x, y, w, h, startA, extentA, type);
		this.c = c;
		
		numObjects++;
		
		this.noteNumber = noteNumber;
	}
	
	public void fillArc(Graphics2D g2)
	{
		if(isDarker)
			g2.setColor(c.darker());
		else
			g2.setColor(c);
		
		g2.fill(this);
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setIsDarker(boolean b)
	{
		isDarker = b;
	}
	
	public void sound(MidiChannel mc)
	{
		mc.noteOn(noteNumber, 1000);
	}

}
