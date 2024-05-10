public class LandingPage{
  
  //to keep track of which screen and transition between each screen
int currentScreen = 1;
float transitionProgress = 0.0;
float transitionProgress1 = 0.0;
int message1 = 0;
int spellCounter = 0;

//keep track of current player
int currentPlayer = 0;
ArrayList<String> taggedImageOwners = new ArrayList<String>();

//starting page - 
//to render landing page images
PImage[] gif;
int numberOfFrames;
int frames;
PImage bgScreen1, bgScreen2, bgScreen3;

//to render text boxes for players to enter their name
ArrayList<Textbox> textArray = new ArrayList<Textbox>();


//second page - 
//choose avatar
PImage img1, img2, img3, img4, img5;
int img1Height;
int message2 = 0, message3 =0;

//spells images 
PImage enlargeImg, portalImg, movingPImg, fogImg, powerball, nextBtn, DiloBox, nextCloud;

LandingPage() {
  
  //size(800, 600);


    //images to be loaded for the player to choose their avatar and backgrouds
    img1 = loadImage("wizard_one.jpg");
    img2 = loadImage("wizard_two.png");
    img3 = loadImage("wizard_3.jpg");
    img4 = loadImage("Vess.png");
    img5 = loadImage("playbutton.png");;
    bgScreen1 = loadImage("Nebula.jpeg");
    bgScreen1.resize(1100,650);
    bgScreen2 = loadImage("VS3.jpeg");
    bgScreen2.resize(1100,650);
    bgScreen3 = loadImage("Wizard.jpeg");
    bgScreen3.resize(1100,650);
    initLayout(); 
     size(1100,650);
     
     //spells images
     enlargeImg = loadImage("enlarge.png");
     portalImg = loadImage("pngegg.png");
     movingPImg = loadImage("moving.png");
     fogImg = loadImage("fog.png");
     powerball = loadImage("powerball.png");
     
     //Tutorial page images
     nextBtn = loadImage("nextnew.png");
     DiloBox = loadImage("DiloBox.png");
     nextCloud = loadImage("nextCloud.png");


}

//main draw funtion

void draw() {
  
        // Set the image as the background
switch(currentScreen){
    case 1:

    if(message1 == 0){

        String message = "Hello Wizards!\n\n Welcome to the wonderful world of wizard pool!\n Plese continue for a magical Wizzperience!";
        UIManager.put("OptionPane.okButtonText", "Continue");
        //JOptionPane.showMessageDialog(null, panel, message, JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, message);
        message1 = 1;
    }
    drawScreen1();
    break;
    
    case 2:

    transitionProgress += 0.01; // increase the progress by a small amount
     
      background(lerpColor(color(0), color(255), transitionProgress)); 
       background(bgScreen1);
  
    
    if(transitionProgress >= 0.5){  
        
        if(message2 == 0){
            String message = "Please choose your avatar before starting game";
            JOptionPane.showMessageDialog(null, message);
            message2 = 1;
        }

        drawScreen2();
    }

    break;
    
    case 3:
    
    transitionProgress1 += 0.01; // increase the progress by a small amount
    background(lerpColor(color(0), color(255), transitionProgress)); 
     background(bgScreen2);
    drawScreen3();
   
    break;
    
    case 4:
        transitionProgress += 0.01;
       background(lerpColor(color(0), color(255), transitionProgress)); 
       background(bgScreen3);
       
    if(transitionProgress >= 1.0){  
        if(message3 == 0){
            String message = "Please Continue for tutorial page.";
            JOptionPane.showMessageDialog(null, message);
            message3 = 1; 
        }
        drawScreen4();
    }
}
}

//player name input screen
void drawScreen1(){
    image(gif[frames], 0, 0, width, height);
    if(frameCount % 2 == 0){
        frames++;
    }

    if(frames == numberOfFrames){
        frames = 0;
    }


    for (Textbox t : textArray){
        t.drawtextbox();
    } 

    PImage button = loadImage("EnterButton.png"); 
    
    
    textFont(mono);
    textSize(32);
    //player 1 & player 2 text to display on screen
    fill(0, 51, 18);
    text("Wizard 1: ", 50,470);
    fill(0, 51, 18);
    text("Wizard 2: ", 280,470);
    image(button, 215, 555,100,50);
    

}

void initLayout (){

    numberOfFrames = 9;
    gif = new PImage[numberOfFrames];

    int i= 0;
    while(i < numberOfFrames){
        gif[i] = loadImage("frame_0"+i+"_delay-0.13s.gif");
        i++;
    }

    Textbox player1 = new Textbox(50, 380, 150, 40);
    textArray.add(player1);

    Textbox player2 = new Textbox(280, 380, 150, 40);
    textArray.add(player2);

} 

//mouse action function
void mousePressedFunction(){

  //difficultyButton.mousePressed();

if(currentScreen == 1){
    for(Textbox t : textArray){
        t.press(mouseX, mouseY);
    }
    

    if(isMouseOver(155,550, 150,100) == true){
      
          String message = "";
          if(textArray.get(0).Textinput.isEmpty() || textArray.get(1).Textinput.isEmpty()) {
            message = "Please provide the name of both wizards!";
            String title = "Error!";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
          }
          else if(textArray.get(0).Textinput.equals(textArray.get(1).Textinput) == true){
            message = "Wizard names can not be same.";
            String title = "Error!";
            textArray.get(0).Textinput = " ";
            textArray.get(1).Textinput = " ";
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
          } 
          else {
            players.add(textArray.get(0).Textinput);
            players.add(textArray.get(1).Textinput);
            
            message = "Welcome " + players.get(0) +" and " + players.get(1); 
            JOptionPane.showMessageDialog(null, message);
            currentScreen = 2;
            return;
        }
    }
}


if(currentScreen == 2){

    if(isMouseOver(100, 100, img1.width, img1.height)){
        if(!taggedImages.contains(img1)){
            selectionAvatar(img1);
        } else{
            String title = "Selection error";
            String message = "Avatar has been already chosen by "+ taggedImageOwners.get(0);
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    if(isMouseOver(300, 100, img2.width, img2.height)){
        if(!taggedImages.contains(img2)){
            selectionAvatar(img2);
        } else
        {
            String title = "Selection error";
            String message = "Avatar has been already chosen by "+ taggedImageOwners.get(0);
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    if(isMouseOver(550, 100, img3.width, img3.height)){
        if(!taggedImages.contains(img3)){
            selectionAvatar(img3);
        } else{
            String title = "Selection error";
            String message = "Avatar has been already chosen by "+ taggedImageOwners.get(0);
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
    if(isMouseOver(850, 100, img4.width, img4.height)){
        if(!taggedImages.contains(img4)){
            selectionAvatar(img4);
        } else{
            String title = "Selection error";
            String message = "Avatar has been already chosen by "+ taggedImageOwners.get(0);
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
 if(currentScreen == 3){ 
   if (mouseX >= 490 && mouseX <= 490 + img5.width && mouseY >= 565 && mouseY <=565 + img5.height) {
    // Handle the click event
    currentScreen = currentScreen +1;
  }
 }
 
 if(currentScreen == 4) {
      if (mouseX >= 600 && mouseX <= 600 + nextCloud.width && mouseY >= 250 && mouseY <=250 + nextCloud.height)
      {
    // Handle the click event
    spellCounter +=1;
    }
}
if(currentScreen == 4 && spellCounter > 5){
    if (mouseX >= 450 && mouseX <= 450 + nextCloud.width && mouseY >= 450 && mouseY <=450 + nextCloud.height) 
    {
    // GAME START
    gameStarted = true;
    
    //setup images for main game
    leftWizard = taggedImages.get(0);
    rightWizard = taggedImages.get(1);
    leftWizard.resize(170/s, 250/s);
    rightWizard.resize(170/s, 250/s);
    
    
    spellCounter +=1;
    }
}
}
void keyPressedFunction(){
    for(Textbox t : textArray){
        t.keypress(key, keyCode);
    }
}  


boolean isMouseOver(int x, int y, int w, int h){
if(mouseX >= x && mouseX<=x + w && mouseY >= y & mouseY <= y + h){
    return true;
}
return false;
}

//choosing player Icon screen
void drawScreen2() {
  
String currentPlayer = "";
  if(taggedImages.size() == 0){
    currentPlayer = textArray.get(0).Textinput;
  } else{
    currentPlayer = textArray.get(1).Textinput;
  }      
      textFont(roboto, 30);
      fill(255,255,255);
      text("Select avatar for wizard: " + currentPlayer, 60, 80);
    // resize width and height of the images/avatars
    img1.resize(200, 285);
    img2.resize(200, 0);
    img3.resize(200, 0);
    img4.resize(200,280);

    // Check if the mouse is hovering over each image
    // img1
    if (mouseX >= 50 && mouseX <= 50 + img1.width && mouseY >= 100 && mouseY <= 100 + img1.height) {
        if(!taggedImages.contains(img1)){
            noTint();
             fill(255,255,255);
             textSize(16);
             text("Robolox \n Mage of mystic land known for \n his kind and humble nature, but \n one should not mistake his \n kindness for weakness, He is one \n of the most powerful mages \n to exist in Mystic land to date.", 50, 420);
        }else{
            noTint();
             fill(255,255,255);
             textSize(16);
             text("You've selected: Robolox \n \n Mage of mystic land known for \n his kind and humble nature, but \n one should not mistake his \n kindness for weakness, He is one \n of the most powerful mages \n to exist in Mystic land to date.", 50, 420);
        }
    } else {
        if(taggedImages.contains(img1)){
            noTint();   
            fill(255,255,255);
             textSize(16);
             text("You've selected: \n Mage of mystic land known for \n his kind and humble nature, but \n one should not mistake his \n kindness for weakness, He is one \n of the most powerful mages \n to exist in Mystic land to date.", 50, 420);
        }else{
            tint(255, 255, 255, 100); // set transparency to be slightly opaque (200/255) for img1
            // fill(255,255,255);
             textSize(30);
             text("Robolox :- ", 50, 450);
        }
    }
    
    image(img1, 50, 100);
    
    // img2
    if (mouseX >= 300 && mouseX <= 300 + img2.width && mouseY >= 100 && mouseY <= 100 + img2.height) {
        if(!taggedImages.contains(img2)){
            noTint();
            fill(255,255,255);
             textSize(16);
             text("Deathlotto \n Mage of Crimson land known for \n his destructive and chaotic nature. \n He is ruthless, doesn't play by \n the rules and only desires power. He \n is one of the most fearsome mages \n to exist in Crimson land.", 300, 420);
        }else{
            noTint();
            fill(255,255,255);
             textSize(16);
             text("You've selected: Deathlotto \n Mage of Crimson land known for \n his destructive and chaotic nature. \n He is ruthless, doesn't play by \n the rules and only desires power. He \n is one of the most fearsome mages \n to exist in Crimson land.", 300, 420);
        }
    } else {
        if(taggedImages.contains(img2)){
            noTint();  
            fill(255,255,255);
             textSize(16);
             text("You've selected: Deathlotto \n Mage of Crimson land known for \n his destructive and chaotic nature. \n He is ruthless, doesn't play by the \n rules and only desires power. He \n is one of the most fearsome mages \n to exist in Crimson land.", 300, 420);
        }else{
            tint(255, 255, 255, 100);
            textSize(30);
             text("Deathlotto :- ", 300, 450);// set transparency to be slightly opaque (200/255) for img2
        }    
    }

    image(img2, 300, 100);


    // img3
    if (mouseX >= 550 && mouseX <= 550 + img3.width && mouseY >= 100 && mouseY <= 100 + img3.height) {
        if(!taggedImages.contains(img3)){
            noTint();
            fill(255,255,255);
             textSize(16);
             text("Viper \n Mage of Toxicverse, a land known \n for its toxicity and gruesome \n environment. Surviving there is a \n struggle itself, and Viper is \n known as king of those lands \n being one of the most \n powerful mages with powers \n unknown to mankind.", 550, 420);
        }else{
            noTint();
            fill(255,255,255);
             textSize(16);
             text("You've selected: Viper \n Mage of Toxicverse, a land known \n for its toxicity and gruesome \n environment. Surviving there is a \n struggle itself, and Viper is \n known as king of those lands \n being one of the most \n powerful mages with powers \n unknown to mankind.", 550, 420);
        }
    } else {
        if(taggedImages.contains(img3)){
            noTint();  
            fill(255,255,255);
             textSize(16);
             text("You've selected: Viper \n Mage of Toxicverse, a land known \n for its toxicity and gruesome \n environment. Surviving there is a \n struggle itself, and Viper is \n known as king of those lands \n being one of the most \n powerful mages with powers \n unknown to mankind.", 550, 420);
        }else{
            tint(255, 255, 255, 100);
            textSize(30);
            text("Viper :- ", 550, 450);// set transparency to be slightly opaque (200/255) for img3
        }   
      }

    image(img3, 550, 100);
    
     if (mouseX >= 800 && mouseX <= 800 + img3.width && mouseY >= 100 && mouseY <= 100 + img4.height) {
        if(!taggedImages.contains(img4)){
            noTint();
            fill(255,255,255);
             textSize(16);
             text("Vanessa \n Queen of mystic land and daughter \n of Great Robolox, unlike \n her father she choose a \n dark power and mastered the art of \n dark sorcery and manupilation. \n There's nothing she cant win \n making her a very desirable \n queen for dark forces.", 800, 420);
        }else{
            noTint();
            textSize(16);
             text("You've selected: Vanessa \n Queen of mystic land and daughter \n of Great Robolox, unlike \n her father she choose a \n dark power and mastered the art of \n dark sorcery and manupilation. \n There's nothing she cant win \n making her a very desirable \n queen for dark forces.", 800, 420);
        }
    } else {
        if(taggedImages.contains(img4)){
            noTint(); 
             textSize(16);
             text("You've selected: Vanessa \n Queen of mystic land and daughter \n of Great Robolox, unlike \n her father she choose a \n dark power and mastered the art of \n dark sorcery and manupilation. \n There's nothing she cant win \n making her a very desirable \n queen for dark forces.", 800, 420);
        }else{
            tint(255, 255, 255, 100);
            textSize(30);
            text("Vanessa :- ", 800, 450);// set transparency to be slightly opaque (200/255) for img4
        }  
      }

    image(img4, 800, 100);

}
//method to select avatar by users
void selectionAvatar (PImage img){

    //check if the img selected has already been selected by another user 
    if(!taggedImages.contains(img)){
        
        taggedImages.add(img);
        taggedImageOwners.add(players.get(currentPlayer));

        if(currentPlayer != 1){
            currentPlayer++;
        }else{
            currentPlayer = 0;
            currentScreen = 3;
        }
    }
}



//screen 3 should show selected avatar page (or) Verses Screen
void drawScreen3 (){
    int h = 140;
   for(int i = 0; i < taggedImages.size(); i++){
    tint(255,255,255,200);
    taggedImages.get(i).resize(270,370);
    image(taggedImages.get(i), h, 110);
     h += 550;
    }
    textFont(mono);
    textSize(55);
    fill(242, 150, 209);
    text(textArray.get(0).Textinput, 220, 525);
    fill(202, 207, 255);
    text(textArray.get(1).Textinput, 780, 525);
    img5.resize(125,100);
    noTint();
    image(img5, 490, 550);
    
}
//tutorial page
void drawScreen4(){
  
  // Spells images
  String spellDescription = "Hello Wizzzzards!\n"
                           +"The only rules of pool you need to worry about are\n"
                           +"don't pot the white or you opponent gets 2 turns, \n"
                           +"and don't pot the black until you have potted all\n"
                           +"of your balls!";

 int dBoxHeight = 0, dBoxWidth = 0, dBoxX = 0, dBoxY = 0, textX = 0, textY = 0, textSize = 25;
if(spellCounter == 0) {
  //spellDescription = "Hello Wizzzzards!, a small tutorial \n of this whole whizzardry before \n we begin, shall we? Press Next to continue..";
  dBoxHeight = 650;
  dBoxWidth = 720;
  dBoxX = 240;
  dBoxY = -150;
  textX= 400;
  textY = 75;
  textSize = 18;
  
   nextCloud.resize(150,150);  //cloud diaouge
   image(nextCloud, 600,250);
   textFont(mono); //next
   textSize(40);
   text("Next",645,335);
}

//1st spell
if(spellCounter == 1) {
    enlargeImg.resize(150,150);
    image(enlargeImg, 460, 450);
    
    spellDescription = "Enlarge Ball:\n"
                      +"Click any ball to enlarge it! Enlarge the white to\n"
                      +"make it easier to hit other balls, or englarge your\n"
                      +"opponents balls to make it harder for them to score!\n";

  dBoxHeight = 600;
  dBoxWidth = 740;
  dBoxX = 240;
  dBoxY = -100;
  textX= 400;
  textY = 115;
  textSize = 18;
  
   nextCloud.resize(150,150);  //cloud next
   image(nextCloud, 600,250);
   textFont(mono); //next
   textSize(40);
   text("Next",645,335);
}
else { 
   enlargeImg.resize(100,100);
  image(enlargeImg, 970, 40);
}
  
//2nd spell
  if(spellCounter == 2){
    portalImg.resize(150,150);
    image(portalImg, 480, 450);
    spellDescription = "Create Portals:\n"
                      +"Click to create an EXIT portal, then again to create\n"
                      +"an ENTRANCE portal on the table. All non-enlarged\n"
                      +"balls will teleport through the entrance to exit!\n";
                      
    dBoxHeight = 600;
  dBoxWidth = 740;
  dBoxX = 240;
  dBoxY = -100;
    textX= 400;
    textY = 115;
    textSize = 18;
    
     nextCloud.resize(150,150);  //cloud next
   image(nextCloud, 600,250);
   textFont(mono); //next
   textSize(40);
   text("Next",645,335);
}
  else {
    portalImg.resize(100,100);
   image(portalImg, 970,150);
  }
  
  //3rd spell
if(spellCounter == 3){
    movingPImg.resize(150,150);
    image(movingPImg, 450, 450);
   spellDescription = "Moving Pockets:\n"
                      +"This spell causes the pockets on the table to\n"
                      +"revolve clockwise! Useful for scooping up balls\n"
                      +"near the edge of the table.\n";
  dBoxHeight = 600;
  dBoxWidth = 740;
  dBoxX = 240;
  dBoxY = -100;
    textX= 400;
    textY = 115;
    textSize = 18;
    
     nextCloud.resize(150,150);  //cloud next
   image(nextCloud, 600,250);
   textFont(mono); //next
   textSize(40);
   text("Next",645,335);
}
  else {
    movingPImg.resize(100,100);
    image(movingPImg, 970,300);
  }
  
  //4th spell
  
if(spellCounter == 4){
    spellDescription = "Fog:\n"
                      +"Click to obscure a large section of the table!\n"
                      +"Try to obscure your opponents balls from view.\n";
                      
    fogImg.resize(250,250);
    image(fogImg, 500, 370);
    dBoxHeight = 600;
  dBoxWidth = 740;
  dBoxX = 240;
  dBoxY = -100;
    textX= 400;
    textY = 115;
    textSize = 18;
    
     nextCloud.resize(150,150);  //cloud next
   image(nextCloud, 600,250);
   textFont(mono); //next
   textSize(40);
   text("Next",645,335);
 }
  else {
    fogImg.resize(100,100);
    image(fogImg, 970,400);
  }
  
  
  //5th spell
  if(spellCounter == 5){
    spellDescription = "Explosive ball:\n"
                      +"select a ball to provide it with explosive powers.\n"
                      +"Hitting this ball will acclerate speed hit to other balls.\n";
                      
    powerball.resize(250,250);
    image(powerball, 500, 370);
    dBoxHeight = 600;
  dBoxWidth = 740;
  dBoxX = 240;
  dBoxY = -100;
    textX= 400;
    textY = 115;
    textSize = 18;
    
     nextCloud.resize(150,150);  //cloud next
   image(nextCloud, 600,250);
   textFont(mono); //next
   textSize(40);
   text("Next",645,335);
 }
  else {
    powerball.resize(100,100);
    image(powerball, 970,500);
  }
  
  //Good luck box
  if(spellCounter > 5){
    spellDescription="Spells last until your next turn. Have Fun!";
    dBoxHeight = 300;
    dBoxWidth = 700;
    dBoxX = 300;
    dBoxY = 0;
    textX= 420;
    textY = 140;
    textSize = 25;
    
    nextCloud.resize(200,200);  //cloud play
    textFont(mono);
    image(nextCloud, 450,410);
    textSize(50);
    text("Play", 520,520);
  }
  

   //Cloud dilaoge box
  DiloBox.resize(dBoxWidth,dBoxHeight);
  image(DiloBox,dBoxX,dBoxY);

  // text description 
    textFont(roboto);  // cloud text
    textSize(textSize);
    fill(0, 0, 0);
    text(spellDescription, textX, textY);
}
  
  
  
  
  
  
}
