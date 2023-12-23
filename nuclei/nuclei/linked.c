#include "lisp.h"
#include "specific.h"

lisp* lisp_list(const int n, ...){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
   va_list valist;
   va_start(valist,n);
   
   lisp* list[LISTSTRLEN];
   lisp* tmp={NULL};
   
   for(int i=n-1;i>=0;i--){
      list[i]=va_arg(valist,lisp*);
   }
   for(int i=0;i<n;i++){
      tmp=lisp_cons(list[i],tmp);
   }
   va_end(valist);
   return tmp;
}

void lisp_reduce(void (*func)(lisp* l, atomtype* n), lisp* l, atomtype* acc){
   if(l==NULL){
      return;
   } 

   lisp* down=(lisp*)l->down;
   lisp* right=(lisp*)l->right;
  
   if(down==NULL){
      func(l,acc);
   }
   lisp_reduce(func,down,acc);
   lisp_reduce(func,right,acc);
   return;
}

lisp* lisp_fromstring(const char* str){
   int index=1;
   lisp* list=str_cal(str,&index);
   return list; 
}

bool situ1_fromstr(int* index, const char* str){
   if(str[*index]=='-'){
      (*index)++;
      return true;
   }
   return false;
}

void situ2_fromstr(int* value, int* index, const char* str,bool* situ2){
   (*value)=(*value)*10+(str[*index]-'0');
   (*index)++;
   (*situ2)=true;
   return;
}

void do_situ2(lisp* string[], int* strlen, int* value, bool* situ1){
   string[(*strlen)]=init();
   if((*situ1)){
      (*value)=(-(*value));
   }
   string[(*strlen)]->elem=(*value);
   (*strlen)++;
   return;
}

lisp* str_cal(const char* str,int* index){
   lisp* string[LISTSTRLEN]={NULL};
   int strlen=0;
   do{
      if(str[*index]=='('){
         (*index)++;
         string[strlen]=str_cal(str,index);
         strlen++;
      }
      else{
         int value=0;
         bool situ1=situ1_fromstr(index,str); 
         bool situ2=false;
         while(str[*index]>='0'&&str[*index]<='9'){
            situ2_fromstr(&value, index, str, &situ2);
         }
         if(situ2){
            do_situ2(string, &strlen, &value, &situ1);
         }
         else{
            (*index)++;
         }
      }
   } while(str[*index-1]!=')');
   (*index)++;
   
   lisp* tocons=to_cons(strlen, string);
   return tocons;
}

lisp* to_cons(int strlen, lisp* string[]){
   lisp* tocon=NULL;
   for(int i=strlen-1;i>=0;i--){
      tocon=cons(((lisp*)string[i]),tocon);
   }
   return tocon;
}

lisp* lisp_copy(const lisp* l){
   if(l==NULL){
      return NULL;
   }
   lisp* copy=init();
   lisp* point=copy;
   lisp* cur=(lisp*)l;
   while(cur!=NULL){ 
      //down space
      if(lisp_isatomic(cur->down)){
         lisp* down=init();
         down->elem=cur->down->elem;
         point->down=down;
      }
      else{
         point->down=lisp_copy(cur->down);
      }
      //right space
      if(cur->right!=NULL){
         lisp* right=init();
         point->right=right;
      }       
      cur=cur->right;
      point=point->right;
   }
   return copy;
}

void lisp_free(lisp** l){
   if((*l)==NULL){
      return;
   }
   if((*l)->down!=NULL){
      lisp_free(&((*l)->down));
   }

   if((*l)->right!=NULL){
      lisp_free(&((*l)->right));
   }
   free(*l);
   *l=NULL;
   return;
}

lisp* lisp_cons(const lisp* l1,  const lisp* l2){
    lisp* cons;
    cons=init();

    cons->down=(lisp*)l1;
    cons->right=(lisp*)l2;

    return cons;
}

bool lisp_isatomic(const lisp*l){
   if(l==NULL){
      return false;
   }
   if(l->right==NULL&&l->down==NULL){
      return true;
   }
   return false;
}

lisp* lisp_atom(const atomtype a){
   lisp* atom;
   atom=init();
   
   atom->elem=a;
   return atom;
}

lisp* init(void){
   lisp* newnode=(lisp*)ncalloc(1,sizeof(lisp));
   return newnode;
}

int lisp_length(const lisp* l){
   if(l==NULL||lisp_isatomic(l)){
      return 0;
   }

   int len=0;
   while(l!=NULL){
      len++;
      l=l->right;
   }
   return len;
}

void lisp_tostring(const lisp* l, char* str){
   str[0]='\0';
   if(l==NULL){
      strcat(str,"()");
      return;
   }

   if(lisp_isatomic(l)){
      no_bracked(l,str);
      return;
   }

   to_string(l,str);
   str[(strlen(str))-1]='\0';
   return;
}

void to_string(const lisp* l, char* str){
   strcat(str,"(");
   lisp* cur=(lisp*)l;
   while(cur!=NULL){
      if(lisp_isatomic(cur->down)){
         char tmp[LISTSTRLEN];   
         snprintf(tmp,LISTSTRLEN,"%d ",cur->down->elem);
         strcat(str,tmp);
      }else{
         to_string(cur->down,str);
      }
      cur=cur->right;
   }
   str[strlen(str)-1]=')';
   strcat(str," ");
   return;
}

atomtype lisp_getval(const lisp* l){
   atomtype value;
   value=l->elem;

   return value;
}

void no_bracked(const lisp* l,char* str){
   char tmp[LISTSTRLEN];
   snprintf(tmp,LISTSTRLEN,"%d",l->elem);
   strcat(str,tmp);
   return;
}

lisp* lisp_car(const lisp* l){
   return l->down;
}

lisp* lisp_cdr(const lisp* l){
   return l->right;
}

void print_str(char* str){
   puts(str);
   return;
}



