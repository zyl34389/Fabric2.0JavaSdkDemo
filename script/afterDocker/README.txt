1.根据通道配置文件生成通道,后面加通道名称
  ./createChannel.sh newChannelName
2.把所有节点加入通道
  ./joinPeers.sh newChannelName
3.将peer1-org1 peer1-org2 peer1-org3 设置为背书节点
  ./setAnchors.sh newChannelName
