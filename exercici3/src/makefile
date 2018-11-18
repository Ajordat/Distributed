SHELL := /bin/bash

all:
	cd rpc && rpcgen chat.x
	make -C client
	make -C server

clean:
	find rpc/ -type f ! -name '*.x' -delete
	make -C client clean
	make -C server clean


again: clean all