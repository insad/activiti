drop index ACT_IDX_BYTEAR_DEPL ;
drop index ACT_IDX_EXE_PROCINST ;
drop index ACT_IDX_EXE_PARENT ;
drop index ACT_IDX_EXE_SUPER;
drop index ACT_IDX_MEMB_GROUP;
drop index ACT_IDX_MEMB_USER;
drop index ACT_IDX_TSKASS_TASK;
drop index ACT_IDX_TASK_EXEC;
drop index ACT_IDX_TASK_PROCINST;
drop index ACT_IDX_TASK_PROCDEF;
drop index ACT_IDX_VAR_EXE;
drop index ACT_IDX_VAR_PROCINST;
drop index ACT_IDX_VAR_BYTEARRAY;
drop index ACT_IDX_JOB_EXCEPTION;

drop index ACT_IDX_EXEC_BUSKEY;
drop index ACT_IDX_TASK_CREATE;
drop index ACT_IDX_IDENT_LNK_USER;
drop index ACT_IDX_IDENT_LNK_GROUP;
drop index ACT_IDX_EVENT_SUBSCR;


alter table ACT_GE_BYTEARRAY 
    drop constraint ACT_FK_BYTEARR_DEPL;

alter table ACT_RU_EXECUTION
    drop constraint ACT_FK_EXE_PROCINST;

alter table ACT_RU_EXECUTION 
    drop constraint ACT_FK_EXE_PARENT;

alter table ACT_RU_EXECUTION 
    drop constraint ACT_FK_EXE_SUPER;
    
alter table ACT_RU_IDENTITYLINK
    drop constraint ACT_FK_TSKASS_TASK;

alter table ACT_RU_IDENTITYLINK
    drop constraint ACT_FK_ATHRZ_PROCEDEF;

alter table ACT_RU_TASK
	drop constraint ACT_FK_TASK_EXE;

alter table ACT_RU_TASK
	drop constraint ACT_FK_TASK_PROCINST;
	
alter table ACT_RU_TASK
	drop constraint ACT_FK_TASK_PROCDEF;
    
alter table ACT_RU_VARIABLE
    drop constraint ACT_FK_VAR_EXE;
    
alter table ACT_RU_VARIABLE
    drop constraint ACT_FK_VAR_PROCINST;
    
alter table ACT_RU_VARIABLE
    drop constraint ACT_FK_VAR_BYTEARRAY;
    
alter table ACT_RU_JOB
    drop constraint ACT_FK_JOB_EXCEPTION;
    
alter table ACT_RU_EVENT_SUBSCR
    drop constraint ACT_FK_EVENT_EXEC; 
    
drop table ACT_GE_PROPERTY;
drop table ACT_GE_BYTEARRAY;
drop table ACT_RE_DEPLOYMENT;
drop table ACT_RE_PROCDEF;
drop table ACT_RU_EXECUTION;
drop table ACT_RU_JOB;
drop table ACT_RU_TASK;
drop table ACT_RU_IDENTITYLINK;
drop table ACT_RU_VARIABLE;
drop table ACT_RU_EVENT_SUBSCR;
