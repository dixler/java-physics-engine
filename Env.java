import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
import javax.swing.JPanel;

// HistogramComponent extends the functionality of a JComponent
// in order to draw a histogram.
public class Env extends JPanel {
	
	//private PhysObj  a;
	private Landscape world;
	//TODO
	//private long time;
	
	//myColor.setRGB(random.nextInt(256) , random.nextInt(256), random.nextInt(256));
	public Env(Landscape userInput){
		world = userInput;
	}
	
//	@Override
   public void paintComponent(Graphics g) {  
      Graphics2D graphicsObj = (Graphics2D) g;
		int objCount = world.getNumObj();
		Rectangle[] obj = new Rectangle[objCount]; 
		for(int i = 0; i < objCount; i++){
			obj[i] = new Rectangle(0,0,world.getObjData(i).width, world.getObjData(i).height);
		}
      if(world.getNumObj() > 0){
	      for(int i = 0; i < objCount; i++){
	    	  //world.getObjData(i).info(i);
	    	  double x = world.getObjData(i).getCurX();
	    	  double y = world.getObjData(i).getCurY();
	    	  obj[i].setLocation((int)(x), (int)(y));
	    	  graphicsObj.setColor(world.getObjData(i).color);
	    	  graphicsObj.fill(obj[i]);
	    	  graphicsObj.setColor(new Color(255,255,255));
	    	  g.drawString(Integer.toString(world.getObjData(i).type), (int)(world.getObjData(i).getCurX()), (int)(world.getObjData(i).getCurY()));
	      }
      }
      
      return;
   }
}
