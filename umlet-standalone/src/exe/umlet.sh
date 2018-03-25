#!/bin/sh
# activate job monitoring
# @see: http://www.linuxforums.org/forum/programming-scripting/139939-fg-no-job-control-script.html
set -m

# ---------------------------------------------------------------------------------
# UMLet Startup Script
# Shell script for running umlet from the command prompt in linux
#
# Environment Variable Prerequisites
#
#   JAVA_HOME | JAVA_CMD  Must point at your Java installation or Java executable.
#   UMLET_JAVA_OPTS       (optional) additional Java start options (-X..., -D...)
#   UMLET_HOME            (optional) program directory of UMLet
# ---------------------------------------------------------------------------------
#
# If you want to put umlet.sh in your home bin directory ($HOME/bin/) to start it from anywhere with
#    $ umlet.sh myDiagram.uxf
# you must export the UMLET_HOME environment variable with the full qualified path of the UMLet installation directory.
# export UMLET_HOME=/path/to/umlet

_UMLET_HOME="$(cd $(dirname $0);pwd)"

# check and use programDir for backward compatibility
if [ ! -z "${programDir}" ] ; then
  echo "programDir is deprecated. Please use UMLET_HOME instead."
  _UMLET_HOME="${programDir}"
fi

# UMLET_HOME wins against deprecated programDir
if [ ! -x "${UMLET_HOME}" ] ; then
  _UMLET_HOME="${UMLET_HOME}"
fi

# try to find the Java executable if JAVA_CMD is empty
if [ ! -x "${JAVA_CMD}" ] ; then
  if [ -z "${JAVA_HOME}" ] ; then
    JAVA_CMD=$(which java)
  else
    JAVA_CMD="${JAVA_HOME}/bin/java"
  fi
fi

# exit script if JAVA_CMD is empty
if [ ! -x "${JAVA_CMD}" ] ; then
  echo "One of these JAVA_HOME, JAVA_CMD environment variables is not defined correctly" >&2
  echo "One of this environment variable is needed to run this program" >&2
  exit 1
fi

# platform independent Java options
_UMLET_JAVA_OPTS="-Dsun.java2d.xrender=f ${UMLET_JAVA_OPTS}"

# platform dependent Java options
_OS_NAME="$(uname -s)"
case ${_OS_NAME} in
  'Darwin')
    _UMLET_JAVA_OPTS=" \
      -Xdock:icon=${_UMLET_HOME}/img/umlet_logo.png \
      -Xdock:name=UMLet \
      ${_UMLET_JAVA_OPTS} \
      -Dapple.laf.useScreenMenuBar=true"
    ;;
    *)
    ;;
esac

if [ $# -eq 1 ] ; then
  _UMLET_OPTS=-filename="${1}"
else
  _UMLET_OPTS="$@"
fi

exec "${JAVA_CMD}" \
  ${_UMLET_JAVA_OPTS} \
  -jar ${_UMLET_HOME}/umlet.jar ${_UMLET_OPTS}
