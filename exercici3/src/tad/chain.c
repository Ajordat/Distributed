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

Chain CHAIN_getMessagesFromTimestamp(Chain h, int timestamp) {
	u_int index;
	Chain messages = CHAIN_create();

	for (index = 0; index < h.length; index++)
		if (h.list[index].timestamp >= timestamp)
			CHAIN_add(&messages, h.list[index]);

	return messages;
}

void CHAIN_toString(Chain h) {
	char aux[LENGTH];
	print("toString\n");
	sprintf(aux, "CHAIN string: %d", h.length);
	print(aux);
	for (u_int i = 0; i < h.length; i++) {
		print("\n-");
		print(h.list[i].data);
	}
	print("\nend\n");
}

void CHAIN_destroy(Chain *h) {

	for (u_int i = 0; i < h->length; i++) {
		free(h->list[i].data);
		free(h->list[i].author);
	}

	free(h->list);
	h->length = 0;
}