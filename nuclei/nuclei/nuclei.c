#include "nuclei.h"

int main(int argc, char* argv[]){
   
   read_file(argc,argv);
   #ifdef INTERP
      test_interp();
   #else
      test_parse();
   #endif

   return 0;
}

#ifdef INTERP

//extension part
#ifdef EXTEN
lisp* int_MOD_inter(lisp* list1, lisp* list2){
   int val1=lisp_getval(list1);
   int val2=lisp_getval(list2);
   int ans=val1%val2;
   return lisp_atom(ans);
}

lisp* int_FABS_inter(lisp* list1){
   int val1=lisp_getval(list1);
   double ans=fabs((double)val1);
   int result=(int)ans;
   return lisp_atom(result);   
}

lisp* int_DIV_inter(lisp* list1, lisp* list2){
   int val1=lisp_getval(list1);
   int val2=lisp_getval(list2);

   int ans=val1/val2;
   return lisp_atom(ans);
}

lisp* int_POW_inter(lisp* list1, lisp* list2){
   int val1=lisp_getval(list1);
   int val2=lisp_getval(list2);

   double ans=pow((double)val1, (double)val2 );
   int result=(int)ans;

   return lisp_atom(result);
}

lisp* int_SQRT_inter(lisp* list1){
   int valori=lisp_getval(list1);
   //let val change to double
   double val=sqrt((double)valori);
   //to int 
   int result=(int)val;

   return lisp_atom(result);
}

lisp* int_MUL_inter(lisp* list1, lisp* list2){
   int mul=lisp_getval(list1)*lisp_getval(list2);
   return lisp_atom(mul);
}
#endif

void IF_judge(Program* p, lisp* list){
   if(judge_func(list)==true){
      judge_if_interp(p);
      next_INSTRCTS(p);
   }else{
      INSTRCTS(p);
      increase_word(p);
      increase_word(p);
   }
   return;
}

void IF_true(Program* p,lisp* list){
   if(judge_func(list)==true){
      INSTRCTS(p);
   }else{
      //find last")"in true statment-->(jump the true statment)
      increase_word(p);
      increase_word(p);
      next_statment(p);          
   }
   return;
}

void LOOP_instrcts(Program* p, lisp* list){
   if(judge_func(list)==true){
      INSTRCTS(p);
      search_while(p);
      LOOP(p);
   }else if((judge_func(list)==false)){
      //jump loop->+3 not end:exit
      if(jumploop_within(p)==false){
         exit(EXIT_SUCCESS);
      }else{
         jump_loop(p);
         INSTRCTS(p);
      }
   }
   return;
}

void judge_print_end(Program* p){
   int a=PRINTEND;
   while(a>0){
      increase_word(p);
      a--;
   }
   a=PRINTEND;
   if(strsame(getwds(p),"")){
      exit(EXIT_SUCCESS);
   }else{
      while(a>0){
         decrease_word(p);
         a--;
      }
   }   
   return;   
}

void judge_end(Program* p){
   increase_word(p);
   if(strsame(getwds(p),"")){
      exit(EXIT_SUCCESS);
   }else{
      decrease_word(p);
   }   
   return;
}

void judge_if_interp(Program* p){
   increase_word(p);
   if(!strsame(getwds(p),"PRINT")){
      exit(EXIT_FAILURE);
   }else{
      decrease_word(p);
   }
   return;
}

void next_INSTRCTS(Program* p){
   int a=NEXTINSTRCTS;
   while(a>0){
      increase_word(p);
      a--;
   }
   return;
}
void next_statment(Program* p){
   while(!strsame(getwds(p),"PRINT")){
      increase_word(p);
   }
   if(strsame(getwds(p),"PRINT")){
      int a=BACKSTATSTART;
      while(a>0){
         decrease_word(p);
         a--;
      } 
   }
   return;
}

bool jumploop_within(Program* p){
   jump_loop(p);
   int a=JUMPEND;
   while(a>0){
      increase_word(p);
      a--;
   }
   if(!strsame(getwds(p),"")){
      return false;
   }else{
      a=JUMPEND;
      while(a>0){
         decrease_word(p);
         a--;
      }
   }
   return true;
}

void jump_loop(Program* p){
   while(!strsame(getwds(p),"PRINT")){
      increase_word(p);
   }
   if(strsame(getwds(p),"PRINT")){
      decrease_word(p);
      return;
   }
}
void search_while(Program* p){
   while(!strsame(getwds(p),"WHILE")){
      decrease_word(p);
   }
   return;
}


void print_general(lisp* list){
   char tmp[STRLEN];
   lisp_tostring(list,tmp);
   fprintf(stdout,"%s\n",tmp);
   return;
}

bool judge_func(lisp* list){
   char str[STRLEN];
   str[0]='\0';
   lisp_tostring(list,str);

   if(strsame(str,"1")){
      return true;
   }
   if(strsame(str,"0")){
      return false;
   }
   return false;
}

lisp* int_LENG_inter(lisp* list){

   //find len
   int len=lisp_length(list);
   //back to lisp
   return lisp_atom(len);
}

lisp* int_PLUS_inter(lisp* list1, lisp* list2){
   //to add 
   int add=lisp_getval(list1)+lisp_getval(list2);
   //back to lisp
   return lisp_atom(add);
}


lisp* bool_inter(char* type,lisp* list1,lisp* list2 ){
   if(strsame(type,"EQUAL")&&lisp_getval(list1)==lisp_getval(list2)){
      return lisp_atom(1);
   }
   if(strsame(type,"LESS")&&lisp_getval(list1)<lisp_getval(list2)){  
      return lisp_atom(1);
   }
   if(strsame(type,"GREATER")&&lisp_getval(list1)>lisp_getval(list2)){
      return lisp_atom(1);
   }
   if(strsame(type,"GRAEQU")&&lisp_getval(list1)>=lisp_getval(list2)){
      return lisp_atom(1);
   }
   if(strsame(type,"LESEQU")&&lisp_getval(list1)<=lisp_getval(list2)){
      return lisp_atom(1);
   }
   if(strsame(type,"NOTEQU")&&lisp_getval(list1)!=lisp_getval(list2)){
      return lisp_atom(1);
   }      
   return lisp_atom(0);
}

lisp* listvar_inter(char var,Program* p){
//in the list, if is <VAR> and defined->
//take it and make it as lisp 
   if(is_defined_var(var, p->table)){
      ERROR("Not defined variable");
   }
   variable* tolisp=var_fromarray(var, p->table);
   return tolisp->value;
}
//<var> is defined? if defined, get the variable and the lisp 
//and print the lisp
void Varprint_inter(char var, Program* p){
   if(is_defined_var(var,p->table)==false){
      variable* var_comple=var_fromarray(var,p->table);
      char string[STRLEN];
      string[0]='\0';
      lisp_tostring(var_comple->value,string);
      fprintf(stdout,"%s\n",string);
   }
}

