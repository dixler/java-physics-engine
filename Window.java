import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
public class Window {
	public Window(){
		
	}
	
	public static void main(String[] args){
		//long simStartTime = System.currentTimeMillis();
		Landscape world = new Landscape();
		int objNum = 5;
		int worldWidth = 1000;
		int worldHeight = 1000;
		world.setWidth(worldWidth);
		world.setHeight(worldHeight);
		Random random = new Random();
		//Scanner input = new Scanner(System.in);
		JFrame appFrame = new JFrame();
		PhysObj obj = new PhysObj();

		for(int i = 0; i < objNum; i++){
			obj = new PhysObj();
			obj.setX(random.nextInt(worldWidth-obj.width));
			obj.setY(random.nextInt(worldHeight-obj.height));
			obj.setMass(random.nextInt(1000)+1);
			//obj.type = (1-2*random.nextInt(2));
			obj.type = 1;
			obj.setColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
			
			if(i == 0){
//				obj.setX(worldWidth/2.0);
//				obj.setY(worldHeight/2.0);
//				obj.type = 50;
//				obj.life = 0;
//				obj.setMass(1000);
			}
			world.addObj(obj);
		}
		appFrame.setTitle("Tontine!");
		appFrame.setSize(new Dimension(worldWidth, worldHeight));
		appFrame.setPreferredSize(new Dimension(worldWidth, worldHeight));
		//appFrame.setBackground(Color.WHITE);
		appFrame.setBackground(Color.BLACK);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    appFrame.setVisible(true);
	    int sustain = 1;
		long secs = (System.currentTimeMillis());
		
		while(sustain == 1){	
			world.evalPhysics(1.0);
			if(System.currentTimeMillis() - secs >= 8){
				appFrame.add(new Env(world));
				secs = System.currentTimeMillis();
				appFrame.repaint();
			}

			//System.gc();
		}
		return;
	}
}
