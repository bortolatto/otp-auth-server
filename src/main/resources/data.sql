delete from role;
delete from user;
insert into user(username, password) values ('edu','{bcrypt}$2a$12$zDIroTrwOzEyxaG5wRFx8OiVYabfqM49Ip.oZTDjpK5hC19Z4bU0W');
insert into role(name, user_id) values ('ROLE_ADMIN',1);