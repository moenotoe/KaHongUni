//NEW landing page stuff
import javax.swing.JOptionPane; //for displaying message
import javax.swing.*;
import java.awt.*;

public boolean gameStarted = false;

public boolean gameWon = false;
public boolean selectingSpell = true;
public boolean castingSpell = false;
public boolean newTurn = true;
public boolean twoTurns = false;
public boolean isLeaderboard=false;
// resizer
public static final int s = 2;

public PFont mono, roboto, sans;

public ArrayList<PImage> taggedImages = new ArrayList<PImage>();
PImage leftWizard, rightWizard;

//(start with player 2, it will immediately flip to player 1)
public int currentPlayer = 2;

Stick stick = new Stick();
PlayerIcons playerIcons = new PlayerIcons();
Spells spells = new Spells();
SpellSelectScreen spellSelectScreen = new SpellSelectScreen();
int numFireworks = 10;
Firework[] fireworks = new Firework[numFireworks];
LandingPage landingPage;

public ArrayList<String> players = new ArrayList<String>();


Ball whiteBall = new Ball(600/s,700/s);
Ball ball1 = new Ball(1295/s,700/s);
Ball ball2 = new Ball(1340/s,725/s);
Ball ball3 = new Ball(1340/s,675/s);
Ball ball4 = new Ball(1385/s,700/s);
Ball ball5 = new Ball(1385/s,750/s);
Ball ball6 = new Ball(1385/s,650/s);
Ball ball7 = new Ball(1430/s,725/s);
Ball ball8 = new Ball(1430/s,675/s);
Ball ball9 = new Ball(1430/s,625/s);
Ball ball10 = new Ball(1430/s,775/s);
Ball ball11 = new Ball(1475/s,700/s);
Ball ball12 = new Ball(1475/s,750/s);
Ball ball13 = new Ball(1475/s,800/s);
Ball ball14 = new Ball(1475/s,650/s);
Ball ball15 = new Ball(1475/s,600/s);

Pocket pocket1 = new Pocket(289/s,313/s);
Pocket pocket2 = new Pocket(1098/s,313/s);
Pocket pocket3 = new Pocket(1907/s,313/s);
Pocket pocket4 = new Pocket(289/s,1086/s);
Pocket pocket5 = new Pocket(1098/s,1086/s);
Pocket pocket6 = new Pocket(1907/s,1086/s);
public ArrayList<Ball> blist = new ArrayList<>();
public ArrayList<Pocket> plist = new ArrayList<>();

// runs once at start
void setup(){
  // set screen size (w,h)
  size(1100,650);
  background(250,250,250);

  // set fill colour
  fill(40,150,100);
  
  //set outline colour
  stroke(250,250,50);
  
  // Fonts
  mono = createFont("fontG.ttf", 128);
  roboto = createFont("textFont.ttf", 128);
  sans = createFont("LucidaSans.ttf", 128);
  
  landingPage = new LandingPage();
  
  blist.add(whiteBall);
  whiteBall.setCol(250,250,250);
  whiteBall.setOwner(0);
  
  blist.add(ball1);
  blist.add(ball2);
  blist.add(ball3);
  blist.add(ball4);
  blist.add(ball5);
  blist.add(ball6);
  blist.add(ball7);
  blist.add(ball8);
  blist.add(ball9);
  blist.add(ball10);
  blist.add(ball11);
  blist.add(ball12);
  blist.add(ball13);
  blist.add(ball14);
  blist.add(ball15);
  
  ball1.giveToPlayer1();
  ball2.giveToPlayer2();
  ball3.giveToPlayer1();
  //ball4 is black
  ball5.giveToPlayer1();
  ball6.giveToPlayer2();
  ball7.giveToPlayer1();
  ball8.giveToPlayer2();
  ball9.giveToPlayer1();
  ball10.giveToPlayer2();
  ball11.giveToPlayer1();
  ball12.giveToPlayer2();
  ball13.giveToPlayer1();
  ball14.giveToPlayer2();
  ball15.giveToPlayer2();
  
  plist.add(pocket1);
  plist.add(pocket2);
  plist.add(pocket3);
  plist.add(pocket4);
  plist.add(pocket5);
  plist.add(pocket6);
  for (int i = 0; i < numFireworks; i++) {
    fireworks[i] = new Firework();
  }
}


