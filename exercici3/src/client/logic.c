//
// Created by ajordat on 17/11/18.
//

#include <signal.h>
#include <time.h>
#include <pthread.h>
#include "logic.h"

char parseCli(char *author) {
	char string[LENGTH];
	time_t timestamp;
	Message msg;

	WIN_write(input, "> ");

	memset(string, '\0', LENGTH);
	WIN_read(input, string);

	timestamp = time(NULL);

	if (strcasecmp(string, "exit") == 0)
		return 1;

	WIN_cleanLine(input);

	msg = CHAIN_createMessage((int) timestamp, string, author);

	writeMessage(msg);

	CHAIN_destroyMessage(&msg);

	return 0;
}

int updateChat(int timestamp) {

	Chain *history = requestChat(timestamp);

	if (history != NULL) {
		for (u_int i = 0; i < history->length; i++)
			WIN_writeMsg(display, history->list[i]);

		if (history->length > 0) {
			wrefresh(display);
			wrefresh(input);
			timestamp = history->list[history->length - 1].timestamp;
		}

		CHAIN_destroy(history);
	}

	return timestamp ?: 0;
}

void *initChat(void *_) {
	UNUSED(_);
	int timestamp = 0;

	while (1) {
		timestamp = updateChat(timestamp);
		sleep(1);
	}
}


void sigint_handler(int _) {
	(void) _;
	destroyRpc();
	WIN_destroy();
	exit(0);
}

void chatRoutine(char *host, char *author) {
	char flag = 0;
	pthread_t thread;

	signal(SIGINT, sigint_handler);

	clnt = createRpc(host);

	WIN_create();
	scrollok(display, true);
	refresh();
	//box(input, 0, 0);


	WIN_write(display, "Welcome to this global chat!\n");
	refresh();

	pthread_create(&thread, NULL, initChat, NULL);

	do {
		flag = parseCli(author);
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