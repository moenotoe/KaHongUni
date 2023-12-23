#include "bingrid.h"
#define BOARDSTR (MAX*MAX+1)


bool str2board(board* brd, char* str);
void board2str(char* str, board* brd);
bool solve_board(board* brd);
void printboard(board* brd);
void test(void);
void counting_rule(board* brd);
void OxO_rule(board* brd);
void pair_rule(board* brd);
#define LEN_INRULE (int)sqrt(strlen(str))

bool str2board(board* brd, char* str){
   //make sure the board is legal
   int str_sz=strlen(str); 
   
   if(((int)sqrt(str_sz))%2 != 0|| str_sz==0){    
      return false;
   }
   for(int i=0;i<str_sz;i++){  
      if(str[i]-ZERO!=1 && str[i]-ZERO!=0 && str[i] != '.'){ 
         return false;    
      }    
   }  
   //initialization the board
   int n=0;
   for(int j=0;j<LEN_INRULE;j++){
      for(int i=0;i<LEN_INRULE;i++){
         brd->b2d[j][i]=str[n++];
      }
   }
   brd->sz=LEN_INRULE;
   return true;
}

void board2str(char* str, board* brd){   
   int n=0;

   for(int j=0;j<brd->sz;j++){
      for(int i=0;i<brd->sz;i++){
         str[n++]=brd->b2d[j][i];
      }
   }
   str[n]=0;
   return;
}

bool solve_board(board* brd){ 
   char string_1[BOARDSTR];
   char string_2[BOARDSTR];

   // Record the old state of the board
   board2str(string_1,brd);

   // Apply rules
   pair_rule(brd);
   OxO_rule(brd);
   counting_rule(brd);

   // Record the new state of the board
   board2str(string_2,brd);

   if(strcmp(string_1,string_2)==0){
      for(int j=0;j<brd->sz;j++){
         for(int i=0;i<brd->sz;i++){
            if(brd->b2d[j][i]==UNK){
               return false;
            }
         }
      }
      return true; 
   }
   return solve_board(brd);
}

void printboard(board* brd){
   for(int j=0;j<brd->sz;j++){
      for(int i=0;i<brd->sz;i++){
         printf("%c",brd->b2d[j][i]);
      }
   }
   return;
}



void counting_rule(board* brd){
    int count_row_0, count_row_1, count_col_0, count_col_1;
    for(int row=0;row<brd->sz;row++){
        count_row_0=0;
        count_row_1=0;
        for(int j=0; j<brd->sz;j++){
            if(brd->b2d[row][j]==ZERO){
                count_row_0++;
            }
            else if(brd->b2d[row][j]==ONE){
                count_row_1++;
            }

        }


        if(count_row_0==brd->sz/2){
            for(int j=0;j<brd->sz;j++){
                if(brd->b2d[row][j]==UNK){
                    brd->b2d[row][j]=ONE;
                }

            }
        }
        else if(count_row_1==brd->sz/2){
            for(int j=0;j<brd->sz;j++){
                if(brd->b2d[row][j]==UNK){
                    brd->b2d[row][j]=ZERO;
                }
            }
        }
    }


    for(int col=0;col<brd->sz;col++){
        count_col_0=0;
        count_col_1=0;

        for(int i=0;i<brd->sz;i++){
            if(brd->b2d[i][col]==ZERO){
                count_col_0++;
            }
            else if(brd->b2d[i][col]==ONE){
                count_col_1++;
            }
        }

     
        if(count_col_0==brd->sz/2){
            for(int i=0;i<brd->sz;i++){
                if(brd->b2d[i][col]==UNK){
                    brd->b2d[i][col]=ONE;
                }
            }
        }
        else if(count_col_1==brd->sz/2){
            for(int i=0;i<brd->sz;i++){
                if(brd->b2d[i][col]==UNK){
                    brd->b2d[i][col]=ZERO;
                }
            }
        }
    }
}