void draw(){
  
  if(!gameStarted){
    landingPage.draw();
    return;
  }
  
  if(isLeaderboard){
    displayBoard();
    return;
  }

  if(gameWon){
    fill(135, 206, 250);
    rect(0,0,width,height);

    for (int i = 0; i < numFireworks; i++) {
      fireworks[i].update();
      fireworks[i].show();
    }
    
   int leaderboardButtonX = 750;
   int leaderboardButtonY = 500;
   int leaderboardtButtonWidth = 300;
   int leaderboardButtonHeight = 120;
    fill(color(0,255,0));
    rect( leaderboardButtonX, leaderboardButtonY,  leaderboardtButtonWidth, leaderboardButtonHeight);
    fill(color(255,255,255));
    textSize(50);
    text("leaderboard",  leaderboardButtonX+8, leaderboardButtonY + leaderboardButtonHeight/2+10);  

    int restartButtonXinWon = 50;
    int restartButtonYinWon = 500;
    int restartButtonWidthinWon = 300;
    int restartButtonHeightinWon = 120;
    String buttonText="restart";

    fill(color(0,255,0));
    rect(restartButtonXinWon,restartButtonYinWon,  restartButtonWidthinWon,  restartButtonHeightinWon);
    fill(color(255,255,255));
    textSize(50);
    text(buttonText,  restartButtonXinWon+70, restartButtonYinWon + restartButtonHeightinWon/2+10);  
    
    int quitButtonX = 400;
    int quitButtonY = 500;
    int quitButtonWidth = 300;
    int quitButtonHeight = 120;
    fill(color(0,255,0));
    rect(quitButtonX,quitButtonY, quitButtonWidth,  quitButtonHeight);
    fill(color(255,255,255));
    textSize(50);
    text("quit", quitButtonX+100, quitButtonY + quitButtonHeight/2+10);  
     
    int winner = playerIcons.getWinner();
    if (winner == 1) {
      fill(200, 10, 30); 
      text(players.get(0)+" wins!", width / 2-150, height / 2 - 50);
    } else if (winner == 2) {
      fill(70, 100, 190); 
      text(players.get(1)+" wins!", width / 2-150, height / 2 - 50);
    }
    return;
  }
  
  stroke(100,100,100);
  fill(90,10,100);
  rect(0,0,width,height);
  fill(30,30,100);
  rect((261-100)/s,(288-100)/s,(1935-261+200)/s,(1111-288+200)/s);
  fill(40,180,60);
  rect(261/s,288/s,(1935-261)/s,(1111-288)/s);
  stroke(35,165,53);
  fill(50,200,70);
  rect(289/s,313/s,(1907-289)/s,(1086-313)/s);
  
  //draw pockets (also handles balls falling in)
  for(Pocket pocket : plist){
    pocket.draw();
  }
  //sees if any balls will collide and adjusts velocity accordingly
  for(int i=0; i<blist.size(); i++){
    blist.get(i).checkCollisions(i+1);
  }
  //draw balls (also handles balls moving)
  for(Ball ball : blist){
    ball.draw();
  }
  //draw any visible spells
  spells.draw();
  //draw cue stick if needed
  if(ballsAreStationary() && !selectingSpell && !castingSpell){
    stick.draw(whiteBall.x, whiteBall.y);
  }
  //draw current player and score
  playerIcons.draw();
  //draw spell select screen if needed
  if(selectingSpell && ballsAreStationary()){
    spellSelectScreen.draw();
  }
  //draw power bar if needed
  stick.isPressed = mousePressed;
    if (mousePressed && ballsAreStationary() && !selectingSpell && !castingSpell) {
    stick.isPowerBarActive = true;
  } else {
    stick.isPowerBarActive = false;
  }
  float powerPercent = stick.getPowerPercent();
  stick.drawPowerBar(200/s, 200/s, powerPercent);
  
  //draw restart button
  int restartButtonX1 =930;
  int restartButtonY1 = 600;
  int restartButtonWidth1 = 150;
  int restartButtonHeight1 = 60;
  String buttonText2="restart";
  fill(color(0,255,0));
  rect(restartButtonX1,restartButtonY1, restartButtonWidth1,  restartButtonHeight1);
  fill(color(255,255,255));
  textSize(30);
    
  text(buttonText2, restartButtonX1+30, restartButtonY1 + restartButtonHeight1/2+5);

  
}

