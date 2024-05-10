public class Textbox {

   public color textborder = color(30, 30, 30);
   public color front = color(0, 0,0);
   public color selectedbackgrd = color(0, 255, 255);
   public color backgrd = color(255, 255, 255);
   public int sizetext = 24;

   public int X = 0, Y = 0, H = 35, W = 200;
   private boolean textboxselected = false;
   
   public int lengthText = 0;
   public String Textinput = "";


   


   public Textbox (int x, int y, int w, int h) {
      this.X = x; 
      this.Y = y; 
      this.W = w; 
      this.H = h;
   }
   
   void drawtextbox() {

      if (textboxselected) {
         fill(selectedbackgrd);
      } else {
         fill(backgrd);
      }
     
      strokeWeight(1);
      stroke(textborder);
      rect(X,120 + Y, 50+W, H);
      
      textFont(roboto);
      fill(front);
      textSize(sizetext);
      text(Textinput,X + (textWidth("a") / 2), 120 + Y + sizetext);
   }
   
   
   private void textadd(char text) {

      if (textWidth(Textinput + text) < W) {
         Textinput += text;
         lengthText++;
      }
   }
   

   private boolean onbox(int x, int y) {
      if (x >= X && x <= X +( 50 +W)) {
         if (y >= 30 + Y && y <= Y +(120 + H)) {
            return true;
         }
      }
      
      return false;
   }
   
   void press(int x, int y) {
      if (onbox(x, y)) {
         textboxselected = true;
      } else {
         textboxselected = false;
      }
   }

   boolean keypress(char k, int keycode) {
      if (textboxselected) {
         if (keycode == (int) BACKSPACE) {
            bkspce();
         } else if (keycode == 32) {
            textadd(' ');
         } else if (keycode == (int)ENTER) {
            return true;
         } else {
            // CHECK IF THE KEY IS A LETTER OR A NUMBER
            boolean isKeyCapitalLetter = (k >= 'A' && k <= 'Z');
            boolean isKeySmallLetter = (k >= 'a' && k <= 'z');
            boolean isKeyNumber = (k >= '0' && k <= '9');
      
            if (isKeyCapitalLetter || isKeySmallLetter || isKeyNumber) {
               textadd(k);
            }
      }
      }
      
      return false;
   }

      private void bkspce() {
      if (lengthText - 1 >= 0) {
         Textinput = Textinput.substring(0, lengthText - 1);
         lengthText--;
      }
   }
   
}
