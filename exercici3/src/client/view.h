//
// Created by ajordat on 18/11/18.
//

#ifndef _VIEW_H_
#define _VIEW_H_

#include <time.h>
#include <ncurses.h>
#include "../rpc/chat_lib.h"

#define USE_NCURSES 1

WINDOW *display;
WINDOW *input;

WINDOW *WIN_create();

void WIN_destroy();

void WIN_read(WINDOW *w, char *string);

void WIN_write(WINDOW *w, char *string);

void WIN_refresh(WINDOW *w);

void WIN_writeMsg(WINDOW *w, Message msg);

#endif
