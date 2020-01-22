create table hibernate_sequence (next_val bigint);

insert into hibernate_sequence values ( 1 );

create table invoice (
invoice_id integer not null auto_increment,
amount bigint,
date datetime,
operation_type varchar(255),
product_id integer, primary key (invoice_id)
);

create table product (
id integer not null,
name varchar(255),
short_title varchar(255),
specifications varchar(255),
primary key (id)
);

alter table invoice add constraint FKr006i8cut2ges4x52xnp9g68i foreign key (product_id) references product (id);
