//
// Created by ajordat on 18/11/18.
//

#include "chain.h"
#include "../utils.h"


Chain CHAIN_create() {
	Chain h;

	h.length = 0;
	h.list = NULL;

	return h;
}

void CHAIN_add(Chain *h, Message msg) {

	h->list = realloc(h->list, (h->length + 1) * sizeof(Message));

	h->list[h->length].data = malloc(strlen(msg.data) + 1);
	h->list[h->length].author = malloc(strlen(msg.author) + 1);

	strcpy(h->list[h->length].data, msg.data);
	strcpy(h->list[h->length].author, msg.author);
	h->list[h->length].timestamp = msg.timestamp;

	h->length++;
}

Message CHAIN_createMessage(int timestamp, char *msg, char *author) {
	Message m;

	m.timestamp = timestamp;
	m.data = malloc(strlen(msg));
	m.author = malloc(strlen(author));
	strcpy(m.data, msg);
	strcpy(m.author, author);

	return m;
}

Chain CHAIN_getMessagesFromTimestamp(Chain h, int timestamp) {
	int index;
	Chain messages = CHAIN_create();

	for (index = 0; index < h.length && h.list[index].timestamp < timestamp; index++);

	for (; index < h.length; index++)
		CHAIN_add(&messages, h.list[index]);

	return messages;
}


#pragma GCC diagnostic ignored "-Wunused-result"

void CHAIN_toString(Chain h) {
	char aux[LENGTH];
	print("toString\n");
	sprintf(aux, "length: %d\n", h.length);
	print(aux);
	for (int i = 0; i < h.length; i++) {
		write(1, h.list[i].data, strlen(h.list[i].data));
	}
	print("end\n");
}

void CHAIN_destroy(Chain *h) {

	for (int i = 0; i < h->length; i++) {
		free(h->list[i].data);
		free(h->list[i].author);
	}

	free(h->list);
}