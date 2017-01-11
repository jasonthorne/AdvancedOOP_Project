package ie.gmit.sw;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
public class CustomControl extends JComponent implements MouseMotionListener, ActionListener{
	private static final long serialVersionUID = 777L;
	private static final int MAX_POINTS = 50;
	private Deque<Point> points = new LinkedList<Point>();
	

	public CustomControl(Dimension size) {
		super();
		setSize(size.width, size.height);
		enableInputMethods(true);   
		addMouseMotionListener(this);
		setFocusable(true);
	}

	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
	        
        for (Point p : points){
        	g.setColor(new Color((int)(Math.random() * 0x1000000)));
        	g.fillOval(p.getX(), p.getY(), Point.POINT_WIDTH, Point.POINT_HEIGHT);
        }
        repaint();
	}

	
	public void mouseMoved(MouseEvent e) {
		if (points.size() == MAX_POINTS) points.removeFirst();
		points.add(new Point(e.getX(), e.getY()));
	}
	
	public void mouseDragged(MouseEvent e) {
		
	}
	
	public void actionPerformed(ActionEvent ev){

	}

	private class Point{
		private static final int POINT_WIDTH = 7;
		private static final int POINT_HEIGHT = 7;
		private int x;
		private int y;

		private Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
}