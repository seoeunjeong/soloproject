-- member 테이블
CREATE TABLE `member` (
                          `member_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `created_at` DATETIME,
                          `last_modified_at` DATETIME,
                          `address` VARCHAR(255),
                          `address_dong` VARCHAR(255),
                          `age` INT,
                          `email` VARCHAR(255),
                          `gender` CHAR(1) NOT NULL,
                          `latitude` DOUBLE PRECISION NOT NULL,
                          `longitude` DOUBLE PRECISION NOT NULL,
                          `name` VARCHAR(255),
                          `password` VARCHAR(255),
                          PRIMARY KEY (`member_id`),
                          CONSTRAINT `UK_mbmcqelty0fbrvxp1q58dn57t` UNIQUE (`email`)
) ENGINE=InnoDB;



-- member_roles 테이블
CREATE TABLE member_roles (
                              member_member_id BIGINT NOT NULL,
                              roles VARCHAR(255),
                              FOREIGN KEY (member_member_id) REFERENCES member (member_id)
) ENGINE=InnoDB;


-- moim 테이블
CREATE TABLE moim (
                      moim_id BIGINT NOT NULL AUTO_INCREMENT,
                      created_at DATETIME(6),
                      last_modified_at DATETIME(6),
                      address_dong VARCHAR(255),
                      content VARCHAR(255),
                      d_day INTEGER NOT NULL,
                      latitude DOUBLE PRECISION NOT NULL,
                      like_count INTEGER NOT NULL,
                      longitude DOUBLE PRECISION NOT NULL,
                      moim_category VARCHAR(255),
                      moim_status VARCHAR(255),
                      participant_count INTEGER NOT NULL,
                      place_address VARCHAR(255),
                      place_name VARCHAR(255),
                      started_at DATETIME(6),
                      title VARCHAR(255),
                      total_participant_count INTEGER NOT NULL,
                      member_id BIGINT,
                      PRIMARY KEY (moim_id),
                      FOREIGN KEY (member_id) REFERENCES member (member_id)
) ENGINE=InnoDB;


-- moim_member 테이블
CREATE TABLE moim_member (
                             moim_member_id BIGINT NOT NULL AUTO_INCREMENT,
                             created_at DATETIME(6),
                             last_modified_at DATETIME(6),
                             status TINYINT(1),
                             member_id BIGINT,
                             moim_id BIGINT,
                             PRIMARY KEY (moim_member_id),
                             FOREIGN KEY (member_id) REFERENCES member (member_id)
) ENGINE=InnoDB;


-- like_moim 테이블
CREATE TABLE like_moim (
                           like_moim_id BIGINT NOT NULL AUTO_INCREMENT,
                           status TINYINT(1),
                           member_id BIGINT,
                           moim_id BIGINT,
                           PRIMARY KEY (like_moim_id),
                           FOREIGN KEY (member_id) REFERENCES member (member_id)
) ENGINE=InnoDB;


-- chat_room 테이블
CREATE TABLE chat_room (
                           room_id BIGINT NOT NULL AUTO_INCREMENT,
                           owner_member_id BIGINT,
                           request_member_id BIGINT,
                           PRIMARY KEY (room_id),
                           FOREIGN KEY (owner_member_id) REFERENCES member (member_id)
) ENGINE=InnoDB;


-- chat_message 테이블
CREATE TABLE chat_message (
                              message_id BIGINT NOT NULL AUTO_INCREMENT,
                              created_at DATETIME(6),
                              last_modified_at DATETIME(6),
                              content VARCHAR(255),
                              read_status BOOLEAN,
                              room_id BIGINT,
                              sender_id BIGINT NOT NULL,
                              PRIMARY KEY (message_id),
                              FOREIGN KEY (room_id) REFERENCES chat_room (room_id)
) ENGINE=InnoDB;




-- profile_image 테이블
CREATE TABLE profile_image (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               profile_image_url VARCHAR(255),
                               uuid VARCHAR(255),
                               member_id BIGINT,
                               PRIMARY KEY (id),
                               FOREIGN KEY (member_id) REFERENCES member (member_id)
) ENGINE=InnoDB;