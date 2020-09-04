-- Add comments to the columns 
comment on column VMS_TRANS_INFO.amt_cny
  is '金额_人民币(价税合计)';
comment on column VMS_TRANS_INFO.tax_amt_cny
  is '税额_人民币';

-- Add/modify columns 
alter table VMS_TRANS_INFO modify short_and_over NUMBER(1,8);
