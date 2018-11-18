//
// Created by ajordat on 18/11/18.
//

#ifndef _LOGIC_H_
#define _LOGIC_H_

#include <time.h>
#include "../rpc/chat.h"


Chain history;


Chain CHAIN_create();

void CHAIN_add(Chain *, Message);

Message CHAIN_createMessage(int timestamp, char *msg, char *author);

Chain CHAIN_getMessagesFromTimestamp(Chain h, int timestamp);

void CHAIN_toString(Chain h);

void CHAIN_destroy(Chain *);


#endif
