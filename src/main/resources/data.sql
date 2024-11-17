CREATE DATABASE IF NOT EXISTS movie_theater;
USE movie_theater;

DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS credit_refund;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS seat;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS screen;
DROP TABLE IF EXISTS movie;

create table if not exists movie (
    id int auto_increment primary key,
    movieName varchar(50) not null,
    addedDate timestamp default NOW() not null,
    url varchar(2048) null
);

create table if not exists screen (
    id int auto_increment primary key,
    screenName varchar(20) not null unique,
    length int not null,
    width int not null,
    capacity int as (width * length) stored
);

create table if not exists schedule (
    id int auto_increment primary key,
    movieId int null,
    screenId int null,
    startTime timestamp not null,
    price decimal(10, 2) not null,
    constraint schedule_movie_id_fk foreign key (movieId) references movie (id),
    constraint schedule_screen_id_fk foreign key (screenId) references screen (id)
);

create table if not exists seat (
    scheduleId int not null,
    seatNumber int not null,
    isAvaliable tinyint(1) default 1 null,
    primary key (seatNumber, scheduleId),
    constraint seat_schedule_id_fk foreign key (scheduleId) references schedule (id)
);

create table if not exists users (
    id int auto_increment primary key,
    email varchar(50) not null unique,
    password varchar(255) not null,
    paymentMethod ENUM('DEBIT', 'CREDIT') null,
    cardNumber varchar(19) null,
    membershipExpiryDate timestamp null
);

create table if not exists ticket (
    id int auto_increment primary key,
    userId int not null,
    scheduleId int not null,
    seatNumber int not null,
    isCancelled tinyint(1) default 0 not null,
    cancellationDate timestamp default null,
    constraint ticket_users_id_fk foreign key (userId) references users (id),
    constraint ticket_schedule_seat_scheduleId_seatNumber_fk foreign key (scheduleId, seatNumber) references seat (scheduleId, seatNumber)
);

create table if not exists payment (
    id int auto_increment primary key,
    ticketId int,
    paymentTime timestamp default NOW() not null,
    paymentMethod ENUM('DEBIT', 'CREDIT') not null,
    cardNumber varchar(19) not null,
    creditSpent decimal(10, 2) default 0.00 not null,
    moneySpent decimal(10, 2) default 0.00 not null,
    constraint payment_ticket_id_fk foreign key (ticketId) references ticket (id)
);

create table if not exists credit_refund (
    ticketId int not null primary key,
    refundAmount decimal(10, 2) null,
    expiryDate timestamp not null,
    constraint credit_refund_ticket_fk foreign key (ticketId) references ticket (id)
);

DROP PROCEDURE IF EXISTS insert_schedule_seats;

DELIMITER $$
create procedure insert_schedule_seats(IN p_scheduleId int, IN p_screenId int)
BEGIN
	DECLARE i INT DEFAULT 1;
	DECLARE screen_capacity INT;

    SELECT capacity INTO screen_capacity FROM screen WHERE id = p_screenId;

    WHILE i <= screen_capacity DO
            INSERT INTO seat (scheduleId, seatNumber)
            VALUES (p_scheduleId, i);
            SET i = i + 1;
	END WHILE;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS after_insert_schedule;

DELIMITER $$
create trigger after_insert_schedule
after insert
on schedule
for each row
BEGIN
    CALL insert_schedule_seats(NEW.id, NEW.screenId);
END $$
DELIMITER ;



INSERT INTO movie (id,movieName, addedDate,url)
VALUES
	(1,'Interstellar Public','2024-08-24','https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/45fc99105415493.619ded0619991.jpg'),
	(2,'Tenet Private','2024-11-08','https://hips.hearstapps.com/hmg-prod/images/tenet-lead-1622118390.jpg?crop=0.563xw:1.00xh;0.155xw,0&resize=2048:*'),
	(3,'Man of Steel Private','2024-11-08','https://i.pinimg.com/736x/ec/f7/32/ecf732e06f0b5cb325e84e78c5ff89c1.jpg'),
	(4,'Inception Public','2024-11-01','https://flxt.tmsimg.com/assets/p7825626_p_v8_af.jpg'),
    (5,'The Dark Knight Rises Public','2024-11-13','https://th.bing.com/th/id/OIP.ILrjvLryU-PMd7Cl7Yh7QQHaK-?rs=1&pid=ImgDetMain');

INSERT INTO screen (id, screenName, length,width)
VALUES
	(1,'1', 5,10),
	(2,'2', 5,10),
    (3,'3', 5,10),
    (4,'4', 5,10);


INSERT INTO schedule (id, movieId, screenId, startTime, price)
VALUES
	(1,1,1,'2024-11-01 10:00:00', 10.0),
    (2,1,1,'2024-11-01 14:00:00', 10.0),
    (3,1,1,'2024-11-03 17:00:00', 10.0),
    (5,1,2,'2024-11-01 17:00:00', 10.0),
    (6,1,2,'2024-11-04 17:00:00', 10.0),

    (4,2,1,'2024-11-01 17:00:00', 10.0),
    (10,3,1,'2024-11-23 17:00:00', 10.0),
    (7,3,1,'2024-11-23 08:00:00', 10.0),
    (9,3,1,'2024-11-30 08:00:00', 10.0),

	(11,4,1,'2024-11-23 17:00:00', 10.0),
    (12,4,1,'2024-11-23 08:00:00', 10.0),
    (13,4,1,'2024-11-30 08:00:00', 10.0);


INSERT INTO users (id, email, password, paymentMethod, cardNumber, membershipExpiryDate)
VALUES
	(1, 'charlie@gmail.com', '1234', 'CREDIT', 1234, null);

INSERT INTO ticket (id, userId, scheduleId, seatNumber, isCancelled, cancellationDate)
VALUES
	(1, 1, 1, 1, 0, null),
    (2, 1, 2, 1, 1, '2024-10-08 00:00:00'),
    (3, 1, 3, 1, 1, '2024-10-09 00:00:00'),
    (4, 1, 4, 1, 1, '2024-10-10 00:00:00');

INSERT INTO payment (id, ticketId, paymentTime, paymentMethod, cardNumber, creditSpent, moneySpent)
VALUES
	(1, 1, NOW(), 'DEBIT', 11111, 10, 0),
    (2, 2, NOW(), 'DEBIT', 11111, 10, 0),
    (3, 3, NOW(), 'DEBIT', 11111, 10, 0),
    (4, 4, NOW(), 'DEBIT', 11111, 10, 0);

INSERT INTO credit_refund (ticketId, refundAmount, expiryDate)
VALUES
	(2, 7, '2025-10-11 00:00:00'),
    (3, 7, '2025-11-11 00:00:00'),
    (4, 7, '2025-12-11 00:00:00');


-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,1);

-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,2);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,3);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,4);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,5);

-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,6);

-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,7);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,8);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,9);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,10);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,11);

-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,12);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,13);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,14);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,15);

-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,16);

-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,17);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,18);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,19);
-- Insert into seat (scheduleId,isAvaliable,seatNumber)
-- VALUES (1,True,20);
