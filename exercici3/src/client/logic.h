//
// Created by ajordat on 17/11/18.
//

#ifndef _CLIENT_LOGIC_H_
#define _CLIENT_LOGIC_H_


#include <ncurses.h>
#include "../rpc/chat_lib.h"
#include "../utils.h"
#include "../tad/chain.h"
#include "view.h"


CLIENT *clnt;


void chatRoutine(char *host, char *author);

CLIENT *createRpc(char *host);

void destroyRpc();

int writeMessage(Message msg);

Chain *requestChat(int id);


#endif
