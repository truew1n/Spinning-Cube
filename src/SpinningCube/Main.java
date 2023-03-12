package SpinningCube;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Timer;

public class Main extends Frame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private int WIDTH = 1000;
	private int HEIGHT = 1000;
	
	private double angle = 0;
	
	double [][][]points = {
		{{-1}, {-1}, {1}},
		{{1}, {-1}, {1}},
		{{1}, {1}, {1}},
		{{-1}, {1}, {1}},
		{{-1}, {-1}, {-1}},
		{{1}, {-1}, {-1}},
		{{1}, {1}, {-1}},
		{{-1}, {1}, {-1}}
	};
	
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("Spinning Cube");
		this.setVisible(true);
		
		Timer timer = new Timer(0, this);
		timer.start();
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				timer.stop();
				dispose();
			}
		});
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				repaint();
			}
		});
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		renderRotating3DCube(g, WIDTH/2, HEIGHT/2, 200, angle);
	}
	
	public void renderRotating3DCube(Graphics g, int x, int y, int scale, double rotation) {
		
		double [][]rotation_z = {
			{Math.cos(angle), -Math.sin(angle), 0},
			{Math.sin(angle), Math.cos(angle), 0},
			{0, 0, 1}
		};
		
		double [][]rotation_x = {
			{1, 0, 0},
			{0, Math.cos(angle), -Math.sin(angle)},
			{0, Math.sin(angle), Math.cos(angle)}
		};
		
		double [][]rotation_y = {
			{Math.cos(angle), 0, Math.sin(angle)},
			{0, 1, 0},
			{-Math.sin(angle), 0, Math.cos(angle)}
		};
		
		ArrayList<int[]> projected_points = new ArrayList<>();
		for(double[][] point : points) {
			double [][]rotated_2d = multiplyMatrix(rotation_x, point);
			rotated_2d = multiplyMatrix(rotation_y, rotated_2d);
			rotated_2d = multiplyMatrix(rotation_z, rotated_2d);
			
			int distance = 5;
			double z = 1/(distance - rotated_2d[2][0]);
			double [][]projection_matrix = {
				{z, 0, 0},
				{0, z, 0}
			};
			rotated_2d = multiplyMatrix(projection_matrix, rotated_2d);
			
			int []scaled_point = {
				(int)(rotated_2d[0][0] * scale + x),
				(int)(rotated_2d[1][0] * scale + y)
			};
			projected_points.add(scaled_point);
			g.fillOval(scaled_point[0]-3, scaled_point[1]-3, 6, 6);
		}
		for(int m = 0; m < 4; ++m) {
			connect_points(g, m, (m+1)%4, projected_points);
			connect_points(g, m+4, (m+1)%4 + 4, projected_points);
			connect_points(g, m, m+4, projected_points);
		}
		
		angle += 0.001;
	}
	
	public void connect_points(Graphics g, int i, int j, ArrayList<int[]> k) {
		int[] a = k.get(i);
		int[] b = k.get(j);
		g.drawLine(a[0], a[1], b[0], b[1]);
	}
	
	public double[][] multiplyMatrix(double matrix0[][], double matrix1[][]) {
        int i, j, k;
        int row0 = matrix0.length;
        int col0 = matrix0[0].length;
        int row1 = matrix1.length;
        int col1 = matrix1[0].length;
        if (row1 != col0) {
            return null;
        }
        double [][]C = new double[row0][col1];
 
        for (i = 0; i < row0; i++) {
            for (j = 0; j < col1; j++) {
                for (k = 0; k < row1; k++)
                    C[i][j] += matrix0[i][k] * matrix1[k][j];
            }
        }
 
        return C;
    }

	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}
