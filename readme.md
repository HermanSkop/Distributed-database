
# A distributed database

## About:

    ### Overall:
    
        A distributed database consists of a set of processes located in the network. Each process hosts a
        certain set of information (for simplicity we assume that it’s just one pair <key,value> per process).
        Processes constitute a logical network, in which each process is connected with at leas one other
        process in a network. The network is incrementally created by executing consecutive processes
        (creation of consecutive nodes) and connecting them to the already existing network. After starting,
        each process (except for the first one) connects to the network according to the assumed network
        communication model and starts waiting for possible connections from new system components (next
        added nodes) or from clients willing to reserve the resources. Communication with clients and newly
        connected nodes uses the TCP protocol. Communication between the nodes during the database
        operation use TCP. All database nodes are symmetric, i.e. they both keep their data and accept requests
        from clients. A node may communicate only with the nodes to which it connected at the start or nodes which
        connected to it later.
        
    ### More detailed:
    
        Once a node receives a request, it creates subprocess that handles it. Depending on operation, handler can create client thread and
        pass operation and already visited nodes that could be received with operation. Created process sends operation, parameter and visited nodes to all
        neighbours that are not yet visited. When operation reaches needed node (f.e. get-value 1:3 reaches node that contain key 1),
        server returns some value to the client. Then ClientThread changes its status to this value. Server, that created the client process,
        waiting for this change and once it happens, value is being returned further. Finally, first server outputs it to the console.
    *More information can be found in documentation folder -> index.html.*
## Formats:
    The network node execution has the following form:
        java -jar DatabaseNode.jar -tcpport <TCP port number> -record <key>:<value> [ -connect <address>:<port> ]
        where:
            • -tcpport <TCP port number> denotes the number of the TCP port, which is used to wait
            for connections from clients.
            • -record <key>:<value> denotes a pair of integers being the initial values stored in this
            node, where the first number is the key and the second is a value associated with this key.
            There is no uniqueness requirement, both to a key and a value.
            • [ -connect <address>:<port> ] denotes a list of other nodes already in the network, to
            which this node should connect and with which it may communicate to perform operations.
            This list is empty for the first node in the network.
    After the network is created, clients may be connected to it.
    A client execution has the following form:
        java DatabaseClient -gateway <address>:<TCP port number> -operation <operation with parameters>
    After a client connects with the network node, it sends a single line message in the format:
        <operation> [ <parameter> ]
        Operations:
            1. set-value <key>:<value> : set a new value (the second parameter) for the key being the
            first parameter. The result of this operation is either an OK message if operation succeeded or
            ERROR if the database contains no pair with a requested key value. If the database contains
            more than 1 such pair, at least one must be altered.
            2. get-value <key> : get a value associated with the key being the parameter. The result of
            this operation is a message consisting of a pair <key>:<value> if operation succeeded or
            ERROR if the database contains no pair with a requested key value. If the database contains
            more than 1 such pair, only one pair must be returned (any valid).
            3. find-key <key> : find the address and the port number of a node, which hosts a pair with
            the key value given as the parameter. If such node exists, the answer is a pair
            <address>:<port> identifying this node, or the message ERROR if no node has a key with
            such a value. If the database contains more than 1 such node, only one pair must be returned
            (any valid).
            4. get-max : find the biggest value of all values stored in the database. The result is a pair
            consisting of <key>:<value>. If the database contains more than 1 such pair, only one pair
            must returned (any valid).
            5. get-min : find the smallest value of all values stored in the database. The result is a pair
            consisting of <key>:<value>. If the database contains more than 1 such pair, only one pair
            must returned (any valid).
            6. new-record <key>:<value> : remember a new pair key:value instead of the pair currently
            stored in the node to which the client is connected. The result of this operation is the OK
            message.
            7. terminate : detaches the node from the database. The node must inform its neighbours
            about this fact and terminate. The informed neighbours store this fact in their resources' and no
            longer communicate with it. Just before the node terminates, it sends back the OK message to
            a client.

How to run?
    Go to "Scripts" directory and run "test.bat" script for windows
