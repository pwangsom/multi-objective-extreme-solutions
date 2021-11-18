#!/bin/bash         

echo "Start to process DA-DAG experiment with 500-size input"
echo ""
echo "Start: NONE COMMU with 300 iterations"

java -jar da-dag-project-1.0.1-SNAPSHOT-full.jar /home/pwangsom/java-program/run2 300 none commu

echo ""
echo "Start: NONE COMMU with 600 iterations"
java -jar da-dag-project-1.0.1-SNAPSHOT-full.jar /home/pwangsom/java-program/run2 600 none commu

echo ""
echo "Start: NONE COMMU with 900 iterations"
java -jar da-dag-project-1.0.1-SNAPSHOT-full.jar /home/pwangsom/java-program/run2 900 none commu
