CREATE DATABASE USERREALM;
USE USERREALM;

CREATE TABLE IF NOT EXISTS ROLE(role_id bigint unsigned primary key, role_name VARCHAR(255) not null unique key, created_ts timestamp not null default current_timestamp);
INSERT INTO ROLE(role_id, created_ts, role_name) values(1, now(), 'ADMIN');
INSERT INTO ROLE(role_id, created_ts, role_name) values(2, now(), 'TEACHER');
INSERT INTO ROLE(role_id, created_ts, role_name) values(3, now(), 'STUDENT');

CREATE TABLE IF NOT EXISTS USER(user_id bigint unsigned primary key not null auto_increment,
                  email varchar(255) unique not null,
                  username varchar(255) unique not null,
                  password varchar(255) not null,
                  phone_number varchar(12) unique not null,
                  qualification varchar(255) not null,
                  role_id bigint unsigned not null,
                  is_email_verified boolean not null default false,
                  is_phone_number_verified boolean not null default false,
                  is_account_locked boolean not null default false,
                  email_verified_ts timestamp null,
                  phone_number_verified_ts timestamp null,
                  registered_ts timestamp not null default current_timestamp,
                  updated_ts timestamp null,
                  foreign key user_role_id_fk(role_id) references ROLE(role_id)
                  );

CREATE TABLE IF NOT EXISTS USER_PROFILE(user_profile_id bigint unsigned primary key not null auto_increment,
        user_id bigint unsigned unique not null,
        first_name varchar(255) not null,
        last_name varchar(255) not null,
        alternate_phone_number varchar(12) null,
        alternate_email varchar(255) null,
        gender varchar(6) not null,
        dob date not null,
        )