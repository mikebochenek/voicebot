     
CREATE DATABASE voicebot;
CREATE USER 'voiceuser'@'localhost' IDENTIFIED BY 'tuzysvoice';
GRANT ALL PRIVILEGES ON voicebot.* TO 'voiceuser'@'localhost';
FLUSH PRIVILEGES;


/* for testing init, might be useful to all tables: 
   drop table users; 
   drop table poll_participants;
   */

create table users (
  id integer not null AUTO_INCREMENT,
  auth_provider_id varchar(255),
  createdate timestamp,
  email varchar(255),
  password varchar(255),
  user_type integer,
  username varchar(255),
  primary key (id),
  fullname VARCHAR(65),
  misc VARCHAR(8192)
);

create table recordings (
  id integer not null AUTO_INCREMENT,
  filename varchar(255),
  url varchar(255),
  createdate timestamp,
  status integer,
  parsedtext varchar(8192),
  primary key (id),
  misc VARCHAR(8192)
);



create table IF NOT EXISTS polls (
  uuid INT NOT NULL AUTO_INCREMENT,
  id VARCHAR(32) NULL,
  adminKey VARCHAR(32) NULL,
  latestChange BIGINT NULL,
  initiated BIGINT NULL,
  participantsCount INT NULL,
  inviteesCount INT NULL,
  ttype VARCHAR(32) NULL, /* NOTE double T - type is probably reserved in some DBMS */
  hidden BOOLEAN,
  preferencesType VARCHAR(32) NULL,
  sstate VARCHAR(32) NULL, /* NOTE double S - state is probably reserved in some DBMS */
  locale VARCHAR(32) NULL,
  title VARCHAR(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  description VARCHAR(1024) CHARACTER SET utf8  NULL,
  initiator_id INT NULL,
  multiDate BOOLEAN,
  device VARCHAR(32) NULL,
  levels VARCHAR(32) NULL,
  jsonfullpretty TEXT CHARACTER SET utf8   NULL,
  PRIMARY KEY (`uuid`),
  INDEX poll_title_idx (title),
  INDEX poll_id_idx (id),
  INDEX poll_initiated_idx (initiated)
) ENGINE=InnoDB;

/** poll_locations! */

create table IF NOT EXISTS locations (
  uuid INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NULL,
  address VARCHAR(1024) NULL,
  countryCode VARCHAR(8) NULL,
  locationId VARCHAR(32) NULL,
  PRIMARY KEY (`uuid`),
  INDEX location_id_idx (locationId)
);


create table IF NOT EXISTS poll_initiators (
  uuid INT NOT NULL AUTO_INCREMENT,
  poll_uuid INT NOT NULL,
  initiator_uuid INT NOT NULL,
  PRIMARY KEY (`uuid`),
  INDEX pi_poll_uuid_idx (poll_uuid),
  INDEX pi_initiator_uuid_idx (initiator_uuid)
);

create table IF NOT EXISTS initiators (
  uuid INT NOT NULL AUTO_INCREMENT,
  poll_uuid INT NOT NULL,
  name VARCHAR(128) NULL,
  email VARCHAR(128) NULL,
  notify VARCHAR(8) NULL,
  PRIMARY KEY (`uuid`),
  INDEX initiator_email_idx (email),
  INDEX initiator_poll_uuid_idx (poll_uuid)
);

create table IF NOT EXISTS participants (
  uuid INT NOT NULL AUTO_INCREMENT,
  id INT NULL,
  name VARCHAR(128) NULL,
  preferences VARCHAR(1024) NULL,
  PRIMARY KEY (`uuid`)
);

create table IF NOT EXISTS poll_participants (
  uuid INT NOT NULL AUTO_INCREMENT,
  poll_uuid INT NOT NULL,
  participant_uuid INT NOT NULL,
  PRIMARY KEY (`uuid`),
  INDEX pp_poll_uuid_idx (poll_uuid),
  INDEX pp_participant_uuid_idx (participant_uuid)
);
  

create table IF NOT EXISTS options (
  uuid INT NOT NULL AUTO_INCREMENT,
  poll_uuid INT NOT NULL,
  start BIGINT NULL,
  end BIGINT NULL,
  allday BOOLEAN,
  date BIGINT NULL,
  startDate BIGINT NULL,
  endDate BIGINT NULL,
  text VARCHAR(128) NULL,
  available VARCHAR(128) NULL,
  PRIMARY KEY (`uuid`)
);