//fromstring to atom
lisp* math_only(const char* str){
   //str->constant-->to lisp
   int num=atoi(str);

   return lisp_atom(num);
}

void set_inter(char var, lisp* list1, Program* p){
   //ALLOW REDEFINED
   variable* var_comple=var_toarray(var,list1);
   add_totable(var_comple, p->table);
   return;
}

void add_totable(variable* v, varArray* arr){
   arr->var_arr[arr->num_var++]=v;
   return;
}

//let var and it's lisp to the program structure(defined the var)
variable* var_toarray(char var, lisp* value){
   variable* completemake=(variable*)ncalloc(1,sizeof(variable));
   completemake->name=var;
   completemake->value=value;
   
   return completemake;
}

//get the var  and it's lisp from p->table into the variable
variable* var_fromarray(char var, varArray* arr){

   variable* getvar=(variable*)ncalloc(1,sizeof(variable));
   for(int i=0;i<arr->num_var;i++){
      if(arr->var_arr[i]->name==var){
         getvar->name=arr->var_arr[i]->name;
         getvar->value=arr->var_arr[i]->value;
      }
   }
   return getvar;
}

bool is_defined_var(char var, varArray* arr){
   for(int i=0;i<arr->num_var;i++){
      if(arr->var_arr[i]->name==var){
         return false;
      }
   }
   return true;
}

variable* init_variable(){
   variable* var=(variable*)ncalloc(1,sizeof(variable));
   return var;
}

varArray* init_varArray(){
   varArray* array=(varArray*)ncalloc(1,sizeof(varArray));
   return array;
}
#endif

bool is_token(char* str){
   if(strsame(str,"GRAEQU")||strsame(str,"LESEQU")){
      return true;
   }
   if(strsame(str,"NOTEQU")){
      return true;
   }      
   if(strsame(str,"MUL")||strsame(str,"SQRT")){
      return true;
   }
   if(strsame(str,"POW")||strsame(str,"DIV")){
      return true;
   }   
   if(strsame(str,"FABS")||strsame(str,"MOD")){
      return true;
   }
   if(strsame(str,"PLUS")||strsame(str,"LESS")){
      return true;
   }
   if(strsame(str,"CONS")||strsame(str,"CDR")||strsame(str,"CAR")){
      return true;
   }
   if(strsame(str,"SET")||strsame(str,"NIL")){
      return true;
   }
   if(strsame(str,"PRINT")||strsame(str,"WHILE")){
      return true;
   }
   return false;
}

void FUNC(Program* p){
   if(strsame(getwds(p),"WHILE")){
      LOOP(p);
   }else if(strsame(getwds(p),"IF")){
      IF(p);
   }else if(is_IOFUNC(p)){
      IOFUNC(p);
   }else if(is_RETFUNC(p)){
      RETFUNC(p);
   }else{
      ERROR("Not a FUNC");
   }
}

bool is_RETFUNC(Program* p){
   if(is_LISTFUNC(p)){
      return true;
   }
   if(is_INTFUNC(p)){
      return true;
   }
   if(is_BOOLFUNC(p)){
      return true;
   }
   return false;
}

bool is_BOOLFUNC(Program* p){
   if(strsame(getwds(p),"LESS")){
      return true;
   }
   if(strsame(getwds(p),"GREATER")){
      return true;
   }
   if(strsame(getwds(p),"EQUAL")){
      return true;
   }
   return false;
}

bool is_INTFUNC(Program* p){
   if(strsame(getwds(p),"PLUS")||strsame(getwds(p),"LENGTH")){
      return true;
   }
   if(strsame(getwds(p),"MUL")||strsame(getwds(p),"SQRT")){
      return true;
   }
   if(strsame(getwds(p),"POW")||strsame(getwds(p),"DIV")){
      return true;
   }
   if(strsame(getwds(p),"FABS")||strsame(getwds(p),"MOD")){
      return true;
   }
   return false;
}

//return lisp
lisp* RETFUNC(Program* p){
   lisp* list=NULL;
   if(is_LISTFUNC(p)){
      list=LISTFUNC(p);
   }else if(is_INTFUNC(p)){
      list=INTFUNC(p);
   }else if(is_BOOLFUNC(p)){
      list=BOOLFUNC(p);
   }else{
      ERROR("Not a RETFUNC");
   }
   return list;
}

lisp* INTF_plus(Program* p){
   lisp* plus=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef INTERP
      plus=int_PLUS_inter(list1,list2);
      return plus;
   #else
      list1=NULL;
      list2=NULL;
      plus=NULL;
   #endif
   return plus;
}

lisp* INTF_length(Program* p){
   lisp* length=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   #ifdef INTERP
      length=int_LENG_inter(list1);
      return length;
   #else
      list1=NULL;
      length=NULL;
   #endif
   return length;
}

lisp* INTF_mul(Program* p){
   lisp* mul=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef EXTEN
      mul=int_MUL_inter(list1,list2);
      return mul;
   #else
      list1=NULL;
      list2=NULL;
      mul=NULL;
   #endif
   return mul;
}

lisp* INTF_sqrt(Program* p){
   lisp* sqrt=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   #ifdef EXTEN
      sqrt=int_SQRT_inter(list1);
      return sqrt;
   #else
      list1=NULL;
      sqrt=NULL;
   #endif
   return sqrt;
}

lisp* INTF_pow(Program* p){
   lisp* pow=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef EXTEN
      pow=int_POW_inter(list1,list2);
      return pow;
   #else
      list1=NULL;
      list2=NULL;
      pow=NULL;
   #endif
   return pow;
}

lisp* INTF_div(Program* p){
   lisp* div=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef EXTEN
      div=int_DIV_inter(list1,list2);
      return div;
   #else
      list1=NULL;
      list2=NULL;
      div=NULL;
   #endif 
   return div; 
}

lisp* INTF_fabs(Program* p){
   lisp* fabs=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   #ifdef EXTEN
      fabs=int_FABS_inter(list1);
      return fabs;
   #else
      list1=NULL;
      fabs=NULL;
   #endif   
   return fabs;
}

lisp* INTF_mod(Program* p){
   lisp* mod=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef EXTEN
      mod=int_MOD_inter(list1,list2);
      return mod;
   #else
      list1=NULL;
      list2=NULL;
      mod=NULL;
   #endif      
   return mod; 
}

