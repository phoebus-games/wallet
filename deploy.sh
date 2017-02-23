#! /bin/sh
set -eux

H=root@games.alexecollins.com
D=/var/helios-games
U=wallet

ssh $H "[ -e $D ] || mkdir $D"
ssh $H "adduser --system $U || true"

rsync -avz --progress target/$U.jar $H:/var/helios-games/$U.jar

ssh $H "echo \"JAVA_OPTS='-Dserver.port=9090 -Dspring.datasource.username=wallet -Dspring.datasource.password=wallet'\" > $D/$U.conf && chmod +x $D/$U.jar && ln -sf $D/$U.jar /etc/init.d/$U && chown $U /etc/init.d/$U && /etc/init.d/$U restart"
