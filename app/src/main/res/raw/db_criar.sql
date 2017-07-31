CREATE TABLE user (
    _id INTEGER PRIMARY KEY NOT NULL,
    usuario varchar ( 255 ) NOT NULL,
    senha varchar ( 255 ) NOT NULL,
    unique(usuario) ON CONFLICT replace
);