lisp* INTFUNC(Program* p){
   lisp* intlist=NULL;
   if(strsame(getwds(p),"PLUS")){
      intlist=INTF_plus(p);
   }else if(strsame(getwds(p),"LENGTH")){
      intlist=INTF_length(p);
   }else if(strsame(getwds(p),"MUL")){
      intlist=INTF_mul(p);
   }else if(strsame(getwds(p),"SQRT")){
      intlist=INTF_sqrt(p);
   }else if(strsame(getwds(p),"POW")){
      intlist=INTF_pow(p);
   }else if(strsame(getwds(p),"DIV")){
      intlist=INTF_div(p);
   }else if(strsame(getwds(p),"FABS")){
      intlist=INTF_fabs(p);
   }else if (strsame(getwds(p),"MOD")){
      intlist=INTF_mod(p);
   }else{
      ERROR("Not an INTFUNC");
   }
   return intlist;
}

void IF_left_brack(Program* p){
   if(!strsame(getwds(p),"(")){
      ERROR("No '(' within IF");
   }
   return;
}

void IF_right_brack(Program* p){
   if(!strsame(getwds(p),")")){
      ERROR("No ')' within IF");
   }
   return;   
}

void is_IF(Program* p){
   if(!strsame(getwds(p),"IF")){
      ERROR("Not 'IF'");
   }
   return;
}

void IF(Program* p){
   is_IF(p);
   increase_word(p);
   IF_left_brack(p);
   increase_word(p);
   lisp* list=BOOLFUNC(p);
   increase_word(p);
   IF_right_brack(p);
   increase_word(p);
   IF_left_brack(p);
   increase_word(p);
   #ifdef INTERP
      IF_true(p, list);
   #else
      list=NULL;
      INSTRCTS(p);
   #endif
   increase_word(p);
   IF_left_brack(p);
   increase_word(p);
   #ifdef INTERP
      IF_judge(p,list);
   #endif
   //parse only
   #ifdef INTEP
   #else
      list=NULL;
      INSTRCTS(p);      
   #endif
}

void PRINT_var(Program* p){
   char var=VAR(p);
   #ifdef INTERP
      Varprint_inter(var,p);
   #else
      var=' ';
   #endif
   return;   
}

void PRINT_str(Program* p){
   char* str=STRING(p);
   #ifdef INTERP
      fprintf(stdout,"%s\n",str); 
   #else
      str[0]='\0';
   #endif
   return;
}

void PRINT_list(Program* p){
   increase_word(p);
   if(is_LISTFUNC(p)){
      lisp* list=LISTFUNC(p);
      #ifdef INTERP
         print_general(list);
      #else
         list=NULL;
      #endif
   }else if(is_INTFUNC(p)){
      PRINT_int(p);
   }else if(is_BOOLFUNC(p)){
      lisp* list=BOOLFUNC(p);
      #ifdef INTERP
         print_general(list);
      #else
         list=NULL;
      #endif      
   }
   
}

void PRINT_int(Program* p){
//PRINT_int
   lisp* list=INTFUNC(p);
   #ifdef INTERP
      print_general(list);
   #else
      list=NULL;
   #endif   
}


void PRINT_iter(Program* p){
   lisp* list=LITERAL(p);
   #ifdef INTERP
      print_general(list);      
   #else
      list=NULL;
   #endif
}

void PRINT(Program* p){
   if(!strsame(getwds(p),"PRINT")){
      ERROR("Not a PRINT!");
   }
   increase_word(p);
   if(strsame(getwds(p),"\"")){
      PRINT_str(p);
   }else if(is_VAR(p)){
      PRINT_var(p);
   }else if(strsame(getwds(p),"(")){
      PRINT_list(p);
   }else if(strsame(getwds(p),"'")){
      PRINT_iter(p);
   }else{
      LIST(p);
   }
   #ifdef INTERP
      judge_print_end(p);
   #endif
   return;
}

void SET(Program* p){
   lisp* list=NULL;
   if(!strsame(getwds(p),"SET")){
      ERROR("Not SET!");
   }
   increase_word(p);
   char var=VAR(p);
   increase_word(p);
   list=LIST(p);

   #ifdef INTERP
      set_inter(var,list,p);
   #else
   //no else-->will show "unued flag"
      var=' ';
      list=NULL;
   #endif
   return;
}

//if boolfunc=1, return loop itself
void LOOP(Program* p){
   if(!strsame(getwds(p),"WHILE")){
      ERROR("Not a WHILE within the LOOP");
   }
   increase_word(p);
   if(!strsame(getwds(p),"(")){
      ERROR("Not a '(' within the LOOP");
   }
   increase_word(p);
   lisp* list=BOOLFUNC(p);
   increase_word(p);
   if(!strsame(getwds(p),")")){
      ERROR("Not a ')' within the LOOP");
   }
   increase_word(p);
   if(!strsame(getwds(p),"(")){
      ERROR("Not a '(' within the LOOP");
   }
   increase_word(p);
   //if bool is true, return instr
   #ifdef INTERP
      LOOP_instrcts(p,list);
   #else
      list=NULL;
      INSTRCTS(p);
   #endif  
   return;
}

lisp* BOOL_equal(Program* p){
   lisp* equal=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef INTERP
      equal=bool_inter("EQUAL",list1,list2);
   #else
      list1=NULL;
      list2=NULL;
      equal=NULL;
   #endif
   return equal;   
}

lisp* BOOL_greater(Program* p){
   lisp* greater=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef INTERP
      greater=bool_inter("GREATER",list1,list2);
   #else
      list1=NULL;
      list2=NULL;
      greater=NULL;
   #endif
   return greater;
}

lisp* BOOL_less(Program* p){
   lisp* less=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef INTERP
      less=bool_inter("LESS",list1,list2);
   #else
      list1=NULL;
      list2=NULL;
      less=NULL;
   #endif     
   return less;
}

lisp* BOOL_graequ(Program* p){
   lisp* graequ=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef EXTEN
      graequ=bool_inter("GRAEQU",list1,list2);
   #else
      list1=NULL;
      list2=NULL;
      graequ=NULL;
   #endif     
   return graequ;   
}

lisp* BOOL_lessequ(Program* p){
   lisp* lessequ=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef EXTEN
      lessequ=bool_inter("LESEQU",list1,list2);
   #else
      list1=NULL;
      list2=NULL;
      lessequ=NULL;
   #endif     
   return lessequ;      
}

lisp* BOOL_notequ(Program* p){
   lisp* notequ=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef EXTEN
      notequ=bool_inter("NOTEQU",list1,list2);
   #else
      list1=NULL;
      list2=NULL;
      notequ=NULL;
   #endif     
   return notequ;         
}

