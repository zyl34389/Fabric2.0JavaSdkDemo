#节点环境变量
CORE_PEER_ADDRESS=peer2-org1.txhy.com:7502
CORE_PEER_TLS_CERT_FILE=/usr/local/home/org1/peer2/tls-msp/signcerts/cert.pem
CORE_PEER_TLS_KEY_FILE=/usr/local/home/org1/peer2/tls-msp/keystore/key.pem
CORE_PEER_TLS_ROOTCERT_FILE=/usr/local/home/org1/peer2/tls-msp/tlscacerts/tls-0-0-0-0-7052.pem

#组织环境变量
CORE_PEER_MSPCONFIGPATH=/usr/local/home/org1/admin/msp
CORE_PEER_LOCALMSPID="org1MSP"

#通道名称
CHANNEL_NAME=${1}
#进入通道配置文件目录
cd /usr/local/home/configtx/channel-artifacts/$CHANNEL_NAME

peer channel join -b $CHANNEL_NAME.block
