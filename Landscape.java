import java.awt.Color;

public class Landscape { 
	
	private PhysObj[] obj;
	private int numObj = 0;
	
	private double g = 10;
	public int width = 0;
	public int height = 0;
	private double colMod = 0.7;
	private int realHeight;
	private double rMod = 1;

	public Landscape(){
		
	}
	public void setWidth(int userNum){
		width = userNum;
	}
	
	public void setHeight(int userNum){
		height = userNum;
		//realHeight = height - 260; //for mac
		realHeight = height;	//for desktop
	}
	
	public void addObj(PhysObj userObj){
		numObj++;
		PhysObj[] tempObj = new PhysObj[numObj];
		for(int i = 0; i < numObj; i++){
			if(i < numObj - 1){
				tempObj[i] = obj[i];
			}
			else{
				tempObj[i] = userObj;
			}
		}
		obj = new PhysObj[numObj];
		obj = tempObj;
	}
	public void delObj(int i){
		int nIndx = 0;
		PhysObj[] tempObj = new PhysObj[numObj-1];
		for(int j = 0; j < numObj; j++){
			if(j != i){
				tempObj[nIndx] = obj[j];
				nIndx++;
			}
		}
		numObj--;
		obj = new PhysObj[numObj];
		obj = tempObj;
	}
	public int getNumObj(){
		return numObj;
	}
	
	public PhysObj getObjData(int i){
		return obj[i];
	}
	
	//Kingpin
	public void evalPhysics(double time){
		evalKin(time);
		evalChem();
	}
	
	private void debugObj(){
		for(int i = 0; i < numObj; i++){
			obj[i].info(i);	
		}
	}
	
	//KINEMATICS EVALUATIONS
	private void evalKin(double time){
		evalAccel(time);
		evalPos(time);
	}
	
	private void evalPos(double time){
		ckWalls();
		for(int i = 0; i < numObj; i++){
			if(obj[i].type != 50){					//Temporary
				obj[i].setPos(time);
			}
		}
	}
	
	//TODO
	/*
	private void evalVel(double time){
		double[][] vel = new double[numObj][2];	//0 is x accel 1 is y accel
		//vel = ckWalls();
		
		for(int i = 0; i < numObj; i++){
			obj[i].dX += vel[i][0];
			obj[i].dY += vel[i][1];
		}
		for(int i = 0; i < numObj; i++){
			obj[i].dX += obj[i].dX2*time;
			obj[i].dY += obj[i].dY2*time;
		}
	}
	*/
	
	private void evalAccel(double time){
		double[][] accel = new double[numObj][2];	//0 is x accel 1 is y accel
		accel = evalGravity(time);
		for(int i = 0; i < numObj; i++){
			obj[i].dX2 = accel[i][0];
			obj[i].dY2 = accel[i][1];
		}
		/*
		accel = evalMagnetism(time);
		for(int i = 0; i < numObj; i++){
			obj[i].dX2 += accel[i][0];
			obj[i].dY2 += accel[i][1];
		}
		*/
		
		//TODO add more forces to add complexity	
	}
	
	public double[][] evalGravity(double time){
		double[][] accel = new double[numObj][2];
		for(int i = 0; i < numObj; i++){
			accel[i][0] = 0;
			accel[i][1] = 0;
			for(int j = 0; j < numObj; j++){
				if(i != j){
					double delX = obj[j].getCurX() - obj[i].getCurX();
					double delY = obj[j].getCurY() - obj[i].getCurY();
					double r = 0.0;
					if((obj[i].type > 0 && obj[j].type > 0) || (obj[i].type < 0 && obj[j].type < 0)){
						r = rMod*rMod*(delX*delX+delY*delY);
					}
					if(r  > 4){	
						double f = g*obj[i].mass*obj[j].mass/(r);
						double theta = Math.asin(delX/Math.sqrt(r));
						accel[i][0] += 1*f/obj[i].mass*Math.sin(theta);
						theta = Math.asin(delY/Math.sqrt(r));
						accel[i][1] += 1*f/obj[i].mass*Math.sin(theta);
					}
				}
			}
		}
		return accel;
	}
	
