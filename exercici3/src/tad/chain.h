//
// Created by ajordat on 18/11/18.
//

#ifndef _LOGIC_H_
#define _LOGIC_H_

#include <time.h>
#include "../rpc/chat_lib.h"

#define HISTORY_FILE "history.txt"

Chain history;


Chain CHAIN_create();

int CHAIN_add(Chain *, Message);

Message CHAIN_createMessage(int timestamp, char *msg, char *author);

void CHAIN_destroyMessage(Message *);

Chain CHAIN_getMessagesFromId(Chain h, int id);

void CHAIN_toString(Chain);

void CHAIN_destroy(Chain *);

void CHAIN_loadFromFile(Chain *, char *filename);


#endif
