create table person (
  id int(11) not null primary key auto_increment,
  name varchar(64) not null,
  age int(3) not null,
  address varchar (255) not null,
  job varchar(255) default null
);
insert into person values (1,'How',23,'Hangzhou','programmer');