//
// Created by ajordat on 18/11/18.
//

#include <fcntl.h>
#include "chain.h"
#include "../utils.h"


Chain CHAIN_create() {
	Chain h;

	h.length = 0;
	h.list = NULL;

	return h;
}

int CHAIN_add(Chain *h, Message msg) {

	h->list = realloc(h->list, (h->length + 1) * sizeof(Message));

	h->list[h->length].data = malloc(strlen(msg.data) + 1);
	h->list[h->length].author = malloc(strlen(msg.author) + 1);

	strcpy(h->list[h->length].data, msg.data);
	strcpy(h->list[h->length].author, msg.author);

	h->list[h->length].timestamp = msg.timestamp;
	h->list[h->length].id = msg.id ? : (int) h->length + 1;

	return ++h->length;
}

Message CHAIN_createMessage(int timestamp, char *msg, char *author) {
	Message m;

	m.timestamp = timestamp;
	m.data = malloc(strlen(msg) + 2);
	m.author = malloc(strlen(author) + 2);
	strcpy(m.data, msg);
	strcpy(m.author, author);

	return m;
}

void CHAIN_destroyMessage(Message *msg) {
	free(msg->author);
	free(msg->data);
}

Chain CHAIN_getMessagesFromId(Chain h, int id) {
	u_int index;
	Chain messages = CHAIN_create();

	for (index = 0; index < h.length; index++)
		if (h.list[index].id > id)
			CHAIN_add(&messages, h.list[index]);

	return messages;
}

void CHAIN_toString(Chain h) {
	char aux[LENGTH];
	print("toString\n");
	sprintf(aux, "CHAIN string: %d\n", h.length);
	print(aux);
	for (u_int i = 0; i < h.length; i++) {
		sprintf(aux, "%d ~> %d - %s: %s\n", h.list[i].id, h.list[i].timestamp, h.list[i].author, h.list[i].data);
		print(aux);
	}
	print("end\n");
}

void CHAIN_destroy(Chain *h) {

	for (u_int i = 0; i < h->length; i++) {
		free(h->list[i].data);
		free(h->list[i].author);
	}

	free(h->list);
	h->length = 0;
}

void CHAIN_loadFromFile(Chain *h, char *filename) {
	char c;
	int fd, index, id = 0;
	Message msg;
	char time[15], aux[LENGTH];

	fd = open(filename, O_RDONLY);
	if (fd == -1)
		return;

	while (read(fd, &c, sizeof(char)) > 0) {

		memset(time, '\0', 15);
		for (index = 0; c != '*'; index++) {
			time[index] = c;
			if (read(fd, &c, sizeof(char)) <= 0) {
				error("History file format error (timestamp parse)\n");
				exit(1);
			}
		}
		msg.timestamp = atoi(time);

		debug("timestamp parsed\n");

		if (read(fd, &c, sizeof(char)) <= 0) {
			error("History file format error (separator parse)\n");
			exit(1);
		}

		memset(aux, '\0', LENGTH);
		for (index = 0; c != '*'; index++) {
			aux[index] = c;
			if (read(fd, &c, sizeof(char)) <= 0) {
				error("History file format error (author parse)\n");
				exit(1);
			}
		}
		msg.author = malloc(strlen(aux) + 1);
		strcpy(msg.author, aux);

		debug("author parsed\n");

		if (read(fd, &c, sizeof(char)) <= 0) {
			error("History file format error (separator parse)\n");
			exit(1);
		}

		memset(aux, '\0', LENGTH);
		for (index = 0; c != '\n'; index++) {
			aux[index] = c;
			if (read(fd, &c, sizeof(char)) <= 0)
				break;
		}
		msg.data = malloc(strlen(aux) + 1);
		strcpy(msg.data, aux);

		msg.id = ++id;

		debug("data parsed\n");

		CHAIN_add(h, msg);

		CHAIN_destroyMessage(&msg);
	}

	close(fd);
	print("parsed\n");
}