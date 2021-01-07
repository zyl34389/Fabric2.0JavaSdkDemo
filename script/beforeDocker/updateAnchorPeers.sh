CHANNEL_NAME=${1}
#更新背书节点配置
# 设置org1配置
export orgmsp=org1MSP
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}/${orgmsp}anchors.tx -channelID ${CHANNEL_NAME} -asOrg ${orgmsp}
# 设置org2配置
export orgmsp=org2MSP
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}/${orgmsp}anchors.tx -channelID ${CHANNEL_NAME} -asOrg ${orgmsp}
# 设置org3配置
export orgmsp=org3MSP
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}/${orgmsp}anchors.tx -channelID ${CHANNEL_NAME} -asOrg ${orgmsp}


