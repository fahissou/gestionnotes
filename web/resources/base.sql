-- Create view v_user_role as select u.login, u.password, h.groupe_code FROM utilisateur u, habilitation h where h.utilisateur_login = u.login;

create view v_groupe_user as select utilisateur_login as login, libelle as nom_groupe from groupe join habilitation on groupe.id=habilitation.groupeutilisateur_id; 

INSERT INTO utilisateur (login, bp, datecreation, datenaissance, email, genre, nom, password, prenom) VALUES ('fahissou', NULL, '2017-02-06 14:09:09', '2017-02-06 14:09:09', 'flomail05@gmail.com',  'M', 'AHISSOU', '231b0997d6ba040db9728a5cece5414d636e94c590418c08dff7df232861351f', 'yevedo');

INSERT INTO groupe (code, libelle) VALUES ('ADMIN','Administrateur application');

create view v_groupe_user as select login as login_utilisateur, password as utilisateur_password, libelle as nom_groupe from utilisateur join habilitation on utilisateur.login=habilitation.utilisateur_login join groupe on  groupe.id=habilitation.groupeutilisateur_id;

insert into anneeacademique VALUES ('fzerezr','2016 - 2017',1)