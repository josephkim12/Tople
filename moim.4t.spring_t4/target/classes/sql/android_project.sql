-- ������ ����
    -- ���� �ڵ�
create sequence moim_seq nocache nocycle;
drop sequence moim_seq;
    -- �� ��ȣ
create sequence board_seq nocache nocycle;
drop sequence board_seq;
    -- ���� ��ȣ
create sequence re_seq nocache nocycle;
drop sequence re_seq;
    -- ���� �ڵ�
create sequence sche_seq nocache nocycle;
drop sequence sche_seq;

commit;

------------------------------------------------------
-- ����� ���� ���̺�
create table moim_user (
    user_id varchar2(4000) primary key,    -- ���̵�
    user_img varchar2(4000)                -- ����� ������ ����
);

insert into moim_user values(
'1153817244', 
'hong.jpg');

select * from moim_user;

select * from moim_user where user_id = '1150321350';

drop table moim_user purge;

commit;


-----------------���� ����---------------------------
-- ���������� ������ ���� �̸� ���
select max(tname) as tname from tab 
where tname like upper('zz%');

-- ���� ���̺� ����
create table zz10000 (
    moimcode number primary key,    -- ���� �ڵ�
    loca varchar2(100),             -- ��ġ
    moimname varchar2(1000),        -- ���Ӹ�
    prod varchar2(4000),            -- ���ӼҰ�
    pic varchar2(500),              -- ���� ���ϸ�
    color varchar2(100)             -- ����
);

insert into zz10000 values(
moim_seq.nextval,
'������',
'������ �ص���',
'������ �ص��� ���鼭 ��� ����',
'banner.jpg',
'wine'
);

select * from zz10039;

-- ���� ���� �� ���� �ο���
select zz10000.*, 
(select count(id) from moimmem where moimcode = zz10000.moimcode) 
as count 
from zz10000;

select tname from tab;
drop table zz114 purge;

commit;


------------------------------------------------------
-- ���� ���
create table moimmem (
    id varchar2(50) not null,       -- ����� ���̵�
    moimcode number not null,       -- ���� �ڵ�
    fav varchar2(10) not null,      -- ���ƿ� ����(true OR false)
    permit number                   -- ���ӿ����� ���ѷ���
);

-- �Է� ����
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
-- ���� ���̺� �̸� ���̺�
create table moimtname (
    moimcode number not null,        -- ���� �ڵ�
    tname varchar2(50) not null      -- ���̺� �̸�
);

insert into moimtname values(
    1,
    'zz10000'
);

select * from moimtname;

drop table moimtname purge;


----------------------------------------------------
-- �Խ��� ���̺�
create table boardlist(
    listnum number primary key,     -- �Խñ� ��ȣ
    id varchar2(50) not null,       -- �ۼ��� ���̵�
    moimcode number not null,       -- ���� �ۼ��� ���� �ڵ�
    subject varchar2(500),          -- ����
    content varchar2(4000),         -- ����
    filename varchar2(4000),        -- ����� ���� �̸�
    thumb varchar2(4000),           -- ������ ����� �̸�
    editdate date not null,         -- �ۼ�/������ ��¥
    lev number not null             -- ���� ���� �з� ��ȣ(����)
);

-- �Է� ����
insert into boardlist values(
board_seq.nextval,
'1153908310',
3,
'�� �Ϲ�',
'�� ����',
'board_pic.jpg',
'board_thumb.jpg',
sysdate,
2
);

select * from boardlist;

drop table boardlist purge;
commit;


------------------------------------------------------------
-- ���� ���̺�
create table redat(
    renum number primary key,       -- ��� ��ȣ
    listnum number not null,        -- ���� ��ȣ
    id varchar2(50),                -- �ۼ��� ���̵�
    moimcode number not null,       -- �ۼ��� ���� �ڵ�
    reple varchar2(4000),           -- ����
    redate date not null            -- ��� ���� ��¥
);

-- �Է� ����
insert into redat values(
re_seq.nextval,
1,
'1153908310',
1,
'�� ��',
sysdate
);

select * from redat order by renum desc;

drop table redat purge;
commit;


-------------------------------------------------------------
-- ���� ���̺�
create table schedule (
    sch_schnum number primary key,      -- �����ڵ�
    sch_moimcode number not null,       -- ������ ��ϵ� ���� �ڵ�
    sch_year varchar2(100) not null,    -- ��-��-��0
    sch_month varchar2(100) not null,   -- ��-��-��0
    sch_day varchar2(100) not null,     -- ��-��-��0
    sch_time varchar2(100) not null,    -- ��
    sch_title varchar2(500),            -- ������Ī(����)
    sch_sub varchar2(4000),             -- ���� ����
    sch_amount varchar2(4000),          -- ȸ��
    sch_lat number,                     -- ����
    sch_lot number                      -- �浵
);


-- �Է� ����
insert into schedule values(
sche_seq.nextval,
19,
'2019',
'08',
'30',
'18:45',
'8�� ����',
'������ ���� ��â �Դϴ�.',
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
    schnum number not null,         -- ���� �ڵ�
    id varchar2(4000) not null,     -- ����� id
    todo varchar2(4000),            -- �� ��
    ex varchar2(4000),              -- �� �� ��
    isdo varchar2(4000),            -- ���� ����
    amount varchar2(4000),          -- ȸ��
    ispay varchar2(4000)            -- ȸ�� ���� ����
);

insert into schemem values(
    2,
    '1153908310',
    '���� ì���',
    '���� �αٹ�, ���� 1�� ì�⼼��',
    'true',
    '50000',
    'true'
);

-- ���� ���� ����
update schemem set isdo = 'false'
where id = '1153908310' and schnum = 1;

-- ���� ���� ����
update schemem set ispay = 'false'
where id = '1153908310' and schnum = 1;

select * from schemem;
-- ������ ������ ����� ����Ʈ ��ȸ
select id from schemem where schnum = 24;

delete from schemem where schnum = 24;

select * from schemem;

commit;











