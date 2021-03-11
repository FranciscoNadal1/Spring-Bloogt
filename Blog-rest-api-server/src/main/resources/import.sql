/* Populate tables */
INSERT INTO user (id, avatar, created_at, password, role, surname,username,email) VALUES(1, 'noAvatar.jpg', 'pending', 'mustSecure', 'ROLE_USER', 'nadal','francisco','francisconadal1@gmail.com');
INSERT INTO user (id, avatar, created_at, password, role, surname,username,email) VALUES(2, 'noAvatar.jpg', 'pending', 'mustSecure', 'ROLE_USER', 'nadal2','francisco2','francisconadal2@gmail.com');

INSERT INTO category(id, name) VALUES(1, "Technology");
INSERT INTO category(id, name) VALUES(2, "News");

INSERT INTO post(id, content, created_at, title, category_id, user_id) VALUES(1,"this is the post","pending", "THE FIRST POST", 1,1);
INSERT INTO post(id, content, created_at, title, category_id, user_id) VALUES(2,"second post","pending", "THE SECOND POST", 2,1);

INSERT INTO comments(id, message, user_id, post_id) VALUES(1,"Este es el primer comentario", 1,1);
INSERT INTO comments(id, message, user_id, post_id) VALUES(2,"Este es el segundo comentario", 1,1);
INSERT INTO comments(id, message, user_id, post_id) VALUES(3,"Este es un comentario aparte", 1,2);

INSERT INTO hashtag(id, name) VALUES(1, "VeryInteresting");
INSERT INTO hashtag(id, name) VALUES(2, "FirstPost");
INSERT INTO hashtag(id, name) VALUES(3, "SecondPost");

INSERT INTO hashtag_post(post_id, hashtag_id) VALUES(1, 1);
INSERT INTO hashtag_post(post_id, hashtag_id) VALUES(2, 1);
INSERT INTO hashtag_post(post_id, hashtag_id) VALUES(2, 3);
INSERT INTO hashtag_post(post_id, hashtag_id) VALUES(1, 2);