
#include <unistd.h>
#include "logic.h"


int main(int argc, char *argv[]) {

	if (argc < 3) {
		char aux[LENGTH];
		sprintf(aux, "usage: %s server_host author\n", argv[0]);
		print(aux);
		exit(1);
	}

	chatRoutine(argv[1], argv[2]);
	exit(0);
}
