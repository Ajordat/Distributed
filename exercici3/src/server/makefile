CFLAGS = -Wall -Wextra -g -O5
MAIN = main.c
MODULES = ../utils ../tad/chain
SOURCES = logic.c
RPC = ../rpc/chat.h ../rpc/chat_xdr.c
EXE = server

FILES = $(MAIN) $(SOURCES) $(patsubst %,%.c, $(MODULES))
HEADERS = $(patsubst %,%.h, $(MODULES))


all: $(FILES) $(HEADERS) $(RPC)
	gcc $(FILES) ../rpc/obj/chat_xdr.o $(CFLAGS) -o $(EXE)

$(RPC):
	make -C ../rpc --no-print-directory

clean:
	make -C ../rpc clean --no-print-directory
	rm -f $(EXE)
	rm -f history.txt

again: clean all
