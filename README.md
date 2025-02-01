# P2P Hash Breaker

A **peer-to-peer hash breaking algorithm** that distributes brute-force cracking tasks among multiple nodes. This project is implemented in **Java** using **libp2p**. Nodes have to be deployed within a local network to connect.

## Features

- **Distributed Brute Force**: Nodes work together.
- **Multi-node Deployment**: You can add multiple nodes, I only tested up to 10
- **Progress Announcement**: Nodes announce when they find the answer or complete a work interval.
- **Local Network Operation**: Currently, nodes can only join within local networks.

## How It Works

1. A user inputs a hash and the hashing algorithm
2. The workload is distributed among available nodes.
3. Each node brute forces a portion of the key space.
4. When a node finds a match, it announces the result to all peers.
5. Nodes notify when they complete their assigned workload.
