alter table ACT_ID_MEMBERSHIP 
    drop CONSTRAINT ACT_FK_MEMB_GROUP;
    
alter table ACT_ID_MEMBERSHIP 
    drop CONSTRAINT ACT_FK_MEMB_USER;

drop table  ACT_ID_MEMBERSHIP;
drop table  ACT_ID_GROUP;
drop table  ACT_ID_USER;
