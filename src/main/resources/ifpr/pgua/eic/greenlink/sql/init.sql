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

CREATE TABLE IF NOT EXISTS tarefas (
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

insert 
    into jardins(nome, descricao) 
values 
    ("Varanda", "Varanda da minha casa!"), 
    ("Cozinha", "Cozinha de casa."),
    ("Quarto", "Meu quarto"),
    ("Escritorio", "Meu escritorio!");

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