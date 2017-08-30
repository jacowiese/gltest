@echo off
set MAVEN_OPTS="-Djava.library.path=target/natives" 
mvn compile exec:java -Dexec.mainClass=org.jacowiese.GLTest
