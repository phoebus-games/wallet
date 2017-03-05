

    brew install postgresql
    postgres -D /usr/local/var/postgres 
    createdb wallet

Load test:

    java -Xmx256m -jar target/wallet.jar 

    while `true` ; do curl localhost:8080/wallets/0 -f -H "Authorization: Basic d2ViOndlYg=="  ; done

    while `true` ; do curl localhost:8080/wallets/0/transactions -f -H "Authorization: Basic cm91bGV0dGU6cm91bGV0dGU=" -H "Content-Type: application/json" -d '{"amount": 0.01, "category": "PAYOUT"}'  ; done
