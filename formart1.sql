/*
 Navicat Premium Data Transfer

 Source Server         : guowang
 Source Server Type    : MySQL
 Source Server Version : 60011
 Source Host           : localhost:3306
 Source Schema         : white_jotter

 Target Server Type    : MySQL
 Target Server Version : 60011
 File Encoding         : 65001

 Date: 16/02/2020 22:35:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for formart1
-- ----------------------------
DROP TABLE IF EXISTS `formart1`;
CREATE TABLE `formart1`  (
  `pos` smallint(255) NULL DEFAULT NULL,
  `fre` int(255) NULL DEFAULT NULL,
  `mode` tinyint(255) NULL DEFAULT NULL,
  `check` tinyint(255) NULL DEFAULT NULL,
  `len` smallint(255) NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `id` smallint(255) NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
