export CHANNEL_NAME=${1}
#创建目录
mkdir /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}
#制作通道配置文件
configtxgen -profile ThreeOrgsChannel -outputCreateChannelTx /usr/local/home/configtx/channel-artifacts/${CHANNEL_NAME}/${CHANNEL_NAME}.tx -channelID ${CHANNEL_NAME}

