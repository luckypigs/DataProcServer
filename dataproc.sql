-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.3.7-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 test.ah 结构
CREATE TABLE IF NOT EXISTS `ah` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `SPI` varchar(4) DEFAULT NULL COMMENT 'SPI',
  `seqNum` varchar(4) DEFAULT NULL COMMENT 'seqNum',
  `payLen` int(11) DEFAULT NULL COMMENT 'payLen',
  `ICV` varchar(12) DEFAULT NULL COMMENT 'ICV',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.bgp 结构
CREATE TABLE IF NOT EXISTS `bgp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `marker` varchar(16) DEFAULT NULL COMMENT 'marker',
  `msgType` varchar(1) DEFAULT NULL COMMENT 'msgType',
  `ver` varchar(1) DEFAULT NULL COMMENT 'ver',
  `ASNum` int(11) DEFAULT NULL COMMENT 'ASNum',
  `rouID` varchar(4) DEFAULT NULL COMMENT 'rouID',
  `authCode` varchar(1) DEFAULT NULL COMMENT 'authCode',
  `authData` varchar(128) DEFAULT NULL COMMENT 'authData',
  `origin` varchar(1) DEFAULT NULL COMMENT 'origin',
  `ASPath` varchar(256) DEFAULT NULL COMMENT 'ASPath',
  `nexHop` varchar(4) DEFAULT NULL COMMENT 'nexHop',
  `MED` int(11) DEFAULT NULL COMMENT 'MED',
  `locPre` int(11) DEFAULT NULL COMMENT 'locPre',
  `comName` varchar(256) DEFAULT NULL COMMENT 'comName',
  `extComName` varchar(256) DEFAULT NULL COMMENT 'extComName',
  `aggAS` varchar(256) DEFAULT NULL COMMENT 'aggAS',
  `aggAddr` varchar(64) DEFAULT NULL COMMENT 'aggAddr',
  `originID` varchar(4) DEFAULT NULL COMMENT 'originID',
  `cluIDList` varchar(256) DEFAULT NULL COMMENT 'cluIDList',
  `addrFam` varchar(2) DEFAULT NULL COMMENT 'addrFam',
  `SAFI` varchar(1) DEFAULT NULL COMMENT 'SAFI',
  `NLRIPreAndMas` varchar(1024) DEFAULT NULL COMMENT 'NLRIPreAndMas',
  `MVNLRIPreAndMas` varchar(1024) DEFAULT NULL COMMENT 'MVNLRIPreAndMas',
  `MVNLURIPreAndMas` varchar(1024) DEFAULT NULL COMMENT 'MVNLURIPreAndMas',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.data_subscribe_config 结构
CREATE TABLE IF NOT EXISTS `data_subscribe_config` (
  `position` int(11) DEFAULT NULL COMMENT '阵地编号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `data_status` int(11) DEFAULT NULL COMMENT '数据订阅调度状态, data_status存储用户的订阅调度状态，0表示未订阅及调度、1表示已订阅、2表示调度'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用于配置阵地用户的数据订阅、调度情况，将订阅信息存储于其中 ';

-- 数据导出被取消选择。
-- 导出  表 test.dns 结构
CREATE TABLE IF NOT EXISTS `dns` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `queType` varchar(2) DEFAULT NULL COMMENT 'queType',
  `queName` varchar(256) DEFAULT NULL COMMENT 'queName',
  `traID` varchar(2) DEFAULT NULL COMMENT 'traID',
  `srvFlag` varchar(2) DEFAULT NULL COMMENT 'srvFlag',
  `ansQue` varchar(256) DEFAULT NULL COMMENT 'ansQue',
  `ansType` int(11) DEFAULT NULL COMMENT 'ansType',
  `ansRes` varchar(256) DEFAULT NULL COMMENT 'ansRes',
  `authAnsType` int(11) DEFAULT NULL COMMENT 'authAnsType',
  `authAnsRes` varchar(2048) DEFAULT NULL COMMENT 'authAnsRes',
  `addAnsType` int(11) DEFAULT NULL COMMENT 'addAnsType',
  `addAnsRes` varchar(2048) DEFAULT NULL COMMENT 'addAnsRes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.dtls 结构
CREATE TABLE IF NOT EXISTS `dtls` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `conType` varchar(1) DEFAULT NULL COMMENT 'conType',
  `version` smallint(6) DEFAULT NULL COMMENT 'version',
  `epoch` int(11) DEFAULT NULL COMMENT 'epoch',
  `aleLev` varchar(1) DEFAULT NULL COMMENT 'aleLev',
  `aleDesc` varchar(1) DEFAULT NULL COMMENT 'aleDesc',
  `handShaType` varchar(1) DEFAULT NULL COMMENT 'handShaType',
  `cliGMTUniTime` datetime DEFAULT NULL COMMENT 'cliGMTUniTime',
  `cliRand` varchar(28) DEFAULT NULL COMMENT 'cliRand',
  `cliSesID` varchar(64) DEFAULT NULL COMMENT 'cliSesID',
  `cliCookie` varchar(64) DEFAULT NULL COMMENT 'cliCookie',
  `cliCipSui` varchar(3) DEFAULT NULL COMMENT 'cliCipSui',
  `cliComMet` varchar(1) DEFAULT NULL COMMENT 'cliComMet',
  `cliExt` varchar(64) DEFAULT NULL COMMENT 'cliExt',
  `srvVer` varchar(2) DEFAULT NULL COMMENT 'srvVer',
  `srvGMTUniTime` datetime DEFAULT NULL COMMENT 'srvGMTUniTime',
  `srvRand` varchar(28) DEFAULT NULL COMMENT 'srvRand',
  `srvSesID` varchar(32) DEFAULT NULL COMMENT 'srvSesID',
  `srvToCliCoo` varchar(64) DEFAULT NULL COMMENT 'srvToCliCoo',
  `srvCipSui` varchar(3) DEFAULT NULL COMMENT 'srvCipSui',
  `srvComMet` varchar(1) DEFAULT NULL COMMENT 'srvComMet',
  `srvExt` varchar(64) DEFAULT NULL COMMENT 'srvExt',
  `srvCertSeqNum` varchar(16) DEFAULT NULL COMMENT 'srvCertSeqNum',
  `srvCertLen` int(11) DEFAULT NULL COMMENT 'srvCertLen',
  `certQueType` varchar(1) DEFAULT NULL COMMENT 'certQueType',
  `cliCertAuthDesc` varchar(16) DEFAULT NULL COMMENT 'cliCertAuthDesc',
  `cliCertSeqNum` varchar(16) DEFAULT NULL COMMENT 'cliCertSeqNum',
  `cliCertLen` int(11) DEFAULT NULL COMMENT 'cliCertLen',
  `ECDHNamCur` smallint(6) DEFAULT NULL COMMENT 'ECDHNamCur',
  `ECDHPubkey` varchar(512) DEFAULT NULL COMMENT 'ECDHPubkey',
  `ECDHSigHasAlg` varchar(1) DEFAULT NULL COMMENT 'ECDHSigHasAlg',
  `ECDHSigAlg` varchar(1) DEFAULT NULL COMMENT 'ECDHSigAlg',
  `ECDHSig` varchar(512) DEFAULT NULL COMMENT 'ECDHSig',
  `RSAModInSrvExc` varchar(1024) DEFAULT NULL COMMENT 'RSAModInSrvExc',
  `RSAExpInSrvExc` varchar(4) DEFAULT NULL COMMENT 'RSAExpInSrvExc',
  `RSASigHashAlg` varchar(1) DEFAULT NULL COMMENT 'RSASigHashAlg',
  `RSASigSigAlg` varchar(1) DEFAULT NULL COMMENT 'RSASigSigAlg',
  `RSASig` varchar(512) DEFAULT NULL COMMENT 'RSASig',
  `DHPriModInSrvExc` varchar(1024) DEFAULT NULL COMMENT 'DHPriModInSrvExc',
  `DHPGenInSrvExc` varchar(512) DEFAULT NULL COMMENT 'DHPGenInSrvExc',
  `DHPubKeyInSrv` varchar(512) DEFAULT NULL COMMENT 'DHPubKeyInSrv',
  `DHESigHashAlg` varchar(1) DEFAULT NULL COMMENT 'DHESigHashAlg',
  `DHESigSigAlg` varchar(1) DEFAULT NULL COMMENT 'DHESigSigAlg',
  `DHESig` varchar(512) DEFAULT NULL COMMENT 'DHESig',
  `preMasKeyEncByRSAKey` varchar(512) DEFAULT NULL COMMENT 'preMasKeyEncByRSAKey',
  `DHPubByCli` varchar(512) DEFAULT NULL COMMENT 'DHPubByCli',
  `ecPoiForByCli` varchar(1) DEFAULT NULL COMMENT 'ecPoiForByCli',
  `ellCurByCli` varchar(2) DEFAULT NULL COMMENT 'ellCurByCli',
  `ecPoiForBySrv` varchar(1) DEFAULT NULL COMMENT 'ecPoiForBySrv',
  `ellCurBySrv` varchar(2) DEFAULT NULL COMMENT 'ellCurBySrv',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.emailcompar 结构
CREATE TABLE IF NOT EXISTS `emailcompar` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `msgID` varchar(1024) DEFAULT NULL COMMENT 'msgID',
  `senderEma` varchar(256) DEFAULT NULL COMMENT 'senderEma',
  `senderAli` varchar(256) DEFAULT NULL COMMENT 'senderAli',
  `rcvrEma` varchar(2048) DEFAULT NULL COMMENT 'rcvrEma',
  `rcvrAli` varchar(2048) DEFAULT NULL COMMENT 'rcvrAli',
  `rcvrType` varchar(4) DEFAULT NULL COMMENT 'rcvrType',
  `repTo` varchar(2048) DEFAULT NULL COMMENT 'repTo',
  `subj` varchar(512) DEFAULT NULL COMMENT 'subj',
  `date` varchar(64) DEFAULT NULL COMMENT 'date',
  `mimType` varchar(32) DEFAULT NULL COMMENT 'mimType',
  `conTraEnc` varchar(32) DEFAULT NULL COMMENT 'conTraEnc',
  `conTexCha` varchar(1) DEFAULT NULL COMMENT 'conTexCha',
  `emaInd` varchar(100) DEFAULT NULL COMMENT 'emaInd',
  `attFileName` varchar(128) DEFAULT NULL COMMENT 'attFileName',
  `attType` varchar(16) DEFAULT NULL COMMENT 'attType',
  `attConSize` int(11) DEFAULT NULL COMMENT 'attConSize',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.emailprocompar 结构
CREATE TABLE IF NOT EXISTS `emailprocompar` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `emaProtType` varchar(8) DEFAULT NULL COMMENT 'emaProtType',
  `login` varchar(64) DEFAULT NULL COMMENT 'login',
  `pwd` varchar(64) DEFAULT NULL COMMENT 'pwd',
  `loginsrv` varchar(128) DEFAULT NULL COMMENT 'loginsrv',
  `SMTPSrv` varchar(128) DEFAULT NULL COMMENT 'SMTPSrv',
  `SMTPSrvAge` varchar(128) DEFAULT NULL COMMENT 'SMTPSrvAge',
  `rcvFromName` varchar(256) DEFAULT NULL COMMENT 'rcvFromName',
  `rcvFromIp` varchar(64) DEFAULT NULL COMMENT 'rcvFromIp',
  `rcvByName` varchar(256) DEFAULT NULL COMMENT 'rcvByName',
  `rcvByIP` varchar(64) DEFAULT NULL COMMENT 'rcvByIP',
  `rcvWit` varchar(128) DEFAULT NULL COMMENT 'rcvWit',
  `rcvDate` varchar(64) DEFAULT NULL COMMENT 'rcvDate',
  `rcvSrvAge` varchar(128) DEFAULT NULL COMMENT 'rcvSrvAge',
  `xMai` varchar(128) DEFAULT NULL COMMENT 'xMai',
  `usrAge` varchar(128) DEFAULT NULL COMMENT 'usrAge',
  `emaActType` varchar(2) DEFAULT NULL COMMENT 'emaActType',
  `emaActTime` varchar(64) DEFAULT NULL COMMENT 'emaActTime',
  `emaActRep` varchar(512) DEFAULT NULL COMMENT 'emaActRep',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.esp 结构
CREATE TABLE IF NOT EXISTS `esp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `SPI` varchar(4) DEFAULT NULL COMMENT 'SPI',
  `seqNum` varchar(4) DEFAULT NULL COMMENT 'seqNum',
  `payLen` int(11) DEFAULT NULL COMMENT 'payLen',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.ftp 结构
