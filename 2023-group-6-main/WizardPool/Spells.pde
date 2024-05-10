public class Spells{
  // 1) Moving Pockets
  // 2) Fog Cloud
  // 3) Enlarge Ball
  // 4) Dispel Magic
  // 5) Create Portals
  // 6) Explosive Ball
  public int spellToCast = 0;
  
  public int MOVINGPOCKETS = 0;
  public int FOG = 0, fogX, fogY, fogW, fogH;
  public int PORTAL = 0, entranceX, entranceY, exitX, exitY;
  //should place exit first?
  public boolean placingEntrance = false;
  
  
  
  
  
  public void draw(){
    //handle portals
    if(PORTAL > 0){
      noFill();
      strokeWeight(14/s);
      stroke(100,250,250);
      ellipse(entranceX, entranceY, 44/s, 44/s);
      stroke(100,250,200);
      strokeWeight(2);
      ellipse(exitX, exitY, 44/s, 44/s);
      strokeWeight(1);
      
      int transformX = exitX - entranceX;
      int transformY = exitY - entranceY;
      
      for(Ball ball : blist){
        if((ball.x-entranceX)*(ball.x-entranceX)+(ball.y-entranceY)*(ball.y-entranceY) < (44/s)*(44/s) && ball.ENLARGED == 0){
          ball.x += transformX;
          ball.y += transformY;
        }
      }
    }
    
    //place fog
    if(FOG > 0){
      fill(153, 0, 115);
      stroke(77, 0, 57);
      ellipse(fogX,fogY,fogW,fogH);
    }
    
  }
  
  
  public void createFog(int x, int y){
    fogX =x;
    fogY =y;
    fogW =500/s+(int) (Math.random()*200.0/s);
    fogH =500/s+(int) (Math.random()*200.0/s);
  }
  
  public void createPortal(int x, int y){
    if(placingEntrance){
      // must be on board
      if(x < (289+44)/s || x > (1907-44)/s || y < (313+44)/s || y > (1086-44)/s) return;
      // must not be overlapping the exit
      if( (x-exitX)*(x-exitX)+(y-exitY)*(y-exitY) < (44/s)*(44/s) ) return;
      entranceX = x;
      entranceY = y;
      placingEntrance = false;
      castingSpell = false;
    }
    else{
      if(x < (289+44)/s || x > (1907-44)/s || y < (313+44)/s || y > (1086-44)/s) return;
      exitX = x;
      exitY = y;
      placingEntrance = true;
    }
  }
  
  public void spellTick(){
    if(FOG != 0) FOG--;
    if(MOVINGPOCKETS != 0) MOVINGPOCKETS--;
    for(Ball ball : blist){
      if(ball.ENLARGED != 0) ball.ENLARGED--;
      if(ball.EXPLOSIVE != 0) ball.EXPLOSIVE--;
    }
    if(PORTAL != 0) PORTAL--;
    
  }
  
  public void cast(int spell){
    switch(spell){
      case 1:
        MOVINGPOCKETS+=2;
        castingSpell = false;
        break;
      case 2:
        spellToCast = 2;
        break;
      case 3:
        spellToCast = 3;
        break;
      case 4:
        dispelMagic();
        castingSpell = false;
        break;
      case 5:
        spellToCast = 5;
        PORTAL = 0;
        entranceX = 0; entranceY = 0; exitX = 0; exitY = 0;
        break;
      case 6:
        spellToCast = 6;
        break;
    }
  }
  
  public void resolveCasting(int x, int y){
    switch(spellToCast){
      case 2:
        createFog(x,y);
        FOG+=2;
        castingSpell = false;
        break;
      case 3:
        enlargeNearestBall(x,y);
        castingSpell = false;
        break;
      case 5:
        createPortal(x,y);
        PORTAL+=1;
        break;
      case 6:
        explodeNearestBall(x,y);
        castingSpell = false;
        break;
    }
  }
  
  public void enlargeNearestBall(int x, int y){
    Ball targetBall = null;
    int shortestDist = width*width+height*height;
    int dist;
    for(Ball ball : blist){
      dist = (ball.x-x)*(ball.x-x)+(ball.y-y)*(ball.y-y);
      if( dist < shortestDist ){
        targetBall = ball;
        shortestDist = dist;
      }
    }
    if(targetBall == null) return;
    
    targetBall.ENLARGED += 2;
    
  }
  
  public void explodeNearestBall(int x, int y){
    Ball targetBall = null;
    int shortestDist = width*width+height*height;
    int dist;
    for(Ball ball : blist){
      dist = (ball.x-x)*(ball.x-x)+(ball.y-y)*(ball.y-y);
      if( dist < shortestDist ){
        targetBall = ball;
        shortestDist = dist;
      }
    }
    if(targetBall == null) return;
    
    targetBall.EXPLOSIVE += 20;
    
  }
  
  public void dispelMagic(){
    MOVINGPOCKETS = 0;
    FOG = 0;
    for(Ball ball : blist){
      ball.ENLARGED = 0;
      ball.EXPLOSIVE = 0;
    }
    PORTAL = 0;
  }
  
  public void reset () {
    spellToCast = 0;
    fogX = 0;
    fogY = 0;
    fogW = 0;
    fogH = 0;
    entranceX = 0;
    entranceY = 0;
    exitX = 0;
    exitY = 0;
    placingEntrance = false;

    dispelMagic();
  }
  
 
}
