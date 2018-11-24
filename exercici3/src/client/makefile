CFLAGS = -Wall -Wextra -g -O5
MAIN = main.c
MODULES = logic ../utils ../tad/chain view
RPC = ../rpc/chat_clnt.c ../rpc/chat_xdr.c ../rpc/chat.h
EXE = client

FILES = $(MAIN) $(patsubst %,%.c, $(MODULES))
HEADERS = $(patsubst %,%.h, $(MODULES))


all: $(FILES) $(HEADERS) $(RPC)
	gcc -o obj/logic.o -c logic.c $(CFLAGS) -lncurses -pthread
	gcc -o obj/utils.o -c ../utils.c $(CFLAGS)
	gcc -o obj/chain.o -c ../tad/chain.c $(CFLAGS)
	gcc -o obj/view.o -c view.c $(CFLAGS)
	gcc -o $(EXE) main.c obj/*.o ../rpc/obj/*.o $(CFLAGS) -lncurses -pthread

$(RPC):
	make -C ../rpc --no-print-directory

clean:
	make -C ../rpc clean --no-print-directory
	rm -f obj/*.o
	rm -f $(EXE)

again: clean all