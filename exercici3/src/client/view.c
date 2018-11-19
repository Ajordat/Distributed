//
// Created by ajordat on 18/11/18.
//

#include "view.h"
#include "../utils.h"

#if USE_NCURSES

inline WINDOW *WIN_create() {
	return initscr();
}

inline void WIN_destroy() {
	endwin();
}

inline void WIN_read(WINDOW *w, char *string) {
	wgetstr(w, string);
}


inline void WIN_write(WINDOW *w, char *string) {
	wprintw(w, string);
}

inline void WIN_refresh(WINDOW *w) {
	wrefresh(w);
}

void WIN_writeMsg(WINDOW *w, Message msg) {
	char aux[LENGTH], time[20];
	time_t ts = msg.timestamp;

	strftime(time, 20, "%d-%m-%y %H:%M", localtime(&ts));
	sprintf(aux,"%s ~ %s > %s\n", time, msg.author, msg.data);
	WIN_write(w, aux);
}

#else

inline WINDOW *WIN_create() {
	return NULL;
}

inline void WIN_destroy() {}

void WIN_read(WINDOW *w, char *string) {
	UNUSED(w);
	ssize_t t = read(0, string, LENGTH);
	string[t - 1] = '\0';
}


inline void WIN_write(WINDOW *w, char *string) {
	UNUSED(w);
	print(string);
}

inline void WIN_refresh(WINDOW *w) {
	UNUSED(w);
}

void WIN_writeMsg(WINDOW *w, Message msg) {
	UNUSED(w);
	char aux[LENGTH], time[20];
	time_t ts = msg.timestamp;

	strftime(time, 20, "%d-%m-%y %H:%M", localtime(&ts));
	sprintf(aux,"%s ~ %s > %s\n", time, msg.author, msg.data);
	print(aux);
}

#endif