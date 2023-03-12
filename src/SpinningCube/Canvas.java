package SpinningCube;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Canvas extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private int WIDTH = 0;
	private int HEIGHT = 0;
	
	private double angle = 0;
	
	int [][]filling_pattern = {
		{0, 4, 5, 1},
		{0, 4, 7, 3},
		{2, 6, 7, 3},
		{2, 6, 5, 1},
		{0, 1, 2, 3},
		{4, 5, 6, 7}
	};
	
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
	
	public Canvas(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				repaint();
			}
		});
		
		Timer timer = new Timer(1, this);
		timer.start();
	}
		
	public void paint(Graphics g) {
		super.paint(g);
		renderRotating3DCube(g, WIDTH/2, HEIGHT/2, 1000, 0);
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
		}
		
		g.setColor(Color.BLACK);
		for(int[] pattern : filling_pattern) {
			Polygon polygon = new Polygon();
			for(int i = 0; i < pattern.length; ++i) {
				int[] singlePoint = projected_points.get(pattern[i]);
				polygon.addPoint(singlePoint[0], singlePoint[1]);
			}
			g.fillPolygon(polygon);
		}
//		g.setColor(Color.RED);
//		for(int m = 0; m < 4; ++m) {
//			connect_points(g, m, (m+1)%4, projected_points);
//			connect_points(g, m+4, (m+1)%4 + 4, projected_points);
//			connect_points(g, m, m+4, projected_points);
//		}
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
