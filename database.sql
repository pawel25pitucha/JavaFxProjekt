exec master..sp_addsrvrolemember adminek , sysadmin;
create login adminek with password='password';
drop login admin22
 
CREATE TABLE Adres (
  Id INTEGER IDENTITY NOT NULL ,
  Miejscowoœæ VARCHAR(30) CHECK(LEN(Miejscowoœæ) > 1) NOT NULL,
  Ulica VARCHAR(30) CHECK(LEN(Ulica) > 1) NULL,
  Nr_domu INTEGER CHECK(Nr_domu > 0 ) NULL,
  Kod_pocztowy CHAR(6) CHECK(LEN(Kod_pocztowy) = 6) NOT NULL,
  PRIMARY KEY(Id)
);

CREATE TABLE Zawodnik (
  Id INTEGER IDENTITY NOT NULL,
  Adres_Id INTEGER  NULL,
  Pesel CHAR(11) CHECK(Pesel LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]') NOT NULL,
  Imiê VARCHAR(30) CHECK(LEN(Imiê) > 1)  NOT NULL,
  Nazwisko VARCHAR(30) CHECK(LEN(Nazwisko) > 1)  NOT NULL,
  Data_urodzenia DATE CHECK(Data_urodzenia <= GETDATE()) NOT NULL,
  P³eæ CHAR(1) CHECK(P³eæ = 'k' OR P³eæ = 'm') NOT NULL,
  Poziom VARCHAR(30) NOT NULL,
  PRIMARY KEY(Id),
  FOREIGN KEY(Adres_Id)
  REFERENCES Adres(Id)
);

CREATE TABLE Trener (
  Id INTEGER IDENTITY NOT NULL ,
  Adres_Id INTEGER  NOT NULL,
  Imiê VARCHAR(30) CHECK(LEN(Imiê) > 1) NOT NULL,
  Nazwisko VARCHAR(30) CHECK(LEN(Nazwisko) > 1) NOT NULL,
  Data_urodzenia DATE CHECK(Data_urodzenia <= GETDATE()) NOT NULL,
  Pesel CHAR(11) CHECK(Pesel LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]') NOT NULL,
  P³eæ CHAR(1) CHECK(P³eæ = 'k' OR P³eæ = 'm') NOT NULL,
  PRIMARY KEY(Id),
  FOREIGN KEY(Adres_Id)
  REFERENCES Adres(Id)
);

CREATE TABLE Sêdzia (
  Id INTEGER IDENTITY NOT NULL,
  Adres_Id INTEGER  NOT NULL,
  Pesel cHAR(11) CHECK(Pesel LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]') NOT NULL,
  Imiê VARCHAR(30) CHECK(LEN(Imiê) > 1) NOT NULL,
  Nazwisko VARCHAR(30) CHECK(LEN(Nazwisko) > 1) NOT NULL,
  Data_urodzenia DATE CHECK(Data_urodzenia <= GETDATE()) NOT NULL,
  P³eæ CHAR(1) CHECK(P³eæ = 'k' OR P³eæ = 'm') NOT NULL,
  PRIMARY KEY(Id),
  FOREIGN KEY(Adres_Id)
  REFERENCES Adres(Id)
);

CREATE TABLE Dyscyplina (
  Id INTEGER IDENTITY NOT NULL ,
  Nazwa VARCHAR(30) CHECK(LEN(Nazwa)>1) NULL,
  PRIMARY KEY(Id)
);

CREATE TABLE Liga (
  Id INTEGER IDENTITY NOT NULL,
  Nazwa VARCHAR(30) CHECK(LEN(Nazwa)>1) NULL,
  PRIMARY KEY(Id)
);

CREATE TABLE Dru¿yna (
  Id INTEGER IDENTITY NOT NULL ,
  Adres_Id INTEGER  NULL,
  Dyscyplina_Id INTEGER  NOT NULL,
  Liga_Id INTEGER NOT NULL,
  Nazwa VARCHAR(30) CHECK(LEN(Nazwa)>1) NULL,
  Data_zalozenia DATE CHECK(Data_zalozenia <= GETDATE()) NULL,
  PRIMARY KEY(Id),
  FOREIGN KEY(Liga_Id)
  REFERENCES Liga(Id),
  FOREIGN KEY(Dyscyplina_Id)
  REFERENCES Dyscyplina(Id),
  FOREIGN KEY(Adres_Id)
  REFERENCES Adres(Id)
);

CREATE TABLE Trener_has_Dru¿yna (
  Trener_Id INTEGER  NOT NULL,
  Dru¿yna_Id INTEGER  NOT NULL,
  DataWstapienia DATE NULL,
  DataWystapienia DATE NULL,
  PRIMARY KEY(Trener_Id, Dru¿yna_Id),
  FOREIGN KEY(Trener_Id)
  REFERENCES Trener(Id),
  FOREIGN KEY(Dru¿yna_Id)
  REFERENCES Dru¿yna(Id)
);


CREATE TABLE Zawodnik_has_Dru¿yna (
  Zawodnik_Id INTEGER  NOT NULL,
  Dru¿yna_Id INTEGER  NOT NULL,
  DataWstapienia DATE CHECK(DataWstapienia<=GETDATE()) NULL,
  DataWystapienia DATE CHECK(DataWystapienia<=GETDATE()) NULL,
  PRIMARY KEY(Zawodnik_Id, Dru¿yna_Id),
  FOREIGN KEY(Zawodnik_Id)
  REFERENCES Zawodnik(Id),
  FOREIGN KEY(Dru¿yna_Id)
  REFERENCES Dru¿yna(Id)
);

