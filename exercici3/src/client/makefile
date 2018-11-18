LOGIN = ls30687
GRUP = x
CFLAGS = -Wall -Wextra -g -O5
MAIN = main.c
MODULES = logic ../utils ../tad/chain view
LIBS = ../rpc/chat.h
SOURCES = ../rpc/chat_clnt.c ../rpc/chat_xdr.c
EXE = client

FILES = $(MAIN) $(SOURCES) $(patsubst %,%.c, $(MODULES))
HEADERS = $(LIBS) $(patsubst %,%.h, $(MODULES))


all: $(FILES) $(HEADERS)
	gcc -o obj/chat_clnt.o -c ../rpc/chat_clnt.c $(CFLAGS)
	gcc -o obj/chat_xdr.o -c ../rpc/chat_xdr.c $(CFLAGS) -Wno-unused-variable
	gcc -o obj/logic.o -c logic.c $(CFLAGS) -lncurses
	gcc -o obj/utils.o -c ../utils.c $(CFLAGS)
	gcc -o obj/chain.o -c ../tad/chain.c $(CFLAGS)
	gcc -o obj/view.o -c view.c $(CFLAGS)
	gcc -o $(EXE) main.c obj/*.o $(CFLAGS) -lncurses

clean:
	rm -f obj/*.o
	rm -f $(EXE)

again: clean all