import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;


public class ResultBox {
	
	Image i;
	int width = 400;
	int height = 300;
	ArrayList<int[]> list;
	Canvas canvas;
	public boolean ready = false;
	double scaleFactor;
	
	public ResultBox(Image i, int width, int height, ArrayList<int[]> list, double scaleFactor) {
		this.i = i;
		this.width = width;
		//System.out.println(this.width);
		//System.out.println(this.height);
		this.height = height;
		this.list = list;
		this.scaleFactor = scaleFactor;
		this.open();
		//System.out.println("Made it here");
	}
	
	private void createCircles() {
		Image temp = new Image(Display.getCurrent(), width, height);
		GC gc = new GC(temp);
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		for(int j = 0; j<list.size(); j++) {
			if (list.get(j)[3] == 0 && list.get(j)[2] > width/50) {
				int y = (int)(list.get(j)[1]*scaleFactor);
				int x = (int)(list.get(j)[0]*scaleFactor);
				/*if (list.get(j)[2]*scaleFactor*.5 < width/40)
					gc.setAlpha(40);
				else
					gc.setAlpha(25);*/
				gc.fillOval(x-(int)(list.get(j)[2]*scaleFactor*.5), y-(int)(list.get(j)[2]*scaleFactor*.5), (int)(list.get(j)[2]*scaleFactor), (int)(list.get(j)[2]*scaleFactor));
			}
		}
		gc.dispose();
		ImageData tempData = temp.getImageData();
		int red = tempData.palette.getPixel(new RGB(255, 0, 0));
		for (int i=0; i<tempData.width; i++) {
			for (int j=0; j<tempData.height; j++) {
				if (tempData.getPixel(i, j) != red) {
					tempData.setAlpha(i, j, 0);
				}
				else {
					tempData.setAlpha(i, j, 100);
				}
			}
		}
		temp = new Image(Display.getCurrent(), tempData);
		gc = new GC(i);
		gc.drawImage(temp, 0, 0);
		//System.out.println("Done");
		canvas.setBackgroundImage(i);
		gc.dispose();
		//canvas.redraw();
	}

	protected Shell shell;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		//shell.setSize(width, height);
		createContents();
		createCircles();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				
			}
		});
		shell.setSize(this.width, this.height+20);
		shell.setText("Result");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		canvas = new Canvas(composite, SWT.NONE);
		//canvas.setBackgroundImage(i);

	}

}
