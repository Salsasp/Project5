import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

public class Page1 {
	private JPanel panel;
	private int previousKey = 0;
	private Random generator = new Random();
	
	public static void main (String[] args) throws FileNotFoundException
	{
		JFrame frame = new JFrame();
		JLayeredPane layered = new JLayeredPane();
		frame.setContentPane(layered);
		Page1 page = new Page1();
		JPanel panel = page.panel;
		MyComponent chart = page.new MyComponent();
		layered.add(chart);
		chart.setBounds(150, 100, 750, 750);
		layered.add(panel);
		panel.setBounds(250, 10, 500, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,1000);

	}

	public Page1() throws FileNotFoundException
	{
		Blocks.readFile("ethereumP1data.txt");
		initializeSliders();
		
	}
	public void initializeSliders()
	{
		panel = new JPanel();
		//initializing first slider
		JSlider s1 = new JSlider(Blocks.getBlocks().get(0).getNumber(), 
				Blocks.getBlocks().get(Blocks.getBlocks().size()-1).getNumber());
        s1.setValue(Blocks.getBlocks().get(0).getNumber());
		s1.setMajorTickSpacing(25);
		s1.setMinorTickSpacing(5);
		s1.setPaintTicks(true);
		
		//adding labels to first slider
		Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(Blocks.getBlocks().get(0).getNumber(), new JLabel(Integer.toString(Blocks.getBlocks().get(0).getNumber())));
        labels.put(Blocks.getBlocks().get(99).getNumber(), new JLabel(Integer.toString(Blocks.getBlocks().get(99).getNumber())));
		s1.setLabelTable(labels);
		s1.setPaintLabels(true);
        
		//initializing second slider
		JSlider s2 = new JSlider(Blocks.getBlocks().get(0).getNumber(), 
				Blocks.getBlocks().get(Blocks.getBlocks().size()-1).getNumber());
		s2.setValue(Blocks.getBlocks().get(0).getNumber());
		s2.setMajorTickSpacing(25);
		s2.setMinorTickSpacing(5);
		s2.setPaintTicks(true);
		
		//adding labels to second slider
		Hashtable<Integer, JLabel> s2labels = new Hashtable<>();
		s2labels.put(Blocks.getBlocks().get(0).getNumber(), new JLabel(Integer.toString(Blocks.getBlocks().get(0).getNumber())));
		s2labels.put(Blocks.getBlocks().get(99).getNumber(), new JLabel(Integer.toString(Blocks.getBlocks().get(99).getNumber())));
		s2.setLabelTable(s2labels);
		s2.setPaintLabels(true);
		
		//listener to dynamically change minimum value of 2nd slider to current value of 1st slider
		s1.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		        int value = s1.getValue();
		        s2.setMinimum(value);
		        s2.setValue(value);
		        s2labels.remove(previousKey);
		        s2labels.put(value, new JLabel(Integer.toString(value)));
		        s2.setLabelTable(s2labels);
		        if(value != Blocks.getBlocks().get(99).getNumber())previousKey = value;
		      }
		    });
		panel.add(s1);
		panel.add(s2);
	}
	
	
	
	
	
	
 class MyComponent extends JComponent
{
	int red = generator.nextInt(255); 
	int green = generator.nextInt(255);
	int blue = generator.nextInt(255);
	ArrayList<Color> colors = new ArrayList<Color>();
	 
	MyComponent() {}
	
	public void paint(Graphics g) 
	{
	      initializePieChart((Graphics2D) g, new Rectangle(750,750));
	}
	
	public void initializePieChart(Graphics2D g, Rectangle area)
	{
		Slice[] slices = getSlices();
		double total = 0.0D;
		for (int i = 0; i < slices.length; i++)
		{
			total += slices[i].value;
		}
		double curValue = 0.0D;
		int startAngle = 0;
		for (int i = 0; i < slices.length; i++)
		{
			startAngle = (int) (curValue * 360 / total);
			int arcAngle = (int) (slices[i].value * 360 / total);
			g.setColor(slices[i].color);
			g.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);
			curValue += slices[i].value;
		}
		
	}
	public Slice[] getSlices()
	{
		randomizeColors();
		HashMap<Blocks, Integer> miners = Blocks.calUniqMiners();
		HashMap<String, Color> minerColor = new HashMap<String, Color>();
		Slice[] slices = new Slice[miners.size()];
		int counter = 0;
		for(Map.Entry<Blocks, Integer> entry : miners.entrySet())
		{
			Color randomColor = colors.get(counter);
			slices[counter] = new Slice(entry.getValue(), randomColor);
			minerColor.put(entry.getKey().getMiner(), randomColor);
			counter++;
		}
		return slices;
	}
	
	public void randomizeColors()
	{
		for(int i = 0; i < 20; i++) 
		{
			red = generator.nextInt(255); 
			green = generator.nextInt(255);
			blue = generator.nextInt(255);
			colors.add(new Color(red, green, blue));
		}
	}
}
	

 
	
 
	public class Slice
	{
		int value;
		Color color;
		public Slice(int value, Color color) {  
			this.value = value;
			this.color = color;
		   }
	}
}