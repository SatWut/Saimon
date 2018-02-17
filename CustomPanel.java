package Simon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CustomPanel extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;

	private Pad[] padds = new Pad[4];

	private Pad middleCircle;

	private int size;
	private int x = 40;
	private int y = 25;
	private int space = 50;

	private ArrayList<Integer> arcPattern;
	private int clickes = 0;
	private boolean isShowPattern;

	private int sleep = 300;

	private Synthesizer synth;
	private MidiChannel[] channel;

	public CustomPanel(int width, int height)
	{
		setBackground(Color.WHITE);
		
		size = (width - space -  x * 2);

		padds[0] = new Pad(x + space, y, size, size, 0, 90, Arc2D.PIE, Color.RED, 60);
		padds[1] = new Pad(x, y, size, size, 90, 90, Arc2D.PIE, Color.GREEN, 61);
		padds[2] = new Pad(x, y + space, size, size, 180, 90, Arc2D.PIE, Color.YELLOW, 62);
		padds[3] = new Pad(x + space, y + space, size, size, 270, 90, Arc2D.PIE, Color.BLUE, 63);

		int middleCircleSize = size / 2;
		int middleCircleX = (int)padds[1].getCenterX() + space / 2 - middleCircleSize / 2;
		int middleCircleY = (int)padds[1].getCenterX() + space / 2 - middleCircleSize / 2;

		middleCircle = new Pad(middleCircleX, middleCircleY, middleCircleSize, middleCircleSize, 0, 360, Arc2D.PIE,
				getBackground(), 10);

		arcPattern = new ArrayList<Integer>();
		arcPattern.add((int) (Math.random() * 4));

		addMouseListener(this);

		try
		{
			synth = MidiSystem.getSynthesizer();
			synth.open();
			channel = synth.getChannels();
			channel[0].programChange(116);
		} catch (MidiUnavailableException e)
		{
			e.printStackTrace();
		}

		isShowPattern = true;
		showPattern();
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		for (Pad ca : padds)
			ca.fillArc(g2);

		middleCircle.fillArc(g2);
	}

	public void mouseClicked(MouseEvent e)
	{
		if (!middleCircle.contains(e.getX(), e.getY()) && !isShowPattern)
			for (Pad ca : padds)
				if (ca.contains(e.getX(), e.getY()))
				{
					changeColor(ca);
					if (arcPattern.get(clickes) == ca.getID())
						clickes++;
					else
					{
						String massage = String.format("Wrong pattern.\nYou did %d rounds.\nTry again.", arcPattern.size() - 1);
						int selection = JOptionPane.showConfirmDialog(this, massage, "Wrong pattern",
								JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
						
						if (selection == 0)
							restart();
						else
							System.exit(0);
					}

					if (clickes == arcPattern.size())
					{
						arcPattern.add((int) (Math.random() * 4));
						showPattern();
						clickes = 0;
						isShowPattern = true;
					}
				}
	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{

	}

	public void mouseReleased(MouseEvent e)
	{

	}
	
	private void changeColor(Pad a)
	{
		a.setIsDarker(true);
		a.sound(channel[0]);
		repaint();

		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(sleep);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				a.setIsDarker(false);
				repaint();
			}
		}).start();
	}

	private void showPattern()
	{
		new Thread(new Runnable()
		{

			public void run()
			{
				try
				{
					Thread.sleep((sleep * 2) / 3);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				for (Integer id : arcPattern)
				{
					try
					{
						Thread.sleep(sleep * 2);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					changeColor(padds[id]);
				}

				try
				{
					Thread.sleep(sleep * 2);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				isShowPattern = false;
			}
		}).start();
	}

	private void restart()
	{
		clickes = 0;
		arcPattern.clear();
		arcPattern.add((int) (Math.random() * 4));

		isShowPattern = true;
		showPattern();
	}
}
