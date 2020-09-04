-- 数据导入
-- VMS_TRANS_BATCH 导入批次
-- VMS_TRANS_INFO_IMP_DATA 批次数据
-- 根据需求重新创建 VMS_TRANS_INFO_IMP_DATA 表
rename VMS_TRANS_INFO_IMP_DATA to VMS_TRANS_INFO_IMP_DATA_BAK;
create table VMS_TRANS_INFO_IMP_DATA
(
  IMP_ID   				VARCHAR2(50) not null,
  IMP_BATCH_ID   		VARCHAR2(100) not null,
  IMP_STATUS     		VARCHAR2(2) not null,
  CUSTOMER_ID			VARCHAR2(50),
  TRANS_ID   			VARCHAR2(50),
  TRANS_DATE			VARCHAR2(20),
  TRANS_TYPE			VARCHAR2(50),
  TRANS_AMT	    		VARCHAR2(16),
  TAX_FLAG   			VARCHAR2(10),
  BANK_CODE  			VARCHAR2(20),
  FAPIAO_TYPE			VARCHAR2(10),
  IS_REVERSE 			VARCHAR2(10),
  REVERSE_TRANS_ID  VARCHAR2(50),
  NARRATIVE1        	VARCHAR2(250),
  NARRATIVE2        	VARCHAR2(250),
  MESSAGE        		VARCHAR2(1000),
  constraint PK_V1S_T3S_I2O_I1P_D2A primary key (IMP_ID, IMP_BATCH_ID)
);
comment on table VMS_TRANS_INFO_IMP_DATA is '交易导入明细表';
comment on column VMS_TRANS_INFO_IMP_DATA.IMP_ID is '逻辑主键';
comment on column VMS_TRANS_INFO_IMP_DATA.IMP_BATCH_ID is '导入批次主键';
comment on column VMS_TRANS_INFO_IMP_DATA.IMP_STATUS is '状态：01 未校验 | 02 校验通过 | 03 校验未通过';
comment on column VMS_TRANS_INFO_IMP_DATA.CUSTOMER_ID is '客户号';
comment on column VMS_TRANS_INFO_IMP_DATA.TRANS_ID is '交易流水号，与 VMS_TRANS_INFO 表 TRANS_ID 匹配';
comment on column VMS_TRANS_INFO_IMP_DATA.TRANS_DATE is '交易时间';
comment on column VMS_TRANS_INFO_IMP_DATA.TRANS_TYPE is '交易类型';
comment on column VMS_TRANS_INFO_IMP_DATA.TRANS_AMT is '交易金额';
comment on column VMS_TRANS_INFO_IMP_DATA.TAX_FLAG is '是否含税，Y 是 | N 否';
comment on column VMS_TRANS_INFO_IMP_DATA.BANK_CODE is '交易发生机构';
comment on column VMS_TRANS_INFO_IMP_DATA.FAPIAO_TYPE is '发票类型';
comment on column VMS_TRANS_INFO_IMP_DATA.IS_REVERSE is '是否冲账，Y 是 | N 否';
comment on column VMS_TRANS_INFO_IMP_DATA.REVERSE_TRANS_ID is '原始业务流水号';
comment on column VMS_TRANS_INFO_IMP_DATA.NARRATIVE1 is '交易描述 1';
comment on column VMS_TRANS_INFO_IMP_DATA.NARRATIVE2 is '交易描述 2';
comment on column VMS_TRANS_INFO_IMP_DATA.MESSAGE is '存储各类信息，如校验信息';

-- 数据导入审核菜单
insert into u_base_menu (SYSTEM_ID, MENU_ID, MENU_NAME, TARGET, URL, IMG_SRC, ORDER_NUM, DISPLAY, ENABLED, MENU_ENAME)
values ('00802', '0002.0015', '数据导入审核', 'mainFramePage', 'http://$address$:$port$/vmss/listAuditImpdata.action?statusList=1&statusList=2&fromFlag=menu', '', 1, 'YES', 'YES', 'ename');
-- 更新数据导入排序
update u_base_menu bm set bm.order_num = 0 where bm.system_id = '00802' and t.menu_id = '0002.0001';



-- VMS_TRANS_INFO
-- 增加字段标识数据来源
alter table VMS_TRANS_INFO add DATA_SOURCES VARCHAR2(2);
comment on column VMS_TRANS_INFO.DATA_SOURCES is '数据来源，99 手工录入';
