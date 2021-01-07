1.首先执行1makeChannelFile_setAnchorPeers.sh脚本,后面加通道名称。可以生成通道配置文件，更新背书节点配置。
  ./1makeChannelFile_setAnchorPeers.sh newChannelName
2.进入Docker环境
  ./2cdDocker.sh 
3.创建通道，把全部节点加入通道，并设置背书节点，后面需要加通道名称
  ./3createChannel_joinPees_setAnchors.sh newChannelName
