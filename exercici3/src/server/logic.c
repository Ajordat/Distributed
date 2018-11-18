
#include <unistd.h>
#include <fcntl.h>
#include "../utils.h"
#include "../tad/chain.h"

#pragma GCC diagnostic ignored "-Wunused-result"


int *writemsg_1_svc(Message *argp, struct svc_req *rqstp) {
	UNUSED(rqstp);

	static int result;
	char *line;
	size_t length = 10 + 1 + strlen(argp->author) + 1 + strlen(argp->data) + 1 + 1;

	debug("write request\n");

	int fd = open("history.txt", O_WRONLY | O_APPEND | O_CREAT, 0644);

	line = malloc(length);
	sprintf(line, "%d*%s*%s\n", argp->timestamp, argp->author, argp->data);
	write(fd, line, length);

	free(line);
	close(fd);

	CHAIN_add(&history, *argp);

	result = 0;

	return &result;
}

Chain *getchat_1_svc(int *argp, struct svc_req *rqstp) {
	static Chain result;
	char aux[LENGTH];
	UNUSED(rqstp);

	debug("chat request\n");
	sprintf(aux, "Received timestamp: %d\n", *argp);
	debug(aux);

	result = CHAIN_getMessagesFromTimestamp(history, *argp);
	CHAIN_toString(result);

	return &result;
}


