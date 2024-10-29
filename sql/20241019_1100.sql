drop table url;
create table url (
	id char(36),
	url_original varchar(400) not null,
	url_short varchar(10) unique not null,
	step_created int not null,
	created_at timestamp default current_timestamp,
	primary key (id)
);

create index url_url_short_idx  on url(url_short);

select *
from url;

--- new
create table url (
	id int auto_increment,
	created_at timestamp default current_timestamp,
	primary key (id)
);

create table url_detail (
	id int auto_increment,
	url_id int not null,
	original_url varchar(400) not null,
	short_url varchar(10) unique not null,
	expire_duration int not null,
	created_at timestamp default current_timestamp,
	primary key (id),
	foreign key (url_id) references url(id)
);

create index url_url_short_idx  on url_detail(short_url);

-- drop table url_detail;
-- drop table url;
select *
from url;

select *
from url_detail;