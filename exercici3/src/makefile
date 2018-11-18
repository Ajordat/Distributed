SHELL := /bin/bash

all:
	make -C rpc --no-print-directory
	make -C client --no-print-directory
	make -C server --no-print-directory

clean:
	make -C rpc clean --no-print-directory
	make -C client clean --no-print-directory
	make -C server clean --no-print-directory


again: clean all