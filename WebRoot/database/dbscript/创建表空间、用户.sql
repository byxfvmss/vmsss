-- 创建表空间
-- 创建表空间 VMSS_IST
CREATE TABLESPACE VMSS_IST 
DATAFILE 'D:/Soft/Oracle/oradata/vms/VMSS_IST_0001.ORA' SIZE 256M 
AUTOEXTEND ON NEXT 64M MAXSIZE 1024M 
EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;

-- 创建用户
-- 创建 fmss_ist 用户
create user vmss_ist identified by VMSS_IST 
default tablespace VMSS_IST 
temporary tablespace TEMP;
-- 为 fmss_ist 用户赋权
GRANT 
	create session, create trigger, create procedure, 
	create sequence, create table, create view, 
	create public synonym, drop public synonym 
TO vmss_ist;
GRANT dba TO vmss_ist;
-- 设置 fmss 可以无限使用表空间
ALTER USER vmss_ist QUOTA UNLIMITED ON VMSS_IST;