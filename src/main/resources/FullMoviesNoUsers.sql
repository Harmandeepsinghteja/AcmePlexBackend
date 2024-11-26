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
    url varchar(2048) not null,
    privatelyAnnounced tinyint(1) default 0 not null,
    publiclyAnnounced tinyint(1) default 0 not null
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
    isAvailable tinyint(1) default 1 null,
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

-- Inserting Movie Information
INSERT INTO movie (id, movieName, addedDate, url, privatelyAnnounced, publiclyAnnounced)
VALUES
    (1, 'Interstellar', '2024-10-24', 'https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/45fc99105415493.619ded0619991.jpg', 0, 0),
    (2, 'Tenet', '2024-10-24', 'https://hips.hearstapps.com/hmg-prod/images/tenet-lead-1622118390.jpg?crop=0.563xw:1.00xh;0.155xw,0&resize=2048:*', 0, 0),
    (3, 'Man of Steel', '2024-10-24', 'https://i.pinimg.com/736x/ec/f7/32/ecf732e06f0b5cb325e84e78c5ff89c1.jpg', 0, 0),
    (4, 'Inception', '2024-10-24', 'https://flxt.tmsimg.com/assets/p7825626_p_v8_af.jpg', 0, 0),
    (5, 'The Dark Knight Rises', '2024-11-24', 'https://th.bing.com/th/id/OIP.ILrjvLryU-PMd7Cl7Yh7QQHaK-?rs=1&pid=ImgDetMain', 0, 0),
    (6, 'Dunkirk', '2024-11-24', 'https://m.media-amazon.com/images/M/MV5BZWU5ZjJkNTQtMzQwOS00ZGE4LWJkMWUtMGQ5YjdiM2FhYmRhXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg', 0, 0),
    (7, 'Memento', '2024-11-24', 'https://upload.wikimedia.org/wikipedia/en/c/c7/Memento_poster.jpg', 0, 0),
    (8, 'The Prestige', '2024-11-24', 'https://m.media-amazon.com/images/M/MV5BMjA4NDI0MTIxNF5BMl5BanBnXkFtZTYwNTM0MzY2._V1_.jpg', 0, 0);

-- Inserting Movie Information INSERT INTO movie (id, movieName, addedDate, url, privatelyAnnounced, publiclyAnnounced) VALUES (1, 'Interstellar', '2024-10-24', 'https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/45fc99105415493.619ded0619991.jpg', 0, 0), (2, 'Tenet', '2024-10-24', 'https://hips.hearstapps.com/hmg-prod/images/tenet-lead-1622118390.jpg?crop=0.563xw:1.00xh;0.155xw,0&resize=2048:*', 0, 0), (3, 'Man of Steel', '2024-11-21', 'https://i.pinimg.com/736x/ec/f7/32/ecf732e06f0b5cb325e84e78c5ff89c1.jpg', 0, 0), (4, 'Inception', '2024-11-21', 'https://flxt.tmsimg.com/assets/p7825626_p_v8_af.jpg', 0, 0), (5, 'The Dark Knight Rises', '2024-11-21', 'https://th.bing.com/th/id/OIP.ILrjvLryU-PMd7Cl7Yh7QQHaK-?rs=1&pid=ImgDetMain', 0, 0), (6, 'Dunkirk', '2024-11-26', 'https://flxt.tmsimg.com/assets/p14141027_p_v8_ab.jpg', 0, 0), (7, 'Memento', '2024-11-26', 'https://upload.wikimedia.org/wikipedia/en/c/c7/Memento_poster.jpg', 0, 0), (8, 'The Prestige', '2024-11-26', 'https://m.media-amazon.com/images/I/51ewfYv2mHL._AC_SY679_.jpg', 0, 0);
INSERT INTO screen (id, screenName, length,width)
VALUES
	(1,'1', 5,10),
	(2,'2', 5,10),
    (3,'3', 5,10),
    (4,'4', 5,10),
    (5,'5', 5,10),
    (6,'6', 5,10),
    (7,'7', 5,10),
    (8,'8', 5,10);


    -- Populating Schedule for Specific Days Starting Next Month
