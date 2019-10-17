-- 시퀀스 생성
    -- 모임 코드
create sequence moim_seq nocache nocycle;
drop sequence moim_seq;
    -- 글 번호
create sequence board_seq nocache nocycle;
drop sequence board_seq;
    -- 덧글 번호
create sequence re_seq nocache nocycle;
drop sequence re_seq;
    -- 일정 코드
create sequence sche_seq nocache nocycle;
drop sequence sche_seq;

commit;

------------------------------------------------------
-- 사용자 정보 테이블
create table moim_user (
    user_id varchar2(4000) primary key,    -- 아이디
    user_img varchar2(4000)                -- 변경된 프로필 사진
);

insert into moim_user values(
'1153817244', 
'hong.jpg');

select * from moim_user;

select * from moim_user where user_id = '1150321350';

drop table moim_user purge;

commit;


-----------------모임 생성---------------------------
-- 마지막으로 생성된 모임 이름 얻기
select max(tname) as tname from tab 
where tname like upper('zz%');

-- 모임 테이블 생성
create table zz10000 (
    moimcode number primary key,    -- 모임 코드
    loca varchar2(100),             -- 위치
    moimname varchar2(1000),        -- 모임명
    prod varchar2(4000),            -- 모임소개
    pic varchar2(500),              -- 사진 파일명
    color varchar2(100)             -- 색상
);

insert into zz10000 values(
moim_seq.nextval,
'강원도',
'정동진 해돋이',
'정동진 해돋이 보면서 고기 먹자',
'banner.jpg',
'wine'
);

select * from zz10039;

-- 모임 정보 및 가입 인원수
select zz10000.*, 
(select count(id) from moimmem where moimcode = zz10000.moimcode) 
as count 
from zz10000;

select tname from tab;
drop table zz114 purge;

commit;


------------------------------------------------------
-- 모임 멤버
create table moimmem (
    id varchar2(50) not null,       -- 사용자 아이디
    moimcode number not null,       -- 모임 코드
    fav varchar2(10) not null,      -- 좋아요 여부(true OR false)
    permit number                   -- 모임에서의 권한레벨
);

-- 입력 쿼리
insert into moimmem values(
'1156076264', 
28, 
'true',
3);

select * from moimmem where moimcode=28;

select * from moimmem where id='1149515249';

delete from moimmem where id='1146095951' and moimcode = 3;

drop table moimmem purge;

commit;
rollback;


-----------------------------------------------------
-- 모임 테이블 이름 테이블
create table moimtname (
    moimcode number not null,        -- 모임 코드
    tname varchar2(50) not null      -- 테이블 이름
);

insert into moimtname values(
    1,
    'zz10000'
);

select * from moimtname;

drop table moimtname purge;


----------------------------------------------------
-- 게시판 테이블
create table boardlist(
    listnum number primary key,     -- 게시글 번호
    id varchar2(50) not null,       -- 작성자 아이디
    moimcode number not null,       -- 글이 작성된 모임 코드
    subject varchar2(500),          -- 제목
    content varchar2(4000),         -- 내용
    filename varchar2(4000),        -- 저장된 사진 이름
    thumb varchar2(4000),           -- 사진의 썸네일 이름
    editdate date not null,         -- 작성/수정된 날짜
    lev number not null             -- 공지 등의 분류 번호(레벨)
);

-- 입력 쿼리
insert into boardlist values(
board_seq.nextval,
'1153908310',
3,
'글 일반',
'글 내용',
'board_pic.jpg',
'board_thumb.jpg',
sysdate,
2
);

select * from boardlist;

drop table boardlist purge;
commit;


------------------------------------------------------------
-- 덧글 테이블
create table redat(
    renum number primary key,       -- 답글 번호
    listnum number not null,        -- 원글 번호
    id varchar2(50),                -- 작성자 아이디
    moimcode number not null,       -- 작성된 모임 코드
    reple varchar2(4000),           -- 내용
    redate date not null            -- 답글 생성 날짜
);

-- 입력 쿼리
insert into redat values(
re_seq.nextval,
1,
'1153908310',
1,
'덧 글',
sysdate
);

select * from redat order by renum desc;

drop table redat purge;
commit;


-------------------------------------------------------------
-- 일정 테이블
create table schedule (
    sch_schnum number primary key,      -- 일정코드
    sch_moimcode number not null,       -- 일정이 등록된 모임 코드
    sch_year varchar2(100) not null,    -- 년-월-일0
    sch_month varchar2(100) not null,   -- 년-월-일0
    sch_day varchar2(100) not null,     -- 년-월-일0
    sch_time varchar2(100) not null,    -- 시
    sch_title varchar2(500),            -- 일정명칭(제목)
    sch_sub varchar2(4000),             -- 일정 내용
    sch_amount varchar2(4000),          -- 회비
    sch_lat number,                     -- 위도
    sch_lot number                      -- 경도
);


-- 입력 쿼리
insert into schedule values(
sche_seq.nextval,
19,
'2019',
'08',
'30',
'18:45',
'8월 정모',
'모임은 이젠 곱창 입니다.',
'50000',
36.1234,
125.512
);

select * from schedule;

select * from schedule where sch_schnum = 24;

select * from schedule where sch_moimcode = 3
order by sch_year, sch_month, sch_day, to_timestamp(sch_time, 'hh24:mi');

delete from schedule where sch_schnum=24;

drop table schedule purge;

commit;

create table schemem (
    schnum number not null,         -- 일정 코드
    id varchar2(4000) not null,     -- 사용자 id
    todo varchar2(4000),            -- 할 일
    ex varchar2(4000),              -- 할 일 상세
    isdo varchar2(4000),            -- 수행 여부
    amount varchar2(4000),          -- 회비
    ispay varchar2(4000)            -- 회비 결제 여부
);

insert into schemem values(
    2,
    '1153908310',
    '삼겹살 챙기기',
    '삼겹살 두근반, 소주 1병 챙기세요',
    'true',
    '50000',
    'true'
);

-- 수행 여부 변경
update schemem set isdo = 'false'
where id = '1153908310' and schnum = 1;

-- 납부 여부 변경
update schemem set ispay = 'false'
where id = '1153908310' and schnum = 1;

select * from schemem;
-- 일정에 참가한 사용자 리스트 조회
select id from schemem where schnum = 24;

delete from schemem where schnum = 24;

select * from schemem;

commit;











