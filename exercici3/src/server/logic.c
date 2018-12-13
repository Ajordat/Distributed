

#include "logic.h"
#include "../utils.h"
#include "../tad/chain.h"

#pragma GCC diagnostic ignored "-Wunused-result"


void sigint_handler(int _) {
	UNUSED(_);
	print("catched sigint");
	CHAIN_destroy(&history);
	exit(0);
}

void initServer() {
	print("init\n");
	signal(SIGINT, sigint_handler);
	history = CHAIN_create();

	CHAIN_loadFromFile(&history, HISTORY_FILE);
	CHAIN_toString(history);
	print("init_end\n");
}

int *writemsg_1_svc(Message *argp, struct svc_req *rqstp) {
	UNUSED(rqstp);

	static int event_id;
	char *line;
	size_t length = 10 + 1 + strlen(argp->author) + 1 + strlen(argp->data) + 1;

	debug("write request\n");

	int fd = open(HISTORY_FILE, O_WRONLY | O_APPEND | O_CREAT, 0644);

	line = malloc(length);
	sprintf(line, "%d*%s*%s\n", argp->timestamp, argp->author, argp->data);
	write(fd, line, length);

	free(line);
	close(fd);

	event_id = CHAIN_add(&history, *argp);

	return &event_id;
}

Chain *getchat_1_svc(int *argp, struct svc_req *rqstp) {
	static Chain result;
	char aux[LENGTH];
	UNUSED(rqstp);

	debug("chat request\n");
	sprintf(aux, "Received timestamp: %d\n", *argp);
	debug(aux);

	result = CHAIN_getMessagesFromId(history, *argp);
	CHAIN_toString(result);

	return &result;
}


void pgrm_chat_1(struct svc_req *rqstp, register SVCXPRT *transp) {
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