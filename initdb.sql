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

-- Add more if need
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

CREATE TABLE token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    access_token VARCHAR(512) UNIQUE NOT NULL,
    refresh_token VARCHAR(512) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
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
('Son Tung', 'M-TP', 'MALE', '1994-07-05', '9876543210', CONCAT('sontung', 'mtp', '@gmail.com'), 'sontungmtp', TRUE),
('TheFatRat', '', 'MALE', '1979-06-01', '1234512345', CONCAT('thefatrat', '@gmail.com'), 'thefatrat', TRUE),
('Juice', 'WRLD', 'MALE', '1998-12-02', '3213213210', CONCAT('juicewrld', '@gmail.com'), 'juicewrld', TRUE),
('Travis', 'Scott', 'MALE', '1991-04-30', '6546546540', CONCAT('travisscott', '@gmail.com'), 'travisscott', TRUE),
('Playboi', 'Carti', 'MALE', '1996-09-13', '7897897890', CONCAT('playboicarti', '@gmail.com'), 'playboicarti', TRUE),
('Odetari', '', 'MALE', '1990-03-15', '1597531590', CONCAT('odetari', '@gmail.com'), 'odetariii', TRUE),
('Ariana', 'Grande', 'FEMALE', '1993-06-26', '1112223333', CONCAT('arianagrande', '@gmail.com'), 'arianagrande', TRUE),
('Ed', 'Sheeran', 'MALE', '1991-02-17', '2223334444', CONCAT('edsheeran', '@gmail.com'), 'edsheeran', TRUE),
('Dua', 'Lipa', 'FEMALE', '1995-08-22', '3334445555', CONCAT('dualipa', '@gmail.com'), 'dualipaaa', TRUE),
('Billie', 'Eilish', 'FEMALE', '2001-12-18', '4445556666', CONCAT('billieeilish', '@gmail.com'), 'billieeilish', TRUE),
('Abel', 'Tesfaye', 'MALE', '1990-02-16', '5556667777', CONCAT('theweeknd', '@gmail.com'), 'theweeknd', TRUE),
('Hung', 'Dam Vinh', 'MALE', '1971-10-02', '9696969696', CONCAT('dam', 'vinhhung', '@gmail.com'), 'damvinhhung', TRUE);

INSERT INTO user_roles (user_id, role_id)
VALUES
(1, 2),  -- adminadmin is ADMIN
(2, 1),  -- Alice is an USER
(3, 1),  -- Bob is an USER
(4, 1),  -- Charlie is an USER
(5, 1),  -- Diana is an USER
(6, 1),  -- Eve is an USER
(7, 1),  -- Frank is an USER
(8, 4),  -- Grace is an ARTIST
(9, 4),  -- Henry is an ARTIST
(10, 4), -- Ivy is an ARTIST
(11, 3),  -- Jack is a MOD
(12, 4),  -- Toms is an ARTIST
(13, 4),  -- SonTungMTP is an ARTIST
(14, 4),  -- TheFatRat is an ARTIST
(15, 4),  -- Juice WRLD is an ARTIST
(16, 4),  -- Travis Scott is an ARTIST
(17, 4),  -- Playboi Carti is an ARTIST
(18, 4),  -- Odetari is an ARTIST
(19, 4),  -- Ariana Grande is an ARTIST
(20, 4),  -- Ed Sheeran is an ARTIST
(21, 4),  -- Dua Lipa is an ARTIST
(22, 4),  -- Billie Eilish is an ARTIST
(23, 4),  -- The Weeknd is an ARTIST
(24, 4);  -- Dam Vinh Hung is an ARTIST

INSERT INTO artist (name, bio, image_url, followers, user_id)
VALUES
('Grace Lee', 'Grace Lee \'s bio', 'http://example.com/images/grace.jpg', 1200, 8),
('Henry Clark', 'Henry Clark \'s bio', 'http://example.com/images/henry.jpg', 20430, 9),
('Ivy Taylor', 'Ivy Taylor \'s bio', 'http://example.com/images/ivy.jpg', 15023, 10),
('Tobu', 'Tobu \'s bio', 'http://example.com/images/tobu.jpg', 100000, 12),
('Son Tung M-TP', 'Son Tung M-TP \'s bio', 'http://example.com/images/sontung.jpg', 123456, 13),
('TheFatRat', 'TheFatRat \'s bio', 'http://example.com/images/thefatrat.jpg', 678909, 14),
('Juice WRLD', 'Juice WRLD \'s bio', 'http://example.com/images/juicewrld.jpg', 5000000, 15),
('Travis Scott', 'Travis Scott \'s bio', 'http://example.com/images/travisscott.jpg', 6000000, 16),
('Playboi Carti', 'Playboi Carti \'s bio', 'http://example.com/images/playboicarti.jpg', 3000000, 17),
('Odetari', 'Odetari \'s bio', 'http://example.com/images/odetari.jpg', 160430, 18),
('Ariana Grande', 'Ariana Grande \'s bio', 'http://example.com/images/arianagrande.jpg', 10000000, 19),
('Ed Sheeran', 'Ed Sheeran \'s bio', 'http://example.com/images/edsheeran.jpg', 15000000, 20),
('Dua Lipa', 'Dua Lipa \'s bio', 'http://example.com/images/dualipa.jpg', 1200000, 21),
('Billie Eilish', 'Billie Eilish \'s bio', 'http://example.com/images/billieeilish.jpg', 2000000, 22),
('The Weeknd', 'The Weeknd \'s bio', 'http://example.com/images/theweeknd.jpg', 30000000, 23),
('Dam Vinh Hung', 'Dam Vinh Hung \'s bio', 'http://example.com/images/damvinhhung.jpg', 69696969, 24);


