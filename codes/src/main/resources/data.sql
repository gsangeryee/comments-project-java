INSERT INTO roles(name) VALUES('ROLE_USER');

INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Alan', 'This is Alan''s first comment', 0, '2022-02-18 16:39:00');
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Alan', 'This is Alan''s second comment', 0, '2022-02-18 16:40:00');
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Bob', 'This is Bob''s first comment', 0, '2022-02-18 16:41:00');
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Cindy', 'This is Cindy''s reply to Alan''s first comment', 1, '2022-02-18 16:42:00');
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('Bob', 'This is Bob''s reply to Cindy''s reply', 4, '2022-02-18 16:43:00');
INSERT INTO COMMENTS(username, comment, parent_id, created_time) VALUES('David', 'This is David''s reply to Alan''s first comment', 1, '2022-02-18 16:44:00');