lisp* BOOLFUNC(Program* p){
   lisp* boollisp=NULL;
   if(strsame(getwds(p),"EQUAL")){
      boollisp=BOOL_equal(p);
   }else if(strsame(getwds(p),"GREATER")){
      boollisp=BOOL_greater(p);
   }else if(strsame(getwds(p),"LESS")){
      boollisp=BOOL_less(p);
   }else if(strsame(getwds(p),"GRAEQU")){
      boollisp=BOOL_graequ(p);
   }else if(strsame(getwds(p),"LESEQU")){
      boollisp=BOOL_lessequ(p);
   }else if(strsame(getwds(p),"NOTEQU")){
      boollisp=BOOL_notequ(p);
   }else{
      ERROR("Not a BOOLFUN");
   }
   return boollisp;
}

char* STRING(Program* p){
   char* string=(char*)ncalloc(STRLEN,sizeof(char));
   if(!strsame(getwds(p),"\"")){
      ERROR("Not a STRING!");
   }
   increase_word(p);
   
   strncpy(string,getwds(p),MAXTOKENSIZE);

   increase_word(p);
   if(!strsame(getwds(p),"\"")){
      ERROR("Not a STRING!");
   }
   return string;
}

void PROG(Program* p){
   if(strsame(getwds(p),"(")){
      increase_word(p);
      INSTRCTS(p);
   }else{
      ERROR("No'(' in PROG!");
   }
   return;
}
void INSTRCTS(Program* p){
   if(strsame(getwds(p),")")){
      #ifdef INTERP
         judge_end(p);
      #endif
      return;
   }
   INSTRCT(p);
   increase_word(p);
   INSTRCTS(p);
}

bool is_IOFUNC(Program* p){
   if(strsame(getwds(p),"SET")){
      return true;
   }
   if(strsame(getwds(p),"PRINT")){
      return true;
   }
   return false;
}

bool is_LISTFUNC(Program* p){
   if(strsame(getwds(p),"CAR")){
      return true;
   }
   if(strsame(getwds(p),"CDR")){
      return true;
   }
   if(strsame(getwds(p),"CONS")){
      return true;
   }
   return false;
}

void INSTRCT(Program* p){
   if(!strsame(getwds(p),"(")){
      ERROR("No '(' in INSTRCT");
   }
   increase_word(p);
   FUNC(p);
   increase_word(p);
   if(!strsame(getwds(p),")")){
      ERROR("NO ')' in INSTRCT")
   }
   return;
}

void IOFUNC(Program* p){
   if(!is_IOFUNC(p)){
      ERROR("Not an IO");
   }
   if(strsame(getwds(p),"SET")){
      SET(p);
   }
   if(strsame(getwds(p),"PRINT")){
      PRINT(p);
   }
   return;
}

lisp* LISTF_cons(Program* p){
   lisp* cons=NULL;
   increase_word(p);
   lisp* list1=LIST(p);  
   increase_word(p);
   lisp* list2=LIST(p);
   #ifdef INTERP
      cons=lisp_cons(list1,list2);
      return cons;
   #else
      list1=NULL;
      list2=NULL;
      cons=NULL;
   #endif
   return cons;
}

lisp* LISTF_cdr(Program* p){
   lisp* cdr=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   #ifdef INTERP
      cdr=lisp_cdr(list1);
      return cdr;
   #else
      list1=NULL;
      cdr=NULL;
   #endif  
   return cdr;
}

lisp* LISTF_car(Program* p){
   lisp* car=NULL;
   increase_word(p);
   lisp* list1=LIST(p);
   #ifdef INTERP
      car=lisp_car(list1);
      return car;
   #else
      list1=NULL;
      car=NULL;
   #endif 
   return car;
}

lisp* LISTFUNC(Program* p){
   lisp* list=NULL;
   if(strsame(getwds(p),"CONS")){
      list=LISTF_cons(p);
   }else if(strsame(getwds(p),"CDR")){
      list=LISTF_cdr(p);
   }else if(strsame(getwds(p),"CAR")){
      list=LISTF_car(p);
   }else{
      ERROR("Not a LISTFUNC");
   }
   return list;
}

lisp* LIST(Program* p){
   lisp* list=NULL;
   if(strsame(getwds(p),"(")){
      increase_word(p);
      list=RETFUNC(p);
      increase_word(p);
      if(!strsame(getwds(p),")")){
         ERROR("NO ')' in LIST")
      }
   }else if(strsame(getwds(p),"NIL")){
      return NULL;
   }else if(strsame(getwds(p),"'")){
      list=LITERAL(p);
   }else{
      char var=VAR(p);
      #ifdef INTERP
         list=listvar_inter(var,p);
      #else
      var=' ';
      #endif
   }
   return list;
}

lisp* LITERAL(Program *p){
   lisp* list=NULL;
   if(!(strsame(getwds(p),"'"))){
      ERROR("LITERAL mistake(')");
   }
   increase_word(p);
   #ifdef INTERP
      int len=strlen(getwds(p));
      if(getwds(p)[0]=='(' && getwds(p)[len-1]!=')'){
         ERROR("interpred worng:illegal literal");
      }
   #endif
   
   if(is_LITERAL_math(p)){
      #ifdef INTERP
         list=math_only(getwds(p));
      #endif
   }
   else{
      #ifdef INTERP
         list=lisp_fromstring(getwds(p));
      #endif
   }
   increase_word(p);
   if(!(strsame(getwds(p),"'"))){
      ERROR("LITERAL mistake(')");
   }   
   return list;
}
bool is_LITERAL_math(Program* p){
   if((getwds(p)[0]>='0'&&getwds(p)[0]<='9')||getwds(p)[0]=='-'){
      return true;
   }
   return false;
}

void increase_word(Program* p){
   p->cw++;
   return;
}

void decrease_word(Program* p){
   p->cw--;
   return;
}


int get_cw(Program* p){
   return p->cw;
}

bool is_VAR(Program* p){
   //only one letter!!!
   if(isupper(getwds(p)[0]) && getwds(p)[1]=='\0'){
      return true;
   }
   return false;
}

char VAR(Program* p){
   if(!is_VAR(p)){
      ERROR("VAR mistake");
   }
   return getwds(p)[0]; 
}

char* getwds(Program* p){
   return p->wds[p->cw];
}

char* read_spec_token(Program* p, int wd){
   p->cw=wd;
   return p->wds[p->cw];
}

void print_var(Program* p){
   for(int i=0;i<p->cw;i++){
      printf("%c\n",p->wds[p->cw][0]);
   }
}

void checkArgument(int argc, char* argv[]){
   if(argc!=2){
      fprintf(stderr, "Usage : %s <filename>\n", argv[0]);
      exit(EXIT_FAILURE);
   }
}


