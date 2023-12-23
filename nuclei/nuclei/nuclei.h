#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>
#include <ctype.h>
#include <stdbool.h>
#include <math.h>
#include "lisp.h"
#include "specific.h"

#define strsame(A,B) (strcmp(A,B)==0)
#define NIL NULL
#define MAXNUMTOKENS 1000
#define MAXTOKENSIZE 50
#define ONECHAR 1
#define STRLEN 50
#define MAXLETTER 1000
#define BACKSTATSTART 3
#define NEXTINSTRCTS 8
#define PRINTEND 3
#define JUMPEND 6
#define ERROR(PHRASE){fprintf(stderr,"%s\n",PHRASE);\
exit(EXIT_FAILURE);}

struct variable{
   char name;   
   lisp* value;  
};
typedef struct variable variable;

struct varArray{  
   variable* var_arr[MAXLETTER];  
   int num_var;  
};
typedef struct varArray varArray; 

struct prog{
   char wds[MAXNUMTOKENS][MAXTOKENSIZE];
   int cw;
   varArray* table; 
};
typedef struct prog Program;

void IF_left_brack(Program* p);
void IF_right_brack(Program* p);
void is_IF(Program* p);
void IF_judge(Program* p, lisp* list);
void IF_true(Program* p,lisp* list);
void LOOP_instrcts(Program* p, lisp* list);
lisp* INTF_plus(Program* p);
lisp* INTF_length(Program* p);
lisp* INTF_mul(Program* p);
lisp* INTF_sqrt(Program* p);
lisp* INTF_pow(Program* p);
lisp* INTF_div(Program* p);
lisp* INTF_fabs(Program* p);
lisp* INTF_mod(Program* p);
lisp* BOOL_graequ(Program* p);
lisp* BOOL_lessequ(Program* p);
lisp* BOOL_notequ(Program* p);
lisp* BOOL_equal(Program* p);
lisp* BOOL_greater(Program* p);
lisp* BOOL_less(Program* p);
lisp* int_POW_inter(lisp* list1, lisp* list2);
lisp* int_DIV_inter(lisp* list1, lisp* list2);
lisp* int_FABS_inter(lisp* list1);
lisp* int_MOD_inter(lisp* list1, lisp* list2);
lisp* LISTF_cons(Program* p);
lisp* LISTF_cdr(Program* p);
lisp* LISTF_car(Program* p);
void PRINT_int(Program* p);
void PRINT_str(Program* p);
void PRINT_var(Program* p);
void PRINT_list(Program* p);
void PRINT_iter(Program* p);
lisp* int_SQRT_inter(lisp* list1);
lisp* int_MUL_inter(lisp* list1, lisp* list2);
void read_last(char* ch, char* tmp, Program* prog);
void read_var(int* douindex, int* litindex,int* tokindex, Program* prog, char* tmp, char* box);
void read_token(int* douindex, int* litindex,int* tokindex, Program* prog, char* tmp, char* box);
void read_brackets(int* douindex, int* litindex, Program* prog, char* tmp);
void read_string(int* douindex, Program* prog, char* tmp);
void read_literal(int* douindex, int* litindex, Program* prog, char* tmp);
void next_INSTRCTS(Program* p);
void next_statment(Program* p);
void jump_loop(Program* p); 
void judge_if_interp(Program* p);
void judge_end(Program* p);
void judge_print_end(Program* p);
bool jumploop_within(Program* p);
lisp* int_LENG_inter(lisp* list);
lisp* int_PLUS_inter(lisp* list1, lisp* list2);
lisp* math_only(const char* str);
void test_interp(void);
variable* init_variable();
varArray* init_varArray();
variable* var_toarray(char var, lisp* value);
variable* var_fromarray(char var, varArray* arr);
bool is_defined_var(char var, varArray* arr);
void add_totable(variable* v, varArray* arr);
void set_inter(char var, lisp* list1, Program* p);
void Varprint_inter(char var, Program* p);
lisp* listvar_inter(char var,Program* p);
lisp* bool_inter(char* type,lisp* list1,lisp* list2);
bool judge_func(lisp* list);
void search_while(Program* p);
void decrease_word(Program* p);
void print_general(lisp* list);
bool is_token(char* str);
bool is_RETFUNC(Program* p);
bool is_BOOLFUNC(Program* p);
bool is_INTFUNC(Program* p);
void PROG(Program* p);
void FUNC(Program* p);
lisp* RETFUNC(Program* p);
lisp* INTFUNC(Program* p);
void IF(Program* p);
void PRINT(Program* p);
void LOOP(Program* p);
void SET(Program* p);
lisp* BOOLFUNC(Program* p);
char* STRING(Program* p);
void INSTRCTS(Program* p);
bool is_IOFUNC(Program* p);
bool is_LISTFUNC(Program* p);
void INSTRCT(Program* p);
void IOFUNC(Program* p);
lisp* LISTFUNC(Program* p);
lisp* LIST(Program* p);
lisp* LITERAL(Program* p);
char VAR(Program* p);
int get_cw(Program* p);
void print_var(Program* p);
char* getwds(Program* p);
void increase_word(Program* p);
void test_parse(void);
void read_file(int argc, char* argv[]);
void read_argv(char* argv[]);  
void checkArgument(int argc, char* argv[]);
bool is_LITERAL_math(Program* p);
bool is_VAR(Program* p);
char* read_spec_token(Program* p, int wd);



