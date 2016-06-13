/*
 * Migration script from which we can create DB.
 * 
 * OR we can create DB by hibernate.
 * 
 * */
create table lib_category (
	id bigserial not null primary key,
	name varchar(25) not null unique
);