INSERT INTO Zawodnik_has_Dru¿yna(Zawodnik_Id,Dru¿yna_Id)
VALUES('1','2')
SELECT * FROM Zawodnik_has_Dru¿yna

CREATE TABLE Spotkanie (
  Id INTEGER IDENTITY NOT NULL,
  GospodarzID INTEGER  NOT NULL,
  GoœæID INTEGER  NOT NULL,
  Sêdzia_Id INTEGER  NOT NULL,
  GospodarzPunkty INTEGER CHECK(GospodarzPunkty>=0)  NULL,
  GoœæPunkty INTEGER CHECK(GoœæPunkty >= 0) NULL,
  Cena INTEGER CHECK(Cena>=0) NULL,
  Data DATE NULL,
  PRIMARY KEY(Id, GospodarzID, GoœæID),
  FOREIGN KEY(GoœæID)
  REFERENCES Dru¿yna(Id),
  FOREIGN KEY(GospodarzID)
  REFERENCES Dru¿yna(Id),
  FOREIGN KEY(Sêdzia_Id)
  REFERENCES Sêdzia(Id)
);

CREATE TABLE Uzytkownik(
 Id INTEGER IDENTITY NOT NULL,
 Login VARCHAR(30) NOT NULL,
 Has³o VARCHAR(32) NOT NULL,
 Data_logowania DATE NULL,
 Godzina_logowania TIME null,
 Data_wylogowania DATE null,
 Godzina_wylogowania TIME null,
 Data_utworzenia DATE NOT NULL,
 Stworzy³ VARCHAR(30) NOT NULL,
);

----USUWAC W TEJ KOLEJNOSCI----
DROP TABLE Spotkanie
DROP TABLE Trener_has_Dru¿yna
DROP TABLE Zawodnik_has_Dru¿yna
DROP TABLE Dru¿yna
DROP TABLE Dyscyplina
DROP TABLE Liga
DROP TABLE Zawodnik
DROP TABLE Trener
DROP TABLE Sêdzia
DROP TABLE Adres
DROP TABLE Uzytkownik 


SELECT * FROM Adres
SELECT * FROM Zawodnik
SELECT * FROM Trener
SELECT * FROM Uzytkownik
SELECT * FROM Sêdzia
SELECT * FROM Dyscyplina
SELECT * FROM Dru¿yna
SELECT * FROM Liga




SELECT Id FROM Dru¿yna













SELECt * FROM Spotkanie

select GospodarzID from Spotkanie WHERE(GospodarzID='3' or GoœæID='8');

DELETE From Spotkanie
WHERE Id='1'
go
INSERT INTO Adres(Miejscowoœæ,Ulica,Nr_domu,Kod_pocztowy)
VALUES('Gdañsk','Startowa',7,'80-461');
INSERT INTO Adres(Miejscowoœæ,Ulica,Nr_domu,Kod_pocztowy)
VALUES('Gdañsk','Pilotów',20,'80-461');
INSERT INTO Adres(Miejscowoœæ,Ulica,Nr_domu,Kod_pocztowy)
VALUES('Pruszcz','Cyprysowa',10,'83-000');
INSERT INTO Trener_has_Dru¿yna(Trener_Id,Dru¿yna_Id)
VALUES('1','8');
go
INSERT INTO Zawodnik(Adres_id,Pesel,Imiê,Nazwisko,Data_urodzenia,P³eæ,Poziom)
VALUES('1','99102502637','Pawe³','Pitucha','1999-10-25','m','Senior');
INSERT INTO Uzytkownik(Login,Has³o,Data_utworzenia,Stworzy³)
VALUES('admin','5f4dcc3b5aa765d61d8327deb882cf99','2020-05-23','pierwsze konto');
 
 INSERT INTO Dyscyplina(Nazwa)
VALUES('Pi³ka no¿na');
 INSERT INTO Dyscyplina(Nazwa)
VALUES('Pi³ka rêczna');
 INSERT INTO Dyscyplina(Nazwa)
VALUES('Koszykówka');
 INSERT INTO Dyscyplina(Nazwa)
VALUES('Siatkówka');

SELECT Nazwa FROM Liga WHERE Id='1'
SELECT MAX(Id) as 'MAX' FROM Dru¿yna
INSERT INTO Liga(Nazwa)
VALUES('SuperLiga');
INSERT INTO Liga(Nazwa)
VALUES('1 Liga');
INSERT INTO Liga(Nazwa)
VALUES('2 Liga');
INSERT INTO Liga(Nazwa)
VALUES('3 Liga');
 SELECT * From Liga
 SELECT COUNT(*) FROM Zawodnik WHERE Poziom='Senior'
 DELETE Adres FROM Adres inner join Zawodnik On Adres.Id=Zawodnik.Adres_id WHERE Pesel=''
 DELETE Zawodnik FROM Zawodnik WHERE Pesel='99192837465'
 DELETE Adres FROM Adres where Id='5'
 SELECT COUNT(*) as 'liczba' FROM Dru¿yna 



 DELETE FROM Uzytkownik WHERE Id='3'

 UPDATE Adres 
 SET Miejscowoœæ='ELdorado'
WHERE Id=1


UPDATE 
SET Imiê = 'treneeerioooo'
WHERE Id=

SELECT AVG(Cena) FROM Spotkanie WHERE MONTH(Data)='5'
SELECt * FROM Spotkanie
UPDATE Uzytkownik
SET Godzina_wylogowania='00:01:14.341'
Godzina_zalogowania='00:01:14.341'
WHERE Login='admin'