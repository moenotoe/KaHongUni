class Ball{
  public int size = 44/s;
  public int ENLARGED = 0;
  public int EXPLOSIVE = 0;
  // co-ords
  public int x;
  public int y;
  // direction of travel
  public float xVel;
  public float yVel;
  // -1 = unassigned, 0 = white
  public int owner;
  //colour
  public int[] col = {20,20,20};
  public int[] outline = {100, 200, 250 };
  public boolean isMoving = true;
  
  // constructor
  Ball(int xstart, int ystart){
    x = xstart;
    y = ystart;
    xVel=0; yVel=0; owner = -1;
  }
  
  void draw(){
    if(ENLARGED>0) size = 100/s;
    else size = 44/s;
      
    this.move();
    if(EXPLOSIVE > 0){
      if(owner == -1){
        fill(102,0,51);
      }
      if(owner == 0){
        fill(255,230,146);
      }
      if(owner == 1){
        fill(239,136,33);
      }
      if(owner == 2){
        fill(70,228,228);
      }
      
    }
    else{
      fill(col[0],col[1],col[2]);
    }
    stroke(outline[0],outline[1],outline[2]);
    ellipse(x,y,size,size);
  }
  
  
  void move(){
    if(abs(xVel) <= 0.5 && abs(yVel) <= 0.5){
      isMoving = false;
    }
    else{
      isMoving = true;
    }
      x += (int) round(xVel);
      y += (int) round(yVel);
      //apply friction
      xVel *= 0.99;
      yVel *= 0.99;
  }
  
  public void checkCollisions(int index){
    //bottom
    if(y + (int) round(yVel) + size/2 > 1086/s){
      yVel = -yVel;
    }
    //right
    if(x + (int) round(xVel) + size/2 > 1907/s){
      xVel = -xVel;
    }
    //top
    if(y + (int) round(yVel) - size/2 < 313/s){
      yVel = -yVel;
    }
    //left
    if(x + (int) round(xVel) - size/2 < 289/s){
      xVel = -xVel;
    }
    //balls
    Ball ball;
    for(int k=index; k<blist.size(); k++){
      ball = blist.get(k);
      int xDist = this.x - ball.x;
      int yDist = this.y - ball.y;
      int distance = xDist*xDist+yDist*yDist;
      if(distance <= (this.size/2+ball.size/2)*(this.size/2+ball.size/2)){
        double dotProduct = xDist*(ball.xVel - this.xVel) + yDist*(ball.yVel - this.yVel);
        // only have balls collide if they are moving towards each other (prevents balls 'sticking')
        if(dotProduct > 0){
          double magnitude = dotProduct/distance;
          if(this.EXPLOSIVE > 0 || ball.EXPLOSIVE > 0){
            magnitude=1.5+magnitude*0.55;
            this.EXPLOSIVE=0;
            ball.EXPLOSIVE=0;
          }
          double xChange = xDist * magnitude;
          double yChange = yDist * magnitude;
          this.xVel += xChange;
          this.yVel += yChange;
          ball.xVel -= xChange;
          ball.yVel -= yChange;
        }
      }
    }
  }
  
  
  // GET/SET FUNCS
  //void setVel(float newVel){ vel = newVel; }
  //float getVel(){ return vel; }
  void setVel(float x, float y) { xVel = x; yVel = y; }
  void setPos(int newx, int newy) { x = newx; y= newy; }
  int getX(){ return x; }
  int getY(){ return y; }
  
  void setCol(int r, int g, int b){
    col[0]=r; col[1]=g; col[2]=b;
    outline[0] = round(r*0.75);
    outline[1] = round(g*0.75);
    outline[2] = round(b*0.75);
  }
  
  void setOwner(int code){ owner = code; }
  
  void giveToPlayer1() {
    setOwner(1);
    setCol(200,10,30);
  }
  
  void giveToPlayer2() {
    setOwner(2);
    setCol(70,100,190);
  }
  
  
}

  
