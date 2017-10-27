/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

SET FOREIGN_KEY_CHECKS = 0;

insert into `authorization_info` values('821', '资源管理-资源实例-获取运维操作资源树', 'BASE_RESOURCE_TREE_BYATM', '1', '', '1509095645645');

insert into `authorization_info` values('817', 'ATM-自动化运维-总览-查看', 'ATM-AUTOMATIC-OVERVIEW-FIND-MENU', '1', '', '1509084687680');

insert into `authorization_info` values('816', '报表管理-告警统计-按监控项查询', 'REPORT-ALERT-SINGLE-INSPECTIONITEM-FIND-MENU', '1', '', '1509077174269');

insert into `authorization_info` values('815', '报表管理-告警统计-按实例查询', 'REPORT-ALERT-SINGLE-INSTANCES-FIND-MENU', '1', '', '1509077148320');

insert into `authorization_info` values('820', '资源管理-资源实例-获取资源管理资源树', 'BASE_RESOURCE_TREE_BYRESOURCE', '1', '', '1509095603922');

update `authorization_info` set `authorization_id` = '754', `authorization_name` = '报表管理-告警统计-按监控项', `authorization_code` = 'REPORT-ALERT-SINGLE-INSPECTIONITEM-MENU', `authorization_status` = '1', `description` = '报表管理-告警统计-按监控项查询', `create_time` = '1508310709000' where `authorization_id` = '754';

update `authorization_info` set `authorization_id` = '753', `authorization_name` = '报表管理-告警统计-按实例', `authorization_code` = 'REPORT-ALERT-SINGLE-INSTANCES-MENU', `authorization_status` = '1', `description` = '报表管理-告警统计-按实例查询', `create_time` = '1508310709000' where `authorization_id` = '753';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
