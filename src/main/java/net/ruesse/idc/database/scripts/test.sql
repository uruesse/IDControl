CREATE TABLE SCANLOG (
    ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    MGLNR BIGINT,
    SCANTIME TIMESTAMP,
    FOREIGN KEY(MGLNR) REFERENCES PERSON(MGLNR)
);