void read_argv(char* argv[]){
   FILE* fp=nfopen(argv[1],"r");
   Program* prog=ncalloc(1,sizeof(Program));
   #ifdef INTERP
      prog->table=init_varArray();
   #endif
   char ch, tmp[STRLEN], tokenbox[STRLEN];
   tmp[0]='\0', tokenbox[0]='\0';
   int litindex=0, douindex=0, tokindex=0;
   while((ch=fgetc(fp))!=EOF){
      if(ch=='#'){
         while(fgetc(fp)!='\n');
         ch='\0';
      }
      if(ch!='\n'){         
         strncpy(tmp,&ch,ONECHAR);
         read_var(&douindex,&litindex,&tokindex,prog,tmp,tokenbox);
         read_token(&douindex,&litindex,&tokindex,prog,tmp,tokenbox);
         read_brackets(&douindex,&litindex,prog,tmp);
         read_literal(&douindex,&litindex,prog,tmp);
         read_string(&douindex,prog,tmp); 
      } 
   }
   read_last(&ch,tmp,prog);
   prog->cw=0;
   PROG(prog); 
   free(prog);
   prog=NULL;
   fclose(fp);
}

void read_last(char* ch, char* tmp, Program* prog){
   if((*ch)!= EOF){
      strcpy(tmp,ch);
      strncat(getwds(prog),tmp,ONECHAR);
      strcpy(tmp,"");
   }   
}

void read_var(int* douindex, int* litindex,int* tokindex, Program* prog, char* tmp, char* box){
 //read var
   if(tmp[0]>='A'&&tmp[0]<='Z'&&(*litindex)==0&&(*douindex)==0&&(*tokindex)==0){
      //strncat(getwds(prog),tmp,ONECHAR);
      strncat(box,tmp,ONECHAR);
      (*tokindex)++;
      strcpy(tmp,"");
   }
   if((*litindex)==0&&(*douindex)==0&&(*tokindex)==1){
      if(tmp[0]==' '||tmp[0]==')'){
         strcpy(getwds(prog),box);
         strcpy(box, "");
         increase_word(prog);
            if(tmp[0]==')'){
               strncpy(getwds(prog),tmp,ONECHAR);
               increase_word(prog);
            }
            (*tokindex)=0;
            strcpy(tmp,"");
      }
   }
}

void read_token(int* douindex, int* litindex,int* tokindex, Program* prog, char* tmp,char* box){
   
   if((*litindex)==0&&(*douindex)==0&&(*tokindex)==1){
      strncat(box,tmp,ONECHAR);
      if(is_token(box)){
         strcpy(getwds(prog),box);
         strcpy(box,"");
         increase_word(prog);
         (*tokindex)=0;
         strcpy(tmp,"");
      }
   }
}
// ka hong chiang
void read_brackets(int* douindex, int* litindex, Program* prog, char* tmp){
//"("and")" OK
   if(tmp[0]=='('&&(*litindex)==0&&(*douindex)==0){
      strncpy(getwds(prog),tmp,ONECHAR);
      increase_word(prog);
      strcpy(tmp,"");         
   }
   if(tmp[0]==')' && (*litindex)==0 && (*douindex)==0){
      strncpy(getwds(prog),tmp,ONECHAR);
      increase_word(prog);
      strcpy(tmp,"");
   }
}
void read_literal(int* douindex, int* litindex, Program* prog, char* tmp){
   if(tmp[0]=='\''&& (*litindex)==0&&(*douindex)==0){
      strncpy(getwds(prog),tmp,ONECHAR);
      (*litindex)++;
      increase_word(prog);
      strcpy(tmp,"");
   }
   if((*litindex)==1&&(tmp[0]!='\''&&(*douindex)==0)){
      strncat(getwds(prog),tmp,ONECHAR);
      strcpy(tmp,"");
   }
   if((*litindex)==1&&tmp[0]=='\''&&(*douindex)==0){
      increase_word(prog);
      strncat(getwds(prog),tmp,ONECHAR);
      strcpy(tmp,"");
      increase_word(prog);
      (*litindex)=0;
   }
}

void read_string(int* douindex, Program* prog, char* tmp){
   //for ""
   if(tmp[0]=='"'&& (*douindex)==0){
      strncpy(getwds(prog),tmp,ONECHAR);
      (*douindex)++;
      increase_word(prog);
      strcpy(tmp,"");
   }

   if(((*douindex)==1 && tmp[0]!='"') ){
      strncat(getwds(prog),tmp,ONECHAR);
      strcpy(tmp,"");
   }

   if(((*douindex)==1 && tmp[0]=='"') ){
      increase_word(prog);
      strncat(getwds(prog),tmp,ONECHAR);
      strcpy(tmp,"");
      increase_word(prog);
      (*douindex)=0;
   }
}

void read_file(int argc, char* argv[]){
//read file, estimate first and final line
   checkArgument(argc, argv);
   read_argv(argv);
   #ifdef INTERP
      exit(EXIT_SUCCESS);
   #else
      fprintf(stdout,"%s\n","Parsed OK");
      exit(EXIT_SUCCESS);
   #endif
}

