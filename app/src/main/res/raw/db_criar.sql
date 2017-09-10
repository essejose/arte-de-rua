CREATE TABLE user (
    _id INTEGER PRIMARY KEY NOT NULL,
    usuario varchar ( 255 ) NOT NULL,
    senha varchar ( 255 ) NOT NULL,
    unique(usuario) ON CONFLICT replace
);

CREATE TABLE event (
    _id INTEGER PRIMARY KEY NOT NULL ,
    _id_user varchar ( 255 ) NOT NULL,
    title varchar ( 255 ) NOT NULL,
    descripion varchar ( 255 ) NOT NULL,
    image varchar ( 255 ) NOT NULL,
    latiude varchar ( 255 ) NOT NULL,
    longitude varchar ( 255 ) NOT NULL
);
