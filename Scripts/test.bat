rem Start 7 network nodes
start java -jar ../out/artifacts/DatabaseNode.jar -tcpport 9000 -record 1:8
timeout 1 > NUL
start java -jar ../out/artifacts/DatabaseNode.jar -tcpport 9001 -connect localhost:9000 -record 2:7
timeout 1 > NUL
start java -jar ../out/artifacts/DatabaseNode.jar -tcpport 9002 -connect localhost:9000 -connect localhost:9001 -record 3:6
timeout 1 > NUL
start java -jar ../out/artifacts/DatabaseNode.jar -tcpport 9003 -connect localhost:9001 -record 4:5
timeout 1 > NUL
start java -jar ../out/artifacts/DatabaseNode.jar -tcpport 9004 -connect localhost:9001 -connect localhost:9003 -record 5:4
timeout 1 > NUL
start java -jar ../out/artifacts/DatabaseNode.jar -tcpport 9005 -connect localhost:9002 -connect localhost:9004 -record 6:3
timeout 1 > NUL
start java -jar ../out/artifacts/DatabaseNode.jar -tcpport 9006 -connect localhost:9002 -connect localhost:9005 -connect localhost:9003 -record 7:1
timeout 1 > NUL

java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9003 -operation get-max
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9004 -operation get-min
timeout 1 > NUL
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9004 -operation find-key 7
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9003 -operation find-key 1
timeout 1 > NUL
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9003 -operation set-value 7:7
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9004 -operation set-value 1:1
timeout 1 > NUL
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9004 -operation get-max
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9003 -operation get-max
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9003 -operation get-value 6
timeout 1 > NUL
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9003 -operation find-key 7
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9004 -operation find-key 1
timeout 1 > NUL

java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9000 -operation terminate
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9001 -operation terminate
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9002 -operation terminate
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9003 -operation terminate
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9004 -operation terminate
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9005 -operation terminate
java -jar ../out/artifacts/DatabaseClient.jar -gateway localhost:9006 -operation terminate
