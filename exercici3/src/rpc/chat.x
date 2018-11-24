struct Message {
	string data<>;
	string author<>;
	int timestamp;
};

typedef Message Chain<>;

program PGRM_CHAT {
	version V_CHAT {
		int writeMsg(Message) = 1;
		Chain getChat(int) = 2;
	} = 1;
} = 0xFACE;