-- Insert Tobu's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('Colors', 'https://i1.sndcdn.com/artworks-000108171434-w7r3us-t500x500.jpg', 4),
('Magic', 'https://i1.sndcdn.com/artworks-000127846396-cm8d41-t500x500.jpg', 4),
('Hope', 'https://i1.sndcdn.com/artworks-000162766958-h9g0k6-t500x500.jpg', 4);

-- Insert Tobu's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- Colors Album
('Infectious', 267, 'https://i.ytimg.com/vi/ux8-EbW6DUI/maxresdefault.jpg',
'https://example.com/songs/tobu/infectious.mp3', 1500000,
(SELECT id FROM album WHERE name = 'Colors' AND artist_id = 4)),

('Colors', 244, 'https://i.ytimg.com/vi/eyLml-zzXzw/maxresdefault.jpg',
'https://example.com/songs/tobu/colors.mp3', 2000000,
(SELECT id FROM album WHERE name = 'Colors' AND artist_id = 4)),

('Cloud 9', 285, 'https://i.ytimg.com/vi/VtKbiyyVZks/maxresdefault.jpg',
'https://example.com/songs/tobu/cloud9.mp3', 1800000,
(SELECT id FROM album WHERE name = 'Colors' AND artist_id = 4)),

-- Magic Album
('Magic', 263, 'https://i.ytimg.com/vi/lMj-iyAoh30/maxresdefault.jpg',
'https://example.com/songs/tobu/magic.mp3', 2500000,
(SELECT id FROM album WHERE name = 'Magic' AND artist_id = 4)),

('Sunburst', 241, 'https://i.ytimg.com/vi/4lXBHD5C8do/maxresdefault.jpg',
'https://example.com/songs/tobu/sunburst.mp3', 1900000,
(SELECT id FROM album WHERE name = 'Magic' AND artist_id = 4)),

('Seven', 258, 'https://i.ytimg.com/vi/h5lGYqXyF8g/maxresdefault.jpg',
'https://example.com/songs/tobu/seven.mp3', 1700000,
(SELECT id FROM album WHERE name = 'Magic' AND artist_id = 4)),

-- Hope Album
('Hope', 271, 'https://i.ytimg.com/vi/EP625xQIGzs/maxresdefault.jpg',
'https://example.com/songs/tobu/hope.mp3', 3000000,
(SELECT id FROM album WHERE name = 'Hope' AND artist_id = 4)),

('Life', 249, 'https://i.ytimg.com/vi/03AKy9bhOMU/maxresdefault.jpg',
'https://example.com/songs/tobu/life.mp3', 2200000,
(SELECT id FROM album WHERE name = 'Hope' AND artist_id = 4)),

('Candyland', 266, 'https://i.ytimg.com/vi/IIrCDAV3EgI/maxresdefault.jpg',
'https://example.com/songs/tobu/candyland.mp3', 2800000,
(SELECT id FROM album WHERE name = 'Hope' AND artist_id = 4));

-- Link songs to Tobu (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT 4, id FROM song WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = 4
);

-- Add genres for Tobu's songs (primarily EDM)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 6 -- 6 is EDM genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = 4
);

-- Some songs also fit in the POP category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.name IN ('Colors', 'Hope', 'Life')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = 4
);

-- Insert TheFatRat's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('PARALLAX', 'https://i.scdn.co/image/ab67616d0000b273b707e5d8ae5acb6ee533c2ee',
    (SELECT id FROM artist WHERE name = 'TheFatRat')),
('Warrior Songs', 'https://i1.sndcdn.com/artworks-000108171434-w7r3us-t500x500.jpg',
    (SELECT id FROM artist WHERE name = 'TheFatRat')),
('RISE UP', 'https://i.scdn.co/image/ab67616d0000b2736974a7b137e2d9d08a8d0e4e',
    (SELECT id FROM artist WHERE name = 'TheFatRat'));