void mouseClicked(){
   int restartButtonX = 50;
   int restartButtonY = 500;
   int restartButtonWidth = 300;
   int restartButtonHeight = 120;
    
    
   int restartButtonX1 =930;
   int restartButtonY1 = 600;
   int restartButtonWidth1 = 150;
   int restartButtonHeight1 = 60;
   
   int quitButtonX = 400;
   int quitButtonY = 500;
   int quitButtonWidth = 300;
   int quitButtonHeight = 120;
  
   int leaderboardButtonX = 750;
   int leaderboardButtonY = 500;
   int leaderboardtButtonWidth = 300;
   int leaderboardButtonHeight = 120;
   

   
   int quitInboardX=350;
   int quitInboardY=550;
   int quitInboardWidth = 300;
   int quitInboardHeight =70;
  
   if(gameWon&&isLeaderboard){

    if(mouseX >=quitInboardX && mouseX <= quitInboardX +  quitInboardWidth &&
        mouseY >= quitInboardY && mouseY <= quitInboardY + quitInboardHeight){
           exit();
           return;
        }
   
   }
  if (gameWon) {   
    if (mouseX >= restartButtonX && mouseX <= restartButtonX + restartButtonWidth &&
        mouseY >= restartButtonY && mouseY <= restartButtonY + restartButtonHeight) {
        resetGame();

    }
    if (mouseX >= quitButtonX && mouseX <= quitButtonX + quitButtonWidth &&
        mouseY >= quitButtonY && mouseY <= quitButtonY + quitButtonHeight) {
         exit();
    }
    if (mouseX >= leaderboardButtonX && mouseX <= leaderboardButtonX + leaderboardtButtonWidth &&
       mouseY >= leaderboardButtonY && mouseY <= leaderboardButtonY + leaderboardButtonHeight) {
       isLeaderboard=true;
       draw();
    }
    
   
    
  }
  
  
   if (mouseX >= restartButtonX1 && mouseX <= restartButtonX1 + restartButtonWidth1 &&
      mouseY >= restartButtonY1 && mouseY <= restartButtonY1 + restartButtonHeight1) {
      resetGame();     
   }
  
  if(selectingSpell && ballsAreStationary()){
    //box 1 clicked
    if(mouseX > 349/s && mouseX < (349+434)/s && mouseY > 160/s && mouseY < (160+170)/s){
      selectingSpell = false;
      castingSpell = true;
      spells.cast(spellSelectScreen.spell1);
    }
    // box 2 clicked
    if(mouseX > 883/s && mouseX < (883+434)/s && mouseY > 160/s && mouseY < (160+170)/s){
      selectingSpell = false;
      castingSpell = true;
      spells.cast(spellSelectScreen.spell2);
    }
    // box 3 clicked
    if(mouseX > 1417/s && mouseX < (1417+434)/s && mouseY > 160/s && mouseY < (160+170)/s){
      selectingSpell = false;
      castingSpell = true;
      spells.cast(spellSelectScreen.spell3);
    }
  
  }
  
  else if(castingSpell){
    spells.resolveCasting(mouseX,mouseY);
  }
}

void mouseReleased(){
  if(!ballsAreStationary()){
    return;
  }
  if(!selectingSpell && !castingSpell){
    //spells.spellTick();
    newTurn = true;
    selectingSpell = true;
    float powerPercent = stick.getPowerPercent();
    float modifier = 0.5/s;
    whiteBall.xVel = -cos(stick.angle)*powerPercent*modifier;
    whiteBall.yVel = -sin(stick.angle)*powerPercent*modifier;
  }
    stick.isPowerBarActive = false;
}

boolean ballsAreStationary(){
  for(Ball ball : blist){
    if(ball.isMoving == true){
      return false;
    }
  }
  return true;
}

//mouse action function
void mousePressed(){
  if(!gameStarted){
    landingPage.mousePressedFunction();
  }
}

void keyPressed(){
  if(!gameStarted){
    landingPage.keyPressedFunction();
  }
}

