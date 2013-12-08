#!/bin/sh

# Sub Routine
usage() {
cat <<END_OF_LINE
Usage: $0 getinfo query-str offset num
       $0 getpkg pkgid

       e.g.
         $0 getinfo game 0 10
         $0 getpkg v2:com.hoge.tool:1:23
END_OF_LINE
exit 1;
}

# Main Routine
[ $# -lt 1 ] && usage
COMMAND="$1"
shift
RET=0
BASEDIR=`dirname $0 2>/dev/null`
GETINFO_OPTS="-cp ${BASEDIR}/lib/androidmarketapi-0.6.jar:${BASEDIR}/lib/protobuf-java-2.2.0.jar:${BASEDIR}/lib/jackson-core-2.3.0.jar:$BASEDIR"
GETPKG_OPTS="-cp ${BASEDIR}/lib/AndroidMarketApi.jar:${BASEDIR}/lib/protobuf-java-2.4.1.jar:${BASEDIR}/lib/jackson-core-2.3.0.jar:$BASEDIR"

case $COMMAND in
	getinfo)
		cmd="java $GETINFO_OPTS GetAppInfo ${BASEDIR}/getapp.conf $*"
		echo $cmd; eval $cmd; RET=$? ;;
	getpkg)
		cmd="java $GETPKG_OPTS GetPackages ${BASEDIR}/getapp.conf $*"
		echo $cmd; eval $cmd; RET=$? ;;
	*)
		usage; exit 2
esac
exit $RET
# __END__
