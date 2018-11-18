CFLAGS = -Wall -Wextra -g -O5
LIBS = chat.h
SOURCES = chat_clnt.c chat_xdr.c

FILES = $(SOURCES)
HEADERS = $(LIBS)


all: $(SOURCES) $(LIBS)
	gcc -o obj/chat_clnt.o -c chat_clnt.c $(CFLAGS)
	gcc -o obj/chat_xdr.o -c chat_xdr.c $(CFLAGS) -Wno-unused-variable

$(SOURCES): chat.x
	rpcgen chat.x

clean:
	rm -f obj/*.o
	rm -f *.c
	rm -f *.h

again: clean all