void displayBoard(){
  Leaderboard leaderboard = new Leaderboard();
  fill(135, 206, 250);
  rect(0, 0, width, height); 
  
  textSize(62); 
  fill(255, 255, 255); 
  textAlign(CENTER, CENTER);
  text("Leaderboard Top Five!", width / 2, 100);
  
  textSize(36);
  fill(255, 0, 0);
  textAlign(LEFT, CENTER);
  
  int yPos = 300;
  text("    Name" + "                  " + "total score", 250, 250);
  
  List<PlayerScore> sortedScores = new ArrayList<>(leaderboard.getScores());
  Collections.sort(sortedScores, new Comparator<PlayerScore>() {
    @Override
    public int compare(PlayerScore score1, PlayerScore score2) {
      return Integer.compare(score2.getScore(), score1.getScore());
    }
  });
  
  int topFiveCount = Math.min(5, sortedScores.size());
  float nameColumnWidth = 350; 

  for (int i = 0; i < topFiveCount; i++) {
    PlayerScore score = sortedScores.get(i);
    float nameWidth = textWidth(score.getName());
    float scoreWidth = textWidth(String.valueOf(score.getScore()));
    float spaceWidth = nameColumnWidth - nameWidth - scoreWidth;

    String spaces = "";
    while (textWidth(spaces) < spaceWidth) {
      spaces += " ";
    }
  
    fill(255, 255, 255);
    text(score.getName() + spaces + score.getScore(), 300, yPos);
    yPos += 50;
  }
  
   int quitInboardX=350;
   int quitInboardY=550;
   int quitInboardWidth = 300;
   int quitInboardHeight =70;
  
   fill(color(0,255,0));
   rect(quitInboardX,quitInboardY,quitInboardWidth,  quitInboardHeight);
   fill(color(255,255,255));
   textSize(50);
   text("quit", quitInboardX+90,  quitInboardY +quitInboardHeight/2+5); 

  
}

void resetGame() {
  isLeaderboard=false;
  gameWon = false;
  selectingSpell = true;
  castingSpell = false;
  newTurn = true;

  blist.clear();
  plist.clear();

  whiteBall = new Ball(600 / s, 700 / s);
  whiteBall.setCol(250, 250, 250);
  whiteBall.setOwner(0);
  
  ball1 = new Ball(1295 / s, 700 / s);
  ball2 = new Ball(1340 / s, 725 / s);
  ball3 = new Ball(1340 / s, 675 / s);
  ball4 = new Ball(1385 / s, 700 / s);
  ball5 = new Ball(1385 / s, 750 / s);
  ball6 = new Ball(1385 / s, 650 / s);
  ball7 = new Ball(1430 / s, 725 / s);
  ball8 = new Ball(1430 / s, 675 / s);
  ball9 = new Ball(1430 / s, 625 / s);
  ball10 = new Ball(1430 / s, 775 / s);
  ball11 = new Ball(1475 / s, 700 / s);
  ball12 = new Ball(1475 / s, 750 / s);
  ball13 = new Ball(1475 / s, 800 / s);
  ball14 = new Ball(1475 / s, 650 / s);
  ball15 = new Ball(1475 / s, 600 / s);

  ball1.giveToPlayer1();
  ball2.giveToPlayer2();
  ball3.giveToPlayer1();
  ball5.giveToPlayer1();
  ball6.giveToPlayer2();
  ball7.giveToPlayer1();
  ball8.giveToPlayer2();
  ball9.giveToPlayer1();
  ball10.giveToPlayer2();
  ball11.giveToPlayer1();
  ball12.giveToPlayer2();
  ball13.giveToPlayer1();
  ball14.giveToPlayer2();
  ball15.giveToPlayer2();

  blist.add(whiteBall);
  blist.add(ball1);
  blist.add(ball2);
  blist.add(ball3);
  blist.add(ball4);
  blist.add(ball5);
  blist.add(ball6);
  blist.add(ball7);
  blist.add(ball8);
  blist.add(ball9);
  blist.add(ball10);
  blist.add(ball11);
  blist.add(ball12);
  blist.add(ball13);
  blist.add(ball14);
  blist.add(ball15);

 
  pocket1 = new Pocket(289 / s, 313 / s);
  pocket2 = new Pocket(1098 / s, 313 / s);
  pocket3 = new Pocket(1907 / s, 313 / s);
  pocket4 = new Pocket(289 / s, 1086 / s);
  pocket5 = new Pocket(1098 / s, 1086 / s);
  pocket6 = new Pocket(1907 /s, 1086 / s);
  
 
  plist.add(pocket1);
  plist.add(pocket2);
  plist.add(pocket3);
  plist.add(pocket4);
  plist.add(pocket5);
  plist.add(pocket6);


  for (int i = 0; i < numFireworks; i++) {
    fireworks[i] = new Firework();
  }


  playerIcons.reset();
  spells.reset();
  spellSelectScreen.reset();
  currentPlayer = 2;
  twoTurns = false;
}
