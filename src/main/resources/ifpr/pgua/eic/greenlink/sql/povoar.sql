--- ----------------------------------------------------------
--- Povoando banco
--- ----------------------------------------------------------

insert 
    into usuarios(nome, salt, hash) 
    values 
        -- nome: admin | senha: admin
    	("admin", X'5c85ca6f432379de7b12426cf74c50a2', X'816b81f23df9e82a15f9f1e503c8d1d86261b7b44808ff35');

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

