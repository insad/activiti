drop index ACT_IDX_EXEC_BUSKEY on ACT_RU_EXECUTION;
drop index ACT_IDX_TASK_CREATE on ACT_RU_TASK;
drop index ACT_IDX_IDENT_LNK_USER on ACT_RU_IDENTITYLINK;
drop index ACT_IDX_IDENT_LNK_GROUP on ACT_RU_IDENTITYLINK;
drop index ACT_IDX_HI_PRO_INST_END on ACT_HI_PROCINST;
drop index ACT_IDX_HI_PRO_I_BUSKEY on ACT_HI_PROCINST;
drop index ACT_IDX_HI_ACT_INST_START on ACT_HI_ACTINST;
drop index ACT_IDX_HI_ACT_INST_END on ACT_HI_ACTINST;
drop index ACT_IDX_HI_DETAIL_PROC_INST on ACT_HI_DETAIL;
drop index ACT_IDX_HI_DETAIL_ACT_INST on ACT_HI_DETAIL;
drop index ACT_IDX_HI_DETAIL_TIME on ACT_HI_DETAIL;
drop index ACT_IDX_HI_DETAIL_NAME on ACT_HI_DETAIL;


alter table ACT_GE_BYTEARRAY 
    drop FOREIGN KEY ACT_FK_BYTEARR_DEPL;

alter table ACT_RU_EXECUTION
    drop FOREIGN KEY ACT_FK_EXE_PROCINST;

alter table ACT_RU_EXECUTION 
    drop FOREIGN KEY ACT_FK_EXE_PARENT;

alter table ACT_RU_EXECUTION 
    drop FOREIGN KEY ACT_FK_EXE_SUPER;
    
alter table ACT_ID_MEMBERSHIP 
    drop FOREIGN KEY ACT_FK_MEMB_GROUP;
    
alter table ACT_ID_MEMBERSHIP 
    drop FOREIGN KEY ACT_FK_MEMB_USER;
    
alter table ACT_RU_IDENTITYLINK
    drop FOREIGN KEY ACT_FK_TSKASS_TASK;

alter table ACT_RU_TASK
	drop FOREIGN KEY ACT_FK_TASK_EXE;

alter table ACT_RU_TASK
	drop FOREIGN KEY ACT_FK_TASK_PROCINST;
	
alter table ACT_RU_TASK
	drop FOREIGN KEY ACT_FK_TASK_PROCDEF;
    
alter table ACT_RU_VARIABLE
    drop FOREIGN KEY ACT_FK_VAR_EXE;
    
alter table ACT_RU_VARIABLE
	drop FOREIGN KEY ACT_FK_VAR_PROCINST;    

alter table ACT_RU_VARIABLE
    drop FOREIGN KEY ACT_FK_VAR_BYTEARRAY;

alter table ACT_RU_JOB
    drop FOREIGN KEY ACT_FK_JOB_EXCEPTION;
    
 drop table if exists ACT_GE_PROPERTY;
 drop table if exists ACT_RU_VARIABLE;
 drop table if exists ACT_GE_BYTEARRAY;
 drop table if exists ACT_RE_DEPLOYMENT;
 drop table if exists ACT_RU_IDENTITYLINK;
 drop table if exists ACT_RU_TASK;
 drop table if exists ACT_RE_PROCDEF;
 drop table if exists ACT_RU_EXECUTION;
 drop table if exists ACT_ID_MEMBERSHIP;
 drop table if exists ACT_ID_GROUP;
 drop table if exists ACT_ID_USER;
 drop table if exists ACT_RU_JOB;
 drop table if exists ACT_HI_PROCINST;
 drop table if exists ACT_HI_ACTINST;
 drop table if exists ACT_HI_DETAIL;
 