class Pocket {
  public int size = 120/s;
  public int x;
  public int y;
  
  public final int left = 289/s;
  public final int right = 1907/s;
  public final int top = 313/s;
  public final int bottom = 1086/s;
  
  private final int initialX;
  private final int initialY;
  
  // constructor
  Pocket(int xstart, int ystart){
    x = xstart;
    y = ystart;
    initialX = xstart;
    initialY = ystart;
  }
  
  
  public void pocketBalls(){
    Ball ball;
    int distSq;
    for(int i=0; i<blist.size(); i++){
      ball = blist.get(i);
      //if(!(ball.owner < 1)){
        distSq = (ball.x - this.x)*(ball.x - this.x)+(ball.y - this.y)*(ball.y - this.y);
        if(distSq <= (this.size/2)*(this.size/2)){
          
          //pot white
          if(ball.owner == 0){
            whiteBall.setPos(width+100,height+100);
            whiteBall.setVel(0, 0);
          }
          //pot black
          else if(ball.owner == -1){
            if(currentPlayer == 1){
              if(playerIcons.p1Points == 7){
                playerIcons.addPoint(1);
                return;
              }
              playerIcons.p2Points = 8;
            }
            else{
              if(playerIcons.p2Points == 7){
                playerIcons.addPoint(2);
                return;
              }
              playerIcons.p1Points = 8;
            }
            return;
          }
          //pot others
          else{
            playerIcons.addPoint(ball.owner);
            blist.remove(ball);
            i--;
          }
        }
      //}
    }
  }
  
  void draw(){
    pocketBalls();
    movePocket();
    fill(10,10,10);
    stroke(250,250,50);
    ellipse(x,y,size,size);
  }
  
  void movePocket(){
    int speed = 2/s;
    if(spells.MOVINGPOCKETS < 1 || ballsAreStationary()) return;
    // up
    if(x <= left && y > top) y-=speed;
    // left
    if(y <= top && x < right) x+=speed;
    // down
    if(x >= right && y < bottom) y+=speed;
    // right
    if(y >= bottom && x > left) x-=speed;
  }
  
  
 public void reset() {
   x = initialX;
   y = initialY;
 }
  
}
