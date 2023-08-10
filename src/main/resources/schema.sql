DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS favorite_games;
DROP TABLE IF EXISTS favorite_players;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS openings;
DROP TABLE IF EXISTS players;


CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS User_Roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES Users (user_id),
    FOREIGN KEY (role_id) REFERENCES Roles (role_id)
);

CREATE TABLE IF NOT EXISTS Openings (
    opening_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(5) NOT NULL,
    name VARCHAR(255) NOT NULL,
    fen VARCHAR(255) NOT NULL,
    pgn_moves VARCHAR(255) NOT NULL,
    uci_moves VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Players (
    player_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    sex VARCHAR(1),
    elo_rating INT
);

CREATE TABLE IF NOT EXISTS Games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    opening_code INT,
    player1_id INT,
    player2_id INT,
    pgn TEXT,
    result VARCHAR(10),
    moves_number INT,
    date DATE,
    FOREIGN KEY (opening_code) REFERENCES Openings (opening_id),
    FOREIGN KEY (player1_id) REFERENCES Players (player_id),
    FOREIGN KEY (player2_id) REFERENCES Players (player_id)
);

CREATE TABLE IF NOT EXISTS favorite_games (
    user_id INT,
    game_id INT,
    PRIMARY KEY (user_id, game_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (game_id) REFERENCES games(game_id)
);

CREATE TABLE IF NOT EXISTS favorite_players (
	user_id INT,
    player_id INT,
    PRIMARY KEY (user_id, player_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);