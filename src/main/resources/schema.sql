create table Authors(
id int auto_increment not null primary key,
pseudonym nvarchar(256) not null
);

create table Books(
id int auto_increment not null primary key,
title nvarchar(256) not null,
description nvarchar(max) not null,
cover nvarchar(max) not null,
price numeric(20,2) not null,
author_id int not null,
FOREIGN KEY(author_id) REFERENCES Authors(id)
);