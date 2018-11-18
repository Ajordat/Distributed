CFLAGS = -Wall -Wextra -g
MAIN = main.c
MODULES = ../utils ../tad/chain
LIBS = ../rpc/chat.h
SOURCES =  ../rpc/chat_xdr.c logic.c
EXE = server

FILES = $(MAIN) $(SOURCES) $(patsubst %,%.c, $(MODULES))
HEADERS = $(LIBS) $(patsubst %,%.h, $(MODULES))


all: $(FILES) $(HEADERS)
	gcc $(FILES) $(CFLAGS) -o $(EXE)

clean:
	rm -f $(EXE)
	rm -f history.txt

again: clean all
