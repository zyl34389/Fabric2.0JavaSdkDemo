CHANNEL_NAME=${1}
cd /usr/local/home/configtx/channel-artifacts/$CHANNEL_NAME
ORDERPEM=/usr/local/home/org0/orderers/orderer1-org0/tls-msp/tlscacerts/tls-0-0-0-0-7052.pem

CORE_PEER_MSPCONFIGPATH=/usr/local/home/org1/admin/msp
CORE_PEER_ADDRESS=peer1-org1.txhy.com:7501
CORE_PEER_LOCALMSPID="org1MSP"
CORE_PEER_TLS_ROOTCERT_FILE=/usr/local/home/org1/peer1/tls-msp/tlscacerts/tls-0-0-0-0-7052.pem

peer channel create -o orderer1-org0.txhy.com:8050 -c $CHANNEL_NAME -f ./${CHANNEL_NAME}.tx --tls true --cafile $ORDERPEM

