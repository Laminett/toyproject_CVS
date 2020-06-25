create database db_cvs;

-- -----------------------------------------------------
-- create user
-- -----------------------------------------------------
GRANT ALL ON *.* to 'admin_cvs'@'%' IDENTIFIED BY 'pwforcvsadmin';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON *.* to 'app_cvs'@'%' IDENTIFIED BY 'pwforcvsapp';

FLUSH PRIVILEGES;

