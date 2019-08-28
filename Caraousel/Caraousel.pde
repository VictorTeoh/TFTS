int value = 0;
int i = 0;
Ball[] balls;  
int[] keys_to_check = {90, 88 };
boolean[] keys_down = new boolean[keys_to_check.length];
boolean[] pkeys_down = new boolean[keys_to_check.length];
boolean move_key_pressed = false;

void setup(){
  size(1000, 1000);
  smooth();
  noStroke();
  frameRate(60);
  balls = new Ball[10];
  for(int i = 0; i < 9; i++){
    balls[i] = new Ball();
    balls[i].setcolor(222, 222, 249);
    balls[i].setviscolor(222, 222, 249);
    balls[i].sethbox(createShape(ELLIPSE, 0, 0, 55, 55)); //55
    balls[i].setvisbox(createShape(ELLIPSE, 0, 0, 80, 80));//130 130 first 2 for some trippy shit
    balls[i].sethboxrad(80);
    balls[i].setraddistance(130*sqrt(2));
    balls[i].setrotpoint(500,500);
    balls[i].setw(radians(i*40));
    balls[i].setdw(PI/900);
  }
  for(int i = 9; i < 10; i++){
    //find a way to set speed to 0 
    balls[i] = new Ball();
    balls[i].setcolor(0, 0, 255);
    balls[i].setviscolor(0, 0, 255);
    balls[i].sethbox(createShape(ELLIPSE, 0, 0, 25, 25));
    balls[i].sethboxrad(25);
    balls[i].setvisbox(createShape(ELLIPSE,0, 0, 30, 30));
    balls[i].setposition(500,800);
  }
//30 90 25 best case
}

 void keyPressed(){
     copeWithKeys(true); // TRUE MEANS KEY PRESSED
  }
 
  void keyReleased(){
     copeWithKeys(false); // FALSE MEANS KEY NOT PRESSED
  }
  
  void copeWithKeys(boolean state){
    for( int i = 0; i< pkeys_down.length; i++){
      pkeys_down[i] = false;
    }
    for( int i = 0; i < keys_to_check.length; i++){
      if( keys_to_check[i] == keyCode ){
         keys_down[i] = state; 
         pkeys_down[i] = state;
      }
    }
  }

void drawcircles(){
  for(i = 0; i < 2; i++){//4 is the movement keys
      if(keys_down[i]){
        move_key_pressed = true;
        break;
      }
    }
  if(move_key_pressed){
  balls[9].setspeed(2);
  followthemouse();
}
  if(!move_key_pressed){
    balls[9].setspeed(0);
  }
  for(Ball b : balls){
    pushMatrix();
    b.move();
    b.drawme(); 
    popMatrix();
  }
}

void collisions(){
  for(Ball b : balls){
     for(Ball a : balls){
       b.collisioncheck(a);
     }
  }
}


void followthemouse(){
  if(move_key_pressed){
//    for( int i = 9; i < 11; i++){    
      balls[9].movetowards(mouseX,mouseY);
//    }
  }
}

void draw()
{
  background(255);
  drawcircles();
  collisions();
   
}
