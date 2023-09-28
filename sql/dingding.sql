/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.7.26 : Database - dingding_mid
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


/*Table structure for table `departments` */

DROP TABLE IF EXISTS `departments`;

CREATE TABLE `departments` (
  `dept_id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `dept_name` varchar(50) DEFAULT NULL COMMENT '部门名',
  `leader` varchar(50) DEFAULT NULL COMMENT '部门主管',
  `parent_id` int(11) DEFAULT NULL COMMENT '父部门id',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6179679 DEFAULT CHARSET=utf8mb4 COMMENT='departments';

/*Data for the table `departments` */

insert  into `departments`(`dept_id`,`dept_name`,`leader`,`parent_id`,`created`,`updated`) values (35453,'业务部',NULL,4319868,NULL,NULL),(231535,'生产管理部',NULL,1486186,NULL,NULL),(264868,'行政人事部',NULL,1486186,NULL,NULL),(689698,'客服部',NULL,4319868,NULL,NULL),(1486186,'xx科技有限公司',NULL,0,NULL,NULL),(4319868,'销售服务部',NULL,1486186,NULL,NULL),(6179678,'研发部',NULL,1486186,NULL,NULL);

/*Table structure for table `form_groups` */

DROP TABLE IF EXISTS `form_groups`;

CREATE TABLE `form_groups` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_name` varchar(50) DEFAULT NULL COMMENT '组名',
  `sort_num` int(11) DEFAULT NULL COMMENT '排序号',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1293705220 DEFAULT CHARSET=utf8mb4 COMMENT='form_groups';

/*Data for the table `form_groups` */

insert  into `form_groups`(`group_id`,`group_name`,`sort_num`,`created`,`updated`) values (2,'测试分组',0,'2022-10-09 15:29:38','2022-10-19 15:29:43'),(1293705217,'qq',1,'2022-10-09 15:39:45','2022-10-09 15:39:45'),(1293705218,'11',1,'2022-10-09 15:41:55','2022-10-09 15:41:55'),(1293705219,'dwadwa',1,'2022-10-09 15:46:36','2022-10-09 15:46:36');

/*Table structure for table `process_templates` */

DROP TABLE IF EXISTS `process_templates`;

CREATE TABLE `process_templates` (
  `template_id` varchar(50) NOT NULL COMMENT '审批摸板ID',
  `template_name` varchar(50) DEFAULT NULL COMMENT '摸板名称',
  `settings` longtext COMMENT '基础设置',
  `form_items` longtext COMMENT '摸板表单',
  `process` longtext COMMENT 'process',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `background` varchar(50) DEFAULT NULL COMMENT '图标背景色',
  `notify` varchar(1000) DEFAULT NULL COMMENT 'notify',
  `who_commit` longtext COMMENT '谁能提交',
  `who_edit` longtext COMMENT '谁能编辑',
  `who_export` longtext COMMENT '谁能导出数据',
  `remark` varchar(200) DEFAULT NULL COMMENT 'remark',
  `group_id` int(11) DEFAULT NULL COMMENT '冗余分组id',
  `is_stop` tinyint(3) DEFAULT NULL COMMENT '是否已停用',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='process_templates';

/*Data for the table `process_templates` */

insert  into `process_templates`(`template_id`,`template_name`,`settings`,`form_items`,`process`,`icon`,`background`,`notify`,`who_commit`,`who_edit`,`who_export`,`remark`,`group_id`,`is_stop`,`created`,`updated`) values ('1580378928039415808','未命名表单222','{\"commiter\":[],\"admin\":[{\"id\":381496,\"name\":\"旅人\",\"type\":\"user\",\"sex\":false,\"selected\":false}],\"sign\":false,\"notify\":{\"types\":[\"APP\"],\"title\":\"消息通知标题\"}}','[{\"title\":\"单行文本输入\",\"name\":\"TextInput\",\"icon\":\"el-icon-edit\",\"value\":\"\",\"valueType\":\"String\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3933426667777\"}]','{\"id\":\"root\",\"parentId\":null,\"type\":\"ROOT\",\"name\":\"发起人\",\"desc\":\"任何人\",\"props\":{\"assignedUser\":[],\"formPerms\":[]},\"children\":{\"id\":\"node_501343291687\",\"parentId\":\"root\",\"props\":{},\"type\":\"CONDITIONS\",\"name\":\"条件分支\",\"children\":{\"id\":\"node_501343297290\",\"parentId\":\"node_501343291687\",\"type\":\"EMPTY\",\"children\":{}},\"branchs\":[{\"id\":\"node_501343297663\",\"parentId\":\"node_501343291687\",\"type\":\"CONDITION\",\"props\":{\"groupsType\":\"OR\",\"groups\":[{\"groupType\":\"AND\",\"cids\":[\"root\"],\"conditions\":[{\"id\":\"root\",\"title\":\"发起人\",\"valueType\":\"User\",\"compare\":\"\",\"value\":[{\"id\":381496,\"name\":\"旅人\",\"type\":\"user\",\"sex\":false,\"selected\":false}]}]}],\"expression\":\"\"},\"name\":\"条件2\",\"children\":{\"id\":\"node_545590085414\",\"parentId\":\"node_501343297663\",\"props\":{\"assignedType\":\"SELF\",\"mode\":\"AND\",\"sign\":false,\"nobody\":{\"handler\":\"TO_PASS\",\"assignedUser\":[]},\"timeLimit\":{\"timeout\":{\"unit\":\"H\",\"value\":0},\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"once\":true,\"hour\":1}}},\"assignedUser\":[],\"formPerms\":[{\"id\":\"field3933426667777\",\"title\":\"单行文本输入\",\"required\":false,\"perm\":\"R\"}],\"selfSelect\":{\"multiple\":false},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"leader\":{\"level\":1},\"role\":[],\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"formUser\":\"\"},\"type\":\"APPROVAL\",\"name\":\"审批人\",\"children\":{}}},{\"id\":\"node_501343294917\",\"parentId\":\"node_501343291687\",\"type\":\"CONDITION\",\"props\":{\"groupsType\":\"OR\",\"groups\":[{\"groupType\":\"AND\",\"cids\":[\"root\"],\"conditions\":[{\"id\":\"root\",\"title\":\"发起人\",\"valueType\":\"User\",\"compare\":\"\",\"value\":[{\"id\":381496,\"name\":\"旅人\",\"type\":\"user\",\"sex\":false,\"selected\":false}]}]}],\"expression\":\"\"},\"name\":\"条件1\",\"children\":{\"id\":\"node_545620352393\",\"parentId\":\"node_501343294917\",\"props\":{\"assignedType\":\"SELF\",\"mode\":\"AND\",\"sign\":false,\"nobody\":{\"handler\":\"TO_PASS\",\"assignedUser\":[]},\"timeLimit\":{\"timeout\":{\"unit\":\"H\",\"value\":0},\"handler\":{\"type\":\"REFUSE\",\"notify\":{\"once\":true,\"hour\":1}}},\"assignedUser\":[],\"formPerms\":[{\"id\":\"field3933426667777\",\"title\":\"单行文本输入\",\"required\":false,\"perm\":\"R\"}],\"selfSelect\":{\"multiple\":false},\"leaderTop\":{\"endCondition\":\"TOP\",\"endLevel\":1},\"leader\":{\"level\":1},\"role\":[],\"refuse\":{\"type\":\"TO_END\",\"target\":\"\"},\"formUser\":\"\"},\"type\":\"APPROVAL\",\"name\":\"审批人\",\"children\":{}}}]}}','{\"icon\":\"el-icon-eleme\",\"background\":\"#1e90ff\"}','{\"icon\":\"el-icon-eleme\",\"background\":\"#1e90ff\"}','{\"types\":[\"APP\"],\"title\":\"消息通知标题\"}','[{\"id\":\"381496\",\"name\":\"旅人\",\"selected\":false,\"sex\":\"false\",\"type\":\"user\"}]','[{\"id\":\"381496\",\"name\":\"旅人\",\"selected\":false,\"sex\":\"false\",\"type\":\"user\"}]','[{\"id\":\"381496\",\"name\":\"旅人\",\"selected\":false,\"sex\":\"false\",\"type\":\"user\"}]','备注说明33',2,0,'2022-10-13 10:04:33','2022-10-14 22:08:19');

/*Table structure for table `template_group` */

DROP TABLE IF EXISTS `template_group`;

CREATE TABLE `template_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `template_id` varchar(50) DEFAULT NULL COMMENT 'templateId',
  `group_id` int(11) DEFAULT NULL COMMENT 'groupId',
  `sort_num` int(11) DEFAULT NULL COMMENT 'sortNum',
  `created` datetime DEFAULT NULL COMMENT 'created',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='template_group';

/*Data for the table `template_group` */

insert  into `template_group`(`id`,`template_id`,`group_id`,`sort_num`,`created`) values (9,'1580378928039415808',2,0,'2022-10-13 10:04:33');

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `user_id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `pingyin` varchar(50) NOT NULL COMMENT '拼音',
  `alisa` varchar(999) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(999) DEFAULT NULL COMMENT '头像',
  `sex` tinyint(3) DEFAULT NULL COMMENT '性别',
  `department_ids` varchar(50) DEFAULT NULL COMMENT '部门id，分隔',
  `entry_date` datetime DEFAULT NULL COMMENT '入职日期',
  `leave_date` datetime DEFAULT NULL COMMENT '离职日期',
  `admin` int(11) DEFAULT NULL COMMENT '管理级别 0=主管理员 1=子管理员 2=普通员工',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61769799 DEFAULT CHARSET=utf8mb4 COMMENT='users';

/*Data for the table `users` */

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100001, 'Java1号', 'lvren', 'Java1号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100002, 'Java2号', 'lvren', 'Java2号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100003, 'Java3号', 'lvren', 'Java3号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100004, 'Java4号', 'lvren', 'Java4号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100005, 'Java5号', 'lvren', 'Java5号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100006, 'Java6号', 'lvren', 'Java6号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100007, 'Java7号', 'lvren', 'Java7号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100008, 'Java8号', 'lvren', 'Java8号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (100009, 'Java9号', 'lvren', 'Java9号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

INSERT INTO `users`(`user_id`, `user_name`, `pingyin`, `alisa`, `avatar`, `sex`, `department_ids`, `entry_date`, `leave_date`, `admin`, `created`, `updated`) VALUES (1000010, 'Java10号', 'lvren', 'Java10号', 'https://dd-static.jd.com/ddimg/jfs/t1/188230/26/28979/10654/633026fdEf64e5e84/fc5c07ab3d5eac19.png', 0, '1486186', '2022-10-20 13:33:36', '2023-10-28 13:33:39', 1, '2022-10-19 13:32:49', '2022-10-19 13:32:52');

CREATE TABLE `cc` (
                      `id` varchar(64) NOT NULL  COMMENT '主键',
                      `user_id` bigint(15)  COMMENT '用户id',
                      `process_instance_id` varchar(64) NOT NULL COMMENT '流程实例id',
                      PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='抄送';


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
