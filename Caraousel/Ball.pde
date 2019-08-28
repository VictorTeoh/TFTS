class Ball{
  color c;
  color visc;
  //cords
  float x;
  float y;
  //point
  float rotpntx;
  float rotpnty;
  float raddistance;
  //rotation
  float w;
  float dw; //speed of rotation
  float speed;
  float angle;
  PShape hitbox;
  float hitboxrad;
  PShape visbox;
  Ball(){
    
   float r = random(256);
   float g = random(256); 
   float b = random(256);
   c = color(r, g, b);
   x = random(width);
   y = random(height);
   w = 0;
   speed = 0;
   
  }
  
  void setcolor(int r, int g, int b){
    c = color(r, g, b);
  }
  
  void setviscolor(int r, int g, int b){
    visc = color(r, g, b);
  }
  
  void setposition(float xpos, float ypos){
    x = xpos;
    y = ypos;
  }
  
  void setrotpoint(float xpos, float ypos){
    rotpntx = xpos;
    rotpnty = ypos;
  }
  
  void sethbox(PShape s){
    hitbox = s; 
  }
  
  void setvisbox(PShape s){
    visbox = s; 
  }
  
  void setspeed(float newspeed){
    speed = newspeed;
  }
  
  void setw(float omega){
    visbox.rotate(omega);
    w = omega;
  }
  
  void setdw(float omega){    
    dw = omega;
  }
  
  void sethboxrad(float newhitboxrad){
     hitboxrad = newhitboxrad; 
  }
  
  void setraddistance(float newraddistance){
     raddistance = newraddistance; 
  }
    
  float getxpos(){
    return x;
  }
  
  float getypos(){
    return y; 
  }
  
  float gethboxrad(){
     return hitboxrad; 
  }
  
  void rotateabtpnt(float pntxpos, float pntypos){
    setposition(pntxpos + cos(w+dw)*raddistance, pntypos + sin(w+dw)*raddistance);
  }
  
  void drawme(){
    if(w != 0){
      rotateabtpnt(rotpntx, rotpnty);
      visbox.setFill(visc);
      shape(visbox, x, y);
    }
    else{ 
      visbox.setFill(visc);
      shape(visbox, x, y);
    }
    hitbox.setFill(c);
    shape(hitbox, x, y);
  }
    
  void move() {
    if( speed != 0){
     x += speed * cos(angle);
     y += speed * sin(angle);
    }
    w += dw;
  }
  
  void movetowards(float xpos, float ypos){
    float xdiff = xpos - x;
    float ydiff = ypos - y;
    float ang = atan2(ydiff, xdiff);
    angle = ang; 
  }
  
  float dist_to_chara(Ball other){
     return sqrt(  sq(other.getxpos() - x)  +  sq( other.getypos() - y ));
  }
  
  boolean collisioncheck(Ball other){
    if (this == other){ return false;}
    boolean statement = ((hitboxrad + other.gethboxrad())/2) >= dist_to_chara(other);
    if(statement){ this.setviscolor(255,255,0);}//check shit out shouldn't
    return(statement);
  }
}
