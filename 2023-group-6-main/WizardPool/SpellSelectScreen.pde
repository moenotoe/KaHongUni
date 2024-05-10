public class SpellSelectScreen{
  // 300 --- 1900
  // 49 -- 434 -- 100 -- 434 -- 100 - 434 -- 49
  // /s
  
  public int spell1, spell2, spell3;
  
  public void draw(){
    //NEW TURN SETUP PHASE
    if(newTurn){
      //reduce duration of all spells by 1
      spells.spellTick();
      
      //pick random spells for the selection
      int spellCount = 6;
      spell1 = round((float) Math.random()*spellCount+0.5);
      spell2 = round((float) Math.random()*(spellCount-1)+0.5);
      if(spell2 == spell1) spell2 = spellCount;
      spell3 = round((float) Math.random()*(spellCount-2)+0.5);
      if(spell3 == spell2){
        if(spell1 != (spellCount-1)) spell3 = spellCount-1;
        else spell3 = spellCount;
      }
      else if(spell3 == spell1){
        if(spell2 != spellCount) spell3 = spellCount;
        else spell3 = 4;
      }
      
      //switch player (unless current player has 2 turns and hasn't just potted the white)
      if(twoTurns && !(whiteBall.x > width)){
        twoTurns = false;
      }
      else{
        currentPlayer = currentPlayer%2 + 1;
      }
      
      if(whiteBall.x > width){
        whiteBall.setPos(600 / s, 700 / s);
        twoTurns = true;
      }
      newTurn = false;
    }
    
    fill(200,200,50);
    stroke(255,255,255);
    rect(300/s,50/s,width-600/s,300/s);
    
    textFont(roboto);
    
    fill(20,20,20);
    textSize(100/s);
    text(players.get(currentPlayer-1)+", select a spell!", 320/s, 135/s);
    
    textFont(sans);
    
    //fill(100,100,100);
    //rect(349, 160, 434, 170);
    //rect(883, 160, 434, 170);
    //rect(1417, 160, 434, 170);
    
    //fill(200,200,200);
    //textSize(50);
    //text("CREATE PORTALS", 349+35, 260);
    
    
    drawBox(349/s,spell1);
    drawBox(883/s,spell2);
    drawBox(1417/s,spell3);
    
  }
  
  
  
  void drawBox(int x, int spell){
    textSize(44/s);
    switch(spell){
      case 1:
        fill(220, 132, 0);
        rect(x, 160/s, 434/s, 170/s);
        fill(200,200,200);
        text("MOVING POCKETS", x+26/s, 260/s);
        break;
      case 2:
        fill(77, 0, 57);
        rect(x, 160/s, 434/s, 170/s);
        fill(200,200,200);
        text("FOG CLOUD", x+85/s, 260/s);
        break;
      case 3:
        fill(179, 0, 0);
        rect(x, 160/s, 434/s, 170/s);
        fill(200,200,200);
        text("ENLARGE BALL", x+56/s, 260/s);
        break;
      case 4:
        fill(0, 0, 153);
        rect(x, 160/s, 434/s, 170/s);
        fill(200,200,200);
        text("DISPEL MAGIC", x+67/s, 260/s);
        break;
      case 5:
        fill(0,179,179);
        rect(x, 160/s, 434/s, 170/s);
        fill(200,200,200);
        text("CREATE PORTALS", x+35/s, 260/s);
        break;
      case 6:
        fill(146, 24, 159);
        rect(x, 160/s, 434/s, 170/s);
        fill(200,200,200);
        text("EXPLOSIVE BALL", x+40/s, 260/s);
        break;
    }
  }
  
  public int getSpell1() { return spell1; }
 
  public void reset(){
    spell1 = 0;
    spell2 = 0;
    spell3 = 0;
  }  
}
