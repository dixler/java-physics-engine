import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JPanel;

// HistogramComponent extends the functionality of a JComponent
// in order to draw a histogram.
public class Env extends JPanel {
	
	private Landscape world;
   private ArrayList<Rectangle> obj;
	
	public Env(Landscape userInput){
		world = userInput;
      obj = new ArrayList<Rectangle>();
	}
	private int get_objCount(){
      return obj.size();
	}
	
//	@Override
   public void paintComponent(Graphics g) {  
      Graphics2D graphicsObj = (Graphics2D) g;
		int objCount = world.getNumObj();
      while(this.get_objCount() < objCount){
         obj.add(new Rectangle(0,0,0,0));
      }
      for(int i = 0; i < objCount; i++){
        //world.getObjData(i).info(i);
        PhysObj phys_obj = world.getObjData(i);
        double x = phys_obj.getCurX();
        double y = phys_obj.getCurY();
        obj.get(i).setLocation((int)(x), (int)(y));
        obj.get(i).setSize(phys_obj.width, phys_obj.height);
        graphicsObj.setColor(world.getObjData(i).color);
        graphicsObj.fill(obj.get(i));
        graphicsObj.setColor(new Color(255,255,255));
        g.drawString(Integer.toString(world.getObjData(i).type), (int)(world.getObjData(i).getCurX()), (int)(world.getObjData(i).getCurY()));
      }
      
      return;
   }
}
