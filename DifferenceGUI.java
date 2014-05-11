import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;


public class DifferenceGUI {
	
	Image c1Img;
	BufferedImage b1Img = null;
	BufferedImage b2Img = null;
	int im1Width = 0;
	int im1Height = 0;
	int im2Width = 0;
	int im2Height = 0;
	Image c2Img;
	Canvas canvas1;
	Canvas canvas2;
	ArrayList<int[]> diffList;
	ResultBox r;
	double scaleFactor = 1;

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DifferenceGUI window = new DifferenceGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
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
				System.exit(0);
			}
		});
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				if (b1Img != null)
					canvas1.setSize(im1Width, im1Height);
				if (b2Img != null)
					canvas2.setSize(im2Width, im2Height);
			}
		});
		shell.setSize(1100, 500);
		shell.setText("Spot the Difference!");
		shell.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		GridData gd_composite = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_composite.widthHint = 1090;
		composite.setLayoutData(gd_composite);
		
		Button btnChooseImage = new Button(composite, SWT.NONE);
		btnChooseImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell);
				dialog.setText("Load Image 1");
				String fileName = dialog.open();
				Image tempImage = null;
				while(true) {
					try {
						tempImage = new Image(null, fileName);
						break;
					}
					catch (Exception ex) {
						if (ex.getMessage().contains("null")) {
							tempImage = canvas1.getBackgroundImage();
							break;
						}
						MessageBox errorBox = new MessageBox(shell, SWT.ERROR);
						errorBox.setText("Error");
						errorBox.setMessage("An error occured opening the file:\n" + ex.getMessage());
						errorBox.open();
					}
				}
				/*if (b1Img.getWidth() > 600 || b2Img.getWidth() > 600 || b1Img.getHeight() > 600 || b2Img.getHeight() > 600) {
				try {
					scaleFactor = b1Img.getWidth() / (600+.0);
					if ((b1Img.getHeight() / (600+.0)) > scaleFactor)
						scaleFactor = b1Img.getHeight() / (600+.0);
					b1Img = Thumbnails.of(b1Img).size(600, 600).asBufferedImage();
					b2Img = Thumbnails.of(b2Img).size(600, 600).asBufferedImage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}*/
				Image tempy = tempImage;
				if (tempImage.getBounds().width > 500 || tempImage.getBounds().height > 500) {
					double aspect = tempImage.getBounds().width / 500.0;
					if (tempImage.getBounds().height / 500.0 > aspect)
						aspect = tempImage.getBounds().height / 500.0;
					tempy = new Image(Display.getCurrent(), tempImage.getImageData().scaledTo(
							(int)(tempImage.getBounds().width/aspect), 
							(int)(tempImage.getBounds().height/aspect)));
					im1Width = (int)(tempImage.getBounds().width/aspect);
					im1Height = (int)(tempImage.getBounds().height/aspect);
				}
				c1Img = tempImage;
				canvas1.setBackgroundImage(tempy);
				canvas1.setSize(tempy.getBounds().width, tempy.getBounds().height);
				//SDFGDSFGDSFGDSFGDSGSD
				BufferedImage tempBuffer = null;
				while(true) {
					FileInputStream in = null;
					try {
						//System.out.println(fileName);
						in = new FileInputStream(fileName);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						tempBuffer = ImageIO.read(in);
						b1Img = tempBuffer;
						break;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		btnChooseImage.setText("Choose Image 1");
		
		Button btnChooseImage_1 = new Button(composite, SWT.NONE);
		btnChooseImage_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell);
				dialog.setText("Load Image 2");
				String fileName = dialog.open();
				Image tempImage = null;
				while(true) {
					try {
						tempImage = new Image(null, fileName);
						break;
					}
					catch (Exception ex) {
						if (ex.getMessage().contains("null")) {
							tempImage = canvas2.getBackgroundImage();
							break;
						}
						MessageBox errorBox = new MessageBox(shell, SWT.ERROR);
						errorBox.setText("Error");
						errorBox.setMessage("An error occured opening the file:\n" + ex.getMessage());
						errorBox.open();
					}
				}
				Image tempy = tempImage;
				if (tempImage.getBounds().width > 500 || tempImage.getBounds().height > 500) {
					double aspect = tempImage.getBounds().width / 500.0;
					if (tempImage.getBounds().height / 500.0 > aspect)
						aspect = tempImage.getBounds().height / 500.0;
					tempy = new Image(Display.getCurrent(), tempImage.getImageData().scaledTo(
							(int)(tempImage.getBounds().width/aspect), 
							(int)(tempImage.getBounds().height/aspect)));
					im2Width = (int)(tempImage.getBounds().width/aspect);
					im2Height = (int)(tempImage.getBounds().height/aspect);
				}
				c2Img = tempImage;
				canvas2.setBackgroundImage(tempy);
				canvas2.setSize(tempy.getBounds().width, tempy.getBounds().height);
				//SDFGDSFGDSFGDSFGDSGSD
				BufferedImage tempBuffer = null;
				while(true) {
					FileInputStream in = null;
					try {
						//System.out.println(fileName);
						in = new FileInputStream(fileName);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						tempBuffer = ImageIO.read(in);
						b2Img = tempBuffer;
						break;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnChooseImage_1.setText("Choose Image 2");
		
		Button btnRun = new Button(composite, SWT.CENTER);
		btnRun.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				/*if (b1Img.getWidth() > 600 || b2Img.getWidth() > 600 || b1Img.getHeight() > 600 || b2Img.getHeight() > 600) {
					try {
						scaleFactor = b1Img.getWidth() / (600+.0);
						if ((b1Img.getHeight() / (600+.0)) > scaleFactor)
							scaleFactor = b1Img.getHeight() / (600+.0);
						b1Img = Thumbnails.of(b1Img).size(600, 600).asBufferedImage();
						b2Img = Thumbnails.of(b2Img).size(600, 600).asBufferedImage();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}*/
				final SpotTheDifference std = new SpotTheDifference(b1Img, b2Img);
				//System.out.println("Running.");
				std.run();
				diffList = std.getRetList();
				//System.out.println("All done.");
				r = new ResultBox(c1Img, c1Img.getBounds().width, c1Img.getBounds().height, diffList, scaleFactor);
			}
		});
		btnRun.setText("Run");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_composite_1.widthHint = 1091;
		gd_composite_1.heightHint = 697;
		composite_1.setLayoutData(gd_composite_1);
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		canvas1 = new Canvas(composite_2, SWT.BORDER);
		
		canvas2 = new Canvas(composite_2, SWT.BORDER);

	}
}