void OxO_rule(board* brd){   
   int left, right;
   //Check row
   for(int i=0;i<brd->sz;i++){
      for(int j=1;j<brd->sz-1;j++){ 
         left=j-1;
         right=j+1;

         if(brd->b2d[i][j]==UNK){
            if(brd->b2d[i][left]==ONE&&brd->b2d[i][right]==ONE){
               brd->b2d[i][j]=ZERO;
            }
            else if(brd->b2d[i][left]==ZERO&&brd->b2d[i][right]==ZERO){
               brd->b2d[i][j]=ONE;
            }
         }
      }
   }
   
   //Check col
   for(int j=0;j<brd->sz;j++){
      for(int i=1;i<brd->sz-1;i++){ 
         left=i-1;
         right=i+1;

         if(brd->b2d[i][j]==UNK){
            if(brd->b2d[left][j]==ONE&&brd->b2d[right][j]==ONE){
               brd->b2d[i][j]=ZERO;
            }
            else if(brd->b2d[left][j]==ZERO&&brd->b2d[right][j]==ZERO){
               brd->b2d[i][j]=ONE;
            }
         }
      }
   }           
}


void pair_rule(board* brd){

    for(int j=0; j<brd->sz;j++){
        for(int i=0; i<brd->sz;i++){
     
            if(i<brd->sz-2){
                if(brd->b2d[j][i]==brd->b2d[j][i+1]){
         
                    if(brd->b2d[j][i]==ONE){
                        brd->b2d[j][i+2]=ZERO;

                        if(i>0){
                           brd->b2d[j][i-1]=ZERO;
                        }
                    } else if(brd->b2d[j][i]==ZERO){
                        brd->b2d[j][i+2]=ONE;

                        if(i>0){
                           brd->b2d[j][i-1]=ONE;
                        }
                    }
                }
            }
            if(j<brd->sz-2){
                if(brd->b2d[j][i]==brd->b2d[j+1][i]){
                 
                    if(brd->b2d[j][i]==ONE){
                        brd->b2d[j+2][i]=ZERO;

                        if(j>0){
                           brd->b2d[j-1][i]=ZERO;
                        }
                    } else if(brd->b2d[j][i]==ZERO){
                        brd->b2d[j+2][i]=ONE;

                        if(j>0){
                           brd->b2d[j-1][i]=ONE;
                        }
                    }
                }
            }
        }
    }
    return;
}


void test(void){
   board b;
   //test for solve_rule
   char test[BOARDSTR];
   
   //3*3 string-odd number is illegal
   assert(str2board(&b,"101010010")==false);
   //the string can only be 1,0 or '.'
   assert(str2board(&b,"80")==false);
   assert(str2board(&b,"")==false);
   assert(str2board(&b,"10.1")==true);
   printf("Test for str2board is:");
   printboard(&b);
   printf("\n");
   
   //when b.sz is 0, it make sense the test<"10.1"
   b.sz=0;
   board2str(test,&b);


   str2board(&b,"10.1");
   board2str(test,&b);
   assert(strcmp(test,"10.1")==0);

   //test each rule respectively
   str2board(&b,"11110000100011110");
   pair_rule(&b); 
   printf("Test for pair rule is:");
   printboard(&b);
   printf("\n");
   
   str2board(&b,"11.000.11.000.11");

   printf("Test for counting rule is:");
   printboard(&b);
   printf("\n");

   str2board(&b,"0001100010100101");
   OxO_rule(&b);
   printf("Test for OxO rule is:");
   printboard(&b);
   printf("\n");

   //test the detail with solve board

   //use example of 4*4 
   str2board(&b,"...1.0......1..1");
   pair_rule(&b); 
   printf("4x4 pair after pair rule: ");
   printboard(&b);
   printf("\n");

   OxO_rule(&b);
   printf("4x4 pair after oxo rule: ");
   printboard(&b);
   printf("\n");
   

   counting_rule(&b);
   printf("4x4 pair after counting rule: ");
   printboard(&b);
   printf("\n");

   pair_rule(&b); 
   printboard(&b);
   printf("\n");

   OxO_rule(&b);
   printboard(&b);
   printf("\n");
   
   counting_rule(&b);
   printboard(&b);
   printf("\n");

   board2str(test,&b);
   printf("the solution after use three rules:");
   printboard(&b);
   printf("\n");

   //answer of 4*4
   if(strcmp(test,"1010010100111100")!=0){
    solve_board(&b);
   }

   //test for solve board, then test 4*4 board
   assert(str2board(&b, "...1.0......1..1"));
   assert(solve_board(&b)==true);
   printf("The 4*4 answer is:");
   printboard(&b);
   printf("\n"); 

   str2board(&b,"1...1...0.....00...1................");
   printf("test \n");
   solve_board(&b);
   printboard(&b);
   printf("\n");


   printf("end test-------------------\n");

   return;
}
