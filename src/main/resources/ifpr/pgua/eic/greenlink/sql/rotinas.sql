--- ----------------------------------------------------------
-- Stored procedures
--- ----------------------------------------------------------

-- listar todas as tarefas
DELIMITER $$
drop procedure if exists listar_tarefas_usuario $$
create procedure listar_tarefas_usuario(in usr_id int)
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
    j.usuario_id = usr_id AND t.ativo = 1 AND t.feito=0;
END $$
DELIMITER ;

-- listar tarefas da planta
DELIMITER $$
drop procedure if exists listar_tarefas_planta $$
create procedure listar_tarefas_planta(in p_id int)
begin
    SELECT * FROM tarefas t where t.planta_id=p_id AND t.ativo=1 AND t.feito=0;
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
WHERE
    j.usuario_id = usr_id AND p.ativo = 1;
END $$
DELIMITER ;

-- buscar planta por nome
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
WHERE
    j.usuario_id = usr_id AND p.nome = p_nome AND p.ativo = 1;
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

-- buscar jardim por nome
DELIMITER $$
drop procedure if exists buscar_jardim_nome $$
create procedure buscar_jardim_nome(in j_nome varchar(50), in usr_id int)
begin
SELECT
    j.id AS id,
    j.descricao AS descricao
FROM
    jardins j
WHERE
    j.usuario_id = usr_id AND j.nome = j_nome AND p.ativo = 1;
END $$
DELIMITER ;

-- buscar jardim por id de planta
DELIMITER $$
drop procedure if exists buscar_jardim_planta $$
create procedure buscar_jardim_planta(in p_id int)
begin
SELECT
    j.id AS id,
    j.nome as nome,
    j.descricao AS descricao
FROM
    jardins j
JOIN plantas p ON 
    p.jardim_id = j.id
WHERE
    p.id=p_id AND j.ativo = 1;
END $$
DELIMITER ;

-- buscar planta por id de tarefa
DELIMITER $$
drop procedure if exists buscar_planta_tarefa $$
create procedure buscar_planta_tarefa(in t_id int)
begin
SELECT
    p.id as id,
    p.nome as nome,
    p.descricao as descricao,
    p.jardim_id as jardim_id
FROM
    plantas p
JOIN tarefas t ON 
    t.planta_id = p.id
WHERE
    t.id=t_id AND p.ativo = 1;
END $$
DELIMITER ;



--- ----------------------------------------------------------
--- Triggers
--- ----------------------------------------------------------

DELIMITER $$
drop trigger if exists remover_planta $$
create trigger remover_planta before update on plantas
for each row
begin
    if new.ativo = 0 then
       update tarefas set ativo=0 where planta_id = new.id;
    end if;
END $$
DELIMITER ;

DELIMITER $$
drop trigger if exists remover_jardim $$
create trigger remover_jardim before update on jardins
for each row
begin
    if new.ativo = 0 then
       update plantas set ativo=0 where jardim_id = new.id;
    end if;
END $$
DELIMITER ;



--- ----------------------------------------------------------
--- Funções
--- ----------------------------------------------------------



DELIMITER $$
drop function if exists busca_salt $$
CREATE FUNCTION busca_salt(usr_name varchar(50)) RETURNS varbinary(255)
DETERMINISTIC
BEGIN
    RETURN (SELECT u.salt FROM usuarios u WHERE u.nome=usr_name);
END $$
DELIMITER ;


DELIMITER $$
drop function if exists compara_hash $$
CREATE FUNCTION compara_hash(usr_name varchar(50), usr_hash varbinary(255)) RETURNS boolean
DETERMINISTIC
BEGIN
    DECLARE chave varbinary(255);
    SET chave = (SELECT u.hash FROM usuarios u WHERE u.nome=usr_name);
    IF usr_hash = chave THEN
        RETURN true;
    END IF;
    RETURN false;
END $$
DELIMITER ;
