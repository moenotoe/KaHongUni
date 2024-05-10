class Stick{
  int resting_distance = 40/s;
  int stick_length = 200/s;
  boolean isPressed = false;
  public float angle;
  float power;
  int stickState = 0;
  int startTime;
  boolean wasPressed = false;
  float lastPower = 0;
 boolean isPowerBarActive = false;
  int powerDirection = 1;
  

 void draw(int x, int y){
    update(x, y);
    if(whiteBall.ENLARGED > 0){
      resting_distance = 40/s + 66/s;
    }
    else{
      resting_distance = 40/s;
    }
    float powPercent = this.getPowerPercent()+20;
    float stickStartX = x + cos(angle) * (resting_distance + (isPressed ? powPercent*0.25 : 0));
    float stickStartY = y + sin(angle) * (resting_distance + (isPressed ? powPercent*0.25 : 0));
    float stickEndX = stickStartX + cos(angle) * stick_length;
    float stickEndY = stickStartY + sin(angle) * stick_length;
    
    float aimStartX = x - cos(angle) * (resting_distance + (isPressed ? powPercent*0.25 : 0));
    float aimStartY = y - sin(angle) * (resting_distance + (isPressed ? powPercent*0.25 : 0));
    float aimEndX, aimEndY;

    fill(30, 30, 30);
    //draw stick
    stroke(150,150,70);
    strokeWeight(16/s);
    line(stickStartX, stickStartY, stickEndX, stickEndY);
    //draw aimer line
    stroke(250,250,250);
    strokeWeight(8/s);
    for(int i=0; i<5; i++){
      aimEndX = aimStartX - cos(angle) * powPercent*0.25;
      aimEndY = aimStartY - sin(angle) * powPercent*0.25;
      line(aimStartX, aimStartY, aimEndX, aimEndY);
      aimStartX = aimEndX - cos(angle) * powPercent*0.55;
      aimStartY = aimEndY - sin(angle) * powPercent*0.55;
    }
    strokeWeight(1); 
   }
  void update(int x, int y) {
    float dx = mouseX - x;
    float dy = mouseY - y;
    angle = atan2(dy, dx);
    power = constrain(resting_distance - sqrt(dx * dx + dy * dy), 70,stick_length);
    if (isPressed && !wasPressed) {
      stickState = 1;
      startTime = millis();
    } else if (!isPressed && wasPressed) {
      stickState = 2;
      lastPower = getPowerMetric();
    }
    wasPressed = isPressed;
    updatePower();
  }
 
  void drawPowerBar(int x, int y, float percent) {
   if (!isPowerBarActive) {
    return;
  }
    float powerMetric = stickState == 1 ? getPowerMetric() : lastPower;
    float barWidth = 700/s;
    float barHeight = 20/s;
    fill(0);
    rect(x, y, barWidth, barHeight);
    color from = color(0, 255, 0);
    color to = color(255, 0, 0);
    color c = lerpColor(from, to, powerMetric / 100.0);
    fill(c);
    rect(x, y, barWidth * powerMetric / 100.0, barHeight);
  }

   float getPowerMetric() {
     float timeDiff = millis() - startTime;
     float powerMetric = map(timeDiff, 0, 1000, 0, 100);
     return constrain(powerMetric, 0, 100);
   }
   
   float getPowerPercent() {
  if (stickState == 1) {
    float timeDiff = millis() - startTime;
    float powerPercent = map(timeDiff, 0, 1000, 0, 100);
    return constrain(powerPercent, 0, 100);
  } else {
    return 0;
  }
}
 
 void updatePower() {
  if (isPowerBarActive) {
    power += powerDirection * 1.5;
    if (power > 100 || power < 0) {
      powerDirection *= -1;
    }
  }
}

 
 
 
}

 
 
 
 
 
