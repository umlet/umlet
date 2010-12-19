#!/bin/sh
# Shell script for running umlet from the command prompt in linux
# 
# To install: 
# 1. Define environment variable $UMLET_HOME in you shell
#    initialization file, e.g. .bashrc
#       UMLET_HOME=/path/to/umlet;
#       export UMLET_HOME;
#    (You can also execute these lines from the command prompt.)
# 2. Put this file (umlet.sh) on the $PATH, e.g. in your
#    $HOME/bin/ directory.
# 3. Change the script to executable:
#    $ chmod +x $HOME/bin/umlet.sh
# 4. Log out and log in.
# 
# To use:
# Now you can use umlet from the command line from any directory.
#    $ umlet.sh myDiagram.uxf 

currentDir=`pwd`

# uncomment this to be able to start umlet from anywhere (UMLET HOME has to be set)
# cd $UMLET_HOME
if [ $# -gt 0 ]
 then java -jar umlet.jar -filename="$currentDir/$1"
 else java -jar umlet.jar
fi
