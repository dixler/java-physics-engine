import java.awt.Color;
import java.util.ArrayList;

public class Landscape { 

   private ArrayList<PhysObj> obj;

   private double g = 10;
   public int width = 0;
   public int height = 0;
   private double colMod = 0.7;
   private int realHeight;
   private double rMod = 1;

   public Landscape(){
      obj = new ArrayList<PhysObj>();
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
      this.obj.add(userObj);
      return;
   }
   public void delObj(int i){
      this.obj.remove(i);
      return;
   }
   public int getNumObj(){
      return this.obj.size();
   }

   public PhysObj getObjData(int i){
      return this.obj.get(i);
   }

   //Kingpin
   public void evalPhysics(double time){
      evalKin(time);
      evalChem();
   }

   private void debugObj(){
      int i = 0;
      for(PhysObj obj : this.obj){
         obj.info(++i);	
      }
   }

   //KINEMATICS EVALUATIONS
   private void evalKin(double time){
      evalAccel(time);
      evalPos(time);
   }

   private void evalPos(double time){
      ckWalls();
      for(PhysObj obj : obj){
         if(obj.type != 50){					//Temporary
            obj.setPos(time);
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
   double[][] accel = new double[this.getNumObj()][2];	//0 is x accel 1 is y accel
   accel = evalGravity(time);
   int i = 0;
   for(PhysObj obj : obj){
      obj.dX2 = accel[i][0];
      obj.dY2 = accel[i][1];
      ++i;
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
   double[][] accel = new double[this.getNumObj()][2];
   int i = 0;
   for(PhysObj obj_a: obj){
      accel[i][0] = 0;
      accel[i][1] = 0;
      int j = 0;
      for(PhysObj obj_b: obj){
         if(i != j){
            double delX = obj_b.getCurX() - obj_a.getCurX();
            double delY = obj_b.getCurY() - obj_a.getCurY();
            double r = 0.0;
            if((obj_a.type > 0 && obj_b.type > 0) || (obj_a.type < 0 && obj_b.type < 0)){
               r = rMod*rMod*(delX*delX+delY*delY);
            }
            if(r  > 4){	
               double f = g*obj_a.mass*obj_b.mass/(r);
               double theta = Math.asin(delX/Math.sqrt(r));
               accel[i][0] += 1*f/obj_a.mass*Math.sin(theta);
               theta = Math.asin(delY/Math.sqrt(r));
               accel[i][1] += 1*f/obj_a.mass*Math.sin(theta);
            }
            ++j;
         }
      }
      ++i;
   }
   return accel;
}

/*
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
   */

private void evalChem(){
   rendGrav();
   rendCol();
}

private void rendCol(){
   int i = 0;
   ArrayList<Integer> dead = new ArrayList<Integer>();
   for(PhysObj obj_a : obj){
      int j = 0;
      for(PhysObj obj_b : obj){
         if(obj_a != obj_b && obj_a.type != 0 && obj_b.type != 0){
            double delX = obj_b.getCurX() - obj_a.getCurX();
            double delY = obj_b.getCurY() - obj_a.getCurY();
            double r = delX*delX+delY*delY;
            double l = obj_a.width*obj_a.width/4.0;
            if(r  < 2){
               double avgMass = (obj_b.mass + obj_a.mass)/2.0;
               obj_a.setMass(avgMass);
               obj_b.setMass(avgMass);
            }
            ///*
            else if(r < l && obj_a.type == obj_b.type && obj_a.mass > 0.0){
               rendFusion(obj_b, obj_a);	//GOES STRAIGHT INTO FUSION
               if(obj_a.mass < 0){
                  dead.add(i);
               }
               else if(obj_b.mass < 0){
                  dead.add(j);
               }
            }

            else if(r < l && obj_a.type*obj_b.type < 0 && obj_a.mass > 0){
               rendFission(obj_a, obj_b);	//GOES STRAIGHT INTO FUSION
               if(obj_a.mass < 0){
                  dead.add(i);
               }
               else if(obj_b.mass < 0){
                  dead.add(j);
               }
            }
            //*/
         }
         ++j;
      }
      ++i;
   }
   for(Integer reaped : dead){
      delObj(reaped);
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
   for(PhysObj obj_a : obj){
      double hue = 0.0;
      double scaling = 90000.0/(300.0*this.getNumObj());
      for(PhysObj obj_b : obj){
         if(obj_a.type != 0){
            if(obj_a != obj_b){
               double delX = obj_b.getCurX() - obj_a.getCurX();
               double delY = obj_b.getCurY() - obj_a.getCurY();
               double r = (delX*delX+delY*delY);
               if(r  > 1 && obj_a.type == obj_b.type){//obj[i].width*obj[i].width){
                  hue += 300.0/(Math.sqrt(r/obj_a.width)*(this.getNumObj()-1));
               }
               }
            }
            else{
               hue = -1;
            }
         }
         hue = Math.sqrt(hue*scaling/300.0)*300;
         if(obj_a.type > 0){
            if(hue < 300){
               obj_a.color = hueToRgb((int)hue);
            }
            else if(hue >= 0){
               obj_a.color = hueToRgb(300);
            }
            else{
               obj_a.color = new Color(255,255,255);
            }
         }
         else if(obj_a.type < 0){
            if(hue < 300){
               obj_a.color = new Color((int)(hue*0.85),(int)(hue*0.85),(int)(hue*0.85));
            }
            else if(hue >= 0){
               obj_a.color = new Color(255,255,255);
            }
            else{
               obj_a.color = new Color(0,0,0);
            }
         }
      }
   }

   private void rendAvg(){
      for(PhysObj obj_a : obj){
         for(PhysObj obj_b : obj){
            if(obj_a != obj_b){
               double delX = obj_b.getCurX() - obj_a.getCurX();
               double delY = obj_b.getCurY() - obj_a.getCurY();
               double r = (delX*delX+delY*delY);
               if(r < 5){
                  int avgR = (int)((obj_a.color.getRed() + obj_b.color.getRed())/2.0);
                  int avgG = (int)((obj_a.color.getGreen() + obj_b.color.getGreen())/2.0);
                  int avgB = (int)((obj_a.color.getBlue() + obj_b.color.getBlue())/2.0);
                  obj_a.setColor(avgR, avgG, avgB);
                  obj_b.setColor(avgR, avgG, avgB);
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
      for(PhysObj obj : obj){
         if(obj.getX() < 0 || obj.getX()+obj.width > width){
            if(obj.getX() < 0){
               obj.setX(0);
            }
            if(obj.getX()+obj.width > width){
               obj.setX(width-obj.width);	
            }
            obj.dX = -1.0*colMod*obj.dX;
         }

         if(obj.getCurY() < 0 || obj.getCurY()-obj.height/2.0 > realHeight){
            if(obj.getCurY() < 0){
               obj.setY(0);
            }
            if(obj.getCurY()-obj.height/2.0 > realHeight){
               obj.setY(realHeight);
            }
            obj.dY = -1.0*colMod*obj.dY;
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