	public double[][] evalMagnetism(double time){
		double[][] accel = new double[numObj][2];
		for(int i = 0; i < numObj; i++){
			accel[i][0] = 0;
			accel[i][1] = 0;
			for(int j = 0; j < numObj; j++){
				if(i != j && Math.abs(obj[i].mass) > 0.1){
					double delX = obj[j].getCurX() - obj[i].getCurX();
					double delY = obj[j].getCurY() - obj[i].getCurY();
					double r = obj[i].type*obj[j].type*(delX*delX+delY*delY);
					if(r  <= 4 && r >= -4 && Math.abs(r) > 0.1){
						double f = obj[i].type*obj[j].type*g*obj[i].mass*obj[j].mass/(r);
						double theta = Math.asin(delX/Math.sqrt(r));
						accel[i][0] += f/obj[i].mass*Math.sin(theta);
						theta = Math.asin(delY/Math.sqrt(r));
						accel[i][1] += f/obj[i].mass*Math.sin(theta);
					}
				}
			}
		}
		return accel;
	}
	
	private void evalChem(){
		rendGrav();
		rendCol();
	}
	
	private void rendCol(){
		for(int i = 0; i < numObj; i++){
			for(int j = 0; j < numObj; j++){
				if(i != j && obj[i].type != 0 && obj[j].type != 0){
					double delX = obj[j].getCurX() - obj[i].getCurX();
					double delY = obj[j].getCurY() - obj[i].getCurY();
					double r = delX*delX+delY*delY;
						double l = obj[i].width*obj[i].width/4.0;
						if(r  < 2){
							double avgMass = (obj[j].mass + obj[i].mass)/2.0;
							obj[i].setMass(avgMass);
							obj[j].setMass(avgMass);
						}
						///*
						else if(r < l && obj[i].type == obj[j].type && obj[i].mass > 0.0){
								//rendFusion(obj[j], obj[i]);	//GOES STRAIGHT INTO FUSION
						}
						
						else if(r < l && obj[i].type*obj[j].type < 0 && obj[i].mass > 0){
								//rendFission(obj[i], obj[j]);	//GOES STRAIGHT INTO FUSION
						}
						//*/
					}
				}
			}
		int changed = 0;
		for(int j = 0; j < numObj; j++){
			for(int i = 0; i < numObj; i++){
				if(obj[i].mass < 0){
					delObj(i);
					changed--;
					break;
				}
			}
		}
		changed = 0;
		for(int j = 0; j < numObj; j++){
			for(int i =0; i < numObj; i++){
				if(obj[i].birth == 1){
					obj[i].birth = 0;
					addObj(obj[i]);
					changed++;
					break;
				}
			}
		}
	}
	//FIXME
	private void rendFusion(PhysObj ob1, PhysObj ob2){
		System.out.println("FUSION");
		ob2.setMass(ob2.mass + ob1.mass);
		ob2.type += ob1.type;
		ob2.dX = ob2.dX+ob1.dX;
		ob2.dY = ob2.dX+ob1.dX;
		ob1.setMass(-1);
	}
	
	private void rendFission(PhysObj ob1, PhysObj ob2){
		System.out.println("FISSION");
		double avgMass = (ob2.mass + ob1.mass)/2.0;
		double avgVelX = (ob2.dX+ob1.dX)/2.0;
		double avgVelY = (ob2.dY+ob1.dY)/2.0;
		ob2.setMass(avgMass);
		ob2.birth = 1;
		ob2.dX = avgVelX;
		ob2.dY = avgVelY;	
		ob1.setMass(avgMass);
		ob1.birth = 1;
		ob1.dX = avgVelX; //velocity of Y gets fucked by eval Kin(eval accel
		ob1.dY = avgVelY;
		if(ob2.type < 0){
			ob2.type++;
		}
		else if(ob2.type > 0){
			ob2.type--;
		}
		if(ob1.type < 0){
			ob1.type++;
		}
		else if(ob1.type > 0){
			ob1.type--;
		}
	}
	
