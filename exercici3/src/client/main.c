
#include <unistd.h>
#include "logic.h"


int main(int argc, char *argv[]) {

	if (argc < 3) {
		printf("usage: %s server_host author\n", argv[0]);
		exit(1);
	}

	chatRoutine(argv[1], argv[2]);
	exit(0);
}