#ifdef INTERP
void test_interp(void){
   //test make_var and add to list
   printf("start interp test\n-----------------------\n");
   Program p24={
      .wds={"(","(","SET" ,"A" ,"'","1","'",")",")"},
      .cw=0,
      .table=init_varArray(),
   };
   

   variable* maketest;
   variable* maketest2;
   lisp* l_make = cons(atom(2), NIL);
   maketest=var_toarray('A',l_make);
   maketest2=var_toarray('B',l_make);
   add_totable(maketest, p24.table);
   add_totable(maketest2, p24.table);
   
   assert(strsame(&p24.table->var_arr[0]->name,"A"));
   assert(strsame(&p24.table->var_arr[1]->name,"B"));
   assert(p24.table->num_var==2);

//is defined?
   assert(is_defined_var('A',p24.table)==false);
   assert(is_defined_var('B',p24.table)==false);
   assert(is_defined_var('C',p24.table)==true);

   lisp_free(&l_make);
   free(maketest);
   free(maketest2);
   free(p24.table);

//test print string
   Program stringtest={
      .wds={"PRINT","\"", "Hello world" ,"\""},
      .cw=0,
      .table=init_varArray(),
}; 
   //PRINT(&stringtest);
   free(stringtest.table);

//test literal(addional situation)
   Program p25={
      .wds={"'","-2","'"},
      .cw=0,
      .table=init_varArray(),
   }; 
   
   //p25.cw=1;
   //assert(is_LITERAL_math(&p25)==true); OK
   char seestr[STRLEN];
   seestr[0]='\0';
   lisp* liter=LITERAL(&p25); 
   lisp_tostring(liter,seestr);
   assert(strsame(seestr,"-2"));
   free(p25.table);

//test basic print
   Program p26={
      .wds={"(","(","SET","A","'","1","'",")"
      ,"(","PRINT","A",")",")"},
      .cw=0,
      .table=init_varArray(),
   }; 
   //PROG(&p26);
   read_spec_token(&p26,2);
   SET(&p26);
   //test the value of A
   lisp* listvar_intertest=listvar_inter('A',&p26);
   lisp_tostring(listvar_intertest,seestr);
   assert(strsame(seestr,"1"));
   free(p26.table);


//test boolean inter
   char boolstr[STRLEN];
   boolstr[0]='\0';
    Program bootest={
      .wds={"SET","B","'","2","'",
      "SET","C","'","3","'"
      ,"EQUAL","B","C"},
      .cw=0,
      .table=init_varArray(),
   }; 
   SET(&bootest);
   bootest.cw++;
   SET(&bootest);
   bootest.cw++;
   lisp* booltostr=BOOLFUNC(&bootest);
   lisp_tostring(booltostr,boolstr);
   assert(strsame(boolstr,"0")==true);
   lisp* bootestvalb=listvar_inter('B',&bootest);
   lisp_tostring(bootestvalb,seestr);
   assert(strsame(seestr,"2"));
   lisp* booltestvalc=listvar_inter('C',&bootest);
   lisp_tostring(booltestvalc,seestr);
   assert(strsame(seestr,"3"));
   free(bootest.table);
   printf("the bool--------------------\n");

//finished basic print and the assertion
   Program p27={
      .wds={"(","(","SET","A","'","1","'",")",
      "(","PRINT","A",")","(","SET",
      "B","'","2","'",")",
      "(","PRINT","B",")",
      "(","SET","C","B",")",
      "(","PRINT","C",")", 
      "(","SET","D",
      "(","LESS","B","C",")",")",
      "(","PRINT","D",")",
      "(","SET","E","(","PLUS","B",
      "'","5","'",")",")",
      "(","PRINT","E",")",
      "(","SET","F","'","(2 3)","'",")",
      "(","PRINT","F",")",
      "(","SET","G","(","LENGTH","F",")",")",
      "(","PRINT","G",")",
      "(","SET","H","(","CONS","A","F",")",")",
      "(","PRINT","H",")",
      "(","SET","I",
      "(","CONS","'","5","'","'",
      "(6 7)","'",")",")",
      "(","PRINT","I",")",
      "(","SET","J",
      "(","CONS","'","1","'",
      "(","CONS","'","2","'","NIL",")",")",")",
      "(","PRINT","J",")",
      "(","SET","K","'","(1 2 3 (4 5) 6)","'",")",
      "(","PRINT","K",")",")"},
      
      .table=init_varArray(),
   }; 
   //PROG(&p27);
   //test set_inter
   lisp* set_inter_lisp=lisp_fromstring("(2 3)");
   set_inter('Z',set_inter_lisp,&p27);
   lisp* p27Z=listvar_inter('Z',&p27);
   lisp_tostring(p27Z,seestr);
   assert(strsame("(2 3)",seestr));

   read_spec_token(&p27,2);
   SET(&p27);
   lisp* p27A=listvar_inter('A',&p27);
   lisp_tostring(p27A,seestr);
   assert(strsame("1",seestr));

   read_spec_token(&p27,13);
   SET(&p27);
   lisp* p27B=listvar_inter('B',&p27);
   lisp_tostring(p27B,seestr);
   assert(strsame("2",seestr));

   //test set and plus
   read_spec_token(&p27,46);
   assert(strsame(getwds(&p27),"SET"));
   lisp* p27_5=math_only("5");
   lisp* p27plus=int_PLUS_inter(p27B,p27_5);
   lisp_tostring(p27plus,seestr);
   assert(strsame("7",seestr));

   //set F=(2 3)
   read_spec_token(&p27,61);
   assert(strsame(getwds(&p27),"SET"));
   SET(&p27);
   lisp* p27F=listvar_inter('F',&p27);
   lisp_tostring(p27F,seestr);
   assert(strsame("(2 3)",seestr));
   //test int_LEN_inter
   lisp* p27Flen=int_LENG_inter(p27F);
   lisp_tostring(p27Flen,seestr);
   assert(strsame(seestr,"2"));
   //set LISTFUNC
   lisp* p27cons=lisp_cons(p27A,p27F);
   set_inter('H',p27cons,&p27);
   lisp_tostring(p27cons,seestr);
   assert(strsame("(1 2 3)",seestr));
   //set cons_2
   lisp* p27cons1=math_only("2");
   lisp* p27cons2=lisp_fromstring("(NIL)");
   lisp* p27cons_inside=lisp_cons(p27cons1,p27cons2);
   lisp* p27cons3=math_only("1");
   lisp* p27cons_outside=lisp_cons(p27cons3,p27cons_inside);
   lisp_tostring(p27cons_outside,seestr);
   assert(strsame("(1 2)",seestr));
   free(p27.table);
   printf("p27----------------\n") ;

   Program p28={
      .wds={"PLUS","'","10","'","'","2","'"},
      .cw=0,
      .table=init_varArray(),
   };  
   INTFUNC(&p28);
   printf("p28----------------\n") ;
   //test set+plus
   Program p29={
      .wds={"SET","A","'","1",
      "'","PLUS","A","'","2","'"},
      .cw=0,
      .table=init_varArray(),
   };  
   printf("p29----------------\n") ;
   IOFUNC(&p29); 
   p29.cw++;  
   INTFUNC(&p29);

//test PLUS use as minus
   Program p30={
      .wds={"(", "SET" ,"E", 
      "(" , "PLUS" ,"'","-4","'",
      "'","5","'",")",")",
      "(","PRINT","E",")"},
      .cw=0,
      .table=init_varArray(),
   };  
   //INSTRCT(&p30);   
   //p30.cw++;
   //INSTRCT(&p30);
   free(&p30);
   printf("p30----------------\n") ;

//test simple loop
   Program p32={
      .wds={"(","(","SET","C","'","5","'",")",
      "(","WHILE","(","LESS","'","0","'","C",")",
      "(","(","PRINT","C",")",
      "(","SET","A","(","PLUS","'",
      "-1","'","C",")",")",
      "(","SET","C","A",")",
      ")",")",")"},
      .cw=0,
      .table=init_varArray(),
   }; 
   //PROG(&p32);
   free(p32.table);  
   printf("p32---------------------\n");
//test PLUS+SET

 Program p33={
      .wds={
      "(","SET","A","(","PLUS",
      "'","-1","'","'","5","'",")",")",
      "(","PRINT","A",")",
      "(","SET","C","A",")",
      "(","PRINT","C",")",},
      .cw=0,
      .table=init_varArray(),
   }; 
   INSTRCT(&p33);
   increase_word(&p33);
   INSTRCT(&p33);
   increase_word(&p33);
   //INSTRCT(&p33);
   //increase_word(&p33);


   INSTRCT(&p33);
   free(p33.table);  
   printf("p33-----------\n");

//the demo 3-->car+var
   Program p34={
      .wds={"(","(","SET","A",
      "'","(5 (1 2 3))","'",
      ")","(","PRINT","(",
      "CAR","A",")",")",")"},
      .cw=0,
      .table=init_varArray(),
   }; 
   PROG(&p34);
   free(p34.table);  

//car+(return/literal
   Program p36={
      .wds={"(","(","PRINT","(","CONS","'","1","'",
      "(","CONS","'","2","'","NIL",")",")",")",")"},
      .cw=0,
      .table=init_varArray(),
   }; 
   PROG(&p36);
   free(p36.table); 


//loop forever
   Program p35={
      .wds={"(","(","WHILE","(","LESS",
      "'","1","'","'","2","'",")",
      "(","(","PRINT","\"",
      "LOOP FOREVER","\"",")",")",")",")"},
      .cw=0,
      .table=init_varArray(),
   }; 
   //PROG(&p35);
   free(p35.table); 
   printf("p35-----------------\n");

//test fib
   Program p37={
      .wds={"(","(","SET","L",
      "'","(1 0)","'",")",
      "(","SET","C","'","2","'",")",
      "(","WHILE","(","LESS","C",
      "'","20","'",")","(", 
      "(","SET","N","(","PLUS" ,
      "(","CAR" ,"L",")", 
      "(","CAR","(","CDR", "L",")",")",")",")", 
      "(","SET","M","(","CONS",
      "N","L",")",")","(","SET","L", "M",")", 
      "(","SET","B","(","PLUS",
      "'","1","'", "C",")",")", 
      "(","SET","C","B",")",")",")", 
      "(","PRINT","M",")",")"},
      .cw=0,
      .table=init_varArray(),
   }; 
   //PROG(&p37);
   free(p37.table); 
   printf("p37-----------------\n"); 

   //pasing ok but can not interp
   Program p39={
      .wds={"(","(","SET","A",
      "'","((1 2","'",")",")"},
      .cw=0,
      .table=init_varArray(),
   }; 
   //PROG(&p39);
   free(p39.table);
   printf("end interp test\n");      

   #ifdef EXTEN
   printf("start extension test\n");
   //the extension function;
   Program extension1={
      .wds={"MUL","'","10","'","'","15","'",
      "SQRT","'","16","'",
      "POW","'","3","'","'","2","'",
      "DIV","'","10","'","'","5","'",
      "FABS","'","-100","'",
      "MOD","'","5","'","'","2","'"},
      .cw=0,
   };
   //the INT extension
   lisp* multest=INTF_mul(&extension1);
   lisp_tostring(multest,seestr);
   assert(strsame(seestr,"150"));
   //to SQRT position
   read_spec_token(&extension1, 7);
   lisp* sqrttest=INTF_sqrt(&extension1);
   lisp_tostring(sqrttest,seestr);
   assert(strsame(seestr,"4"));
   //to POW position   
   read_spec_token(&extension1, 11);
   assert(strsame(getwds(&extension1),"POW"));
   lisp* powtest=INTF_pow(&extension1);
   lisp_tostring(powtest,seestr);
   assert(strsame(seestr,"9"));
   //to DIV position
   read_spec_token(&extension1, 18);
   assert(strsame(getwds(&extension1),"DIV"));
   lisp* divtest=INTF_div(&extension1);
   lisp_tostring(divtest,seestr);
   assert(strsame(seestr,"2"));   
   //to FABS position
   read_spec_token(&extension1, 25);
   assert(strsame(getwds(&extension1),"FABS"));
   lisp* fabstest=INTF_fabs(&extension1);
   lisp_tostring(fabstest,seestr);
   assert(strsame(seestr,"100"));      
   //to MOD position
   read_spec_token(&extension1, 29);
   assert(strsame(getwds(&extension1),"MOD"));
   lisp* modtest=INTF_mod(&extension1);
   lisp_tostring(modtest,seestr);
   assert(strsame(seestr,"1"));

   //test bool extension
   Program extension2={
      .wds={"GRAEQU","'","5","'","'","5","'",
      "GRAEQU","'","6","'","'","5","'",
      "LESEQU","'","-100","'","'","-100","'",
      "LESEQU","'","-3","'","'","2","'",
      "NOTEQU","'","-2","'","'","-2","'"},
      .cw=0,
   };
   //GRAEQU
   lisp* graequtest1=BOOL_graequ(&extension2);
   lisp_tostring(graequtest1,seestr);
   assert(strsame(seestr,"1"));
   read_spec_token(&extension2,7);
   assert(strsame(getwds(&extension2),"GRAEQU"));
   lisp* graequtest2=BOOL_graequ(&extension2);
   lisp_tostring(graequtest2,seestr);
   assert(strsame(seestr,"1"));   

   //LESEQU
   read_spec_token(&extension2,14);
   lisp* lesequtest1=BOOL_lessequ(&extension2);
   lisp_tostring(lesequtest1,seestr);
   assert(strsame(seestr,"1"));   
   read_spec_token(&extension2,21);
   lisp* lesequtest2=BOOL_lessequ(&extension2);
   lisp_tostring(lesequtest2,seestr);
   assert(strsame(seestr,"1")); 

   //NOTEQU
   read_spec_token(&extension2,28);
   assert(strsame(getwds(&extension2),"NOTEQU"));
   lisp* notequtest=BOOL_notequ(&extension2);
   lisp_tostring(notequtest,seestr);
   assert(strsame(seestr,"0"));   
   
   printf("end extension test\n");
   #endif
   return;

}
#else
void test_parse(void){
   printf("testing start\n");

//test VAR grammer
   Program p1={
    .wds={"W","E"},
    .cw=0
   };
   char var1=VAR(&p1);
   assert(is_VAR(&p1)==true);
   assert(var1=='W');

//test increase_word
   increase_word(&p1);
   int cw1=get_cw(&p1);
   assert(cw1==1);
   //print_var(&p1);

   printf("--------------------\n");


//test if error
  Program err={
   .wds={"w"},
   .cw=0
  };
  assert(is_VAR(&err)==false);
  assert(is_LITERAL_math(&err)==false);
   
//test literal
   Program p2={
      .wds={"'","(5 5 6)","'"},
      .cw=0
   };
  
   assert(strsame(p2.wds[0],"'"));
   assert(strsame(p2.wds[1],"(5 5 6)")); 
   assert(strsame(p2.wds[2],"'"));
   LITERAL(&p2);
   printf("p2-----------------\n");
//test LIST & test LISTFUNC
   Program p3={
      .wds={"(","CDR","NIL",")"},
      .cw=0
   };
   LIST(&p3);
   printf("p3--------------------\n");
   Program p4={
      .wds={"(","CONS","B","'","(2)","'",")"},
      .cw=0
   };
   LIST(&p4);

   printf("p4--------------------\n");

   Program p5={
      .wds={"(","CONS","NIL",
      "(","CAR","'","2","'",")",")"},
      .cw=0
   };
   LIST(&p5);
   printf("p5--------------------\n");
//test IOFUNC & INSTRCT

  //combine the IOFUNC
   Program p6={
      .wds={"SET","A","'","(4 1)","'"},
      .cw=0
   };
   IOFUNC(&p6);
   printf("p6--------------------\n");
   Program p7={
      .wds={"PRINT","Z"},
      .cw=0
   };
   IOFUNC(&p7);  
   printf("p7--------------------\n");
   Program p8={
      .wds={"(","PRINT","O",")"},
      .cw=0
   }; 
   INSTRCT(&p8);
   printf("p8--------------------\n");
    //combine the LISTFUNC
   Program p9={
      .wds={"(","CDR","'","3","'",")"},
      .cw=0
   }; 
   INSTRCT(&p9);   
   printf("p9--------------------\n");
   Program p10={
      .wds={"(" , "CONS" , "'" , "(2)" ,
       "'" , "'" , "(5)" , "'" , ")"},
      .cw=0
   }; 
   INSTRCT(&p10); 
   printf("p10--------------------\n");
   //complicate version
   Program p11={
      .wds={"(" ,"SET", "A" ,
      "(", "CONS", "(","CAR","'","(4 5)","'",")" , 
      "(","CDR","'","(5 6)","'",")" ,")" , ")"},
      .cw=0
   }; 
   INSTRCT(&p11);    
   printf("p11--------------------\n");   
   //test is_ function
   Program p12={
      .wds={"SET","E","'","(4 5)","'"},
      .cw=0
   }; 
   printf("p12--------------------\n");


   Program p13={
      .wds={"CONS","'","(4 5)",
      "'","CAR","'","(51 22)","'"},
      .cw=0
   };
   assert(is_IOFUNC(&p12)==true);
   assert(is_LISTFUNC(&p13)==true);
   printf("p13--------------------\n");
   //test INSTRCTS
   Program p14={
      .wds={")"},
      .cw=0
   };
   INSTRCTS(&p14);
   printf("p14--------------------\n");
   Program p15={
      .wds={"(","CAR","'","(4 5)","'",")",")"},
      .cw=0
   };
   INSTRCTS(&p15);  
   printf("p15--------------------\n");
//test prog
   Program p16={
      .wds={"(",")"},
      .cw=0
   };
   PROG(&p16);
   printf("p16--------------------\n");   
   Program p17={
      .wds={"(","(","SET","A","'",
      "(4 5)","'",")","(","PRINT","A",")",")"},
      .cw=0
   };
   PROG(&p17);
   assert(strsame(p17.wds[0],"("));
   assert(strsame(p17.wds[2],"SET"));
   assert(strsame(p17.wds[5],"(4 5)"));
   assert(p17.cw==12);
   printf("p17--------------------\n");

//test the demo2 in cut down grammar
   Program demo2cut={
      .wds={"(","(","PRINT","(",
      "CONS","'","1","'", 
      "(","CONS", "'","2","'", "NIL",")",")",")",")"},
      .cw=0
   };
   PROG(&demo2cut); 
   read_spec_token(&demo2cut, 6);
   assert(is_LITERAL_math(&demo2cut)==true);
   read_spec_token(&demo2cut, 0);
   assert(is_LITERAL_math(&demo2cut)==false);
   read_spec_token(&demo2cut, 11);
   assert(is_LITERAL_math(&demo2cut)==true);
   read_spec_token(&demo2cut, 9);
   assert(is_LISTFUNC(&demo2cut)==true);
   read_spec_token(&demo2cut, 4);
   assert(is_LISTFUNC(&demo2cut)==true);

   printf("demo2cut--------------------\n");

   printf("finish cutdown grammar\n-------------------------------\n");

//test STRING
   Program p18={
      .wds={"\"","Hello World","\""},
      .cw=0
   };
   char* str=STRING(&p18);
   assert(strsame(str,"Hello World")==true);
   free(str);
   printf("p18--------------------\n");
//test BOOLFUNC
   Program p19={
      .wds={"LESS","A","'","(4 4 5)","'"},
      .cw=0
   };
   BOOLFUNC(&p19);
   printf("p19--------------------\n");
//test LOOP
   Program p20={
      .wds={"WHILE","(","LESS",
      "T","A",")","(",")"},
      .cw=0
   }; 
   LOOP(&p20);
   printf("p20--------------------\n");
   Program p21={
      .wds={"E"},
      .cw=0
   }; 
   LIST(&p21);   
   printf("p21--------------------\n");
//test PRINT
   Program p22={
      .wds={"PRINT","'","(4 5)","'"},
      .cw=0
   }; 
   PRINT(&p22);
   printf("p22--------------------\n");
//test demo3
   Program p23={
      .wds={"(","(","SET","A",
      "'","(5 (1 2 3))","'",")",
      "(","PRINT","(","CAR","A",")",")",")"},
      .cw=0
   }; 
   PROG(&p23);       
//test is_token
   char toktest[STRLEN]="SET";
   assert(is_token(toktest)==true);
   
   //char toktest2[STRLEN]="'";


   //pasing ok but can not interp
   Program p38={
      .wds={"(","(","SET","A",
      "'","((1 2","'",")",")"},
      .cw=0,
   }; 
   PROG(&p38);
   free(p38.table);       

   //test if parsing
   Program p42={
      .wds={"(","IF","(","EQUAL",
      "(","LENGTH","'","(1)","'",")","'","0","'",")", 
      "(","(","PRINT","\"","FAILURE","\"",")",")", 
      "(","(","PRINT","\"",
      "(1) DOESN'T HAVE ZERO LENGTH","\"",")",")",")"
      },
      .cw=0,
   }; 
   INSTRCT(&p42);
   printf("test end\n");
   printf("finish parsing test\n-----------------------\n");
   return;
}
#endif
