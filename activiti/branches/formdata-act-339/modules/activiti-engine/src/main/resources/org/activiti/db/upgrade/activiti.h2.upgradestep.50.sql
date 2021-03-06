alter table ACT_RU_VARIABLE 
add TASK_ID_ varchar(64);

alter table ACT_HI_DETAIL 
add TASK_ID_ varchar(64);

create table ACT_HI_TASKINST (
    ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64),
    TASK_DEF_KEY_ varchar(255),
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    NAME_ varchar(255),
    DESCRIPTION_ varchar(255),
    ASSIGNEE_ varchar(64),
    START_TIME_ timestamp not null,
    END_TIME_ timestamp,
    DURATION_ bigint,
    DELETE_REASON_ varchar(255),
    primary key (ID_)
);

update ACT_GE_PROPERTY
set 
  VALUE_ = '5.2-SNAPSHOT',
  REV_ = 2
where
  NAME_ = 'schema.version';