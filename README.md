# Distributed architecture exercises

Multiple exercices from my subject on distributed architectures.

They share some classes that have been improved over the different exercises,
the last one (exercise 4) should be the best version of them.

### Exercise 0

Token ring implementation in Java.

### Exercise 1

Multiple questions about parallel and sequential searches on arrays. Implemented in Java.

### Exercise 2

Distributed mutual exclusion between 7 nodes: `ProcessA`, `ProcessB`, `LWA[1,2,3]` and `LWB[1,2]`.
The objective is that all `LWA` and `LWB` nodes must print their ids several times, sharing the same screen,
without interrupting the others. Implemented in Java.

- `ProcessA` and `ProcessB` are mutually excluded using a token-based protocol.
- When it's `ProcessA`'s turn, it enables the nodes `LWA[1,2,3]` to perform the action described.
- The three `LWA` nodes are mutually excluded using Lamport's protocol.
- When the three `LWA` nodes have finished, `ProcessA` gives the token to `ProcessB`,
which enables the nodes `LWB[1,2]` to perform the action described.
- `LWB` nodes are mutually excluded using Ricart&Agrawala's protocol.
- When the two `LWB` nodes have finished, `ProcessB` gives the token back to `ProcessA`.

### Exercise 3

Chat implemented in C using the `ncurses` library. It uses a client-server architecture using RPC calls.

### Exercise 4

Data replication exercise implemented in Java.
This scenario has seven server nodes and one client. The server nodes have to store the data (key, value, both integers)
and replicate it between them. The client's only task is to write the data and read it, so we can verify it's working.

This scenario has seven nodes sorted in three different layers:
- Nodes `A1`, `A2` and `A3` in the core layer. It's the only layer that accepts write operations from the client.
- Nodes `B1` (replicates `A2`) and `B2` (replicates `A3`) in the first layer.
- Nodes `C1` and `C2` in the second layer. They both replicate node `B2`.

Each layer has different update policies:
- Core layer has a strong consistency: update everywhere, active and eager replication.
- Layer 1 has a casual consistency: lazy updates (every 10 updates), passive replication and primary backup.
- Layer 2 has a weaker casual consistency: lazy updates (every 10 seconds), passive replication and primary backup.

Each server node can use web sockets to show their stored data.
So we can monitor all the data on every server just using an explorer.
