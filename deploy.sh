#! /bin/sh
set -eux

H=root@games.alexecollins.com
D=/var/helios-games
U=wallet

ssh $H "[ -e $D ] || mkdir $D"
ssh $H "adduser --system $U || true"

rsync -avz --progress target/$U-1.0.0-SNAPSHOT.jar $H:/var/helios-games/$U.jar

ssh $H "chmod +x $D/$U.jar && ln -sf $D/$U.jar /etc/init.d/$U && chown $U /etc/init.d/$U && systemctl daemon-reload && systemctl start $U && systemctl restart $U"
