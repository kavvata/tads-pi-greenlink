CREATE TABLE IF NOT EXISTS jardins (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nome      TEXT    NOT NULL,
    descricao TEXT    NOT NULL,
    ativo     BOOLEAN NOT NULL
                      DEFAULT true
);

CREATE TABLE IF NOT EXISTS plantas (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nome      TEXT    NOT NULL,
    descricao TEXT    NOT NULL,
    jardim_id INT     NOT NULL,
    ativo     BOOLEAN NOT NULL
                      DEFAULT true,

    FOREIGN KEY(jardim_id) REFERENCES jardins (id) 
);

CREATE TABLE tarefas (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nome      TEXT    NOT NULL,
    descricao TEXT    NOT NULL,
    prazo     TEXT,
    feito     BOOLEAN NOT NULL
                      DEFAULT false,
    planta_id INTEGER NOT NULL,
    ativo     BOOLEAN NOT NULL
                      DEFAULT true,

    FOREIGN KEY (planta_id) REFERENCES plantas (id) 
);

-- TODO: implementar duas triggers e duas funções.