CORE_PEER_LOCALMSPID=org3MSP
CORE_PEER_ADDRESS=peer1-org3.txhy.com:7601
CORE_PEER_MSPCONFIGPATH=/usr/local/home/org3/admin/msp
CORE_PEER_TLS_CERT_FILE=/usr/local/home/org3/peer1/tls-msp/signcerts/cert.pem
CORE_PEER_TLS_KEY_FILE=/usr/local/home/org3/peer1/tls-msp/keystore/key.pem
CORE_PEER_TLS_ROOTCERT_FILE=/usr/local/home/org3/peer1/tls-msp/tlscacerts/tls-0-0-0-0-7052.pem


CHANNEL_NAME=${1}
cd /usr/local/home/configtx/channel-artifacts/$CHANNEL_NAME

peer channel join -b $CHANNEL_NAME.block 
