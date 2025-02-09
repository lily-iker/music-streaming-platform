CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    date_of_birth DATE,
    phone_number VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE role (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO role (name) VALUES ('USER'), ('ADMIN'), ('MOD'), ('ARTIST');

CREATE TABLE permission (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- Add more if need
INSERT INTO permission (name) VALUES
('CREATE'),
('READ'),
('UPDATE'),
('DELETE'),
('LIKE'),
('DISLIKE'),
('FOLLOW'),
('UNFOLLOW'),
('UPLOAD_SONG'),
('EDIT_SONG'),
('DELETE_SONG'),
('CREATE_PLAYLIST'),
('ADD_TO_PLAYLIST'),
('REMOVE_FROM_PLAYLIST'),
('SHARE_PLAYLIST'),
('SEARCH_SONG'),
('SEARCH_ARTIST'),
('SEARCH_ALBUM'),
('VIEW_HISTORY'),
('MANAGE_ACCOUNT'),
('SUBSCRIBE'),
('UNSUBSCRIBE'),
('RECEIVE_NOTIFICATIONS'),
('SET_PRIVACY'),
('BLOCK_USER'),
('REPORT_CONTENT'),
('VIEW_USER_PROFILE'),
('SEND_MESSAGES'),
('VIEW_TRENDING'),
('ACCESS_PREMIUM_FEATURES');

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT user_roles_user_fk FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT user_roles_role_fk FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

CREATE TABLE role_permissions (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT role_permissions_role_fk FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    CONSTRAINT role_permissions_permission_fk FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

CREATE TABLE artist (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    bio VARCHAR(2048),
    image_url VARCHAR(512),
    followers BIGINT DEFAULT 0,
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT artist_user_fk FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
);

CREATE TABLE album (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    image_url VARCHAR(512),
    artist_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT album_artist_fk FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE
);

CREATE TABLE genre (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
-- Add more if need
INSERT INTO genre (name) VALUES
    ('POP'),
    ('INDIE'),
    ('HIP_HOP'),
    ('R_AND_B'),
    ('PHONK'),
    ('EDM'),
    ('JAZZ'),
    ('COUNTRY'),
    ('PUNK'),
    ('ROCK');

CREATE TABLE song (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    duration INT,
    image_url VARCHAR(512),
    song_url VARCHAR(512),
    like_count BIGINT DEFAULT 0,
    album_id BIGINT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT song_album_fk FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE SET NULL
);

CREATE TABLE artist_songs (
    artist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    PRIMARY KEY (artist_id, song_id),
    CONSTRAINT artist_songs_artist_fk FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE,
    CONSTRAINT artist_songs_song_fk FOREIGN KEY (song_id) REFERENCES song(id) ON DELETE CASCADE
);

CREATE TABLE song_genres (
    song_id BIGINT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (song_id, genre_id),
    CONSTRAINT song_genres_song_fk FOREIGN KEY (song_id) REFERENCES song(id) ON DELETE CASCADE,
    CONSTRAINT song_genres_genre_fk FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
);

CREATE TABLE playlist (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT playlist_user_fk FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
);

CREATE TABLE song_playlists (
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    PRIMARY KEY (playlist_id, song_id),
    CONSTRAINT song_playlists_playlist_fk FOREIGN KEY (playlist_id) REFERENCES playlist(id) ON DELETE CASCADE,
    CONSTRAINT song_playlists_song_fk FOREIGN KEY (song_id) REFERENCES song(id) ON DELETE CASCADE
);

CREATE TABLE listening_history (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    song_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT listening_history_user_fk FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT listening_history_song_fk FOREIGN KEY (song_id) REFERENCES song(id) ON DELETE CASCADE
);

CREATE TABLE address (
    id BIGINT NOT NULL AUTO_INCREMENT,
    street_number VARCHAR(255),
    street VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT address_user_fk FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

INSERT INTO user (first_name, last_name, gender, date_of_birth, phone_number, email, username, is_active)
VALUES
('admin', 'admin', 'MALE', '2004-12-10', '6969696969', 'trongdz2200444@gmail.com', 'adminadmin', TRUE),
('Alice', 'Smith', 'FEMALE', '1990-05-15', '1234567890', CONCAT('alice', 'smith', '@gmail.com'), 'alicesmith', TRUE),
('Bob', 'Johnson', 'MALE', '1988-08-20', '0987654321', CONCAT('bob', 'johnson', '@gmail.com'), 'bobjohnson', TRUE),
('Charlie', 'Brown', 'OTHER', '1992-12-12', '1122334455', CONCAT('charlie', 'brown', '@gmail.com'), 'charliebrown', TRUE),
('Diana', 'Prince', 'FEMALE', '1995-01-01', '5566778899', CONCAT('diana', 'prince', '@gmail.com'), 'dianaprince', TRUE),
('Eve', 'Davis', 'FEMALE', '1986-03-22', '1010101010', CONCAT('eve', 'davis', '@gmail.com'), 'evedavis', TRUE),
('Frank', 'Wilson', 'MALE', '1990-06-25', '1212121212', CONCAT('frank', 'wilson', '@gmail.com'), 'frankwilson', TRUE),
('Grace', 'Lee', 'FEMALE', '1985-09-10', '1313131313', CONCAT('grace', 'lee', '@gmail.com'), 'gracelee', TRUE),
('Henry', 'Clark', 'MALE', '1989-11-18', '1414141414', CONCAT('henry', 'clark', '@gmail.com'), 'henryclark', TRUE),
('Ivy', 'Taylor', 'FEMALE', '1993-04-30', '1515151515', CONCAT('ivy', 'taylor', '@gmail.com'), 'ivytaylor', TRUE),
('Jack', 'White', 'MALE', '1991-07-15', '1616161616', CONCAT('jack', 'white', '@gmail.com'), 'jackwhite', TRUE),
('Toms', 'Burkovskis', 'MALE', '1992-09-10', '1231231234', CONCAT('toms', 'burkovskis', '@gmail.com'), 'tomsburkovskis', TRUE),
('Sơn Tùng', 'M-TP', 'MALE', '1994-07-05', '9876543210', CONCAT('sontung', 'mtp', '@gmail.com'), 'sontungmtp', TRUE),
('TheFatRat', '', 'MALE', '1979-06-01', '1234512345', CONCAT('thefatrat', '@gmail.com'), 'thefatrat', TRUE),
('Juice', 'WRLD', 'MALE', '1998-12-02', '3213213210', CONCAT('juicewrld', '@gmail.com'), 'juicewrld', TRUE),
('Travis', 'Scott', 'MALE', '1991-04-30', '6546546540', CONCAT('travisscott', '@gmail.com'), 'travisscott', TRUE),
('Playboi', 'Carti', 'MALE', '1996-09-13', '7897897890', CONCAT('playboicarti', '@gmail.com'), 'playboicarti', TRUE),
('Ariana', 'Grande', 'FEMALE', '1993-06-26', '1112223333', CONCAT('arianagrande', '@gmail.com'), 'arianagrande', TRUE),
('Ed', 'Sheeran', 'MALE', '1991-02-17', '2223334444', CONCAT('edsheeran', '@gmail.com'), 'edsheeran', TRUE),
('Abel', 'Tesfaye', 'MALE', '1990-02-16', '5556667777', CONCAT('theweeknd', '@gmail.com'), 'theweeknd', TRUE),
('Hung', 'Dam Vinh', 'MALE', '1971-10-02', '9696969696', CONCAT('dam', 'vinhhung', '@gmail.com'), 'damvinhhung', TRUE),
('Big', 'Bang', 'MALE', '2006-01-01', '1234567812', CONCAT('big', 'bang', '@gmail.com'), 'bigbanggg', TRUE),
('Various', 'Artists', 'MALE', '2006-01-01', '1234567869', CONCAT('various', 'artists', '@gmail.com'), 'variousartists', TRUE);

INSERT INTO user_roles (user_id, role_id)
VALUES
(1, 2),  -- adminadmin is ADMIN
(2, 1),  -- Alice is an USER
(3, 1),  -- Bob is an USER
(4, 1),  -- Charlie is an USER
(5, 1),  -- Diana is an USER
(6, 1),  -- Eve is an USER
(7, 1),  -- Frank is an USER
(8, 1),  -- Grace is an USER
(9, 1),  -- Henry is an USER
(10, 1), -- Ivy is an USER
(11, 3),  -- Jack is a MOD
(12, 4),  -- Toms is an ARTIST
(13, 4),  -- Sơn Tùng MTP is an ARTIST
(14, 4),  -- TheFatRat is an ARTIST
(15, 4),  -- Juice WRLD is an ARTIST
(16, 4),  -- Travis Scott is an ARTIST
(17, 4),  -- Playboi Carti is an ARTIST
(18, 4),  -- Ariana Grande is an ARTIST
(19, 4),  -- Ed Sheeran is an ARTIST
(20, 4),  -- The Weeknd is an ARTIST
(21, 4),  -- Đàm Vĩnh Hưng is an ARTIST
(22, 4),
(23, 4);

INSERT INTO artist (name, bio, image_url, followers, user_id)
VALUES
('Tobu', 'Tobu \'s bio', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731717360/tobu_cd7a9l.jpg', 100000000, 12),
('Sơn Tùng M-TP', 'Sơn Tùng M-TP \'s bio', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731717759/mtp_zm7tdf.jpg', 123456789, 13),
('TheFatRat', 'TheFatRat \'s bio', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731721468/thefatrat_yeyrj6.jpg', 45678909, 14),
('Juice WRLD', 'Juice WRLD \'s bio', 'https://i.ytimg.com/vi/mzB1VGEGcSU/maxresdefault.jpg', 5000000, 15),
('Travis Scott', 'Travis Scott \'s bio', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739117774/travis_scott_sbg33p.jpg', 6000000, 16),
('Playboi Carti', 'Playboi Carti \'s bio', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739117802/playboi_dpehzc.png', 3000000, 17),
('Ariana Grande', 'Ariana Grande \'s bio', 'https://upload.wikimedia.org/wikipedia/en/7/7a/Sweetener_album_cover.png', 10000000, 18),
('Ed Sheeran', 'Ed Sheeran \'s bio', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739118230/ed_sheeran_t0ci3e.jpg', 15000000, 19),
('The Weeknd', 'The Weeknd \'s bio', 'https://upload.wikimedia.org/wikipedia/en/c/c1/The_Weeknd_-_After_Hours.png', 30000000, 20),
('Đàm Vĩnh Hưng', 'Đàm Vĩnh Hưng \'s bio', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738244776/dam_vinh_hung_kkvsgx.jpg', 69696969, 21),
('Big Bang', 'Big Bang \'s bio', 'https://upload.wikimedia.org/wikipedia/vi/b/b2/BIGBANG_-_MADE.jpg', 12345678, 22),
('Various Artists', 'Various Artists \'s bio', 'https://avatars.githubusercontent.com/u/157276347?s=400&u=93818af931519c98da9245b9e758634fccfcac10&v=4', 12345679, 23);

-- Insert Tobu's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('Colors', 'https://i.ytimg.com/vi/eyLml-zzXzw/maxresdefault.jpg', 1),
('Magic', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738237194/magic_xzyuqx.jpg', 1),
('Hope', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718358/hope_fljucm.jpg', 1);

-- Insert Tobu's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- Colors Album
('Infectious', 317, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731719420/infectious_qlnuc0.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731718393/Infectious_-_Tobu_ybjtgc.mp4', 15000000,
(SELECT id FROM album WHERE name = 'Colors' AND artist_id = 1)),

('Colors', 279, 'https://i.ytimg.com/vi/eyLml-zzXzw/maxresdefault.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731717696/Colors_-_Tobu_utajvf.mp4', 20000000,
(SELECT id FROM album WHERE name = 'Colors' AND artist_id = 1)),

('Cloud 9', 278, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731719423/cloud9_azz9i3.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731718402/Cloud_9_-Tobu_vs19b6.mp4', 18000000,
(SELECT id FROM album WHERE name = 'Colors' AND artist_id = 1)),

-- Magic Album
('Magic', 253, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738237194/magic_xzyuqx.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731717718/Magic_-_Tobu_m0yno3.mp4', 25000000,
(SELECT id FROM album WHERE name = 'Magic' AND artist_id = 1)),

('Sunburst', 189, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731719426/sunburst_qykxjg.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731718409/Sunburst_-_Tobu_t8gcad.mp4', 19000000,
(SELECT id FROM album WHERE name = 'Magic' AND artist_id = 1)),

-- Hope Album
('Hope', 288, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718358/hope_fljucm.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731717726/Hope_-_Tobu_yuw6xq.mp4', 30000000,
(SELECT id FROM album WHERE name = 'Hope' AND artist_id = 1)),

('Candyland', 198, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731719433/candyland_nmdgep.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731718414/Candyland_-_Tobu_ugwzux.mp4', 28000000,
(SELECT id FROM album WHERE name = 'Hope' AND artist_id = 1));

-- Link songs to Tobu (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT 1, id FROM song WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = 1
);

-- Add genres for Tobu's songs (primarily EDM)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 6 -- 6 is EDM genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = 1
);

-- Some songs also fit in the POP category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.name IN ('Colors', 'Hope')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = 1
);

-- Insert TheFatRat's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('PARALLAX', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731721331/parallax_qsqbss.jpg',
    (SELECT id FROM artist WHERE name = 'TheFatRat')),
('Warrior Songs', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731721335/warrior-songs_hnbaej.jpg',
    (SELECT id FROM artist WHERE name = 'TheFatRat')),
('RISE UP', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731721339/rise-up_fglypm.jpg',
    (SELECT id FROM artist WHERE name = 'TheFatRat'));

-- Insert TheFatRat's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- PARALLAX Album
('Unity', 249, 'https://i.ytimg.com/vi/n8X9_MgEdCg/maxresdefault.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1738241606/Unity_kgky9h.mp3', 50000000,
(SELECT id FROM album WHERE name = 'PARALLAX' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('Monody (feat. Laura Brehm)', 291, 'https://i.ytimg.com/vi/B7xai5u_tnk/maxresdefault.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1738241611/Monody_n1vdhw.mp3', 45000000,
(SELECT id FROM album WHERE name = 'PARALLAX' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('The Calling (feat. Laura Brehm)', 234, 'https://i.ytimg.com/vi/KR-eV7fHNbM/maxresdefault.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1738241689/The_Calling_edjbbe.mp3', 40000000,
(SELECT id FROM album WHERE name = 'PARALLAX' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

-- Warrior Songs Album
('Xenogenesis', 235, 'https://i.ytimg.com/vi/2Ax_EIb1zks/maxresdefault.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1738241697/Xenogenesis_dh4k2d.mp3', 35000000,
(SELECT id FROM album WHERE name = 'Warrior Songs' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

-- RISE UP Album
('Fly Away (feat. Anjulie)', 194, 'https://i.ytimg.com/vi/cMg8KaMdDYo/maxresdefault.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731721580/fly-away_fmidhb.mp4', 38000000,
(SELECT id FROM album WHERE name = 'RISE UP' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('Never Be Alone', 263, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731721499/never-be-alone_kahz8f.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1738241704/Never_Be_Alone_eq6vxt.mp3', 36000000,
(SELECT id FROM album WHERE name = 'RISE UP' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat')));

-- Link songs to TheFatRat (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'TheFatRat'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat')
);

-- Add genres for TheFatRat's songs (primarily EDM)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 6 -- 6 is EDM genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat')
);

-- Some songs also fit in the POP category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.name IN ('Monody (feat. Laura Brehm)', 'Fly Away (feat. Anjulie)')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat')
);

-- Insert Sơn Tùng M-TP's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('m-tp M-TP', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738240155/mtpmtp_ffiymt.jpg',
    (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP')),
('Sky Tour', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718590/sky-tour_kw0m3b.jpg',
    (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP')),
('Chúng Ta', 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718593/chung-ta_iey6qb.jpg',
    (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'));

-- Insert Sơn Tùng M-TP's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- m-tp M-TP Album
('Nắng Ấm Xa Dần', 191, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738240155/mtpmtp_ffiymt.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731719459/nang-am-xa-dan_emobrc.mp4', 99000000,
(SELECT id FROM album WHERE name = 'm-tp M-TP' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'))),

('Cơn Mưa Ngang Qua', 230, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738240155/mtpmtp_ffiymt.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731719465/con-mua-ngang-qua_u5j9gt.mp4', 100000000,
(SELECT id FROM album WHERE name = 'm-tp M-TP' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'))),

('Em Của Ngày Hôm Qua', 296, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738240155/mtpmtp_ffiymt.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731719470/em_cua_ngay_hom_qua_oz6mxs.mp4', 80000000,
(SELECT id FROM album WHERE name = 'm-tp M-TP' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'))),

-- Sky Tour Album
('Chạy Ngay Đi', 162, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718590/sky-tour_kw0m3b.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731719474/chay-ngay-di_nl185j.mp4', 30000000,
(SELECT id FROM album WHERE name = 'Sky Tour' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'))),

('Lạc Trôi', 152, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718590/sky-tour_kw0m3b.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731719477/lac-troi_nyvgnx.mp4', 35000000,
(SELECT id FROM album WHERE name = 'Sky Tour' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'))),

('Nơi Này Có Anh', 199, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718590/sky-tour_kw0m3b.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731719481/noi-nay-co-anh_jejlj6.mp4', 40000000,
(SELECT id FROM album WHERE name = 'Sky Tour' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'))),

-- Chúng Ta Album
('Chúng Ta Của Hiện Tại', 232, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718593/chung-ta_iey6qb.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731720234/chung-ta-cua-hien-tai_eimqfn.mp4', 45000000,
(SELECT id FROM album WHERE name = 'Chúng Ta' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'))),

('Có Chắc Yêu Là Đây', 142, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718593/chung-ta_iey6qb.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731719484/co-chac-yeu-la-day_bgqppi.mp4', 38000000,
(SELECT id FROM album WHERE name = 'Chúng Ta' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'))),

('Muộn Rồi Mà Sao Còn', 199, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1731718593/chung-ta_iey6qb.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731720243/muon-roi-ma-sao-con_cndmnq.mp4', 50000000,
(SELECT id FROM album WHERE name = 'Chúng Ta' AND artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP')));

-- Link songs to Sơn Tùng M-TP (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP')
);

-- Add genres for Sơn Tùng M-TP's songs (primarily POP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP')
);

-- Some songs also fit in the R&B category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 4 -- 4 is R_AND_B genre
FROM song s
WHERE s.name IN ('Chúng Ta Của Hiện Tại', 'Muộn Rồi Mà Sao Còn', 'Nơi Này Có Anh')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Sơn Tùng M-TP')
);

-- Insert Juice WRLD's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('Goodbye & Good Riddance', 'https://upload.wikimedia.org/wikipedia/en/8/86/Goodbye_%26_Good_Riddance_Album_Cover.jpg',
    (SELECT id FROM artist WHERE name = 'Juice WRLD'));

-- Insert Juice WRLD's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- Goodbye & Good Riddance Album
('Lucid Dreams', 195, 'https://i.ytimg.com/vi/mzB1VGEGcSU/maxresdefault.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739117659/lucid_dreams_ixdofd.mp3', 15000000,
(SELECT id FROM album WHERE name = 'Goodbye & Good Riddance' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD')));

-- Link songs to Juice WRLD (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Juice WRLD'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD')
);

-- Add primary genre for Juice WRLD's songs (HIP_HOP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 3 -- 3 is HIP_HOP genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD')
);

-- Insert Travis Scott's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
('FE!N', 106, 'https://i.ytimg.com/vi/2a8PgqWrc_4/maxresdefault.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739013218/fein_ccjz5r.mp3', 16000000,
NULL);

-- Link FE!N to Playboi Carti and Travis Scott (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT artist.id, song.id
FROM artist
JOIN song ON song.name = 'FE!N'
WHERE artist.name IN ('Playboi Carti', 'Travis Scott');

INSERT INTO song_genres (song_id, genre_id)
SELECT id, 3 -- 3 is HIP_HOP genre
FROM song
WHERE song.name = 'FE!N';

-- Insert Ariana Grande's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('thank u, next', 'https://upload.wikimedia.org/wikipedia/en/d/dd/Thank_U%2C_Next_album_cover.png',
    (SELECT id FROM artist WHERE name = 'Ariana Grande')),
('positions', 'https://upload.wikimedia.org/wikipedia/en/a/a0/Ariana_Grande_-_Positions.png',
    (SELECT id FROM artist WHERE name = 'Ariana Grande')),
('Sweetener', 'https://upload.wikimedia.org/wikipedia/en/7/7a/Sweetener_album_cover.png',
    (SELECT id FROM artist WHERE name = 'Ariana Grande'));

-- Insert The Weeknd's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('After Hours', 'https://upload.wikimedia.org/wikipedia/en/c/c1/The_Weeknd_-_After_Hours.png',
    (SELECT id FROM artist WHERE name = 'The Weeknd')),
('Starboy', 'https://upload.wikimedia.org/wikipedia/en/3/39/The_Weeknd_-_Starboy.png',
    (SELECT id FROM artist WHERE name = 'The Weeknd')),
('Dawn FM', 'https://upload.wikimedia.org/wikipedia/en/b/b9/The_Weeknd_-_Dawn_FM.png',
    (SELECT id FROM artist WHERE name = 'The Weeknd'));

-- Insert The Weeknd's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
('Save Your Tears', 184, 'https://upload.wikimedia.org/wikipedia/en/c/c1/The_Weeknd_-_After_Hours.png',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739117543/save_your_tears_zv6bys.mp3', 30000000,
(SELECT id FROM album WHERE name = 'After Hours' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

('After Hours', 296, 'https://upload.wikimedia.org/wikipedia/en/c/c1/The_Weeknd_-_After_Hours.png',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739117552/after_hours_r7jlrd.mp3', 25000000,
(SELECT id FROM album WHERE name = 'After Hours' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

-- Starboy Album
('Starboy (feat. Daft Punk)', 215, 'https://upload.wikimedia.org/wikipedia/en/3/39/The_Weeknd_-_Starboy.png',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731722277/starboy_uooa7i.mp4', 32000000,
(SELECT id FROM album WHERE name = 'Starboy' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

('Die For You', 215, 'https://upload.wikimedia.org/wikipedia/en/3/39/The_Weeknd_-_Starboy.png',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1731722468/die-for-you_kaou3m.mp4', 27000000,
(SELECT id FROM album WHERE name = 'Starboy' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd')));


-- Link songs to The Weeknd (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'The Weeknd'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd')
);

-- Add primary genre for The Weeknd's songs (R_AND_B)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 4 -- 4 is R_AND_B genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd')
);

-- Some songs also fit in the POP category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.name IN ('Save Your Tears', 'Starboy (feat. Daft Punk)')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd')
);

-- Some songs also fit in the EDM category (Daft Punk collaborations)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 6 -- 6 is EDM genre
FROM song s
WHERE s.name IN ('Starboy (feat. Daft Punk)')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd')
);

-- Insert Ed Sheeran's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('÷ (Divide)', 'https://upload.wikimedia.org/wikipedia/en/4/45/Divide_cover.png',
    (SELECT id FROM artist WHERE name = 'Ed Sheeran')),
('× (Multiply)', 'https://upload.wikimedia.org/wikipedia/en/a/ad/X_cover.png',
    (SELECT id FROM artist WHERE name = 'Ed Sheeran')),
('= (Equals)', 'https://upload.wikimedia.org/wikipedia/en/c/cd/Ed_Sheeran_-_Equals.png',
    (SELECT id FROM artist WHERE name = 'Ed Sheeran'));

-- Insert Đàm Vĩnh Hưng's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
('Lâu Đài Tình Ái', 318, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738244750/lau_dai_tinh_ai_q4vp6a.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1738245731/lau_dai_tinh_ai_abmmvb.mp3', 70000000, null),

('Biển Tình', 266, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1738244776/dam_vinh_hung_kkvsgx.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1738245769/bien_tinh_xqfzog.mp3', 65000000, null);

-- Link songs to Đàm Vĩnh Hưng (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    a.id AS artist_id,
    s.id AS song_id
FROM song s
JOIN artist a ON a.name = 'Đàm Vĩnh Hưng'
WHERE s.name IN ('Lâu Đài Tình Ái', 'Biển Tình');

-- Add primary genre for Đàm Vĩnh Hưng's songs (POP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
JOIN artist a ON a.name = 'Đàm Vĩnh Hưng'
WHERE s.name IN ('Lâu Đài Tình Ái', 'Biển Tình');

-- Insert Big Bang's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
('Haru Haru', 324, 'https://upload.wikimedia.org/wikipedia/vi/b/b2/BIGBANG_-_MADE.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739118043/HARU_HARU_jodrbo.mp3', 56000000, null);

-- Link songs to Big Bang (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    a.id AS artist_id,
    s.id AS song_id
FROM song s
JOIN artist a ON a.name = 'Big Bang'
WHERE s.name IN ('Haru Haru');

-- Add primary genre for Big Bang's songs (POP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
JOIN artist a ON a.name = 'Big Bang'
WHERE s.name IN ('Haru Haru');

-- Insert Various Artists 's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
('Sunshine Rainbow White Pony', 258, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739118759/sunshine_nroe9c.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739118647/sunshine_rainbow_white_pony_vui4fi.mp3', 69000000, null),

('Judas', 247, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739118803/judas_boqhsj.jpg',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739118774/Judas_db2yz4.mp3', 20000000, null),

('Doodle', 75, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739118923/doodle_rbvgxp.png',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739118838/doodle_atngc2.mp3', 180000000, null),

('lunar', 135, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739119113/lunar_ymtsrj.png',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739119105/lunar_ua5tba.mp3', 90000000, null),

('Magnetic', 123, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739119091/Magnetic_hykqek.png',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739119072/Magnetic_eqlotu.mp3', 100000000, null),

('Jersey Club', 109, 'https://res.cloudinary.com/dr4kiyshe/image/upload/v1739119451/Jersey_Club_ixpjoh.png',
'https://res.cloudinary.com/dr4kiyshe/video/upload/v1739119437/Jersey_Club_nmyhrz.mp3', 980000000, null);

-- Link songs to Various Artists (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    a.id AS artist_id,
    s.id AS song_id
FROM song s
JOIN artist a ON a.name = 'Various Artists'
WHERE s.name IN ('Sunshine Rainbow White Pony', 'Judas', 'Doodle', 'lunar', 'Magnetic', 'Jersey Club');

-- (
-- admin account:
-- username:adminadmin
-- password:adminadmin
-- )

-- (
-- others account:
-- username:${username}
-- password:${username}
-- )
