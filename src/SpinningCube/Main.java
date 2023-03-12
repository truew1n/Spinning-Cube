package SpinningCube;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends Frame {
	private static final long serialVersionUID = 1L;
	private int WIDTH = 1000;
	private int HEIGHT = 1000;
	
	Canvas canvas;
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		
		canvas = new Canvas(WIDTH, HEIGHT);
		
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("Spinning Cube");
		this.setVisible(true);
		
		this.add(canvas);
		this.pack();
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				dispose();
			}
		});
	}
	
}
