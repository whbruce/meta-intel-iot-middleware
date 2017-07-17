#! /bin/sh
### BEGIN INIT INFO
# Provides:          mosquitto
# Required-Start:    $network $syslog $named $local_fs $remote_fs
# Required-Stop:     $network $syslog $named $local_fs $remote_fs
# Should-Start:      dahdi misdn lcr wanrouter mysql postgresql
# Should-Stop:       dahdi misdn lcr wanrouter mysql postgresql
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Mosquitto broker
# Description:       Mosquitto Open Source message broker
### END INIT INFO

# The definition of actions: (From LSB 3.1.0)
# start         start the service
# stop          stop the service
# restart       stop and restart the service if the service is already running,
#               otherwise start the service
# status    print the current status of the service


# PATH should only include /usr/* if it runs after the mountnfs.sh script
PATH=/sbin:/usr/sbin:/bin:/usr/bin

DESC="Mosquitto message broker"
NAME="mosquitto"
DAEMON=/usr/sbin/$NAME
DAEMON_ARGS="-c /etc/mosquitto/mosquitto.conf"
PIDFILE=/var/run/$NAME.pid

. /etc/init.d/functions || exit 1

log_end_msg() {
    if [ $1 -eq 0 ]; then
        success $*
    else
        failure $*
    fi
}

# Exit if the package is not installed
[ -x "$DAEMON" ] || exit 0

# Read configuration variable file if it is present
[ -r /etc/default/$NAME ] && . /etc/default/$NAME
 
#
# Function that starts the daemon/service
#
do_start() {
    echo "Starting $DESC ..."

    start-stop-daemon --start --background --chuid mosquitto \
                      --name $NAME --make-pidfile --pidfile $PIDFILE \
                      --startas /bin/bash -- \
                      -c "exec $DAEMON $DAEMON_ARGS >/dev/null 2>&1"

    status="0"
    sleep 1
    pidofproc $DAEMON >/dev/null || status="$?"
    log_end_msg $status 
    return $status
}

#
# Function that stops the daemon/service
#
do_stop() {
    echo "Stopping $DESC ..."

    status="0"
    start-stop-daemon --stop --signal INT --quiet --chuid mosquitto \
                      --exec $DAEMON --pidfile $PIDFILE --retry 30 \
                      --oknodo || status="$?"

    log_end_msg $status 
    return $status
}

#
# Function that shows the daemon/service status
#
status_of_proc () {
    local pid status

    status=0
    # pidof output null when no program is running, so no "2>/dev/null".
    pid=`pidofproc $NAME` || status=$?
    case $status in
    0)
        echo "$DESC is running ($pid)."
        exit 0
        ;;
    *)
        echo "$DESC is not running." >&2
        exit $status
        ;;
    esac
}

case "$1" in
start)
    do_start
    ;;
stop)
    do_stop || exit $?
    ;;
status)
    status_of_proc
    ;;
restart)
    # Always start the service regardless the status of do_stop
    do_stop
    do_start
    ;;
*)
    echo "Usage: $0 {start|stop|status|restart}" >&2
    exit 3
    ;;
esac
