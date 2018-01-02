
/*     
CREATE DATABASE voicebot;
CREATE USER 'voiceuser'@'localhost' IDENTIFIED BY 'tuzysvoice';
GRANT ALL PRIVILEGES ON voicebot.* TO 'voiceuser'@'localhost';
FLUSH PRIVILEGES;
*/


/* for testing init, might be useful to all tables: 
   drop table users; 
   drop table recordings;
   drop table prompts;
   drop table restaurants;
   */

create table IF NOT EXISTS users (
  id integer not null AUTO_INCREMENT,
  authprovider varchar(255),
  createdate timestamp,
  email varchar(255),
  password varchar(255),
  usertype integer,
  username varchar(55),
  phone varchar(55),
  primary key (id),
  fullname VARCHAR(65),
  misc VARCHAR(8192)
);

create table IF NOT EXISTS recordings (
  id integer not null AUTO_INCREMENT,
  filename varchar(255),
  url varchar(255),
  createdate timestamp,
  status integer,
  conversation integer,
  phone varchar(55),
  phonecalled varchar(55),
  urlcalled varchar(255),
  parsedtext varchar(8192),
  primary key (id),
  misc VARCHAR(8192)
);


create table IF NOT EXISTS prompts (
  id INT NOT NULL AUTO_INCREMENT,
  createdate timestamp,
  status integer,
  phone varchar(55),
  ptext varchar(8192),
  actionurl varchar(255),
  url varchar(255),
  primary key (id)
);

create table IF NOT EXISTS restaurants (
  id INT NOT NULL AUTO_INCREMENT,
  name varchar(255),
  city varchar(45),
  address varchar(255),
  schedulecron varchar(1024),
  createdate timestamp,
  status int,
  seats int,
  email varchar(45),
  phone varchar(45),
  website varchar(255),
  googleplaces varchar(35),
  PRIMARY KEY (`id`)
);


INSERT INTO `voicebot`.`prompts` (`phone`, `ptext`, `actionurl`)
VALUES ('+12262429755', 'Hello there cowboy, please leave a message to make a reservation.', '/voice/intro');

INSERT INTO `voicebot`.`prompts` (`phone`, `ptext`, `actionurl`)
VALUES ('+12262429755', 'And how many people in your party?', '/voice/record');

INSERT INTO `voicebot`.`prompts` (`phone`, `ptext`, `actionurl`)
VALUES ('+12262429755', 'Please give me a second to see if I understood everything fully.', '/voice/guests');

INSERT INTO `voicebot`.`prompts` (`phone`, `ptext`, `actionurl`)
VALUES ('+12262429755', 'OK.', '/voice/processingwait');

INSERT INTO `voicebot`.`prompts` (`phone`, `ptext`, `actionurl`)
VALUES ('+12262429755', 'OK.  All done.  Thank you for calling.', '/voice/confirmation');


