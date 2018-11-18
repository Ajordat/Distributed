//
// Created by ajordat on 17/11/18.
//

#ifndef _UTILS_H_
#define _UTILS_H_

#include <unistd.h>
#include <memory.h>


#define UNUSED(x) (void)(x);

#define DEBUG	1

#define STDIN	0
#define STDOUT	1
#define STDERR	2

#define LENGTH 100


void print(char *);

void debug(char *);

void error(char *);

#endif
