DROP TABLE IF EXISTS SECTION;
DROP TABLE IF EXISTS STATION;
DROP TABLE IF EXISTS LINE;

create table STATION
(
    name varchar(255) not null unique
);

create table LINE
(
    id                 bigint auto_increment not null,
    name               varchar(255)          not null unique,
    color              varchar(20)           not null,
    start_station_name varchar(255),
    primary key (id)
);

create table SECTION
(
    id                 bigint auto_increment not null,
    line_id            bigint                not null,
    start_station_name varchar(255)          not null,
    end_station_name   varchar(255)          not null,
    distance           int                   not null,
    primary key (id),
    foreign key (line_id) REFERENCES LINE (id)
);

INSERT INTO STATION (name) VALUES ('잠실역');
INSERT INTO LINE (name, color, start_station_name) VALUES ('2호선', 'green', '잠실역');
