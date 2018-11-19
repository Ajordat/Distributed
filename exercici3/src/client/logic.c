//
// Created by ajordat on 17/11/18.
//

#include <signal.h>
#include <time.h>
#include "logic.h"

char parseCli(WINDOW *w, char *author) {
	char string[LENGTH];
	time_t timestamp;
	Message msg;

	WIN_write(w, "> ");

	memset(string, '\0', LENGTH);
	WIN_read(w, string);

	timestamp = time(NULL);

	if (strcasecmp(string, "exit") == 0)
		return 1;

	wmove(w, getcury(w) - 1, 0);
	wclrtoeol(w);

	msg = CHAIN_createMessage((int) timestamp, string, author);
	WIN_writeMsg(w, msg);

	writeMessage(msg);

	WIN_refresh(w);

	CHAIN_destroyMessage(&msg);

	return 0;
}

void updateChat(WINDOW *w, int timestamp) {
	UNUSED(w);

	Chain *history = requestChat(timestamp);

	if (history != NULL)
		for (u_int i = 0; i < history->length; i++)
			WIN_writeMsg(w, history->list[i]);

}

inline void initChat(WINDOW *w) {
	updateChat(w, 0);
}


void sigint_handler(int _) {
	(void)_;
	destroyRpc();
	exit(0);
}

void chatRoutine(char *host, char *author) {
	char flag = 0;
	WINDOW *window;

	signal(SIGINT, sigint_handler);

	window = WIN_create();
	WIN_write(window, "Welcome to this global chat!\n");

	clnt = createRpc(host);

	initChat(window);

	do {
		flag = parseCli(window, author);
	} while (!flag);


	destroyRpc();

	endwin();
}


CLIENT *createRpc(char *host) {
	CLIENT *clnt;

	clnt = clnt_create(host, PGRM_CHAT, V_CHAT, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror(host);
		exit(1);
	}

	return clnt;
}

void destroyRpc() {
	clnt_destroy(clnt);
}

int writeMessage(Message msg) {
	int *response;

	response = writemsg_1(&msg, clnt);
	if (response == (int *) NULL) {
		clnt_perror(clnt, "write message");
		return 1;
	}
	return 0;
}

Chain *requestChat(int timestamp) {
	Chain *response;

	response = getchat_1(&timestamp, clnt);
	if (response == (Chain *) NULL) {
		clnt_perror(clnt, "request chat");
		return NULL;
	}

	return response;
}