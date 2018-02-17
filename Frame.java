package Simon;

import javax.swing.JFrame;

public class Frame extends JFrame
{
	private static final long serialVersionUID = 1L;

	private CustomPanel p;

	private int barHeight;

	public Frame()
	{
		super("Simon");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		barHeight = getHeight();
		
		setSize(getToolkit().getScreenSize().height, getToolkit().getScreenSize().height);

		p = new CustomPanel(getWidth(), getHeight() - barHeight);
		setContentPane(p);

		setVisible(true);
	}
	
}
