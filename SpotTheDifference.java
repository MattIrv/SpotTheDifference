import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class SpotTheDifference {
	
	private BufferedImage img1;
	private BufferedImage img2;
	ArrayList<int[]> retList;

	public SpotTheDifference(BufferedImage img1, BufferedImage img2) {
		this.img1 = img1;
		this.img2 = img2;
	}
	
	public ArrayList<int[]> getRetList() {
		return retList;
	}

	public void run() {
		retList = new ArrayList<int[]>();
		//USE PRINCIPAL COMPONENT ANALYSIS
		for(int i=0; i<img1.getWidth(); i++) {
			for (int j=0; j<img1.getHeight(); j++) {
				int data = img1.getRGB(i, j) ^ img2.getRGB(i, j);
				int blue = data << 24;
				blue = blue >>> 24;
				int green = data << 16;
				green = green >>> 24;
				int red = data << 8;
				red = red >>> 24;
				if (blue > 32 || green > 32 || red > 32) {
					int[] temp = {i,j,1, 0};
					retList.add(temp);
				}
			}
		}

		
		for(int i=0; i<retList.size(); i++) {
			for (int j=0; j<retList.size(); j++) {
				if (retList.get(i)[3] == 0  && retList.get(j)[3] == 0
						&& retList.get(i)[0] >= retList.get(j)[0] - 5
						&& retList.get(i)[0] <= retList.get(j)[0] + 5 &&
						retList.get(i)[1] >= retList.get(j)[1] - 5 &&
						retList.get(i)[1] <= retList.get(j)[1] + 5 && i != j) {
					retList.get(i)[2]+=1;
					retList.get(j)[3] = 1;
				}
			}
		}
	}
}
