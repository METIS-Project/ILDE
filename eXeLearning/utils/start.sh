#!/bin/sh
export EXELEARNING_LOCATION=/opt/exelearning/
export EXELEARNING_PASSWORD="mypass"
export HOME=/var/local/exelearning
export EXELEARNING_USER=twistd
export PYTHONPATH=$(which python2)

cd $EXELEARNING_LOCATION

exec $PYTHONPATH ${EXELEARNING_LOCATION}utils/twistd --pidfile=/var/run/exelearning.pid --syslog --prefix exelearning -u $(id -u ${EXELEARNING_USER}) -g $(id -g ${EXELEARNING_USER}) -n -y exe/exe.tac