-- -----------------------------------------------------
-- For admin_cvs user
-- -----------------------------------------------------
GRANT ALL ON *.* to 'admin_cvs'@'%' IDENTIFIED BY 'pwforcvsadmin';
GRANT ALL ON *.* to 'admin_cvs'@'localhost' IDENTIFIED BY 'pwforcvsadmin';

-- -----------------------------------------------------
-- For app_cvs user
-- -----------------------------------------------------
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON *.* to 'app_cvs'@'%' IDENTIFIED BY 'pwforcvsapp';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON *.* to 'app_cvs'@'localhost' IDENTIFIED BY 'pwforcvsapp';

FLUSH PRIVILEGES;

