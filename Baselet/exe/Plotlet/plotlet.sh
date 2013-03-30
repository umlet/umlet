#!/bin/sh

# Shell script for running plotlet from the command prompt in linux

# If you want to put plotlet.sh in your home bin directory ($HOME/bin/) to start it from anywhere with
#    $ plotlet.sh myDiagram.uxf
# you must specify the programDir directly instead
#programDir=/path/to/plotlet
programDir=$(cd $(dirname $0);pwd)

if [ $# -gt 0 ]
 then java -jar ${programDir}/plotlet.jar -filename="$1"
 else java -jar ${programDir}/plotlet.jar
fi