CREATE TABLE IF NOT EXISTS `ftp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `login` varchar(64) DEFAULT NULL COMMENT 'login',
  `pwd` varchar(64) DEFAULT NULL COMMENT 'pwd',
  `dataPort` int(11) DEFAULT NULL COMMENT 'dataPort',
  `srvAddr` varchar(4) DEFAULT NULL COMMENT 'srvAddr',
  `fileName` varchar(256) DEFAULT NULL COMMENT 'fileName',
  `fileSize` mediumtext DEFAULT NULL COMMENT 'fileSize',
  `conType` varchar(8) DEFAULT NULL COMMENT 'conType',
  `hostName` varchar(128) DEFAULT NULL COMMENT 'hostName',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.gre 结构
CREATE TABLE IF NOT EXISTS `gre` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ver` varchar(1) DEFAULT NULL COMMENT 'ver',
  `protType` varchar(2) DEFAULT NULL COMMENT 'protType',
  `callID` varchar(2) DEFAULT NULL COMMENT 'callID',
  `auth` varchar(4) DEFAULT NULL COMMENT 'auth',
  `seqNum` int(11) DEFAULT NULL COMMENT 'seqNum',
  `ackNum` int(11) DEFAULT NULL COMMENT 'ackNum',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.http 结构
CREATE TABLE IF NOT EXISTS `http` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `host` varchar(64) DEFAULT NULL COMMENT 'host',
  `URL` varchar(1024) DEFAULT NULL COMMENT 'URL',
  `varConEnc` varchar(128) DEFAULT NULL COMMENT 'varConEnc',
  `authInfo` varchar(1024) DEFAULT NULL COMMENT 'authInfo',
  `conEncType` varchar(32) DEFAULT NULL COMMENT 'conEncType',
  `conLan` varchar(64) DEFAULT NULL COMMENT 'conLan',
  `conLen` int(11) DEFAULT NULL COMMENT 'conLen',
  `conURL` varchar(128) DEFAULT NULL COMMENT 'conURL',
  `conMD5` varchar(32) DEFAULT NULL COMMENT 'conMD5',
  `conType` varchar(64) DEFAULT NULL COMMENT 'conType',
  `cookie` varchar(4096) DEFAULT NULL COMMENT 'cookie',
  `cookie2` varchar(32) DEFAULT NULL COMMENT 'cookie2',
  `date` varchar(32) DEFAULT NULL COMMENT 'date',
  `req_from` varchar(128) DEFAULT NULL COMMENT 'req_from',
  `loc` varchar(256) DEFAULT NULL COMMENT 'loc',
  `proAuthen` varchar(64) DEFAULT NULL COMMENT 'proAuthen',
  `proAuthor` varchar(128) DEFAULT NULL COMMENT 'proAuthor',
  `refURL` varchar(256) DEFAULT NULL COMMENT 'refURL',
  `srv` varchar(128) DEFAULT NULL COMMENT 'srv',
  `setCookie` varchar(512) DEFAULT NULL COMMENT 'setCookie',
  `setCookie2` varchar(512) DEFAULT NULL COMMENT 'setCookie2',
  `title` varchar(128) DEFAULT NULL COMMENT 'title',
  `traEnc` varchar(128) DEFAULT NULL COMMENT 'traEnc',
  `usrAge` varchar(256) DEFAULT NULL COMMENT 'usrAge',
  `via` varchar(512) DEFAULT NULL COMMENT 'via',
  `wwwAut` varchar(512) DEFAULT NULL COMMENT 'wwwAut',
  `xForFor` varchar(128) DEFAULT NULL COMMENT 'xForFor',
  `statCode` varchar(16) DEFAULT NULL COMMENT 'statCode',
  `met` varchar(16) DEFAULT NULL COMMENT 'met',
  `srvAge` varchar(128) DEFAULT NULL COMMENT 'srvAge',
  `proAuth` varchar(64) DEFAULT NULL COMMENT 'proAuth',
  `proLogin` varchar(128) DEFAULT NULL COMMENT 'proLogin',
  `proRea` varchar(128) DEFAULT NULL COMMENT 'proRea',
  `xPowBy` varchar(128) DEFAULT NULL COMMENT 'xPowBy',
  `extHdrs` varchar(1024) DEFAULT NULL COMMENT 'extHdrs',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.icmp 结构
