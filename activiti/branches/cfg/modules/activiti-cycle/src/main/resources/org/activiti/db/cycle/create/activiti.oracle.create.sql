create table ACT_CY_CONFIG (
	ID_ NVARCHAR2(64),
    VALUE_ clob,
    REV_ INTEGER,
    primary key (ID_)
);

create table ACT_CY_LINK (
	ID_ NVARCHAR2(255) NOT NULL,
	SOURCE_CONNECTOR_ID_ NVARCHAR2(255),
	SOURCE_ARTIFACT_ID_ NVARCHAR2(550),
	SOURCE_ELEMENT_ID_ NVARCHAR2(255) DEFAULT NULL,
	SOURCE_ELEMENT_NAME_ NVARCHAR2(255) DEFAULT NULL,
	SOURCE_REVISION_ INTEGER DEFAULT NULL,
	TARGET_CONNECTOR_ID_ NVARCHAR2(255),	
	TARGET_ARTIFACT_ID_ NVARCHAR2(550),
	TARGET_ELEMENT_ID_ NVARCHAR2(255) DEFAULT NULL,
	TARGET_ELEMENT_NAME_ NVARCHAR2(255) DEFAULT NULL,
	TARGET_REVISION_ INTEGER DEFAULT NULL,
	LINK_TYPE_ NVARCHAR2(255),
	COMMENT_ NVARCHAR2(1000),
	LINKED_BOTH_WAYS_ NUMBER(1,0) CHECK (LINKED_BOTH_WAYS_ IN (1,0)),
	primary key(ID_)
);

create table ACT_CY_PEOPLE_LINK (
	ID_ NVARCHAR2(255) NOT NULL,
	SOURCE_CONNECTOR_ID_ NVARCHAR2(255),
	SOURCE_ARTIFACT_ID_ NVARCHAR2(550),
	SOURCE_REVISION_ bigint DEFAULT NULL,
	USER_ID_ NVARCHAR2(255),
	GROUP_ID_ NVARCHAR2(255),
	LINK_TYPE_ NVARCHAR2(255),
	COMMENT_ NVARCHAR2(1000),
	primary key(ID_)
);

create table ACT_CY_TAG (
	ID_ NVARCHAR2(255),
	NAME_ NVARCHAR2(700),
	CONNECTOR_ID_ NVARCHAR2(255),
	ARTIFACT_ID_ NVARCHAR2(550),
	ALIAS_ NVARCHAR2(255),
	primary key(ID_)	
);

create table ACT_CY_COMMENT (
	ID_ NVARCHAR2(255) NOT NULL,
	CONNECTOR_ID_ NVARCHAR2(255) NOT NULL,
	ARTIFACT_ID_ NVARCHAR2(550) NOT NULL,
	ELEMENT_ID_ NVARCHAR2(255) DEFAULT NULL,
	CONENT_ NVARCHAR2(5000) NOT NULL,
	AUTHOR_ NVARCHAR2(255),
	DATE_ TIMESTAMP(6) NOT NULL,
	ANSWERED_COMMENT_ID_ NVARCHAR2(255) DEFAULT NULL,
	primary key(ID_)
);

create index ACT_CY_IDX_COMMENT on ACT_CY_COMMENT(ANSWERED_COMMENT_ID_);
alter table ACT_CY_COMMENT 
    add constraint FK_CY_COMMENT_COMMENT 
    foreign key (ANSWERED_COMMENT_ID_) 
    references ACT_CY_COMMENT (ID_);
