CORE_PEER_ADDRESS=peer3-org1.txhy.com:7503
CORE_PEER_TLS_CERT_FILE=/usr/local/home/org1/peer3/tls-msp/signcerts/cert.pem
CORE_PEER_TLS_KEY_FILE=/usr/local/home/org1/peer3/tls-msp/keystore/key.pem
CORE_PEER_TLS_ROOTCERT_FILE=/usr/local/home/org1/peer3/tls-msp/tlscacerts/tls-0-0-0-0-7052.pem

CORE_PEER_MSPCONFIGPATH=/usr/local/home/org1/admin/msp
CORE_PEER_LOCALMSPID="org1MSP"

CHANNEL_NAME=${1}
cd /usr/local/home/configtx/channel-artifacts/$CHANNEL_NAME

peer channel join -b $CHANNEL_NAME.block