CREATE TABLE IF NOT EXISTS `icmp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `msgType` varchar(1) DEFAULT NULL COMMENT 'msgType',
  `infoCode` varchar(1) DEFAULT NULL COMMENT 'infoCode',
  `echoSeqNum` varchar(2) DEFAULT NULL COMMENT 'echoSeqNum',
  `resTime` int(11) DEFAULT NULL COMMENT 'resTime',
  `dataCon` varchar(20) DEFAULT NULL COMMENT 'dataCon',
  `unrSrcAddr` int(11) DEFAULT NULL COMMENT 'unrSrcAddr',
  `unrDstAddr` int(11) DEFAULT NULL COMMENT 'unrDstAddr',
  `unrProt` int(11) DEFAULT NULL COMMENT 'unrProt',
  `uncTTL` int(11) DEFAULT NULL COMMENT 'uncTTL',
  `recNumMul` int(11) DEFAULT NULL COMMENT 'recNumMul',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.imap4mailindpar 结构
CREATE TABLE IF NOT EXISTS `imap4mailindpar` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `msgListSubj` varchar(1024) DEFAULT NULL COMMENT 'msgListSubj',
  `msgListDate` varchar(64) DEFAULT NULL COMMENT 'msgListDate',
  `msgListSenderAli` varchar(1024) DEFAULT NULL COMMENT 'msgListSenderAli',
  `msgListSenderEma` varchar(1024) DEFAULT NULL COMMENT 'msgListSenderEma',
  `msgListRcvrAli` varchar(2048) DEFAULT NULL COMMENT 'msgListRcvrAli',
  `msgListRcvrEma` varchar(2048) DEFAULT NULL COMMENT 'msgListRcvrEma',
  `msgListMimType` varchar(1024) DEFAULT NULL COMMENT 'msgListMimType',
  `msgListAttMimType` varchar(1024) DEFAULT NULL COMMENT 'msgListAttMimType',
  `msgListAttFile` varchar(1024) DEFAULT NULL COMMENT 'msgListAttFile',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.ipv4 结构
CREATE TABLE IF NOT EXISTS `ipv4` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `difSvc` varchar(1) DEFAULT NULL COMMENT 'difSvc',
  `totLen` int(11) DEFAULT NULL COMMENT 'totLen',
  `IPFra` varchar(2) DEFAULT NULL COMMENT 'IPFra',
  `TTL` varchar(1) DEFAULT NULL COMMENT 'TTL',
  `protNum` varchar(1) DEFAULT NULL COMMENT 'protNum',
  `srcAddr` varchar(4) DEFAULT NULL COMMENT 'srcAddr',
  `dstAddr` varchar(4) DEFAULT NULL COMMENT 'dstAddr',
  `IPHeadChe` tinyint(1) DEFAULT NULL COMMENT 'IPHeadChe',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.isakmp 结构
CREATE TABLE IF NOT EXISTS `isakmp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `iniCookie` varchar(8) DEFAULT NULL COMMENT 'iniCookie',
  `respCookie` varchar(8) DEFAULT NULL COMMENT 'respCookie',
  `initVer` varchar(1) DEFAULT NULL COMMENT 'initVer',
  `respVer` varchar(1) DEFAULT NULL COMMENT 'respVer',
  `excType` varchar(1) DEFAULT NULL COMMENT 'excType',
  `msgID` varchar(4) DEFAULT NULL COMMENT 'msgID',
  `DOI` varchar(4) DEFAULT NULL COMMENT 'DOI',
  `situ` varchar(4) DEFAULT NULL COMMENT 'situ',
  `proNum` varchar(1) DEFAULT NULL COMMENT 'proNum',
  `proID` varchar(1) DEFAULT NULL COMMENT 'proID',
  `traNum` varchar(1) DEFAULT NULL COMMENT 'traNum',
  `traID` varchar(1) DEFAULT NULL COMMENT 'traID',
  `initEncryAlg` varchar(2) DEFAULT NULL COMMENT 'initEncryAlg',
  `respEncryAlg` varchar(2) DEFAULT NULL COMMENT 'respEncryAlg',
  `initKeyLen` int(11) DEFAULT NULL COMMENT 'initKeyLen',
  `respKeyLen` int(11) DEFAULT NULL COMMENT 'respKeyLen',
  `initHasAlg` varchar(2) DEFAULT NULL COMMENT 'initHasAlg',
  `respHasAlg` varchar(2) DEFAULT NULL COMMENT 'respHasAlg',
  `initGroDes` varchar(2) DEFAULT NULL COMMENT 'initGroDes',
  `respGroDes` varchar(2) DEFAULT NULL COMMENT 'respGroDes',
  `initGroType` varchar(1) DEFAULT NULL COMMENT 'initGroType',
  `respGroType` varchar(1) DEFAULT NULL COMMENT 'respGroType',
  `initAuthMet` varchar(2) DEFAULT NULL COMMENT 'initAuthMet',
  `respAuthMet` varchar(2) DEFAULT NULL COMMENT 'respAuthMet',
  `lifeType` varchar(2) DEFAULT NULL COMMENT 'lifeType',
  `lifeDur` varchar(4) DEFAULT NULL COMMENT 'lifeDur',
  `initVenID` varchar(64) DEFAULT NULL COMMENT 'initVenID',
  `respVenID` varchar(64) DEFAULT NULL COMMENT 'respVenID',
  `iniKeyExc` varchar(256) DEFAULT NULL COMMENT 'iniKeyExc',
  `respKeyExc` varchar(256) DEFAULT NULL COMMENT 'respKeyExc',
  `certCod` varchar(1) DEFAULT NULL COMMENT 'certCod',
  `initNon` varchar(1024) DEFAULT NULL COMMENT 'initNon',
  `respNon` varchar(2) DEFAULT NULL COMMENT 'respNon',
  `notMsgType` varchar(2) DEFAULT NULL COMMENT 'notMsgType',
  `notMsgTypeInIKE2` varchar(2) DEFAULT NULL COMMENT 'notMsgTypeInIKE2',
  `encryAlgInIKE2` varchar(2) DEFAULT NULL COMMENT 'encryAlgInIKE2',
  `pseRandFunInIKE2` varchar(2) DEFAULT NULL COMMENT 'pseRandFunInIKE2',
  `intAlgInIKE2` varchar(2) DEFAULT NULL COMMENT 'intAlgInIKE2',
  `initEncryDataSize` int(11) DEFAULT NULL COMMENT 'initEncryDataSize',
  `respEncryDataSize` int(11) DEFAULT NULL COMMENT 'respEncryDataSize',
  `IDInInit` varchar(1024) DEFAULT NULL COMMENT 'IDInInit',
  `IDInResp` varchar(1024) DEFAULT NULL COMMENT 'IDInResp',
  `authDataInInit` varchar(1024) DEFAULT NULL COMMENT 'authDataInInit',
  `authDataInResp` varchar(1024) DEFAULT NULL COMMENT 'authDataInResp',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.l2tp 结构
CREATE TABLE IF NOT EXISTS `l2tp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tunID` varchar(2) DEFAULT NULL COMMENT 'tunID',
  `sesID` varchar(2) DEFAULT NULL COMMENT 'sesID',
  `msgType` varchar(2) DEFAULT NULL COMMENT 'msgType',
  `hostName` varchar(256) DEFAULT NULL COMMENT 'hostName',
  `calleNum` varchar(16) DEFAULT NULL COMMENT 'calleNum',
  `calliNum` varchar(16) DEFAULT NULL COMMENT 'calliNum',
  `venName` varchar(256) DEFAULT NULL COMMENT 'venName',
  `challenge` varchar(16) DEFAULT NULL COMMENT 'challenge',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.ldap 结构
CREATE TABLE IF NOT EXISTS `ldap` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `msgID` int(11) DEFAULT NULL COMMENT 'msgID',
  `msgType` varchar(1) DEFAULT NULL COMMENT 'msgType',
  `attType` varchar(128) DEFAULT NULL COMMENT 'attType',
  `attVal` varchar(512) DEFAULT NULL COMMENT 'attVal',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.ospf 结构
