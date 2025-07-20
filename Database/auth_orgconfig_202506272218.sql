CREATE TABLE IF NOT EXISTS auth_orgconfig (
  pk_auth_orgconfig BIGINT(20) NOT NULL AUTO_INCREMENT,
  client_id         VARCHAR(255) DEFAULT NULL,
  client_secret     VARCHAR(255) DEFAULT NULL,
  orgid             BIGINT(20) NOT NULL,
  realm             VARCHAR(255) DEFAULT NULL,
  server_url        VARCHAR(255) DEFAULT NULL,
  created_by        VARCHAR(255) DEFAULT NULL,
  created_on        DATETIME(6)    DEFAULT NULL,
  delete_flag       INT(11)        DEFAULT NULL,
  updated_by        VARCHAR(255)   DEFAULT NULL,
  updated_on        DATETIME(6)    DEFAULT NULL,
  PRIMARY KEY (pk_auth_orgconfig),
  UNIQUE KEY UKp6bs9for0okp7wujqifa917lo (orgid)
) ENGINE=InnoDB
  AUTO_INCREMENT=2
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;



INSERT INTO auth_orgconfig 
  (client_id,
   client_secret,
   orgid,
   realm,
   server_url,
   created_by,
   created_on,
   delete_flag,
   updated_by,
   updated_on)
VALUES
  ('dev-network',
   'j6mfRsC1QTClu5cEv3XF78YMneQ4h1js',
   10101,
   'network',
   'http://localhost:9090',
   'ADMIN',
   NOW(6),
   0,
   NULL,
   NULL);

