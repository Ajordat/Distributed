//
// Created by ajordat on 18/11/18.
//

#include "view.h"
#include "../utils.h"

#if USE_NCURSES

inline WINDOW *WIN_create() {
	return initscr();
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

void WIN_read(WINDOW *w, char *string) {
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

#endif