INSERT INTO schedule (id, movieId, screenId, startTime, price)
VALUES
    -- Interstellar (Next Month, Tomorrow, Next Week, Next Next Week, Next Month)
    (1, 1, 1, '2024-12-26 10:00:00', 10.0),
    (2, 1, 1, '2024-12-26 14:00:00', 10.0),
    (3, 1, 1, '2024-12-26 18:00:00', 10.0),
    (4, 1, 1, '2024-12-27 10:00:00', 10.0),
    (5, 1, 1, '2024-12-27 14:00:00', 10.0),
    (6, 1, 1, '2024-12-27 18:00:00', 10.0),
    (7, 1, 1, '2025-01-03 10:00:00', 10.0),
    (8, 1, 1, '2025-01-03 14:00:00', 10.0),
    (9, 1, 1, '2025-01-03 18:00:00', 10.0),
    (10, 1, 1, '2025-01-10 10:00:00', 10.0),
    (11, 1, 1, '2025-01-10 14:00:00', 10.0),
    (12, 1, 1, '2025-01-10 18:00:00', 10.0),
    (13, 1, 1, '2025-01-26 10:00:00', 10.0),
    (14, 1, 1, '2025-01-26 14:00:00', 10.0),
    (15, 1, 1, '2025-01-26 18:00:00', 10.0),

    -- Tenet (Next Month, Tomorrow, Next Week, Next Next Week, Next Month)
    (16, 2, 1, '2024-12-26 10:00:00', 10.0),
    (17, 2, 1, '2024-12-26 14:00:00', 10.0),
    (18, 2, 1, '2024-12-26 18:00:00', 10.0),
    (19, 2, 1, '2024-12-27 10:00:00', 10.0),
    (20, 2, 1, '2024-12-27 14:00:00', 10.0),
    (21, 2, 1, '2024-12-27 18:00:00', 10.0),
    (22, 2, 1, '2025-01-03 10:00:00', 10.0),
    (23, 2, 1, '2025-01-03 14:00:00', 10.0),
    (24, 2, 1, '2025-01-03 18:00:00', 10.0),
    (25, 2, 1, '2025-01-10 10:00:00', 10.0),
    (26, 2, 1, '2025-01-10 14:00:00', 10.0),
    (27, 2, 1, '2025-01-10 18:00:00', 10.0),
    (28, 2, 1, '2025-01-26 10:00:00', 10.0),
    (29, 2, 1, '2025-01-26 14:00:00', 10.0),
    (30, 2, 1, '2025-01-26 18:00:00', 10.0),

    -- Man of Steel (Next Month, Tomorrow, Next Week, Next Next Week, Next Month)
    (31, 3, 1, '2024-12-26 10:00:00', 10.0),
    (32, 3, 1, '2024-12-26 14:00:00', 10.0),
    (33, 3, 1, '2024-12-26 18:00:00', 10.0),
    (34, 3, 1, '2024-12-27 10:00:00', 10.0),
    (35, 3, 1, '2024-12-27 14:00:00', 10.0),
    (36, 3, 1, '2024-12-27 18:00:00', 10.0),
    (37, 3, 1, '2025-01-03 10:00:00', 10.0),
    (38, 3, 1, '2025-01-03 14:00:00', 10.0),
    (39, 3, 1, '2025-01-03 18:00:00', 10.0),
    (40, 3, 1, '2025-01-10 10:00:00', 10.0),
    (41, 3, 1, '2025-01-10 14:00:00', 10.0),
    (42, 3, 1, '2025-01-10 18:00:00', 10.0),
    (43, 3, 1, '2025-01-26 10:00:00', 10.0),
    (44, 3, 1, '2025-01-26 14:00:00', 10.0),
    (45, 3, 1, '2025-01-26 18:00:00', 10.0),

    -- Inception (Next Month, Tomorrow, Next Week, Next Next Week, Next Month)
    (46, 4, 1, '2024-12-26 10:00:00', 10.0),
    (47, 4, 1, '2024-12-26 14:00:00', 10.0),
    (48, 4, 1, '2024-12-26 18:00:00', 10.0),
    (49, 4, 1, '2024-12-27 10:00:00', 10.0),
    (50, 4, 1, '2024-12-27 14:00:00', 10.0),
    (51, 4, 1, '2024-12-27 18:00:00', 10.0),
    (52, 4, 1, '2025-01-03 10:00:00', 10.0),
    (53, 4, 1, '2025-01-03 14:00:00', 10.0),
    (54, 4, 1, '2025-01-03 18:00:00', 10.0),
    (55, 4, 1, '2025-01-10 10:00:00', 10.0),
    (56, 4, 1, '2025-01-10 14:00:00', 10.0),
    (57, 4, 1, '2025-01-10 18:00:00', 10.0),
    (58, 4, 1, '2025-01-26 10:00:00', 10.0),
    (59, 4, 1, '2025-01-26 14:00:00', 10.0),
    (60, 4, 1, '2025-01-26 18:00:00', 10.0),

