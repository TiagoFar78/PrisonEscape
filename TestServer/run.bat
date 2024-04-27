@ECHO OFF
java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar paper-1.20.4-462.jar nogui
PAUSE