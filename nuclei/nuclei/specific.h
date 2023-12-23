#pragma once

#include "lisp.h"
#define NIL NULL
#define LISTSTRLEN 1000
#define LISPIMPL "LISPIMPL"

#define atom(X)       lisp_atom(X)
#define cons(X, Y)    lisp_cons(X, Y)
#define car(L)        lisp_car(L)
#define cdr(L)        lisp_cdr(L)
#define copy(L)       lisp_copy(L)
#define fromstring(L) lisp_fromstring(L)

struct lisp{
   atomtype elem;
   struct lisp* down;
   struct lisp* right;
};

void do_situ2(lisp* string[], int* strlen, int* value, bool* situ1);
void situ2_fromstr(int* value, int* index, const char* str, bool* situ2);
bool situ1_fromstr(int* index, const char* str);
lisp* to_cons(int strlen, lisp* string[]);
lisp* str_cal(const char* str, int* index);
lisp* init(void);
void to_string(const lisp* l, char* str);
void print_str(char* str);
void no_bracked(const lisp* l,char* str);



