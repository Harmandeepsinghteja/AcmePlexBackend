CREATE DATABASE IF NOT EXISTS test;
USE test;
-- Truncate all tables
TRUNCATE TABLE movie;
TRUNCATE TABLE schedule;
TRUNCATE TABLE screen;
TRUNCATE TABLE seat;


INSERT INTO movie (id,title, added_date,url) 
VALUES (1,'Interstellar Public','2024-08-24','https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/45fc99105415493.619ded0619991.jpg');


INSERT INTO movie (id,title, added_date,url) 
VALUES (2,'Tenet Private','2024-11-08','https://hips.hearstapps.com/hmg-prod/images/tenet-lead-1622118390.jpg?crop=0.563xw:1.00xh;0.155xw,0&resize=2048:*');


INSERT INTO movie (id,title, added_date,url) 
VALUES (3,'Man of Steel Private','2024-11-08','https://i.pinimg.com/736x/ec/f7/32/ecf732e06f0b5cb325e84e78c5ff89c1.jpg');


INSERT INTO movie (id,title, added_date,url) 
VALUES (4,'Inception Public','2024-11-01','https://flxt.tmsimg.com/assets/p7825626_p_v8_af.jpg');

INSERT INTO movie (id,title, added_date,url) 
VALUES (5,'The Dark Knight Rises Public','2024-11-01','https://th.bing.com/th/id/OIP.ILrjvLryU-PMd7Cl7Yh7QQHaK-?rs=1&pid=ImgDetMain');


INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (1,1,1,'2024-11-01 10:00:00', 10.0);

INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (2,1,1,'2024-11-01 14:00:00', 10.0);


INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (3,1,1,'2024-11-03 17:00:00', 10.0);

INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (5,1,2,'2024-11-01 17:00:00', 10.0);


INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (6,1,2,'2024-11-04 17:00:00', 10.0);



INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (4,2,1,'2024-11-01 17:00:00', 10.0);

INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (10,3,1,'2024-11-23 17:00:00', 10.0);

INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (7,3,1,'2024-11-23 08:00:00', 10.0);

INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (9,3,1,'2024-11-30 08:00:00', 10.0);






INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (11,4,1,'2024-11-23 17:00:00', 10.0);

INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (12,4,1,'2024-11-23 08:00:00', 10.0);

INSERT INTO schedule (id, movie_id, screen_id, start_time, price)
VALUES (13,4,1,'2024-11-30 08:00:00', 10.0);








INSERT INTO screen (id, screen_name, length,wide,capacity)
VALUES (1,'Screen 1', 1,5,5);

INSERT INTO screen (id, screen_name, length,wide,capacity)
VALUES (2,'Screen 2', 1,5,5);

INSERT INTO screen (id, screen_name, length,wide,capacity)
VALUES (3,'Screen 3', 1,5,5);

INSERT INTO screen (id, screen_name, length,wide,capacity)
VALUES (4,'Screen 4', 1,5,5);




Insert into seat (schedule_id,is_avaliable,seat_number)
VALUES (1,True,1);

Insert into seat (schedule_id,is_avaliable,seat_number)
VALUES (1,True,2);
Insert into seat (schedule_id,is_avaliable,seat_number)
VALUES (1,True,3);
Insert into seat (schedule_id,is_avaliable,seat_number)
VALUES (1,True,4);
Insert into seat (schedule_id,is_avaliable,seat_number)
VALUES (1,True,5);

