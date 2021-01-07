#节点环境变量
CORE_PEER_MSPCONFIGPATH=/usr/local/home/org1/admin/msp
CORE_PEER_ADDRESS=peer1-org1.txhy.com:7501
CORE_PEER_LOCALMSPID="org1MSP"
CORE_PEER_TLS_ROOTCERT_FILE=/usr/local/home/org1/peer1/tls-msp/tlscacerts/tls-0-0-0-0-7052.pem

#通道名称
CHANNEL_NAME=${1}
#进入通道配置文件目录
cd /usr/local/home/configtx/channel-artifacts/$CHANNEL_NAME

#将节点加入通道
peer channel join -b $CHANNEL_NAME.block 
