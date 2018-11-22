
#include <rpc/pmap_clnt.h>
#include "logic.h"
#include "../utils.h"
#include "../rpc/chat_lib.h"

#ifndef SIG_PF
#define SIG_PF void(*)(int)
#endif



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

	initServer();

	svc_run();
	fprintf(stderr, "%s", "svc_run returned");
	exit(1);
}
