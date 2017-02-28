#! /bin/sh
set -eux

H=root@games.alexecollins.com
D=/var/phoebus-games
U=wallet

ssh $H "[ -e $D ] || mkdir $D"
ssh $H "adduser --system $U || true"

cat src/main/bin/service target/$U.jar > target/$U-exec.jar

rsync -avz --progress target/$U-exec.jar $H:/var/phoebus-games/$U.jar

ssh $H "echo \"JAVA_OPTS='-Dserver.port=9090 -Ddatasource.username=wallet -Ddatasource.password=wallet'\" > $D/$U.conf && chmod +x $D/$U.jar && ln -sf $D/$U.jar /etc/init.d/$U && chown $U /etc/init.d/$U && /etc/init.d/$U restart"
