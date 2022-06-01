INSERT INTO USER(USER_ID, FIRST_NAME, LAST_NAME, PASSWORD, SALT, USERNAME)
VALUES (1, 'Test', 'User','Vbm5scKi7uJ9qnKmXD1Vzw==','mbTAY3p5kaAG4zi+nb1ARw==','testuser');

INSERT INTO NOTE(NOTE_ID, NOTE_DESCRIPTION, NOTE_TITLE, USER_ID)
VALUES (1, 'This is your first note.','Welcome to the Cloud Storage',1);