CREATE TABLE IF NOT EXISTS `ospf` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ver` varchar(1) DEFAULT NULL COMMENT 'ver',
  `msgType` varchar(1) DEFAULT NULL COMMENT 'msgType',
  `rouID` varchar(4) DEFAULT NULL COMMENT 'rouID',
  `areaID` varchar(4) DEFAULT NULL COMMENT 'areaID',
  `authType` varchar(2) DEFAULT NULL COMMENT 'authType',
  `authPwd` varchar(8) DEFAULT NULL COMMENT 'authPwd',
  `keyID` varchar(1) DEFAULT NULL COMMENT 'keyID',
  `cryptSeqNum` int(11) DEFAULT NULL COMMENT 'cryptSeqNum',
  `authData` varchar(256) DEFAULT NULL COMMENT 'authData',
  `helInt` int(11) DEFAULT NULL COMMENT 'helInt',
  `netMask` varchar(4) DEFAULT NULL COMMENT 'netMask',
  `rouPri` varchar(1) DEFAULT NULL COMMENT 'rouPri',
  `deadInt` int(11) DEFAULT NULL COMMENT 'deadInt',
  `DR` varchar(4) DEFAULT NULL COMMENT 'DR',
  `BDR` varchar(4) DEFAULT NULL COMMENT 'BDR',
  `neighbor` varchar(4) DEFAULT NULL COMMENT 'neighbor',
  `DBSeqNum` int(11) DEFAULT NULL COMMENT 'DBSeqNum',
  `linkStaType` varchar(4) DEFAULT NULL COMMENT 'linkStaType',
  `linkStaID` varchar(4) DEFAULT NULL COMMENT 'linkStaID',
  `advRou` varchar(4) DEFAULT NULL COMMENT 'advRou',
  `LSASeqNum` varchar(4) DEFAULT NULL COMMENT 'LSASeqNum',
  `lsaAge` varchar(4) DEFAULT NULL COMMENT 'lsaAge',
  `VLFlag` tinyint(1) DEFAULT NULL COMMENT 'VLFlag',
  `extBit` tinyint(1) DEFAULT NULL COMMENT 'extBit',
  `borBit` tinyint(1) DEFAULT NULL COMMENT 'borBit',
  `LSANetMask` varchar(4) DEFAULT NULL COMMENT 'LSANetMask',
  `linkID` varchar(4) DEFAULT NULL COMMENT 'linkID',
  `linkData` varchar(4) DEFAULT NULL COMMENT 'linkData',
  `linkType` varchar(1) DEFAULT NULL COMMENT 'linkType',
  `metric` varchar(2) DEFAULT NULL COMMENT 'metric',
  `attRou` varchar(4) DEFAULT NULL COMMENT 'attRou',
  `fwdAddr` varchar(4) DEFAULT NULL COMMENT 'fwdAddr',
  `extType` varchar(1) DEFAULT NULL COMMENT 'extType',
  `extRouTag` int(11) DEFAULT NULL COMMENT 'extRouTag',
  `neigIntID` varchar(4) DEFAULT NULL COMMENT 'neigIntID',
  `neigRouID` varchar(4) DEFAULT NULL COMMENT 'neigRouID',
  `dstTouID` varchar(4) DEFAULT NULL COMMENT 'dstTouID',
  `rouIntAddr` varchar(4) DEFAULT NULL COMMENT 'rouIntAddr',
  `locIntIPAddr` varchar(4) DEFAULT NULL COMMENT 'locIntIPAddr',
  `remIntIP` varchar(4) DEFAULT NULL COMMENT 'remIntIP',
  `traProjMet` varchar(4) DEFAULT NULL COMMENT 'traProjMet',
  `maxBW` varchar(4) DEFAULT NULL COMMENT 'maxBW',
  `maxResBW` varchar(4) DEFAULT NULL COMMENT 'maxResBW',
  `unresBW` varchar(32) DEFAULT NULL COMMENT 'unresBW',
  `adminGrp` varchar(4) DEFAULT NULL COMMENT 'adminGrp',
  `LSALinkType` varchar(4) DEFAULT NULL COMMENT 'LSALinkType',
  `LSALinkID` varchar(4) DEFAULT NULL COMMENT 'LSALinkID',
  `traEngMet` varchar(4) DEFAULT NULL COMMENT 'traEngMet',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.pkt_info 结构
CREATE TABLE IF NOT EXISTS `pkt_info` (
  `user_id` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `Header` int(11) DEFAULT NULL,
  `SatPos` int(11) DEFAULT NULL,
  `Frequence` int(11) DEFAULT NULL,
  `ModeCode` int(11) DEFAULT NULL,
  `Length` int(11) DEFAULT NULL,
  `Time0` datetime DEFAULT NULL,
  `intf_type` int(11) DEFAULT NULL,
  `Check` int(11) DEFAULT NULL,
  `Time1` datetime DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户收集端口输入的所有的报文信息';

-- 数据导出被取消选择。
-- 导出  表 test.pptp 结构
CREATE TABLE IF NOT EXISTS `pptp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `conType` varchar(2) DEFAULT NULL COMMENT 'conType',
  `venName` varchar(256) DEFAULT NULL COMMENT 'venName',
  `hostName` varchar(256) DEFAULT NULL COMMENT 'hostName',
  `calliNum` varchar(16) DEFAULT NULL COMMENT 'calliNum',
  `calleNum` varchar(16) DEFAULT NULL COMMENT 'calleNum',
  `callID` varchar(2) DEFAULT NULL COMMENT 'callID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.proto_factor_select 结构