-- Insert TheFatRat's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- PARALLAX Album
('Unity', 248, 'https://i.ytimg.com/vi/n8X9_MgEdCg/maxresdefault.jpg',
'https://example.com/songs/thefatrat/unity.mp3', 50000000,
(SELECT id FROM album WHERE name = 'PARALLAX' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('Monody (feat. Laura Brehm)', 271, 'https://i.ytimg.com/vi/B7xai5u_tnk/maxresdefault.jpg',
'https://example.com/songs/thefatrat/monody.mp3', 45000000,
(SELECT id FROM album WHERE name = 'PARALLAX' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('The Calling (feat. Laura Brehm)', 225, 'https://i.ytimg.com/vi/KR-eV7fHNbM/maxresdefault.jpg',
'https://example.com/songs/thefatrat/the-calling.mp3', 40000000,
(SELECT id FROM album WHERE name = 'PARALLAX' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

-- Warrior Songs Album
('Xenogenesis', 264, 'https://i.ytimg.com/vi/2Ax_EIb1zks/maxresdefault.jpg',
'https://example.com/songs/thefatrat/xenogenesis.mp3', 35000000,
(SELECT id FROM album WHERE name = 'Warrior Songs' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('Windfall', 238, 'https://i.ytimg.com/vi/jqkPqfOFmbY/maxresdefault.jpg',
'https://example.com/songs/thefatrat/windfall.mp3', 30000000,
(SELECT id FROM album WHERE name = 'Warrior Songs' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('Time Lapse', 259, 'https://i.ytimg.com/vi/3fxq7kqyWO8/maxresdefault.jpg',
'https://example.com/songs/thefatrat/time-lapse.mp3', 25000000,
(SELECT id FROM album WHERE name = 'Warrior Songs' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

-- RISE UP Album
('Rise Up', 269, 'https://i.ytimg.com/vi/j0T8K-iMqFM/maxresdefault.jpg',
'https://example.com/songs/thefatrat/rise-up.mp3', 42000000,
(SELECT id FROM album WHERE name = 'RISE UP' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('Fly Away (feat. Anjulie)', 251, 'https://i.ytimg.com/vi/cMg8KaMdDYo/maxresdefault.jpg',
'https://example.com/songs/thefatrat/fly-away.mp3', 38000000,
(SELECT id FROM album WHERE name = 'RISE UP' AND artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat'))),

('Never Be Alone', 266, 'https://i.ytimg.com/vi/Ic-gZlPFTkQ/maxresdefault.jpg',
'https://example.com/songs/thefatrat/never-be-alone.mp3', 36000000,
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
WHERE s.name IN ('Monody (feat. Laura Brehm)', 'Fly Away (feat. Anjulie)', 'Rise Up')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'TheFatRat')
);

-- Insert Son Tung M-TP's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('m-tp M-TP', 'https://upload.wikimedia.org/wikipedia/vi/2/22/M-TP_album.jpg',
    (SELECT id FROM artist WHERE name = 'Son Tung M-TP')),
('Sky Tour', 'https://upload.wikimedia.org/wikipedia/vi/8/8d/Sky_Tour_Movie_Poster.jpg',
    (SELECT id FROM artist WHERE name = 'Son Tung M-TP')),
('Chúng Ta', 'https://i.ytimg.com/vi/32sYGCOYJUM/maxresdefault.jpg',
    (SELECT id FROM artist WHERE name = 'Son Tung M-TP'));

-- Insert Son Tung M-TP's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- m-tp M-TP Album
('Nắng Ấm Xa Dần', 297, 'https://i.ytimg.com/vi/mUlqQuNs5HU/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/nang-am-xa-dan.mp3', 15000000,
(SELECT id FROM album WHERE name = 'm-tp M-TP' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP'))),

('Cơn Mưa Ngang Qua', 324, 'https://i.ytimg.com/vi/07IiCZUKsPE/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/con-mua-ngang-qua.mp3', 20000000,
(SELECT id FROM album WHERE name = 'm-tp M-TP' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP'))),

('Em Của Ngày Hôm Qua', 301, 'https://i.ytimg.com/vi/NWO8MmQChZY/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/em-cua-ngay-hom-qua.mp3', 25000000,
(SELECT id FROM album WHERE name = 'm-tp M-TP' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP'))),

-- Sky Tour Album
('Chạy Ngay Đi', 269, 'https://i.ytimg.com/vi/32sYGCOYJUM/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/chay-ngay-di.mp3', 30000000,
(SELECT id FROM album WHERE name = 'Sky Tour' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP'))),

('Lạc Trôi', 285, 'https://i.ytimg.com/vi/DrY_K0mT-As/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/lac-troi.mp3', 35000000,
(SELECT id FROM album WHERE name = 'Sky Tour' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP'))),

('Nơi Này Có Anh', 274, 'https://i.ytimg.com/vi/FN7ALfpGxiI/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/noi-nay-co-anh.mp3', 40000000,
(SELECT id FROM album WHERE name = 'Sky Tour' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP'))),

-- Chúng Ta Album
('Chúng Ta Của Hiện Tại', 267, 'https://i.ytimg.com/vi/hiX12aHcwOQ/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/chung-ta-cua-hien-tai.mp3', 45000000,
(SELECT id FROM album WHERE name = 'Chúng Ta' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP'))),

('Có Chắc Yêu Là Đây', 208, 'https://i.ytimg.com/vi/5e3HeDpXBvw/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/co-chac-yeu-la-day.mp3', 38000000,
(SELECT id FROM album WHERE name = 'Chúng Ta' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP'))),

('Muộn Rồi Mà Sao Còn', 274, 'https://i.ytimg.com/vi/xypzmu5mMPY/maxresdefault.jpg',
'https://example.com/songs/sontungmtp/muon-roi-ma-sao-con.mp3', 50000000,
(SELECT id FROM album WHERE name = 'Chúng Ta' AND artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP')));

-- Link songs to Son Tung M-TP (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Son Tung M-TP'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP')
);

-- Add genres for Son Tung M-TP's songs (primarily POP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP')
);

-- Some songs also fit in the R&B category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 4 -- 4 is R_AND_B genre
FROM song s
WHERE s.name IN ('Chúng Ta Của Hiện Tại', 'Muộn Rồi Mà Sao Còn', 'Nơi Này Có Anh')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Son Tung M-TP')
);

-- Insert Juice WRLD's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('Goodbye & Good Riddance', 'https://upload.wikimedia.org/wikipedia/en/8/86/Goodbye_%26_Good_Riddance_Album_Cover.jpg',
    (SELECT id FROM artist WHERE name = 'Juice WRLD')),
('Death Race for Love', 'https://upload.wikimedia.org/wikipedia/en/9/98/Death_Race_for_Love.jpg',
    (SELECT id FROM artist WHERE name = 'Juice WRLD')),
('Legends Never Die', 'https://upload.wikimedia.org/wikipedia/en/e/e8/Juice_Wrld_-_Legends_Never_Die.png',
    (SELECT id FROM artist WHERE name = 'Juice WRLD'));

-- Insert Juice WRLD's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- Goodbye & Good Riddance Album
('Lucid Dreams', 239, 'https://i.ytimg.com/vi/mzB1VGEGcSU/maxresdefault.jpg',
'https://example.com/songs/juicewrld/lucid-dreams.mp3', 15000000,
(SELECT id FROM album WHERE name = 'Goodbye & Good Riddance' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD'))),

('All Girls Are The Same', 165, 'https://i.ytimg.com/vi/h3EJICKwITw/maxresdefault.jpg',
'https://example.com/songs/juicewrld/all-girls-are-the-same.mp3', 12000000,
(SELECT id FROM album WHERE name = 'Goodbye & Good Riddance' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD'))),

('Lean Wit Me', 176, 'https://i.ytimg.com/vi/5SejM_hBvMM/maxresdefault.jpg',
'https://example.com/songs/juicewrld/lean-wit-me.mp3', 10000000,
(SELECT id FROM album WHERE name = 'Goodbye & Good Riddance' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD'))),

-- Death Race for Love Album
('Robbery', 240, 'https://i.ytimg.com/vi/iI34LYmJ1Fs/maxresdefault.jpg',
'https://example.com/songs/juicewrld/robbery.mp3', 14000000,
(SELECT id FROM album WHERE name = 'Death Race for Love' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD'))),

('Hear Me Calling', 213, 'https://i.ytimg.com/vi/7YvnrR7pVjA/maxresdefault.jpg',
'https://example.com/songs/juicewrld/hear-me-calling.mp3', 11000000,
(SELECT id FROM album WHERE name = 'Death Race for Love' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD'))),

('Empty', 247, 'https://i.ytimg.com/vi/bf1KtYz8v8M/maxresdefault.jpg',
'https://example.com/songs/juicewrld/empty.mp3', 9000000,
(SELECT id FROM album WHERE name = 'Death Race for Love' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD'))),

-- Legends Never Die Album
('Wishing Well', 224, 'https://i.ytimg.com/vi/C5i-UnuUKUI/maxresdefault.jpg',
'https://example.com/songs/juicewrld/wishing-well.mp3', 13000000,
(SELECT id FROM album WHERE name = 'Legends Never Die' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD'))),

('Come & Go (with Marshmello)', 204, 'https://i.ytimg.com/vi/5Di20x6vVVU/maxresdefault.jpg',
'https://example.com/songs/juicewrld/come-and-go.mp3', 12500000,
(SELECT id FROM album WHERE name = 'Legends Never Die' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD'))),

('Life\'s A Mess (feat. Halsey)', 217, 'https://i.ytimg.com/vi/Y2dtxIzQGjI/maxresdefault.jpg',
'https://example.com/songs/juicewrld/lifes-a-mess.mp3', 11500000,
(SELECT id FROM album WHERE name = 'Legends Never Die' AND artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD')));

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

-- Some songs also fit in the R_AND_B category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 4 -- 4 is R_AND_B genre
FROM song s
WHERE s.name IN ('Lucid Dreams', 'All Girls Are The Same', 'Life\'s A Mess (feat. Halsey)')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD')
);

-- Come & Go also fits in EDM category due to Marshmello collaboration
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 6 -- 6 is EDM genre
FROM song s
WHERE s.name = 'Come & Go (with Marshmello)'
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Juice WRLD')
);

-- Insert Travis Scott's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('ASTROWORLD', 'https://upload.wikimedia.org/wikipedia/en/0/0b/Astroworld_by_Travis_Scott.jpg',
    (SELECT id FROM artist WHERE name = 'Travis Scott')),
('UTOPIA', 'https://upload.wikimedia.org/wikipedia/en/9/9f/Travis_Scott_-_Utopia.png',
    (SELECT id FROM artist WHERE name = 'Travis Scott')),
('RODEO', 'https://upload.wikimedia.org/wikipedia/en/0/0b/Travis_Scott_-_Rodeo.jpg',
    (SELECT id FROM artist WHERE name = 'Travis Scott'));

-- Insert Travis Scott's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- ASTROWORLD Album
('SICKO MODE', 312, 'https://i.ytimg.com/vi/6ONRf7h3Mdk/maxresdefault.jpg',
'https://example.com/songs/travisscott/sicko-mode.mp3', 25000000,
(SELECT id FROM album WHERE name = 'ASTROWORLD' AND artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott'))),

('STARGAZING', 270, 'https://i.ytimg.com/vi/2a8PgqWrc_4/maxresdefault.jpg',
'https://example.com/songs/travisscott/stargazing.mp3', 20000000,
(SELECT id FROM album WHERE name = 'ASTROWORLD' AND artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott'))),

('BUTTERFLY EFFECT', 190, 'https://i.ytimg.com/vi/_EyZUTDAH0U/maxresdefault.jpg',
'https://example.com/songs/travisscott/butterfly-effect.mp3', 18000000,
(SELECT id FROM album WHERE name = 'ASTROWORLD' AND artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott'))),

-- UTOPIA Album
('HYAENA', 239, 'https://i.ytimg.com/vi/Wl4JSQcQxqo/maxresdefault.jpg',
'https://example.com/songs/travisscott/hyaena.mp3', 15000000,
(SELECT id FROM album WHERE name = 'UTOPIA' AND artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott'))),

('THANK GOD', 204, 'https://i.ytimg.com/vi/ZH_-uQP0Qh8/maxresdefault.jpg',
'https://example.com/songs/travisscott/thank-god.mp3', 14000000,
(SELECT id FROM album WHERE name = 'UTOPIA' AND artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott'))),

('FE!N', 229, 'https://i.ytimg.com/vi/Tx0qGqz_nLE/maxresdefault.jpg',
'https://example.com/songs/travisscott/fein.mp3', 16000000,
NULL),

-- RODEO Album
('ANTIDOTE', 249, 'https://i.ytimg.com/vi/KnZ8h3MRuYg/maxresdefault.jpg',
'https://example.com/songs/travisscott/antidote.mp3', 22000000,
(SELECT id FROM album WHERE name = 'RODEO' AND artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott'))),

('90210 (feat. Kacy Hill)', 334, 'https://i.ytimg.com/vi/BuNBLjJzRoo/maxresdefault.jpg',
'https://example.com/songs/travisscott/90210.mp3', 19000000,
(SELECT id FROM album WHERE name = 'RODEO' AND artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott'))),

('3500 (feat. Future & 2 Chainz)', 427, 'https://i.ytimg.com/vi/3qNaoLgHU94/maxresdefault.jpg',
'https://example.com/songs/travisscott/3500.mp3', 17000000,
(SELECT id FROM album WHERE name = 'RODEO' AND artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott')));

-- Link songs to Travis Scott (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Travis Scott'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott')
);

-- Link FE!N to Playboi Carti (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Playboi Carti'),
    id
FROM song
WHERE song.name = 'FE!N';

-- Add primary genre for Travis Scott's songs (HIP_HOP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 3 -- 3 is HIP_HOP genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott')
);

-- Some songs also fit in the R_AND_B category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 4 -- 4 is R_AND_B genre
FROM song s
WHERE s.name IN ('90210 (feat. Kacy Hill)', 'STARGAZING', 'BUTTERFLY EFFECT')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott')
);

-- Some songs also fit in the PHONK category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 5 -- 5 is PHONK genre
FROM song s
WHERE s.name IN ('SICKO MODE', 'FE!N (feat. Playboi Carti)', 'HYAENA')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Travis Scott')
);

-- Insert Playboi Carti's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('Die Lit', 'https://upload.wikimedia.org/wikipedia/en/5/5f/Playboi_Carti_-_Die_Lit.jpg',
    (SELECT id FROM artist WHERE name = 'Playboi Carti')),
('Whole Lotta Red', 'https://upload.wikimedia.org/wikipedia/en/9/9f/Whole_Lotta_Red.png',
    (SELECT id FROM artist WHERE name = 'Playboi Carti')),
('Playboi Carti', 'https://upload.wikimedia.org/wikipedia/en/1/1a/Playboi_Carti_-_Playboi_Carti.png',
    (SELECT id FROM artist WHERE name = 'Playboi Carti'));

-- Insert Playboi Carti's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- Die Lit Album
('Shoota (feat. Lil Uzi Vert)', 156, 'https://i.ytimg.com/vi/j3EwWAMWM6Q/maxresdefault.jpg',
'https://example.com/songs/playboicarti/shoota.mp3', 20000000,
(SELECT id FROM album WHERE name = 'Die Lit' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti'))),

('R.I.P.', 204, 'https://i.ytimg.com/vi/GRoa6w-wnT4/maxresdefault.jpg',
'https://example.com/songs/playboicarti/rip.mp3', 18000000,
(SELECT id FROM album WHERE name = 'Die Lit' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti'))),

('Love Hurts (feat. Travis Scott)', 182, 'https://i.ytimg.com/vi/cBpyRX7JEr0/maxresdefault.jpg',
'https://example.com/songs/playboicarti/love-hurts.mp3', 15000000,
(SELECT id FROM album WHERE name = 'Die Lit' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti'))),

-- Whole Lotta Red Album
('Stop Breathing', 182, 'https://i.ytimg.com/vi/JRK_in-dkRY/maxresdefault.jpg',
'https://example.com/songs/playboicarti/stop-breathing.mp3', 25000000,
(SELECT id FROM album WHERE name = 'Whole Lotta Red' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti'))),

('Sky', 191, 'https://i.ytimg.com/vi/KnumAWWWgUE/maxresdefault.jpg',
'https://example.com/songs/playboicarti/sky.mp3', 22000000,
(SELECT id FROM album WHERE name = 'Whole Lotta Red' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti'))),

('ILoveUIHateU', 157, 'https://i.ytimg.com/vi/YPwXMkWZfF0/maxresdefault.jpg',
'https://example.com/songs/playboicarti/iloveuihateu.mp3', 19000000,
(SELECT id FROM album WHERE name = 'Whole Lotta Red' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti'))),

-- Self-titled Album
('Magnolia', 181, 'https://i.ytimg.com/vi/oCveByMXd_0/maxresdefault.jpg',
'https://example.com/songs/playboicarti/magnolia.mp3', 30000000,
(SELECT id FROM album WHERE name = 'Playboi Carti' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti'))),

('wokeuplikethis* (feat. Lil Uzi Vert)', 206, 'https://i.ytimg.com/vi/REmZhFKmOmo/maxresdefault.jpg',
'https://example.com/songs/playboicarti/wokeuplikethis.mp3', 28000000,
(SELECT id FROM album WHERE name = 'Playboi Carti' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti'))),

('Location', 203, 'https://i.ytimg.com/vi/39XR4EXFz5Y/maxresdefault.jpg',
'https://example.com/songs/playboicarti/location.mp3', 26000000,
(SELECT id FROM album WHERE name = 'Playboi Carti' AND artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti')));

-- Link songs to Playboi Carti (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Playboi Carti'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti')
);

-- Add primary genre for Playboi Carti's songs (HIP_HOP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 3 -- 3 is HIP_HOP genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti')
);

-- Some songs also fit in the PHONK category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 5 -- 5 is PHONK genre
FROM song s
WHERE s.name IN ('Stop Breathing', 'R.I.P.', 'Sky')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Playboi Carti')
);

-- Insert Ariana Grande's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('thank u, next', 'https://upload.wikimedia.org/wikipedia/en/d/dd/Thank_U%2C_Next_album_cover.png',
    (SELECT id FROM artist WHERE name = 'Ariana Grande')),
('positions', 'https://upload.wikimedia.org/wikipedia/en/a/a0/Ariana_Grande_-_Positions.png',
    (SELECT id FROM artist WHERE name = 'Ariana Grande')),
('Sweetener', 'https://upload.wikimedia.org/wikipedia/en/7/7a/Sweetener_album_cover.png',
    (SELECT id FROM artist WHERE name = 'Ariana Grande'));

-- Insert Ariana Grande's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- thank u, next Album
('7 rings', 178, 'https://i.ytimg.com/vi/QYh6mYIJG2Y/maxresdefault.jpg',
'https://example.com/songs/arianagrande/7-rings.mp3', 28000000,
(SELECT id FROM album WHERE name = 'thank u, next' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande'))),

('thank u, next', 207, 'https://i.ytimg.com/vi/gl1aHhXnN1k/maxresdefault.jpg',
'https://example.com/songs/arianagrande/thank-u-next.mp3', 25000000,
(SELECT id FROM album WHERE name = 'thank u, next' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande'))),

('break up with your girlfriend, i\'m bored', 190, 'https://i.ytimg.com/vi/LH4Y1ZUUx2g/maxresdefault.jpg',
'https://example.com/songs/arianagrande/break-up.mp3', 22000000,
(SELECT id FROM album WHERE name = 'thank u, next' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande'))),

-- positions Album
('positions', 172, 'https://i.ytimg.com/vi/tcYodQoapMg/maxresdefault.jpg',
'https://example.com/songs/arianagrande/positions.mp3', 20000000,
(SELECT id FROM album WHERE name = 'positions' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande'))),

('34+35', 173, 'https://i.ytimg.com/vi/B6_iQvaIjXw/maxresdefault.jpg',
'https://example.com/songs/arianagrande/34-35.mp3', 19000000,
(SELECT id FROM album WHERE name = 'positions' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande'))),

('pov', 202, 'https://i.ytimg.com/vi/nQJEp-k-ogs/maxresdefault.jpg',
'https://example.com/songs/arianagrande/pov.mp3', 18000000,
(SELECT id FROM album WHERE name = 'positions' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande'))),

-- Sweetener Album
('no tears left to cry', 205, 'https://i.ytimg.com/vi/ffxKSjUwKdU/maxresdefault.jpg',
'https://example.com/songs/arianagrande/no-tears-left-to-cry.mp3', 24000000,
(SELECT id FROM album WHERE name = 'Sweetener' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande'))),

('God is a woman', 197, 'https://i.ytimg.com/vi/kHLHSlExFis/maxresdefault.jpg',
'https://example.com/songs/arianagrande/god-is-a-woman.mp3', 23000000,
(SELECT id FROM album WHERE name = 'Sweetener' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande'))),

('breathin', 198, 'https://i.ytimg.com/vi/kN0iD0pI3o0/maxresdefault.jpg',
'https://example.com/songs/arianagrande/breathin.mp3', 21000000,
(SELECT id FROM album WHERE name = 'Sweetener' AND artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande')));

-- Link songs to Ariana Grande (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Ariana Grande'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande')
);

-- Add primary genre for Ariana Grande's songs (POP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande')
);

-- Some songs also fit in the R_AND_B category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 4 -- 4 is R_AND_B genre
FROM song s
WHERE s.name IN ('positions', '34+35', 'pov')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Ariana Grande')
);

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
-- After Hours Album
('Blinding Lights', 200, 'https://i.ytimg.com/vi/4NRXx6U8ABQ/maxresdefault.jpg',
'https://example.com/songs/theweeknd/blinding-lights.mp3', 35000000,
(SELECT id FROM album WHERE name = 'After Hours' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

('Save Your Tears', 215, 'https://i.ytimg.com/vi/XXYlFuWEuKI/maxresdefault.jpg',
'https://example.com/songs/theweeknd/save-your-tears.mp3', 30000000,
(SELECT id FROM album WHERE name = 'After Hours' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

('After Hours', 361, 'https://i.ytimg.com/vi/ygTZZpVkmKg/maxresdefault.jpg',
'https://example.com/songs/theweeknd/after-hours.mp3', 25000000,
(SELECT id FROM album WHERE name = 'After Hours' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

-- Starboy Album
('Starboy (feat. Daft Punk)', 230, 'https://i.ytimg.com/vi/34Na4j8AVgA/maxresdefault.jpg',
'https://example.com/songs/theweeknd/starboy.mp3', 32000000,
(SELECT id FROM album WHERE name = 'Starboy' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

('I Feel It Coming (feat. Daft Punk)', 269, 'https://i.ytimg.com/vi/qFLhGq0060w/maxresdefault.jpg',
'https://example.com/songs/theweeknd/i-feel-it-coming.mp3', 28000000,
(SELECT id FROM album WHERE name = 'Starboy' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

('Die For You', 260, 'https://i.ytimg.com/vi/uPD0QOGTmMI/maxresdefault.jpg',
'https://example.com/songs/theweeknd/die-for-you.mp3', 27000000,
(SELECT id FROM album WHERE name = 'Starboy' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

-- Dawn FM Album
('Take My Breath', 339, 'https://i.ytimg.com/vi/rhxX8_LYC9k/maxresdefault.jpg',
'https://example.com/songs/theweeknd/take-my-breath.mp3', 24000000,
(SELECT id FROM album WHERE name = 'Dawn FM' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

('Sacrifice', 189, 'https://i.ytimg.com/vi/VafTMsrnSTU/maxresdefault.jpg',
'https://example.com/songs/theweeknd/sacrifice.mp3', 22000000,
(SELECT id FROM album WHERE name = 'Dawn FM' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd'))),

('Out of Time', 214, 'https://i.ytimg.com/vi/2fDzCWNS3ig/maxresdefault.jpg',
'https://example.com/songs/theweeknd/out-of-time.mp3', 20000000,
(SELECT id FROM album WHERE name = 'Dawn FM' AND artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd')));

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
WHERE s.name IN ('Blinding Lights', 'Save Your Tears', 'Starboy (feat. Daft Punk)', 'Take My Breath')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'The Weeknd')
);

-- Some songs also fit in the EDM category (Daft Punk collaborations)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 6 -- 6 is EDM genre
FROM song s
WHERE s.name IN ('Starboy (feat. Daft Punk)', 'I Feel It Coming (feat. Daft Punk)')
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

-- Insert Ed Sheeran's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- ÷ (Divide) Album
('Shape of You', 233, 'https://i.ytimg.com/vi/JGwWNGJdvx8/maxresdefault.jpg',
'https://example.com/songs/edsheeran/shape-of-you.mp3', 40000000,
(SELECT id FROM album WHERE name = '÷ (Divide)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran'))),

('Perfect', 263, 'https://i.ytimg.com/vi/2Vv-BfVoq4g/maxresdefault.jpg',
'https://example.com/songs/edsheeran/perfect.mp3', 35000000,
(SELECT id FROM album WHERE name = '÷ (Divide)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran'))),

('Castle on the Hill', 261, 'https://i.ytimg.com/vi/K0ibBPhiaG0/maxresdefault.jpg',
'https://example.com/songs/edsheeran/castle-on-the-hill.mp3', 30000000,
(SELECT id FROM album WHERE name = '÷ (Divide)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran'))),

-- × (Multiply) Album
('Thinking Out Loud', 281, 'https://i.ytimg.com/vi/lp-EO5I60KA/maxresdefault.jpg',
'https://example.com/songs/edsheeran/thinking-out-loud.mp3', 32000000,
(SELECT id FROM album WHERE name = '× (Multiply)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran'))),

('Photograph', 258, 'https://i.ytimg.com/vi/nSDgHBxUbVQ/maxresdefault.jpg',
'https://example.com/songs/edsheeran/photograph.mp3', 28000000,
(SELECT id FROM album WHERE name = '× (Multiply)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran'))),

('Don\'t', 219, 'https://i.ytimg.com/vi/3TqGEbROFd4/maxresdefault.jpg',
'https://example.com/songs/edsheeran/dont.mp3', 25000000,
(SELECT id FROM album WHERE name = '× (Multiply)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran'))),

-- = (Equals) Album
('Bad Habits', 230, 'https://i.ytimg.com/vi/orJSJGHjBLI/maxresdefault.jpg',
'https://example.com/songs/edsheeran/bad-habits.mp3', 27000000,
(SELECT id FROM album WHERE name = '= (Equals)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran'))),

('Shivers', 207, 'https://i.ytimg.com/vi/Il0S8BoucSA/maxresdefault.jpg',
'https://example.com/songs/edsheeran/shivers.mp3', 24000000,
(SELECT id FROM album WHERE name = '= (Equals)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran'))),

('Overpass Graffiti', 236, 'https://i.ytimg.com/vi/w7nXWGkgvTY/maxresdefault.jpg',
'https://example.com/songs/edsheeran/overpass-graffiti.mp3', 20000000,
(SELECT id FROM album WHERE name = '= (Equals)' AND artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran')));

-- Link songs to Ed Sheeran (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Ed Sheeran'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran')
);

-- Add primary genre for Ed Sheeran's songs (POP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran')
);

-- Some songs also fit in the INDIE category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 2 -- 2 is INDIE genre
FROM song s
WHERE s.name IN ('Castle on the Hill', 'Photograph', 'Overpass Graffiti')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Ed Sheeran')
);

-- Insert Dam Vinh Hung's albums
INSERT INTO album (name, image_url, artist_id) VALUES
('Tình Bolero', 'https://example.com/images/albums/damvinhhung/tinh-bolero.jpg',
    (SELECT id FROM artist WHERE name = 'Dam Vinh Hung')),
('Giọt Nước Mắt Cho Đời', 'https://example.com/images/albums/damvinhhung/giot-nuoc-mat-cho-doi.jpg',
    (SELECT id FROM artist WHERE name = 'Dam Vinh Hung')),
('Huyền Thoại', 'https://example.com/images/albums/damvinhhung/huyen-thoai.jpg',
    (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'));

-- Insert Dam Vinh Hung's songs
INSERT INTO song (name, duration, image_url, song_url, like_count, album_id) VALUES
-- Tình Bolero Album
('Xin Lỗi Tình Yêu', 320, 'https://example.com/images/songs/damvinhhung/xin-loi-tinh-yeu.jpg',
'https://example.com/songs/damvinhhung/xin-loi-tinh-yeu.mp3', 5000000,
(SELECT id FROM album WHERE name = 'Tình Bolero' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'))),

('Tình Đơn Phương', 285, 'https://example.com/images/songs/damvinhhung/tinh-don-phuong.jpg',
'https://example.com/songs/damvinhhung/tinh-don-phuong.mp3', 4500000,
(SELECT id FROM album WHERE name = 'Tình Bolero' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'))),

('Say Tình', 278, 'https://example.com/images/songs/damvinhhung/say-tinh.jpg',
'https://example.com/songs/damvinhhung/le-da.mp3', 4000000,
(SELECT id FROM album WHERE name = 'Tình Bolero' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'))),

-- Giọt Nước Mắt Cho Đời Album
('Số Nghèo', 305, 'https://example.com/images/songs/damvinhhung/so-ngheo.jpg',
'https://example.com/songs/damvinhhung/say-tinh.mp3', 6000000,
(SELECT id FROM album WHERE name = 'Giọt Nước Mắt Cho Đời' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'))),

('Giọt Nước Mắt Cho Đời', 312, 'https://example.com/images/songs/damvinhhung/giot-nuoc-mat-cho-doi.jpg',
'https://example.com/songs/damvinhhung/giot-nuoc-mat-cho-doi.mp3', 5500000,
(SELECT id FROM album WHERE name = 'Giọt Nước Mắt Cho Đời' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'))),

('Đêm Tóc Rối', 287, 'https://example.com/images/songs/damvinhhung/dem-toc-roi.jpg',
'https://example.com/songs/damvinhhung/nguoi-tinh-oi-mo-gi.mp3', 4800000,
(SELECT id FROM album WHERE name = 'Giọt Nước Mắt Cho Đời' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'))),

-- Huyền Thoại Album
('Lâu Đài Tình Ái', 369, 'https://example.com/images/songs/damvinhhung/lau-dai-tinh-ai.jpg',
'https://example.com/songs/damvinhhung/so-ngheo.mp3', 7000000,
(SELECT id FROM album WHERE name = 'Huyền Thoại' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'))),

('Biển Tình', 300, 'https://example.com/images/songs/damvinhhung/bien-tinh.jpg',
'https://example.com/songs/damvinhhung/mot-thoi-da-xa.mp3', 6500000,
(SELECT id FROM album WHERE name = 'Huyền Thoại' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'))),

('Người Tình Ơi Mơ Gì', 289, 'https://example.com/images/songs/damvinhhung/nguoi-tinh-oi-mo-gi.jpg',
'https://example.com/songs/damvinhhung/dem-toc-roi.mp3', 5800000,
(SELECT id FROM album WHERE name = 'Huyền Thoại' AND artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung')));

-- Link songs to Dam Vinh Hung (artist_songs table)
INSERT INTO artist_songs (artist_id, song_id)
SELECT
    (SELECT id FROM artist WHERE name = 'Dam Vinh Hung'),
    id
FROM song
WHERE album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung')
);

-- Add primary genre for Dam Vinh Hung's songs (POP)
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 1 -- 1 is POP genre
FROM song s
WHERE s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung')
);

-- Some songs also fit in the R_AND_B category
INSERT INTO song_genres (song_id, genre_id)
SELECT s.id, 4 -- 4 is R_AND_B genre
FROM song s
WHERE s.name IN ('Say Tình', 'Xin Lỗi Tình Yêu', 'Đêm Tóc Rối')
AND s.album_id IN (
    SELECT id FROM album WHERE artist_id = (SELECT id FROM artist WHERE name = 'Dam Vinh Hung')
);

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
