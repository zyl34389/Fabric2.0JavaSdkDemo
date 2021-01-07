1.configtx.yaml是通道的配置文件
2.根据通道配置文件生成通道配置文件,后面需要加通道名称
  ./createChannelConfigFile.sh newChannelName
3.更新背书节点配置,后面需要加通道名称
  ./updateAnchorPeers.sh newChannelName