-- The Dark Knight Rises (Next Month, Tomorrow, Next Week, Next Next Week, Next Month)
    (61, 5, 1, '2024-12-26 10:00:00', 10.0),
    (62, 5, 1, '2024-12-26 14:00:00', 10.0),
    (63, 5, 1, '2024-12-26 18:00:00', 10.0),
    (64, 5, 1, '2024-12-27 10:00:00', 10.0),
    (65, 5, 1, '2024-12-27 14:00:00', 10.0),
    (66, 5, 1, '2024-12-27 18:00:00', 10.0),
    (67, 5, 1, '2025-01-03 10:00:00', 10.0),
    (68, 5, 1, '2025-01-03 14:00:00', 10.0),
    (69, 5, 1, '2025-01-03 18:00:00', 10.0),
    (70, 5, 1, '2025-01-10 10:00:00', 10.0),
    (71, 5, 1, '2025-01-10 14:00:00', 10.0),
    (72, 5, 1, '2025-01-10 18:00:00', 10.0),
    (73, 5, 1, '2025-01-26 10:00:00', 10.0),
    (74, 5, 1, '2025-01-26 14:00:00', 10.0),
    (75, 5, 1, '2025-01-26 18:00:00', 10.0),

    -- Dunkirk (Next Month, Tomorrow, Next Week, Next Next Week, Next Month)
    (76, 6, 1, '2024-12-26 10:00:00', 10.0),
    (77, 6, 1, '2024-12-26 14:00:00', 10.0),
    (78, 6, 1, '2024-12-26 18:00:00', 10.0),
    (79, 6, 1, '2024-12-27 10:00:00', 10.0),
    (80, 6, 1, '2024-12-27 14:00:00', 10.0),
    (81, 6, 1, '2024-12-27 18:00:00', 10.0),
    (82, 6, 1, '2025-01-03 10:00:00', 10.0),
    (83, 6, 1, '2025-01-03 14:00:00', 10.0),
    (84, 6, 1, '2025-01-03 18:00:00', 10.0),
    (85, 6, 1, '2025-01-10 10:00:00', 10.0),
    (86, 6, 1, '2025-01-10 14:00:00', 10.0),
    (87, 6, 1, '2025-01-10 18:00:00', 10.0),
    (88, 6, 1, '2025-01-26 10:00:00', 10.0),
    (89, 6, 1, '2025-01-26 14:00:00', 10.0),
    (90, 6, 1, '2025-01-26 18:00:00', 10.0),

    -- Memento (Next Month, Tomorrow, Next Week, Next Next Week, Next Month)
    (91, 7, 1, '2024-12-26 10:00:00', 10.0),
    (92, 7, 1, '2024-12-26 14:00:00', 10.0),
    (93, 7, 1, '2024-12-26 18:00:00', 10.0),
    (94, 7, 1, '2024-12-27 10:00:00', 10.0),
    (95, 7, 1, '2024-12-27 14:00:00', 10.0),
    (96, 7, 1, '2024-12-27 18:00:00', 10.0),
    (97, 7, 1, '2025-01-03 10:00:00', 10.0),
    (98, 7, 1, '2025-01-03 14:00:00', 10.0),
    (99, 7, 1, '2025-01-03 18:00:00', 10.0),
    (100, 7, 1, '2025-01-10 10:00:00', 10.0),
    (101, 7, 1, '2025-01-10 14:00:00', 10.0),
    (102, 7, 1, '2025-01-10 18:00:00', 10.0),
    (103, 7, 1, '2025-01-26 10:00:00', 10.0),
    (104, 7, 1, '2025-01-26 14:00:00', 10.0),
    (105, 7, 1, '2025-01-26 18:00:00', 10.0),

    -- The Prestige (Next Month, Tomorrow, Next Week, Next Next Week, Next Month)
    (106, 8, 1, '2024-12-26 10:00:00', 10.0),
    (107, 8, 1, '2024-12-26 14:00:00', 10.0),
    (108, 8, 1, '2024-12-26 18:00:00', 10.0),
    (109, 8, 1, '2024-12-27 10:00:00', 10.0),
    (110, 8, 1, '2024-12-27 14:00:00', 10.0),
    (111, 8, 1, '2024-12-27 18:00:00', 10.0),
    (112, 8, 1, '2025-01-03 10:00:00', 10.0),
    (113, 8, 1, '2025-01-03 14:00:00', 10.0),
    (114, 8, 1, '2025-01-03 18:00:00', 10.0),
    (115, 8, 1, '2025-01-10 10:00:00', 10.0),
    (116, 8, 1, '2025-01-10 14:00:00', 10.0),
    (117, 8, 1, '2025-01-10 18:00:00', 10.0),
    (118, 8, 1, '2025-01-26 10:00:00', 10.0),
    (119, 8, 1, '2025-01-26 14:00:00', 10.0),
    (120, 8, 1, '2025-01-26 18:00:00', 10.0);



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
