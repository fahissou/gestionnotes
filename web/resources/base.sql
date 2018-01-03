-- Create view v_user_role as select u.login, u.password, h.groupe_code FROM utilisateur u, habilitation h where h.utilisateur_login = u.login;

INSERT INTO utilisateur (login, bp, datecreation, datenaissance, email, genre, nom, password, prenom) VALUES ('fahissou', NULL, '2017-02-06 14:09:09', '2017-02-06 14:09:09', 'flomail05@gmail.com',  'M', 'AHISSOU', '231b0997d6ba040db9728a5cece5414d636e94c590418c08dff7df232861351f', 'yevedo');

INSERT INTO groupe (code, libelle) VALUES ('ADMIN','Administrateur application');