	private void rendGrav(){
		for(int i = 0; i < numObj; i++){
			double hue = 0.0;
			double scaling = 90000.0/(300.0*numObj);
			for(int j = 0; j < numObj; j++){
				if(obj[i].type != 0){
					if(i != j){
						double delX = obj[j].getCurX() - obj[i].getCurX();
						double delY = obj[j].getCurY() - obj[i].getCurY();
						double r = (delX*delX+delY*delY);
						if(r  > 1 && obj[i].type == obj[j].type){//obj[i].width*obj[i].width){
							hue += 300.0/(Math.sqrt(r/obj[i].width)*(numObj-1));
						}
					}
				}
				else{
					hue = -1;
				}
			}
			hue = Math.sqrt(hue*scaling/300.0)*300;
			if(obj[i].type > 0){
				if(hue < 300){
					obj[i].color = hueToRgb((int)hue);
				}
				else if(hue >= 0){
					obj[i].color = hueToRgb(300);
				}
				else{
					obj[i].color = new Color(255,255,255);
				}
			}
			else if(obj[i].type < 0){
				if(hue < 300){
					obj[i].color = new Color((int)(hue*0.85),(int)(hue*0.85),(int)(hue*0.85));
				}
				else if(hue >= 0){
					obj[i].color = new Color(255,255,255);
				}
				else{
					obj[i].color = new Color(0,0,0);
				}
			}
		}
	}
	
	private void rendAvg(){
		for(int i = 0; i < numObj; i++){
			for(int j = 0; j < numObj; j++){
				if(i != j){
					double delX = obj[j].getCurX() - obj[i].getCurX();
					double delY = obj[j].getCurY() - obj[i].getCurY();
					double r = (delX*delX+delY*delY);
					if(r < 5){
						int avgR = (int)((obj[i].color.getRed() + obj[j].color.getRed())/2.0);
						int avgG = (int)((obj[i].color.getGreen() + obj[j].color.getGreen())/2.0);
						int avgB = (int)((obj[i].color.getBlue() + obj[j].color.getBlue())/2.0);
						obj[i].setColor(avgR, avgG, avgB);
						obj[j].setColor(avgR, avgG, avgB);
					}
				}
			}
		}
	}
	
	private Color hueToRgb(int hue){
		Color rgb;
		double red = 255;
		double green = 0;
		double blue = 255;
		for(int i = 300; i > 0; i--){
			if(i == hue){
				i = 0;
				break;
			}
			if(i >= 240){
				red+=-4.25;
			}
			else if(i >= 180){
				green+=4.25;
			}
			else if(i >= 120){
				blue+=-4.25;
			}
			else if(i >= 60){
				red+=4.25;
			}
			else{
				green+=-4.25;
			}
		}
		rgb = new Color(Math.abs((int)red), Math.abs((int)green), Math.abs((int)blue));
		
		return rgb;
	}
	
	private void ckWalls(){
		for(int i = 0; i < numObj; i++){
			if(obj[i].getX() < 0 || obj[i].getX()+obj[i].width > width){
				if(obj[i].getX() < 0){
					obj[i].setX(0);
				}
				if(obj[i].getX()+obj[i].width > width){
					obj[i].setX(width-obj[i].width);	
				}
				obj[i].dX = -1.0*colMod*obj[i].dX;
				}
				
				if(obj[i].getCurY() < 0 || obj[i].getCurY()-obj[i].height/2.0 > realHeight){
					if(obj[i].getCurY() < 0){
						obj[i].setY(0);
					}
					if(obj[i].getCurY()-obj[i].height/2.0 > realHeight){
						obj[i].setY(realHeight);
					}
					obj[i].dY = -1.0*colMod*obj[i].dY;
				}
			}
		}
	

	/*
	public void ckCollision(){
		for(int i = 0; i < objCount; i++){
			xA[i] = (int)obj[i].curX;
			xB[i] = (int)(obj[i].curX+obj[i].width);
			yA[i] = (int)(obj[i].curY);
			yB[i] = (int)(obj[i].curY+obj[i].height);
		}
		for(int i = 0; i < objCount; i++){
			for(int j = 0; j < objCount; j++){
				if(xB[i] >= xB[j] && xB[i] <= xB[j]+obj[i].width && yB[i] <= yB[j] && yB[i] >= yA[j]){
					obj[i].dX = -1*obj[i].dX;
					obj[i].dY = -1*obj[i].dY;
					System.out.println("Collision working");
				}
			}
		}
		
	}
	*/
	
}
