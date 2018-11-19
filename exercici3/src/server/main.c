
#include "../rpc/chat.h"
#include "../tad/chain.h"
#include "../utils.h"
#include <stdio.h>
#include <stdlib.h>
#include <rpc/pmap_clnt.h>
#include <string.h>
#include <memory.h>
#include <sys/socket.h>
#include <netinet/in.h>

#ifndef SIG_PF
#define SIG_PF void(*)(int)
#endif

static void pgrm_chat_1(struct svc_req *rqstp, register SVCXPRT *transp) {
	union {
		Message writemsg_1_arg;
		int getchat_1_arg;
	} argument;
	char *result;
	xdrproc_t _xdr_argument, _xdr_result;
	char *(*local)(char *, struct svc_req *);

	switch (rqstp->rq_proc) {
		case NULLPROC:
			(void) svc_sendreply(transp, (xdrproc_t) xdr_void, (char *) NULL);
			return;

		case writeMsg:
			_xdr_argument = (xdrproc_t) xdr_Message;
			_xdr_result = (xdrproc_t) xdr_int;
			local = (char *(*)(char *, struct svc_req *)) writemsg_1_svc;
			break;

		case getChat:
			_xdr_argument = (xdrproc_t) xdr_int;
			_xdr_result = (xdrproc_t) xdr_Chain;
			local = (char *(*)(char *, struct svc_req *)) getchat_1_svc;
			break;

		default:
			svcerr_noproc(transp);
			return;
	}
	memset((char *) &argument, 0, sizeof(argument));
	if (!svc_getargs (transp, (xdrproc_t) _xdr_argument, (caddr_t) &argument)) {
		svcerr_decode(transp);
		return;
	}
	result = (*local)((char *) &argument, rqstp);
	if (result != NULL && !svc_sendreply(transp, (xdrproc_t) _xdr_result, result)) {
		svcerr_systemerr(transp);
	}
	if (!svc_freeargs (transp, (xdrproc_t) _xdr_argument, (caddr_t) &argument)) {
		fprintf(stderr, "%s", "unable to free arguments");
		exit(1);
	}
}

void sigint_handler(int _) {
	UNUSED(_);
	print("catched sigint");
	CHAIN_destroy(&history);
	exit(0);
}


int main(int argc, char **argv) {
	register SVCXPRT *transp;
	UNUSED(argc);
	UNUSED(argv);

	pmap_unset(PGRM_CHAT, V_CHAT);

	transp = svcudp_create(RPC_ANYSOCK);
	if (transp == NULL) {
		fprintf(stderr, "%s", "cannot create udp service.");
		exit(1);
	}
	if (!svc_register(transp, PGRM_CHAT, V_CHAT, pgrm_chat_1, IPPROTO_UDP)) {
		fprintf(stderr, "%s", "unable to register (PGRM_CHAT, V_CHAT, udp).");
		exit(1);
	}

	transp = svctcp_create(RPC_ANYSOCK, 0, 0);
	if (transp == NULL) {
		fprintf(stderr, "%s", "cannot create tcp service.");
		exit(1);
	}
	if (!svc_register(transp, PGRM_CHAT, V_CHAT, pgrm_chat_1, IPPROTO_TCP)) {
		fprintf(stderr, "%s", "unable to register (PGRM_CHAT, V_CHAT, tcp).");
		exit(1);
	}

	signal(SIGINT, sigint_handler);
	history = CHAIN_create();

	svc_run();
	fprintf(stderr, "%s", "svc_run returned");
	exit(1);
}
