DROP DATABASE IF EXISTS hello;
CREATE DATABASE hello;
USE hello;
create table contacts (id bigint not null auto_increment PRIMARY KEY, name varchar(255) not null);