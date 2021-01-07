export CHANNEL_NAME=${1}
#创建目录
mkdir /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}

#制作通道配置文件
configtxgen -profile ThreeOrgsChannel -outputCreateChannelTx /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}/${CHANNEL_NAME}.tx -channelID ${CHANNEL_NAME}

更新背书节点配置
# 设置org1配置
export orgmsp=org1MSP
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}/${orgmsp}anchors.tx -channelID ${CHANNEL_NAME} -asOrg ${orgmsp}
# 设置org2配置
export orgmsp=org2MSP
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}/${orgmsp}anchors.tx -channelID ${CHANNEL_NAME} -asOrg ${orgmsp}
# 设置org3配置
export orgmsp=org3MSP
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}/${orgmsp}anchors.tx -channelID ${CHANNEL_NAME} -asOrg ${orgmsp}

CHANNEL_NAME=${1}
peer channel join -b $CHANNEL_NAME.block