CREATE TABLE IF NOT EXISTS `proto_factor_select` (
  `position` int(11) DEFAULT NULL COMMENT '阵地编号',
  `proto_id` int(11) DEFAULT NULL COMMENT '协议编号',
  `proto_select` int(11) DEFAULT NULL COMMENT '协议是否选择',
  `factor_id` int(11) DEFAULT NULL COMMENT '要素编号',
  `factor_select` int(11) DEFAULT NULL COMMENT '要素是否选择'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用于前端配置元数据提取选择的协议和对应要素,如果协议未选择，则后面所有的要素选择不关注';

-- 数据导出被取消选择。
-- 导出  表 test.proto_info 结构
CREATE TABLE IF NOT EXISTS `proto_info` (
  `proto_id` int(11) DEFAULT NULL,
  `proto_name` varchar(50) DEFAULT NULL,
  `proto_factor_num` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用于存储所有支持的协议及相关属性信息';

-- 数据导出被取消选择。
-- 导出  表 test.restore_file 结构
CREATE TABLE IF NOT EXISTS `restore_file` (
  `file_type` int(11) DEFAULT NULL COMMENT '文件类型，1为通用加密协议文件， 2为不明协议文件',
  `proto_type` varchar(50) DEFAULT NULL COMMENT '加密协议类型，用文本表示',
  `file_path` varchar(50) DEFAULT NULL COMMENT '还原数据文件路径'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='通用加密协议, 不明协议数据还原出来的数据文件存储路径表';

-- 数据导出被取消选择。
-- 导出  表 test.restore_html 结构
CREATE TABLE IF NOT EXISTS `restore_html` (
  `id` int(11) DEFAULT NULL,
  `key_word` varchar(50) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `author` varchar(50) DEFAULT NULL,
  `keywords` varchar(50) DEFAULT NULL,
  `copyright` varchar(50) DEFAULT NULL,
  `generator` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='html协议恢复的数据要素及文件路径';

-- 数据导出被取消选择。
-- 导出  表 test.restore_ipphone 结构
CREATE TABLE IF NOT EXISTS `restore_ipphone` (
  `begin_time` time DEFAULT NULL COMMENT '通联起始时间',
  `end_time` time DEFAULT NULL COMMENT '通联结束时间',
  `time_len` int(11) DEFAULT NULL COMMENT '通联时长',
  `sip` varchar(50) DEFAULT NULL COMMENT '源IP',
  `sport` int(11) DEFAULT NULL COMMENT '源端口',
  `dip` varchar(50) DEFAULT NULL COMMENT '目的ip',
  `dport` int(11) DEFAULT NULL COMMENT '目的端口',
  `call_no` varchar(50) DEFAULT NULL COMMENT '主叫号码',
  `call_name` varchar(50) DEFAULT NULL COMMENT '主叫用户名',
  `called_no` varchar(50) DEFAULT NULL COMMENT '被叫号码',
  `called_name` varchar(50) DEFAULT NULL COMMENT '被叫用户名',
  `type` varchar(50) DEFAULT NULL COMMENT '媒体相关信息（类型、编码、速率）',
  `code` varchar(50) DEFAULT NULL,
  `rate` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='SIP/H323协议还原信息存储表\r\n\r\n通联起始时间、通联结束时间、通联时长、源目的的IP、源目的端口、主叫号码、主叫用户名、被叫号码、被叫用户名、媒体相关信息（类型、编码、速率）';

-- 数据导出被取消选择。
-- 导出  表 test.restore_mail 结构
CREATE TABLE IF NOT EXISTS `restore_mail` (
  `mail_type` varchar(50) DEFAULT NULL COMMENT '邮件类型，也即POP3/STMP/MAP4',
  `junk_mail` int(11) DEFAULT NULL COMMENT '垃圾邮件标识，也即是否为垃圾邮件',
  `mail_tile` varchar(50) DEFAULT NULL COMMENT '邮件标题',
  `file_path` varchar(50) DEFAULT NULL COMMENT '还原邮件文件路径'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='POP3/STMP/IMAP4协议还原信息输出存储表';

-- 数据导出被取消选择。
-- 导出  表 test.restore_para_config 结构
CREATE TABLE IF NOT EXISTS `restore_para_config` (
  `position` int(11) DEFAULT NULL COMMENT '阵地编号',
  `junk_mail` varchar(50) DEFAULT NULL COMMENT '垃圾邮件关键词',
  `mail_select` int(11) DEFAULT NULL COMMENT '邮件是否选择',
  `web_key` varchar(50) DEFAULT NULL COMMENT '网页关键词',
  `web_select` int(11) DEFAULT NULL COMMENT '网页是否选择',
  `vad_period` int(11) DEFAULT NULL COMMENT '静音检测周期'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='配置垃圾邮件、网页关键词及静音检测周期等参数， 用于协议还原时使用';

-- 数据导出被取消选择。
-- 导出  表 test.restore_rtp 结构
CREATE TABLE IF NOT EXISTS `restore_rtp` (
  `type` int(11) DEFAULT NULL COMMENT '语音还是视频，1为语音，2为视频',
  `proto_name` varchar(50) DEFAULT NULL COMMENT '编码格式类别（如H.261、H.263和G.711等）',
  `file_path` varchar(50) DEFAULT NULL COMMENT '文件路径'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='语音、视频协议恢复数据存储表，语音还是视频、编码格式类别（如H.261、H.263和G.711等）';

-- 数据导出被取消选择。
-- 导出  表 test.rip 结构
CREATE TABLE IF NOT EXISTS `rip` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cmd` varchar(1) DEFAULT NULL COMMENT 'cmd',
  `ver` varchar(1) DEFAULT NULL COMMENT 'ver',
  `addrFamID` varchar(2) DEFAULT NULL COMMENT 'addrFamID',
  `IPAddr` varchar(4) DEFAULT NULL COMMENT 'IPAddr',
  `rouTag` varchar(4) DEFAULT NULL COMMENT 'rouTag',
  `subMask` varchar(4) DEFAULT NULL COMMENT 'subMask',
  `nextHop` varchar(4) DEFAULT NULL COMMENT 'nextHop',
  `metric` int(11) DEFAULT NULL COMMENT 'metric',
  `authType` varchar(2) DEFAULT NULL COMMENT 'authType',
  `auth` varchar(32) DEFAULT NULL COMMENT 'auth',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.smb 结构
CREATE TABLE IF NOT EXISTS `smb` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `usrID` varchar(2) DEFAULT NULL COMMENT 'usrID',
  `dir` varchar(256) DEFAULT NULL COMMENT 'dir',
  `path` varchar(256) DEFAULT NULL COMMENT 'path',
  `srvDom` varchar(256) DEFAULT NULL COMMENT 'srvDom',
  `hostName` varchar(256) DEFAULT NULL COMMENT 'hostName',
  `natOS` varchar(64) DEFAULT NULL COMMENT 'natOS',
  `ver` varchar(16) DEFAULT NULL COMMENT 'ver',
  `authType` varchar(8) DEFAULT NULL COMMENT 'authType',
  `fileName` varchar(256) DEFAULT NULL COMMENT 'fileName',
  `fileSize` mediumtext DEFAULT NULL COMMENT 'fileSize',
  `fileAtt` varchar(4) DEFAULT NULL COMMENT 'fileAtt',
  `fileExtAtt` varchar(4) DEFAULT NULL COMMENT 'fileExtAtt',
  `maiSlotName` varchar(64) DEFAULT NULL COMMENT 'maiSlotName',
  `fileID` int(11) DEFAULT NULL COMMENT 'fileID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.smtpmailindpar 结构
CREATE TABLE IF NOT EXISTS `smtpmailindpar` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `senderMailFrom` varchar(256) DEFAULT NULL COMMENT 'senderMailFrom',
  `senderDom` varchar(64) DEFAULT NULL COMMENT 'senderDom',
  `CC` varchar(2048) DEFAULT NULL COMMENT 'CC',
  `BCC` varchar(2048) DEFAULT NULL COMMENT 'BCC',
  `staTime` varchar(64) DEFAULT NULL COMMENT 'staTime',
  `stopTime` varchar(64) DEFAULT NULL COMMENT 'stopTime',
  `duration` int(11) DEFAULT NULL COMMENT 'duration',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.snmp 结构
CREATE TABLE IF NOT EXISTS `snmp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ver` varchar(1) DEFAULT NULL COMMENT 'ver',
  `comm` varchar(256) DEFAULT NULL COMMENT 'comm',
  `PDUType` varchar(1) DEFAULT NULL COMMENT 'PDUType',
  `reqID` varchar(32) DEFAULT NULL COMMENT 'reqID',
  `entID` varchar(256) DEFAULT NULL COMMENT 'entID',
  `secMode` varchar(1) DEFAULT NULL COMMENT 'secMode',
  `OID` varchar(256) DEFAULT NULL COMMENT 'OID',
  `objVal` varchar(1024) DEFAULT NULL COMMENT 'objVal',
  `ObjSyn` int(11) DEFAULT NULL COMMENT 'ObjSyn',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.ssh 结构
CREATE TABLE IF NOT EXISTS `ssh` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cliVer` varchar(64) DEFAULT NULL COMMENT 'cliVer',
  `cliCookie` varchar(16) DEFAULT NULL COMMENT 'cliCookie',
  `cliKeyExcAndAutMet` varchar(256) DEFAULT NULL COMMENT 'cliKeyExcAndAutMet',
  `cliHostKeyAlg` varchar(64) DEFAULT NULL COMMENT 'cliHostKeyAlg',
  `cliEncryAlg` varchar(64) DEFAULT NULL COMMENT 'cliEncryAlg',
  `cliMsgAuthCodeAlg` varchar(64) DEFAULT NULL COMMENT 'cliMsgAuthCodeAlg',
  `cliComprAlg` varchar(16) DEFAULT NULL COMMENT 'cliComprAlg',
  `cliDHPubKey` varchar(512) DEFAULT NULL COMMENT 'cliDHPubKey',
  `srvVer` varchar(64) DEFAULT NULL COMMENT 'srvVer',
  `srvCookie` varchar(16) DEFAULT NULL COMMENT 'srvCookie',
  `srvKeyExcAndAuthMet` varchar(256) DEFAULT NULL COMMENT 'srvKeyExcAndAuthMet',
  `srvHostKeyAlg` varchar(64) DEFAULT NULL COMMENT 'srvHostKeyAlg',
  `srvEncryAlg` varchar(64) DEFAULT NULL COMMENT 'srvEncryAlg',
  `srvMsgAuthCodeAlg` varchar(16) DEFAULT NULL COMMENT 'srvMsgAuthCodeAlg',
  `srvComprAlg` varchar(16) DEFAULT NULL COMMENT 'srvComprAlg',
  `srvDHPubKey` varchar(512) DEFAULT NULL COMMENT 'srvDHPubKey',
  `expNumBySrvHostKey` varchar(512) DEFAULT NULL COMMENT 'expNumBySrvHostKey',
  `modBySrvHostKey` varchar(512) DEFAULT NULL COMMENT 'modBySrvHostKey',
  `pBySrvHostKey` varchar(512) DEFAULT NULL COMMENT 'pBySrvHostKey',
  `qBySrvHostKey` varchar(512) DEFAULT NULL COMMENT 'qBySrvHostKey',
  `gBySrvHostKey` varchar(512) DEFAULT NULL COMMENT 'gBySrvHostKey',
  `yBySrvHostKey` varchar(512) DEFAULT NULL COMMENT 'yBySrvHostKey',
  `expNumOfSryHostKey` varchar(512) DEFAULT NULL COMMENT 'expNumOfSryHostKey',
  `modOfSrvHostKey` varchar(512) DEFAULT NULL COMMENT 'modOfSrvHostKey',
  `sigOfSrvKey` varchar(256) DEFAULT NULL COMMENT 'sigOfSrvKey',
  `DHGen` varchar(512) DEFAULT NULL COMMENT 'DHGen',
  `DHMod` varchar(512) DEFAULT NULL COMMENT 'DHMod',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.ssl_tls 结构
CREATE TABLE IF NOT EXISTS `ssl_tls` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `conType` varchar(1) DEFAULT NULL COMMENT 'conType',
  `aleLev` varchar(1) DEFAULT NULL COMMENT 'aleLev',
  `aleDes` varchar(1) DEFAULT NULL COMMENT 'aleDes',
  `handShaType` varchar(1) DEFAULT NULL COMMENT 'handShaType',
  `cliVer` varchar(2) DEFAULT NULL COMMENT 'cliVer',
  `cliGMTUniTime` datetime DEFAULT NULL COMMENT 'cliGMTUniTime',
  `cliRand` varchar(28) DEFAULT NULL COMMENT 'cliRand',
  `cliSesID` varchar(32) DEFAULT NULL COMMENT 'cliSesID',
  `cliCipSui` varchar(3) DEFAULT NULL COMMENT 'cliCipSui',
  `cliComMet` varchar(1) DEFAULT NULL COMMENT 'cliComMet',
  `srvVer` varchar(1) DEFAULT NULL COMMENT 'srvVer',
  `srvName` varchar(64) DEFAULT NULL COMMENT 'srvName',
  `srvGMTUniTime` datetime DEFAULT NULL COMMENT 'srvGMTUniTime',
  `srvRand` varchar(28) DEFAULT NULL COMMENT 'srvRand',
  `srvSesID` varchar(32) DEFAULT NULL COMMENT 'srvSesID',
  `srvCipSui` varchar(3) DEFAULT NULL COMMENT 'srvCipSui',
  `srvComprMet` varchar(1) DEFAULT NULL COMMENT 'srvComprMet',
  `srvCertSeqNum` varchar(16) DEFAULT NULL COMMENT 'srvCertSeqNum',
  `srvCertLen` int(11) DEFAULT NULL COMMENT 'srvCertLen',
  `certResType` varchar(1) DEFAULT NULL COMMENT 'certResType',
  `cliCertAuthDesc` varchar(16) DEFAULT NULL COMMENT 'cliCertAuthDesc',
  `cliCertSeqNum` varchar(16) DEFAULT NULL COMMENT 'cliCertSeqNum',
  `cliCertLen` int(11) DEFAULT NULL COMMENT 'cliCertLen',
  `RSAModOfSrvKeyExc` varchar(512) DEFAULT NULL COMMENT 'RSAModOfSrvKeyExc',
  `RSAExpOfSrvKeyExc` varchar(4) DEFAULT NULL COMMENT 'RSAExpOfSrvKeyExc',
  `DHModOfSrvKeyExc` varchar(512) DEFAULT NULL COMMENT 'DHModOfSrvKeyExc',
  `DHGenOfSrvKeyExc` varchar(512) DEFAULT NULL COMMENT 'DHGenOfSrvKeyExc',
  `srvDHPubKey` varchar(512) DEFAULT NULL COMMENT 'srvDHPubKey',
  `preMasKeyEncryByRSA` varchar(512) DEFAULT NULL COMMENT 'preMasKeyEncryByRSA',
  `cliDHPubKey` varchar(512) DEFAULT NULL COMMENT 'cliDHPubKey',
  `extTypeInSSL` varchar(2) DEFAULT NULL COMMENT 'extTypeInSSL',
  `cliEllCurPoiFor` varchar(1) DEFAULT NULL COMMENT 'cliEllCurPoiFor',
  `cliEllCur` varchar(2) DEFAULT NULL COMMENT 'cliEllCur',
  `srvEllCurPoiFor` varchar(1) DEFAULT NULL COMMENT 'srvEllCurPoiFor',
  `srvEllCur` varchar(2) DEFAULT NULL COMMENT 'srvEllCur',
  `srvEllCurDHPubKey` varchar(256) DEFAULT NULL COMMENT 'srvEllCurDHPubKey',
  `cliEllCurDHPubKey` varchar(256) DEFAULT NULL COMMENT 'cliEllCurDHPubKey',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.stun 结构
CREATE TABLE IF NOT EXISTS `stun` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `msgType` varchar(4) DEFAULT NULL COMMENT 'msgType',
  `traID` varchar(16) DEFAULT NULL COMMENT 'traID',
  `mapAddr` varchar(4) DEFAULT NULL COMMENT 'mapAddr',
  `mapPort` int(11) DEFAULT NULL COMMENT 'mapPort',
  `respAddr` varchar(4) DEFAULT NULL COMMENT 'respAddr',
  `respPort` int(11) DEFAULT NULL COMMENT 'respPort',
  `chaIPFlag` tinyint(1) DEFAULT NULL COMMENT 'chaIPFlag',
  `chaPortFlag` tinyint(1) DEFAULT NULL COMMENT 'chaPortFlag',
  `chaAddr` varchar(4) DEFAULT NULL COMMENT 'chaAddr',
  `chaPort` int(11) DEFAULT NULL COMMENT 'chaPort',
  `srcAddrResp` varchar(4) DEFAULT NULL COMMENT 'srcAddrResp',
  `srcPort` int(11) DEFAULT NULL COMMENT 'srcPort',
  `usrName` varchar(32) DEFAULT NULL COMMENT 'usrName',
  `pwd` varchar(64) DEFAULT NULL COMMENT 'pwd',
  `softName` varchar(64) DEFAULT NULL COMMENT 'softName',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.tcp 结构
CREATE TABLE IF NOT EXISTS `tcp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `srcPort` int(11) DEFAULT NULL COMMENT 'srcPort',
  `dstPort` int(11) DEFAULT NULL COMMENT 'dstPort',
  `optType` varchar(1) DEFAULT NULL COMMENT 'optType',
  `TCPChe` tinyint(1) DEFAULT NULL COMMENT 'TCPChe',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.telnet 结构
CREATE TABLE IF NOT EXISTS `telnet` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `login` varchar(64) DEFAULT NULL COMMENT 'login',
  `pwd` varchar(32) DEFAULT NULL COMMENT 'pwd',
  `terType` varchar(128) DEFAULT NULL COMMENT 'terType',
  `comRespCon` varchar(1024) DEFAULT NULL COMMENT 'comRespCon',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.tftp 结构
CREATE TABLE IF NOT EXISTS `tftp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tra_mode` varchar(8) DEFAULT NULL COMMENT 'tra_mode',
  `fileSize` mediumtext DEFAULT NULL COMMENT 'fileSize',
  `fileName` varchar(256) DEFAULT NULL COMMENT 'fileName',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.udp 结构
CREATE TABLE IF NOT EXISTS `udp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `srcPort` int(11) DEFAULT NULL COMMENT 'srcPort',
  `dstPort` int(11) DEFAULT NULL COMMENT 'dstPort',
  `dataLen` int(11) DEFAULT NULL COMMENT 'dataLen',
  `UDPChe` tinyint(1) DEFAULT NULL COMMENT 'UDPChe',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.user_flow_factor 结构
CREATE TABLE IF NOT EXISTS `user_flow_factor` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userID` int(11) DEFAULT NULL COMMENT 'userID',
  `sip` varchar(50) DEFAULT NULL COMMENT '源IP',
  `dip` varchar(50) DEFAULT NULL COMMENT '目的ip',
  `sport` varchar(50) DEFAULT NULL COMMENT '源端口',
  `dport` varchar(50) DEFAULT NULL COMMENT '目的端口',
  `pt` varchar(50) DEFAULT NULL COMMENT '协议类型',
  `pkts` varchar(50) DEFAULT NULL COMMENT '包数',
  `bytes` varchar(50) DEFAULT NULL COMMENT '字节数',
  `time` datetime DEFAULT NULL COMMENT 'time',
  `IPv4` int(11) DEFAULT NULL COMMENT 'IPv4',
  `TCP` int(11) DEFAULT NULL COMMENT 'TCP',
  `UDP` int(11) DEFAULT NULL COMMENT 'UDP',
  `STUN` int(11) DEFAULT NULL COMMENT 'STUN',
  `HTTP` int(11) DEFAULT NULL COMMENT 'HTTP',
  `ISAKMP` int(11) DEFAULT NULL COMMENT 'ISAKMP',
  `ESP` int(11) DEFAULT NULL COMMENT 'ESP',
  `AH` int(11) DEFAULT NULL COMMENT 'AH',
  `SSL_TLS` int(11) DEFAULT NULL COMMENT 'SSL_TLS',
  `SSH` int(11) DEFAULT NULL COMMENT 'SSH',
  `X_509` int(11) DEFAULT NULL COMMENT 'X_509',
  `DTLS` int(11) DEFAULT NULL COMMENT 'DTLS',
  `GRE` int(11) DEFAULT NULL COMMENT 'GRE',
  `L2TP` int(11) DEFAULT NULL COMMENT 'L2TP',
  `PPTP` int(11) DEFAULT NULL COMMENT 'PPTP',
  `FTP` int(11) DEFAULT NULL COMMENT 'FTP',
  `TFTP` int(11) DEFAULT NULL COMMENT 'TFTP',
  `SMB` int(11) DEFAULT NULL COMMENT 'SMB',
  `EmailProComPar` int(11) DEFAULT NULL COMMENT 'EmailProComPar',
  `EmailComPar` int(11) DEFAULT NULL COMMENT 'EmailComPar',
  `SMTPMailIndPar` int(11) DEFAULT NULL COMMENT 'SMTPMailIndPar',
  `IMAP4MailIndPar` int(11) DEFAULT NULL COMMENT 'IMAP4MailIndPar',
  `WebMailIndPar` int(11) DEFAULT NULL COMMENT 'WebMailIndPar',
  `SNMP` int(11) DEFAULT NULL COMMENT 'SNMP',
  `ICMP` int(11) DEFAULT NULL COMMENT 'ICMP',
  `LDAP` int(11) DEFAULT NULL COMMENT 'LDAP',
  `DNS` int(11) DEFAULT NULL COMMENT 'DNS',
  `RIP` int(11) DEFAULT NULL COMMENT 'RIP',
  `BGP` int(11) DEFAULT NULL COMMENT 'BGP',
  `OSPF` int(11) DEFAULT NULL COMMENT 'OSPF',
  `TELNET` int(11) DEFAULT NULL COMMENT 'TELNET',
  `H323` int(11) DEFAULT NULL COMMENT 'H323',
  `H248` int(11) DEFAULT NULL COMMENT 'H248',
  `MobAccNet` int(11) DEFAULT NULL COMMENT 'MobAccNet',
  `MobEqu` int(11) DEFAULT NULL COMMENT 'MobEqu',
  `MobApp` int(11) DEFAULT NULL COMMENT 'MobApp',
  `SocApp` int(11) DEFAULT NULL COMMENT 'SocApp',
  `SeaApp` int(11) DEFAULT NULL COMMENT 'SeaApp',
  `MapApp` int(11) DEFAULT NULL COMMENT 'MapApp',
  `WeaApp` int(11) DEFAULT NULL COMMENT 'WeaApp',
  `TraApp` int(11) DEFAULT NULL COMMENT 'TraApp',
  `EBusApp` int(11) DEFAULT NULL COMMENT 'EBusApp',
  `SpoApp` int(11) DEFAULT NULL COMMENT 'SpoApp',
  `HeaApp` int(11) DEFAULT NULL COMMENT 'HeaApp',
  `FinApp` int(11) DEFAULT NULL COMMENT 'FinApp',
  `GamApp` int(11) DEFAULT NULL COMMENT 'GamApp',
  `expAPP` int(11) DEFAULT NULL COMMENT 'expAPP',
  `TakeOutApp` int(11) DEFAULT NULL COMMENT 'TakeOutApp',
  `ResApp` int(11) DEFAULT NULL COMMENT 'ResApp',
  `HotApp` int(11) DEFAULT NULL COMMENT 'HotApp',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_1` (`H248`),
  KEY `FK_Reference_10` (`WebMailIndPar`),
  KEY `FK_Reference_11` (`X_509`),
  KEY `FK_Reference_12` (`ICMP`),
  KEY `FK_Reference_13` (`sip`(4)),
  KEY `FK_Reference_14` (`SMB`),
  KEY `FK_Reference_15` (`TCP`),
  KEY `FK_Reference_16` (`HeaApp`),
  KEY `FK_Reference_17` (`ISAKMP`),
  KEY `FK_Reference_18` (`MapApp`),
  KEY `FK_Reference_19` (`DNS`),
  KEY `FK_Reference_2` (`H323`),
  KEY `FK_Reference_20` (`TakeOutApp`),
  KEY `FK_Reference_21` (`WeaApp`),
  KEY `FK_Reference_22` (`SSH`),
  KEY `FK_Reference_23` (`SSL_TLS`),
  KEY `FK_Reference_24` (`ESP`),
  KEY `FK_Reference_25` (`L2TP`),
  KEY `FK_Reference_26` (`OSPF`),
  KEY `FK_Reference_27` (`expAPP`),
  KEY `FK_Reference_28` (`SeaApp`),
  KEY `FK_Reference_29` (`DTLS`),
  KEY `FK_Reference_3` (`IMAP4MailIndPar`),
  KEY `FK_Reference_30` (`FTP`),
  KEY `FK_Reference_31` (`GamApp`),
  KEY `FK_Reference_32` (`UDP`),
  KEY `FK_Reference_33` (`SocApp`),
  KEY `FK_Reference_34` (`MobApp`),
  KEY `FK_Reference_35` (`MobAccNet`),
  KEY `FK_Reference_36` (`MobEqu`),
  KEY `FK_Reference_37` (`TFTP`),
  KEY `FK_Reference_38` (`SNMP`),
  KEY `FK_Reference_39` (`TELNET`),
  KEY `FK_Reference_4` (`IPv4`),
  KEY `FK_Reference_40` (`ResApp`),
  KEY `FK_Reference_41` (`HotApp`),
  KEY `FK_Reference_42` (`EBusApp`),
  KEY `FK_Reference_43` (`HTTP`),
  KEY `FK_Reference_44` (`RIP`),
  KEY `FK_Reference_45` (`LDAP`),
  KEY `FK_Reference_46` (`BGP`),
  KEY `FK_Reference_47` (`SpoApp`),
  KEY `FK_Reference_48` (`GRE`),
  KEY `FK_Reference_49` (`FinApp`),
  KEY `FK_Reference_5` (`STUN`),
  KEY `FK_Reference_50` (`AH`),
  KEY `FK_Reference_51` (`TraApp`),
  KEY `FK_Reference_6` (`EmailComPar`),
  KEY `FK_Reference_7` (`EmailProComPar`),
  KEY `FK_Reference_8` (`PPTP`),
  KEY `FK_Reference_9` (`SMTPMailIndPar`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.user_flow_restore 结构
CREATE TABLE IF NOT EXISTS `user_flow_restore` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userID` int(11) DEFAULT NULL COMMENT 'userID',
  `sip` varchar(50) DEFAULT NULL COMMENT '源IP',
  `dip` varchar(50) DEFAULT NULL COMMENT '目的ip',
  `sport` varchar(50) DEFAULT NULL COMMENT '源端口',
  `dport` varchar(50) DEFAULT NULL COMMENT '目的端口',
  `pt` varchar(50) DEFAULT NULL COMMENT '协议类型',
  `pkts` varchar(50) DEFAULT NULL COMMENT '包数',
  `bytes` varchar(50) DEFAULT NULL COMMENT '字节数',
  `time` datetime DEFAULT NULL COMMENT 'time',
  `IPv4` int(11) DEFAULT NULL COMMENT 'IPv4',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
-- 导出  表 test.user_info 结构
CREATE TABLE IF NOT EXISTS `user_info` (
  `user_id` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `file_path` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户保存用户id信息，及用户下保存的数据文件路径';

-- 数据导出被取消选择。
-- 导出  表 test.user_proto_select 结构
CREATE TABLE IF NOT EXISTS `user_proto_select` (
  `position` int(11) DEFAULT NULL COMMENT '阵地编号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `user_select` int(11) DEFAULT NULL COMMENT '用户是否选择',
  `proto_id` int(11) DEFAULT NULL COMMENT '协议编号',
  `proto_select` int(11) DEFAULT NULL COMMENT '协议是否选择'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用于前端配置选择的用户和对应的协议种类';

-- 数据导出被取消选择。
-- 导出  表 test.x_509 结构
CREATE TABLE IF NOT EXISTS `x_509` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ver` varchar(1) DEFAULT NULL COMMENT 'ver',
  `srvNum` varchar(32) DEFAULT NULL COMMENT 'srvNum',
  `issDataLen` int(11) DEFAULT NULL COMMENT 'issDataLen',
  `issComName` varchar(32) DEFAULT NULL COMMENT 'issComName',
  `issSurName` varchar(16) DEFAULT NULL COMMENT 'issSurName',
  `issServNum` varchar(16) DEFAULT NULL COMMENT 'issServNum',
  `issConName` varchar(16) DEFAULT NULL COMMENT 'issConName',
  `issLoaName` varchar(16) DEFAULT NULL COMMENT 'issLoaName',
  `issStaOrProName` varchar(16) DEFAULT NULL COMMENT 'issStaOrProName',
  `issStrAddr` varchar(16) DEFAULT NULL COMMENT 'issStrAddr',
  `issOrgName` varchar(16) DEFAULT NULL COMMENT 'issOrgName',
  `issOrgUniName` varchar(16) DEFAULT NULL COMMENT 'issOrgUniName',
  `issTit` varchar(16) DEFAULT NULL COMMENT 'issTit',
  `issDesc` varchar(16) DEFAULT NULL COMMENT 'issDesc',
  `issBusCat` varchar(16) DEFAULT NULL COMMENT 'issBusCat',
  `issPosAddr` varchar(16) DEFAULT NULL COMMENT 'issPosAddr',
  `issPosCode` varchar(16) DEFAULT NULL COMMENT 'issPosCode',
  `issPosOffBox` varchar(16) DEFAULT NULL COMMENT 'issPosOffBox',
  `issPhyDelOffName` varchar(16) DEFAULT NULL COMMENT 'issPhyDelOffName',
  `issTelNum` varchar(16) DEFAULT NULL COMMENT 'issTelNum',
  `issTelgNum` varchar(16) DEFAULT NULL COMMENT 'issTelgNum',
  `issFasTelNum` varchar(16) DEFAULT NULL COMMENT 'issFasTelNum',
  `issX121Addr` varchar(16) DEFAULT NULL COMMENT 'issX121Addr',
  `issIntISDNNum` varchar(32) DEFAULT NULL COMMENT 'issIntISDNNum',
  `issRegAddr` varchar(16) DEFAULT NULL COMMENT 'issRegAddr',
  `issPreDelMet` varchar(16) DEFAULT NULL COMMENT 'issPreDelMet',
  `issPreAddr` varchar(16) DEFAULT NULL COMMENT 'issPreAddr',
  `issSupAppCon` varchar(16) DEFAULT NULL COMMENT 'issSupAppCon',
  `subComName` varchar(32) DEFAULT NULL COMMENT 'subComName',
  `subSurName` varchar(16) DEFAULT NULL COMMENT 'subSurName',
  `subSrvNum` varchar(16) DEFAULT NULL COMMENT 'subSrvNum',
  `subConName` varchar(16) DEFAULT NULL COMMENT 'subConName',
  `subLoaName` varchar(16) DEFAULT NULL COMMENT 'subLoaName',
  `subStaOrProName` varchar(16) DEFAULT NULL COMMENT 'subStaOrProName',
  `subStrAddr` varchar(16) DEFAULT NULL COMMENT 'subStrAddr',
  `subOrgName` varchar(16) DEFAULT NULL COMMENT 'subOrgName',
  `subOrgUniName` varchar(16) DEFAULT NULL COMMENT 'subOrgUniName',
  `subTit` varchar(16) DEFAULT NULL COMMENT 'subTit',
  `subDesc` varchar(16) DEFAULT NULL COMMENT 'subDesc',
  `subBusCat` varchar(16) DEFAULT NULL COMMENT 'subBusCat',
  `subPosAddr` varchar(16) DEFAULT NULL COMMENT 'subPosAddr',
  `subPosCode` varchar(16) DEFAULT NULL COMMENT 'subPosCode',
  `subPosOffBox` varchar(16) DEFAULT NULL COMMENT 'subPosOffBox',
  `subPhyDelOffName` varchar(16) DEFAULT NULL COMMENT 'subPhyDelOffName',
  `subTelNum` varchar(16) DEFAULT NULL COMMENT 'subTelNum',
  `subTelgNum` varchar(16) DEFAULT NULL COMMENT 'subTelgNum',
  `subaFasTelNum` varchar(16) DEFAULT NULL COMMENT 'subaFasTelNum',
  `subX121Addr` varchar(16) DEFAULT NULL COMMENT 'subX121Addr',
  `subIntISDNNum` varchar(32) DEFAULT NULL COMMENT 'subIntISDNNum',
  `subRegAddr` varchar(16) DEFAULT NULL COMMENT 'subRegAddr',
  `subPreDelMet` varchar(16) DEFAULT NULL COMMENT 'subPreDelMet',
  `subPreAddr` varchar(16) DEFAULT NULL COMMENT 'subPreAddr',
  `valNotBef` datetime DEFAULT NULL COMMENT 'valNotBef',
  `valNotAft` datetime DEFAULT NULL COMMENT 'valNotAft',
  `RSAMod` varchar(1024) DEFAULT NULL COMMENT 'RSAMod',
  `RSAExp` varchar(512) DEFAULT NULL COMMENT 'RSAExp',
  `DHPriMod` varchar(1024) DEFAULT NULL COMMENT 'DHPriMod',
  `DHPGen` varchar(512) DEFAULT NULL COMMENT 'DHPGen',
  `DHPubKey` varchar(512) DEFAULT NULL COMMENT 'DHPubKey',
  `DSAPubKeyP` varchar(512) DEFAULT NULL COMMENT 'DSAPubKeyP',
  `DSAPubKeyQ` varchar(512) DEFAULT NULL COMMENT 'DSAPubKeyQ',
  `DSAPubKeyG` varchar(512) DEFAULT NULL COMMENT 'DSAPubKeyG',
  `sigAlg` varchar(16) DEFAULT NULL COMMENT 'sigAlg',
  `sigVal` varchar(512) DEFAULT NULL COMMENT 'sigVal',
  `authKeyID` varchar(32) DEFAULT NULL COMMENT 'authKeyID',
  `subKeyID` varchar(32) DEFAULT NULL COMMENT 'subKeyID',
  `keyUsage` varchar(16) DEFAULT NULL COMMENT 'keyUsage',
  `priKeyUsaPerNotBef` datetime DEFAULT NULL COMMENT 'priKeyUsaPerNotBef',
  `priKeyUsaPerNotAft` datetime DEFAULT NULL COMMENT 'priKeyUsaPerNotAft',
  `certPol` varchar(16) DEFAULT NULL COMMENT 'certPol',
  `subAltDNS` varchar(64) DEFAULT NULL COMMENT 'subAltDNS',
  `subAltIP` varchar(16) DEFAULT NULL COMMENT 'subAltIP',
  `subAltName` varchar(64) DEFAULT NULL COMMENT 'subAltName',
  `issAltNameSys` varchar(64) DEFAULT NULL COMMENT 'issAltNameSys',
  `issAltIP` varchar(16) DEFAULT NULL COMMENT 'issAltIP',
  `issAltName` varchar(64) DEFAULT NULL COMMENT 'issAltName',
  `subDirAtt` varchar(32) DEFAULT NULL COMMENT 'subDirAtt',
  `certCouNeeBefNewPolFou` int(11) DEFAULT NULL COMMENT 'certCouNeeBefNewPolFou',
  `certCouNeeBefOldPolLos` int(11) DEFAULT NULL COMMENT 'certCouNeeBefOldPolLos',
  `extKeyUsage` varchar(16) DEFAULT NULL COMMENT 'extKeyUsage',
  `certRevListSrc` varchar(128) DEFAULT NULL COMMENT 'certRevListSrc',
  `certRevRea` varchar(1) DEFAULT NULL COMMENT 'certRevRea',
  `certRevIss` varchar(128) DEFAULT NULL COMMENT 'certRevIss',
  `certAuthInfAccMet` varchar(16) DEFAULT NULL COMMENT 'certAuthInfAccMet',
  `certAuthInfAccLoc` varchar(128) DEFAULT NULL COMMENT 'certAuthInfAccLoc',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
