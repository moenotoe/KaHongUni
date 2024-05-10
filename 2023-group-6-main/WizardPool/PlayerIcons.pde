public class PlayerIcons{
  Leaderboard board=new Leaderboard();
 
  int p1Points = 0;
  int p2Points = 0;
  
  public void draw() {
    //fill(255,255,255);
    //rect(10/s,10/s,170/s,250/s);
    //rect(width+(-10-170)/s,10/s,170/s,250/s);
    
    if (currentPlayer == 1) {
      tint(100);
      image(rightWizard,width+(-10-170)/s,10/s);
      tint(255);
      image(leftWizard,10/s,10/s);
    } else if (currentPlayer == 2) {
      tint(100);
      image(leftWizard,10/s,10/s);
      tint(255);
      image(rightWizard,width+(-10-170)/s,10/s);
    }
    
    
    stroke(60,60,60);
    
    int potted = p1Points;
    int gap = 100/s;
    int start = 240/s;
    fill(200,10,30);
    for(int i=0; i<7; i++){
      if(potted < i+1){
        fill(60,60,60);
      }
      ellipse(start+gap*i,60/s,80/s,80/s);
    }
    
    potted = p2Points;
    start = width + (- 10 - 170 - 60)/s;
    fill(70,100,190);
    for(int i=0; i<7; i++){
      if(potted < i+1){
        fill(60,60,60);
      }
      ellipse(start-gap*i,60/s,80/s,80/s);
    }
  
    if(p1Points == 8) gameWon = true;
    if(p2Points == 8) gameWon = true;
    
    displayTurn();
  }
  
  public void addPoint(int p){
   
  if(p == 1) {
    p1Points++;
    board.addScoreAndUpdate(players.get(0),1);
  }
  if(p == 2) {
    p2Points++;
    board.addScoreAndUpdate(players.get(1),1);
  }
  }
  
 public int getWinner() {
  if (p1Points == 8) {
    return 1;
  } else if (p2Points == 8) {
    return 2;
  } else {
    return 0;
  }
}
  
 public void reset() {
   p1Points = 0;
   p2Points = 0;
 }
 
 public void displayTurn() {
    noFill();
    strokeWeight(14);

    if (currentPlayer == 1) {
      stroke(255, 0, 0);
      rect(10 / s, 10 / s, 170 / s, 250 / s);
    } else if (currentPlayer == 2) {
      stroke(0, 0, 255);
      rect(width + (-10 - 170) / s, 10 / s, 170 / s, 250 / s);
    }

    strokeWeight(1);
  }
  
  
  
}
