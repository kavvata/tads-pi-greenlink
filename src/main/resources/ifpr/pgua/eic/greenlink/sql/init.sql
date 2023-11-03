CREATE TABLE IF NOT EXISTS jardins (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nome      TEXT    NOT NULL,
    descricao TEXT    NOT NULL
);

CREATE TABLE IF NOT EXISTS plantas (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nome      TEXT    NOT NULL,
    descricao TEXT    NOT NULL,
    jardim_id INT     NOT NULL,

    FOREIGN KEY(jardim_id) REFERENCES jardim (id) 
);