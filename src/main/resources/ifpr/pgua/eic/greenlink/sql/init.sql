--- ----------------------------------------------------------
--- Criação do banco
--- ----------------------------------------------------------

CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL UNIQUE,
    salt VARBINARY(255) NOT NULL,
    hash VARBINARY(255) NOT NULL,
    ativo BIT NOT NULL DEFAULT 1
); 

CREATE TABLE IF NOT EXISTS jardins (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT NOT NULL,
    usuario_id integer not null,
    ativo BIT NOT NULL DEFAULT 1,

    foreign key(usuario_id) references usuarios(id)
); 

CREATE TABLE IF NOT EXISTS plantas (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT NOT NULL,
    jardim_id INTEGER NOT NULL,
    ativo BIT NOT NULL DEFAULT 1,

    FOREIGN KEY(jardim_id) REFERENCES jardins(id)
); 

CREATE TABLE IF NOT EXISTS tarefas (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT NOT NULL,
    prazo DATE NOT NULL,
    feito BIT NOT NULL DEFAULT 0,
    planta_id INTEGER NOT NULL,
    ativo BIT NOT NULL DEFAULT 1,

    FOREIGN KEY(planta_id) REFERENCES plantas(id)
);
