-- chat-server 최소 DDL: auth(AuthMapper/Auth-mapper.xml)와 file(FileMapper/File-mapper.xml)이
-- 참조하는 테이블. motive-server의 USERS/SOCIAL_ACCOUNTS/CMM_FILE 구조를 그대로 재사용한다.
-- CHAT 스키마(신규 유저) 기준. motive-server 원본 리포지토리에는 이 DDL이 커밋되어 있지 않아
-- Auth-mapper.xml / File-mapper.xml의 컬럼 참조를 근거로 새로 작성함.

CREATE TABLE USERS (
    USER_ID             NUMBER          NOT NULL,
    EMAIL               VARCHAR2(255),
    PASSWORD_HASH       VARCHAR2(255),  -- 소셜 전용 가입자는 NULL
    STATUS              VARCHAR2(20)    DEFAULT 'ACTIVE' NOT NULL,
    NAME                VARCHAR2(100),
    PHONE               VARCHAR2(20),
    "ROLE"              VARCHAR2(20)    DEFAULT 'USER' NOT NULL, -- Oracle 예약어라 quoted identifier
    REG_DT              DATE            DEFAULT SYSDATE NOT NULL,
    MOD_DT              DATE            DEFAULT SYSDATE NOT NULL,
    PROFILE_FILE_ID     NUMBER,
    LAST_LOGIN_DT       DATE,
    GENDER              VARCHAR2(10),
    BIRTH               DATE,
    NICKNAME            VARCHAR2(100),
    CONSTRAINT PK_USERS PRIMARY KEY (USER_ID)
);

CREATE SEQUENCE USERS_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE SOCIAL_ACCOUNTS (
    SOCIAL_ID           NUMBER          NOT NULL,
    USER_ID             NUMBER          NOT NULL,
    PROVIDER            VARCHAR2(20)    NOT NULL, -- GOOGLE, KAKAO, GITHUB
    PROVIDER_USER_ID    VARCHAR2(255)   NOT NULL,
    REG_DT              DATE            DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_SOCIAL_ACCOUNTS PRIMARY KEY (SOCIAL_ID),
    CONSTRAINT FK_SOCIAL_ACCOUNTS_USER FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID),
    CONSTRAINT UK_SOCIAL_ACCOUNTS UNIQUE (PROVIDER, PROVIDER_USER_ID)
);

CREATE SEQUENCE SOCIAL_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE CMM_FILE (
    FILE_ID             NUMBER          NOT NULL,
    ORG_FILE_NM         VARCHAR2(255),
    SAVE_FILE_NM        VARCHAR2(255),
    FILE_PATH           VARCHAR2(500),
    FILE_SIZE           NUMBER,
    FILE_EXT            VARCHAR2(20),
    TEMP_YN              VARCHAR2(1)     DEFAULT 'Y' NOT NULL,
    TEMP_KEY             VARCHAR2(100),
    CONSTRAINT PK_CMM_FILE PRIMARY KEY (FILE_ID)
);

CREATE SEQUENCE CMM_FILE_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;
