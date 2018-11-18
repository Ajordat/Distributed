//
// Created by ajordat on 17/11/18.
//

#include "utils.h"

#pragma GCC diagnostic ignored "-Wunused-result"


inline void print(char *string) {
	write(STDOUT, string, strlen(string));
}

inline void debug(char *string) {
	if (DEBUG)
		write(STDOUT, string, strlen(string));
}

inline void error(char *string) {
	write(STDERR, string, strlen(string));
}
