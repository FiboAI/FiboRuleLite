DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '删除标志：0未删除，1已删除',
  `app_name` varchar(50) DEFAULT NULL COMMENT 'app名称',
  `app_code` varchar(50) DEFAULT NULL COMMENT 'appcode,手动输入',
  `descriptions` varchar(2550) DEFAULT NULL COMMENT '描述',
  `status` int(11) DEFAULT '1' COMMENT '状态：1有效，0无效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建用户id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '最新更新用户id',
  `del_flag` int(11) DEFAULT '0' COMMENT '删除标志:0未删除，1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COMMENT='应用表';

-- ----------------------------
-- Table structure for t_engine
-- ----------------------------
DROP TABLE IF EXISTS `t_engine`;
CREATE TABLE `t_engine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联id',
  `engine_name` varchar(50) DEFAULT NULL COMMENT '引擎名称',
  `engine_code` varchar(50) DEFAULT NULL COMMENT '引擎code,手动输入',
  `scene` varchar(255) DEFAULT NULL COMMENT '场景名称',
  `descriptions` varchar(2550) DEFAULT NULL COMMENT '引擎描述',
  `boot_status` int(11) DEFAULT '0' COMMENT '发布状态:0创建，1发布，2取消发布',
  `status` int(11) DEFAULT '1' COMMENT '状态：0无效，1有效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '最新更新人id',
  `del_flag` int(11) DEFAULT '0' COMMENT '删除标志:0未删除，1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COMMENT='引擎表';

-- ----------------------------
-- Table structure for t_engine_node
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_node`;
CREATE TABLE `t_engine_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '节点id',
  `engine_id` bigint(20) NOT NULL COMMENT '关联引擎id',
  `node_name` varchar(50) DEFAULT NULL COMMENT '节点名称（客户输入）',
  `bean_name` varchar(50) DEFAULT NULL COMMENT '组件注解上的名称（不可重复）',
  `node_code` varchar(255) DEFAULT NULL COMMENT '节点编码(自动编码)',
  `node_type` int(11) DEFAULT NULL COMMENT '节点类型：开始节点、结束节点、普通节点、IF节点、switch节点、并行节点、聚合节点',
  `pre_nodes` varchar(50) DEFAULT NULL COMMENT '前置节点，多个节点以逗号分隔',
  `next_nodes` varchar(50) DEFAULT NULL COMMENT '后置节点，多个节点以逗号分隔',
  `node_config` varchar(2550) DEFAULT NULL COMMENT '节点配置信息（json类型，if/switch有lineValue，并行网关有isAny(先不做)，其他节点需设置阈值）',
  `node_x` varchar(25) DEFAULT NULL,
  `node_y` varchar(25) DEFAULT NULL,
  `node_clazz` varchar(255) DEFAULT NULL COMMENT '类全名称，包名+类名',
  `clazz_name` varchar(50) DEFAULT NULL COMMENT '类名称',
  `node_group` varchar(50) DEFAULT NULL COMMENT 'node组',
  `next_node_value` varchar(255) DEFAULT NULL COMMENT '//后续节点对应的分支值-[{"key":"Y",value:"node1"}]',
  `status` int(11) DEFAULT '1' COMMENT '状态：0无效，1有效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '最新更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '最新更新人关联id',
  `del_flag` int(11) DEFAULT '0' COMMENT '删除标志：0为删除，1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=325 DEFAULT CHARSET=utf8mb4 COMMENT='引擎节点表';