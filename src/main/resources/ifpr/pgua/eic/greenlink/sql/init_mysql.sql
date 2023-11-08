CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL UNIQUE,
    hash_senha VARCHAR(255) NOT NULL,
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

--
-- Stored procedures
--

-- mostrar todas as tarefas
DELIMITER $$
drop procedure if exists mostrar_tarefas_usuario $$
create procedure mostrar_tarefas_usuario(in usr_id int)
begin
SELECT
    t.id AS id,
    t.nome AS nome,
    t.descricao AS descricao,
    t.prazo AS prazo,
    t.feito AS feito,
    t.planta_id AS planta_id
FROM
    tarefas t
JOIN plantas p ON
    t.planta_id = p.id
JOIN jardins j ON
    p.jardim_id = j.id
JOIN usuarios u ON
    j.usuario_id = u.id
WHERE
    u.id = usr_id AND t.ativo = 1 AND t.feito=0;
END $$
DELIMITER ;

-- listar tarefas da planta
DELIMITER $$
drop procedure if exists listar_tarefas_planta $$
create procedure listar_tarefas_planta(in p_id int)
begin
    SELECT * FROM tarefas t where t.planta_id=p_id;
END $$
DELIMITER ;

-- listar tarefas do jardim
DELIMITER $$
drop procedure if exists listar_tarefas_jardim $$
create procedure listar_tarefas_jardim(in j_id int)
begin
SELECT
    t.id AS id,
    t.nome AS nome,
    t.descricao AS descricao,
    t.prazo AS prazo,
    t.feito AS feito,
    t.planta_id AS planta_id
FROM
    tarefas t
JOIN plantas p ON
    t.planta_id = p.id
JOIN jardins j ON
    p.jardim_id = j.id
WHERE
    j.id = j_id AND t.ativo = 1 AND t.feito=0;
END $$
DELIMITER ;

-- listar todas as plantas
DELIMITER $$
drop procedure if exists listar_plantas_usuario $$
create procedure listar_plantas_usuario(in usr_id int)
begin
SELECT
    p.id AS id,
    p.nome AS nome,
    p.descricao AS descricao,
    p.jardim_id AS jardim_id
FROM
    plantas p
JOIN jardins j ON
    p.jardim_id = j.id
JOIN usuarios u ON
    j.usuario_id = u.id
WHERE
    u.id = usr_id AND p.ativo = 1;
END $$
DELIMITER ;

-- busca planta por nome
DELIMITER $$
drop procedure if exists buscar_planta_nome $$
create procedure buscar_planta_nome(in p_nome varchar(50), in usr_id int)
begin
SELECT
    p.id AS id,
    p.descricao AS descricao,
    p.jardim_id AS jardim_id
FROM
    plantas p
JOIN jardins j ON
    p.jardim_id = j.id
JOIN usuarios u ON
    j.usuario_id = u.id
WHERE
    u.id = usr_id AND p.nome = p_nome AND p.ativo = 1;
END $$
DELIMITER ;

-- listar jardins do usuario
DELIMITER $$
drop procedure if exists listar_jardins_usuario $$
create procedure listar_jardins_usuario(in usr_id int)
begin
SELECT
    j.id AS id,
    j.nome as nome,
    j.descricao AS descricao
FROM
    jardins j
WHERE
    j.usuario_id = usr_id AND j.ativo = 1;
END $$
DELIMITER ;

--
-- Povoando banco
--

insert
    into usuarios(nome, senha) values ("admin", "admin");

insert 
    into jardins(nome, descricao, usuario_id) 
values 
    ("Varanda", "Varanda da minha casa!", 1), 
    ("Cozinha", "Cozinha de casa.", 1),
    ("Quarto", "Meu quarto", 1),
    ("Escritorio", "Meu escritorio!", 1);

insert
    into plantas(nome, descricao, jardim_id)
values
    ("Samambaia", "", 1),
    ("Espada de São Jorge", "", 1),
    ("Cacto", "Ganhei de presente", 1),
    ("Pimenta de bode", "o melhor para o mal olhado!", 2),
    ("Hortelã", "", 2), 
    ("Planta de Jade", "Suculenta", 2),
    ("Lavanda", "na mesa de cabeceira", 3),
    ("Cacto", "encima da escrivaninha", 3),
    ("Flor da fortuna", "", 4),
    ("Pimenta malagueta", "contra a inveja", 4);

insert 
    into tarefas(nome, descricao, prazo, planta_id)
values
    ("regar", "dois litros", "2023-11-05", 1),
    ("regar", "ate a terra ficar umida", "2023-11-10", 5),
    ("plantar no quintal", "cresceu bastante!", "2023-11-23", 5),
    ("colher pimenta", "", "2023-11-28", 4),
    ("regar", "fora do quarto para nao molhar os servidores", "2023-11-15", 7),
    ("podar", "folhas secas", "2023-11-15", 7),
    ("jogar fora", "secou :(", "2023-11-06", 10),
    ("trocar terra", "colocar terra mais arenada", "2023-11-07", 6);


-- TODO: implementar duas triggers e duas funções.

-- mostrar todas as tarefas