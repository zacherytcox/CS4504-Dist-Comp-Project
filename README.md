# CS4504-Dist-Comp-Project
This code is a hand in for CS4504 Distributed Computing at Kennesaw State University. Latest version is Phase 2 of the project where we are needing to implement a P2P network. This program uses Java Threads to simulate multiple machines. Launches 3 server routers, each server router contains its own routing table, and allows nodes (clients or servers) to connect. By default we have 1000 pairs (pair is one server and one client ment to communicate between each other) to connect to a random server router. Each node starts a TCP handshake, then the client starts a TCP handshake via server routers, then after the connection is accepted the client will connect directly to the server. Currently the only action the server performs is "Upper casing" a string. This project was ment to learn networking and simulate a P2P network.

## Installation
* Install Latest JDK
* Download Package

## Usage
* Modify the ip address to the host machine's ip
* Modify the logFilePath for the location to create the TCPPhase2Testing folder
* Can change number of pairs if desired

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History
* Phase1
* Phase2
## Credits
* Zachery Cox (git:zacherytcox)
* Andrew Marks (git:amarks15)
