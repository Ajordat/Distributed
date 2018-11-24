//
// Created by ajordat on 20/11/18.
//

#ifndef _SERVER_LOGIC_H_
#define _SERVER_LOGIC_H_

#include <unistd.h>
#include <fcntl.h>
#include <rpc/svc.h>



void initServer();

void pgrm_chat_1(struct svc_req *rqstp, register SVCXPRT *transp);

#endif
