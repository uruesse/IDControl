DROP TABLE STAGE.RECHNUNGEN;
DROP TABLE STAGE.BEITRAGSPOSITIONEN;
DROP TABLE STAGE.LEBENSLAUF;
DROP TABLE STAGE.OFFENERECHNUNGEN;
DROP TABLE STAGE.PERSON;

CREATE TABLE STAGE.PERSON (
ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
NUMMER INT,
MITGLIEDSNUMMER_SEWOBE BIGINT,
FAMILIENNUMMER_SEWOBE INT,
FIRMA VARCHAR(128),
ANREDE VARCHAR(128),
TITEL VARCHAR(128),
NACHNAME VARCHAR(128),
VORNAME VARCHAR(128),
STRASSE VARCHAR(128),
PLZ VARCHAR(16),
ORT VARCHAR(128),
LAND VARCHAR(128),
HAUPTKATEGORIE VARCHAR(128),
TELEFON_1 VARCHAR(128),
TELEFON_2 VARCHAR(128),
MOBIL VARCHAR(128),
TELEFAX VARCHAR(128),
EMAIL VARCHAR(128),
GEBURTSDATUM DATE,
BEMERKUNG VARCHAR(128),
STATUS VARCHAR(128),
EINTRITT DATE,
AUSTRITT DATE,
KUENDIGUNG DATE,
IBAN VARCHAR(128),
BIC VARCHAR(128),
KONTOINHABER VARCHAR(128),
MANDATSREFERENZ VARCHAR(128),
MANDAT_VORHANDEN BOOLEAN,
ZAHLUNGSMODUS VARCHAR(128),
ABWEICHENDERZAHLER BOOLEAN
);

CREATE TABLE STAGE.RECHNUNGEN (
ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
MITGLIEDSNUMMER_SEWOBE BIGINT,
FAMILIENNUMMER_SEWOBE INT,
RECHNUNGSDATUM DATE,
RECHNUNGSNUMMER BIGINT,
RECHNUNGSSUMME INT,
ZAHLMODUS VARCHAR(128),
ZAHLUNGSZIEL DATE
);

CREATE TABLE STAGE.BEITRAGSPOSITIONEN (
ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
MITGLIEDSNUMMER_SEWOBE BIGINT,
FAMILIENNUMMER_SEWOBE INT,
BEITRAGSPOSITION VARCHAR(128),
BEITRAGSKOMMENTAR VARCHAR(128),
FAELLIG_START DATE,
FAELLIG_ENDE DATE,
ABRECHNUNGSSTATUS VARCHAR(128),
ZAHLUNGSWEISE VARCHAR(128)
);

CREATE TABLE STAGE.LEBENSLAUF (
ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
MITGLIEDSNUMMER_SEWOBE BIGINT,
LEBENSLAUF_ART VARCHAR(128),
LEBENSLAUF_BEGINN DATE,
LEBENSLAUF_ENDE DATE, 
LEBENSLAUF_BEZEICHNUNG VARCHAR(128),
LEBENSLAUF_KURZTEXT VARCHAR(128),
LEBENSLAUF_LANGTEXT VARCHAR(128)
);

CREATE TABLE STAGE.OFFENERECHNUNGEN (
ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
KDNR VARCHAR(128),
MITGLIEDSNUMMER_SEWOBE BIGINT,
RECHNUNGSNUMMER BIGINT,
RECHNUNGSDATUM DATE,
ZAHLUNGSZIEL DATE,
ZAHLWEISE VARCHAR(128),
MAHNSTUFE INT,
BEZEICHNUNG VARCHAR(128),
AKTUELLOFFEN INT
);