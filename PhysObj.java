//Temporarily calculating freefall 
import java.awt.Color;
public class PhysObj {
	
	public PhysObj(){
	}
	//TODO expand width and height
	public int width = 0;
	public int height = width;
	public double mass = 0.0;
	public Color color = new Color(0,0,0);
	private double x = 0.0;
	private double y = 0.0;
	public double dX = 0.0;
	public double dY = 0.0;
	public double dX2 = 0.0;
	public double dY2 = 0.0; //gravity is upside down bc reasons
	public double time = 0.0;
	private double curX = 0.0;
	private double curY = 0.0;
	public int type = 1;
	public int birth = 0;
	//1 is matter
	//-1 is antimatter
	//0 is light
	//private double actualTime = 0.0;
	//TODO Reconstrain by PhysObj size

	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getCurX(){
		return curX;
	}
	public double getCurY(){
		return curY;
	}
	public void setCurX(double userNum){
		curX = userNum;
		x = curX-width/2.0;
	}
	public void setCurY(double userNum){
		curY = userNum;
		y = curY-height/2.0;
	}
	public void setX(double userNum){
		x = userNum;
		curX = x+height/2.0;
	}
	public void setY(double userNum){
		y = userNum;
		curY = y+height/2.0;
	}
	
	public void setPos(double userNum){
		
		time = userNum/1000.0;
		dY += dY2*time;
		curY += dY*time;
		dX += dX2*time;
		curX += dX*time;
		x = curX-width/2.0;
		y = curY-height/2.0;
	}
	
	public void info(int id){
		System.out.println("\nObj id: " + id);
		System.out.println("width: "+width);
		System.out.println("height: "+height);
		System.out.println("mass: "+mass);
		System.out.println("x: "+x);
		System.out.println("y: "+y);
		System.out.println("dX: "+dX);
		System.out.println("dY: "+dY);
		System.out.println("dX2: "+dX2);
		System.out.println("dY2: "+dY2);
		System.out.println("curX: "+curX);
		System.out.println("curY: "+curY);
		System.out.println("type: "+ type);
		//System.out.println("life: "+ life);
	}
	public void setMass(double userNum){
		mass = userNum;
		width = (int)Math.sqrt(userNum);
		height = (int)Math.sqrt(userNum);
	}
	public void setColor(int r, int g, int b){
		color = new Color(r,g,b);
	}
	public void addColor(int r, int g, int b){
		color = new Color(color.getRed() + r, color.getGreen() + g, color.getBlue() + b);
	}
	public void setDX(int userNum){
		dX = userNum;
	}
	public void setDY(int userNum){
		dY = userNum;
	}
	public void setDX2(int userNum){
		dX2 = userNum;
	}
	public void setDY2(int userNum){
		dY2 = userNum;
	}
}
