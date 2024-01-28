-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-11-2023 a las 03:45:39
-- Versión del servidor: 10.4.22-MariaDB
-- Versión de PHP: 7.3.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `theallseeingeye`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bans_reasons`
--

CREATE TABLE `bans_reasons` (
  `id` int(11) NOT NULL,
  `sanctionReasonId` text DEFAULT NULL,
  `sanctionReasonValue` text DEFAULT NULL,
  `sanctionReasonDesc` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cfh_logs`
--

CREATE TABLE `cfh_logs` (
  `cfh_id` int(11) NOT NULL,
  `user` varchar(255) DEFAULT NULL,
  `room_id` int(11) DEFAULT NULL,
  `room` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `created_time` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `expire_time` varchar(255) DEFAULT NULL,
  `is_deleted` varchar(255) DEFAULT NULL,
  `cry_id` varchar(255) DEFAULT NULL,
  `moderator` varchar(255) DEFAULT NULL,
  `picked_time` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `housekeeping_rcon_logs`
--

CREATE TABLE `housekeeping_rcon_logs` (
  `id` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  `moderator` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `timestamp` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `housekeeping_login_log`
--

CREATE TABLE `housekeeping_login_log` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `login_time` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `settings_desc`
--

CREATE TABLE `settings_desc` (
  `setting` varchar(50) NOT NULL,
  `description` longtext DEFAULT NULL,
  `category` varchar(55) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `bans_reasons`
--

INSERT INTO `bans_reasons` (`id`, `sanctionReasonId`, `sanctionReasonValue`, `sanctionReasonDesc`) VALUES
(1, 'AUTO_TRIGGER', 'mal comportamiento', NULL);

-- --------------------------------------------------------

--
-- Volcado de datos para la tabla `settings_desc`
--

INSERT INTO `settings_desc` (`setting`, `description`, `category`) VALUES
('afk.timer.seconds', 'Set the time to get afk in seconds', 'client'),
('alerts.gift.message', 'Ha llegado un nuevo regalo. Esta vez has recibido %item_name%.', NULL),
('april.fools', 'April fools', 'miscellaneous'),
('battleball.create.game.enabled', 'Toggle can create game of BattleBall as true or false', 'games'),
('battleball.game.lifetime.seconds', 'Set the minimum game duration of BattleBall in seconds', 'games'),
('battleball.increase.points', 'Toggle increase points of BattleBall game as true or false', 'games'),
('battleball.preparing.game.seconds', 'Set the preparing game time of BattleBall in seconds', 'games'),
('battleball.restart.game.seconds', 'Set the restart game time of BattleBall in seconds', 'games'),
('battleball.start.minimum.active.teams', 'Set the minimum teams needed to start a game of BattleBall', 'games'),
('battleball.ticket.charge', 'Set the ticket charge of BattleBall', 'games'),
('carry.timer.seconds', '300', NULL),
('catalogue.frontpage.input.1', 'ts_coll_09_sept_asteroid.gif', NULL),
('catalogue.frontpage.input.2', '¡Servidor Beta!', NULL),
('catalogue.frontpage.input.3', 'Porfavor comparte con nosotros mientras que hacemos los arreglos finales', NULL),
('catalogue.frontpage.input.4', 'http://localhost', NULL),
('chat.spam.count', '10', NULL),
('childline.group.id', 'Set the ID of Childline group', 'server'),
('club.gift.interval', '30', NULL),
('club.gift.present.label', '¡Ha llegado un nuevo regalo mensual del Habbo Club!', NULL),
('club.gift.timeunit', 'DAYS', NULL),
('collectables.page', 'Set the ID of the catalogue Collectibles page', 'catalogue'),
('credits.scheduler.amount', 'Set the credits scheduler amount', 'coins'),
('credits.scheduler.interval', 'Set the credits scheduler interval', 'coins'),
('credits.scheduler.timeunit', 'Set the credits scheduler timeunit', 'coins'),
('daily.credits.amount', 'Set the daily credits amount', 'coins'),
('daily.credits.wait.time', 'Set the daily credits wait time in seconds', 'coins'),
('delete.chatlogs.after.x.age', '2592000', NULL),
('delete.iplogs.after.x.age', '2592000', NULL),
('delete.tradelogs.after.x.age', '2592000', NULL),
('discussions.per.page', 'Set the max number of forum discussions per page', 'site'),
('discussions.replies.per.page', 'Set the max number of forum discussions replies per page', 'site'),
('email.smtp.enable', 'Toggle status of email SMTP service as true or false', 'email'),
('email.smtp.from.email', 'Set the email adress of SMTP', 'email'),
('email.smtp.from.name', 'Set the username of the email SMTP', 'email'),
('email.smtp.host', 'Set the host adress of email SMTP', 'email'),
('email.smtp.login.password', 'Set the password of email SMTP', 'email'),
('email.smtp.login.username', 'Set the adress of the email SMTP', 'email'),
('email.smtp.port', 'Set the port of email SMTP', 'email'),
('email.static.content.path', 'Set the URL of email static content path', 'email'),
('enforce.strict.packet.policy', 'Enforce strict packet policy', 'miscellaneous'),
('events.category.count', '11', NULL),
('events.expiry.minutes', '120', NULL),
('free.month.hc.registration', 'true', NULL),
('fuck.aaron', 'true', NULL),
('group.default.badge', 'b0503Xs09114s05013s05015', NULL),
('group.purchase.cost', '20', NULL),
('groups.ids.permission.addcredits', '', NULL),
('groups.ids.permission.ban', '', NULL),
('groups.ids.permission.bot', '', NULL),
('groups.ids.permission.checkbalance', '', NULL),
('groups.ids.permission.coords', '', NULL),
('groups.ids.permission.copyroom', '', NULL),
('groups.ids.permission.dc', '', NULL),
('groups.ids.permission.deletebadge', '', NULL),
('groups.ids.permission.giftroom', '', NULL),
('groups.ids.permission.givebadge', '', NULL),
('groups.ids.permission.headrotate', '', NULL),
('groups.ids.permission.hotelalert', '', NULL),
('groups.ids.permission.itemdebug', '', NULL),
('groups.ids.permission.mute', '', NULL),
('groups.ids.permission.packet', '', NULL),
('groups.ids.permission.permban', '', NULL),
('groups.ids.permission.reload', '', NULL),
('groups.ids.permission.removecredits', '', NULL),
('groups.ids.permission.resetpw', '', NULL),
('groups.ids.permission.roomalert', '', NULL),
('groups.ids.permission.roommute', '', NULL),
('groups.ids.permission.setconfig', '', NULL),
('groups.ids.permission.shutdown', '', NULL),
('groups.ids.permission.talk', '', NULL),
('groups.ids.permission.teleport', '', NULL),
('groups.ids.permission.tradeban', '', NULL),
('groups.ids.permission.unacceptable', '', NULL),
('groups.ids.permission.unban', '', NULL),
('groups.ids.permission.unmute', '', NULL),
('guide.completion.minutes', '0', NULL),
('guide.search.timeout.minutes', '5', NULL),
('guides.group.id', 'Set the ID of Habbo Guides group', 'server'),
('habbo.experts.group.id', 'Set the ID of Habbo eXperts group', 'server'),
('happy.hour.weekday.end', '18:00:00', NULL),
('happy.hour.weekday.start', '17:00:00', NULL),
('happy.hour.weekend.end', '13:00:00', NULL),
('happy.hour.weekend.start', '12:00:00', NULL),
('homepage.template.file', 'Set the homepage template file', 'site'),
('hot.groups.community.limit', 'Set the hot groups community limit number', 'site'),
('hot.groups.limit', 'Set the hot groups limit number', 'site'),
('hotel.check.online', 'Set hotel check online as true or false', 'server'),
('loader.dcr', 'Set the URL of the Habbo.dcr of the Schockwave client', 'loader'),
('loader.dcr.http', 'Set the URL of the Habbo.dcr without SSL of the Schockwave client', 'loader'),
('loader.external.texts', 'Set the URL of the external_texts.txt of the Schockwave client', 'loader'),
('loader.external.texts.http', 'Set the URL of the external_texts.txt without SSL of the Schockwave client', 'loader'),
('loader.external.variables', 'Set the URL of the external_variables.txt of the Schockwave client', 'loader'),
('loader.external.variables.http', 'Set the URL of the external_variables.txt without SSL of the Schockwave client', 'loader'),
('loader.flash.base', 'Set the URL of the Flash client base', 'loader'),
('loader.flash.beta.base', 'Set the URL of the Flash BETA client base', 'loader'),
('loader.flash.beta.external.flash.texts', 'Set the URL of the external_flash_texts.txt of the Flash BETA client', 'loader'),
('loader.flash.beta.external.variables', 'Set the URL of the external_variables.txt of the Flash BETA client', 'loader'),
('loader.flash.beta.swf', 'Set the URL of the Habbo.swf of the Flash BETA client', 'loader'),
('loader.flash.external.texts', 'Set the URL of the external_texts.txt of the Flash client', 'loader'),
('loader.flash.external.variables', 'Set the URL of the external_variables.txt of the Flash client', 'loader'),
('loader.flash.external.variables.exe', 'Set the URL of the external_variables.txt of the Flash client from app', 'loader'),
('loader.flash.port', 'Set the port of the Flash clients', 'host'),
('loader.flash.swf', 'Set the URL of the Habbo.swf of the Flash client', 'loader'),
('loader.game.ip', 'Set the IP or host of all clients', 'host'),
('loader.game.port', 'Set the port of the Shockwave client', 'host'),
('loader.mus.ip', 'Set the MUS IP of all clients', 'host'),
('loader.mus.port', 'Set the MUS port of all clients', 'host'),
('maintenance', 'Set maintenance status as true or false', 'maintenance'),
('max.connections.per.ip', 'Set the max connections per IP', 'server'),
('max.tags.groups', 'Set the max number of tags groups', 'server'),
('max.tags.users', 'Set the max number of tags users', 'server'),
('messenger.enable.official.update.speed', 'Set the messenger official update speed status as true or false', 'client'),
('messenger.max.friends.club', 'Set the number of the messenger max friends for Habbo Club member', 'client'),
('messenger.max.friends.nonclub', 'Set the number of the messenger max friends for non Habbo Club member', 'client'),
('navigator.hide.empty.public.categories', 'Set the navigator hide empty public categories status as true or false', 'client'),
('navigator.show.hidden.rooms', 'Set the navigator show hidden rooms status as true or false', 'client'),
('normalise.input.strings', 'Normalise input strings', 'miscellaneous'),
('pixels.max.tries.single.room.instance', 'Set the pixels max tries single room instance', 'coins'),
('pixels.received.interval', 'Set the pixels received interval', 'coins'),
('pixels.received.timeunit', 'Set the pixels received timeunit', 'coins'),
('players.all.time.peak', '4', NULL),
('players.daily.peak', '1', NULL),
('players.daily.peak.date', '16-10-2023', NULL),
('players.online', '1', NULL),
('profile.editing', 'Profile editing', 'miscellaneous'),
('rare.cycle.page.id', 'Set the ID of the catalogue Rare cycle page', 'catalogue'),
('regenerate.map.enabled', 'true', NULL),
('regenerate.map.interval', '1', NULL),
('registration.disabled', 'Set registration status as true or false', 'site'),
('reset.sso.after.login', 'true', NULL),
('reward.credits.loser.range', '0-4', NULL),
('reward.credits.winner.range', '10-20', NULL),
('roller.tick.default', '2000', NULL),
('room.ads', 'true', NULL),
('room.dispose.timer.enabled', 'true', NULL),
('room.dispose.timer.seconds', '30', NULL),
('room.intersitial.ads', 'Set room intersitial ads status as true or false', 'client'),
('seasonal.items', 'false', NULL),
('shutdown.minutes', 'Set the shutdown time in minutes', 'client'),
('site.housekeeping', 'Set the URL of Housekeeping', 'hotel'),
('site.imaging.endpoint', 'Set the URL of the imaging endpoint', 'hotel'),
('site.imaging.endpoint.timeout', 'Set the imaging endpoint timeout in seconds', 'hotel'),
('site.imaging.path', 'Set the URL of imaging path', 'hotel'),
('site.imaging.timeout', 'Set the imaging timeout in seconds', 'hotel'),
('site.name', 'Set the Hotel name', 'hotel'),
('site.path', 'Set the URL of main path (of the Hotel)', 'hotel'),
('sleep.timer.seconds', 'Set the sleep timer in seconds', 'client'),
('snowstorm.create.game.enabled', 'Toggle can create game of SnowStorm as true or false', 'games'),
('snowstorm.game.lifetime.seconds', 'Set the SnowStorm game lifetime in seconds', 'games'),
('snowstorm.increase.points', 'Toggle increase points of SnowStorm game as true or false', 'games'),
('snowstorm.preparing.game.seconds', 'Set the preparing game time of SnowStorm in seconds', 'games'),
('snowstorm.restart.game.seconds', 'Set the restart game time of SnowStorm in seconds', 'games'),
('snowstorm.start.minimum.active.teams', 'Set the minimum teams needed to start a game of SnowStorm in seconds', 'games'),
('snowstorm.ticket.charge', 'Set the ticket charge of SnowStorm', 'games'),
('stack.height.limit', 'Set the number of stack height limit', 'client'),
('static.content.path', 'Set the URL of the static content path', 'hotel'),
('talk.bubble.timeout.seconds', 'Set the talk bubble timeout in seconds', 'client'),
('talk.garbled.text', 'Set the talk garbled text status as true or false', 'client'),
('trade.email.verification', 'Set the trade email verification status as true or false', 'server'),
('tutorial.enabled', 'Set the tutorial status as true or false', 'client'),
('vouchers.enabled', 'Set the vouchers status as true or false', 'server'),
('walk.spam.count', 'Set the number of the walk spam count', 'client'),
('welcome.message.content', 'Set the welcome message inside client', 'client'),
('welcome.message.enabled', 'Set the welcome message status as true or false', 'client'),
('wordfilter.word.replacement', 'Set the wordfilter word replacement', 'server'),
('wordfitler.enabled', 'Set the wordfilter status as true or false', 'server'),
('xp.monthly.expiry', 'Set the XP monthly expiry (it will be done automatically else)', 'games');

--
-- Índices para tablas volcadas
--


--
-- Indices de la tabla `bans_reasons`
--
ALTER TABLE `bans_reasons`
  ADD PRIMARY KEY (`id`);
  
--
-- Indices de la tabla `cfh_logs`
--
ALTER TABLE `cfh_logs`
  ADD PRIMARY KEY (`cfh_id`);

--
-- Indices de la tabla `housekeeping_rcon_logs`
--
ALTER TABLE `housekeeping_rcon_logs`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `housekeeping_login_log`
--
ALTER TABLE `housekeeping_login_log`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `settings_desc`
--
ALTER TABLE `settings_desc`
  ADD PRIMARY KEY (`setting`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `bans_reasons`
--
ALTER TABLE `bans_reasons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

--
-- AUTO_INCREMENT de la tabla `cfh_logs`
--
ALTER TABLE `cfh_logs`
  MODIFY `cfh_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `housekeeping_rcon_logs`
--
ALTER TABLE `housekeeping_rcon_logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

--
-- AUTO_INCREMENT de la tabla `housekeeping_login_log`
--
ALTER TABLE `housekeeping_login_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


ALTER TABLE `settings` ADD COLUMN `category` varchar(55) DEFAULT NULL;

UPDATE `settings` SET
  `category` =
    CASE
      WHEN `setting` = 'afk.timer.seconds' THEN 'client'
      WHEN `setting` = 'alerts.gift.message' THEN NULL
      WHEN `setting` = 'april.fools' THEN 'miscellaneous'
      WHEN `setting` = 'battleball.create.game.enabled' THEN 'games'
      WHEN `setting` = 'battleball.game.lifetime.seconds' THEN 'games'
      WHEN `setting` = 'battleball.increase.points' THEN 'games'
      WHEN `setting` = 'battleball.preparing.game.seconds' THEN 'games'
      WHEN `setting` = 'battleball.restart.game.seconds' THEN 'games'
      WHEN `setting` = 'battleball.start.minimum.active.teams' THEN 'games'
      WHEN `setting` = 'battleball.ticket.charge' THEN 'games'
      WHEN `setting` = 'carry.timer.seconds' THEN 'client'
      WHEN `setting` = 'catalogue.frontpage.input.1' THEN NULL
      WHEN `setting` = 'catalogue.frontpage.input.2' THEN NULL
      WHEN `setting` = 'catalogue.frontpage.input.3' THEN NULL
      WHEN `setting` = 'catalogue.frontpage.input.4' THEN NULL
      WHEN `setting` = 'chat.spam.count' THEN 'client'
      WHEN `setting` = 'childline.group.id' THEN 'server'
      WHEN `setting` = 'club.gift.interval' THEN NULL
      WHEN `setting` = 'club.gift.present.label' THEN NULL
      WHEN `setting` = 'club.gift.timeunit' THEN NULL
      WHEN `setting` = 'collectables.page' THEN 'catalogue'
      WHEN `setting` = 'credits.scheduler.amount' THEN 'coins'
      WHEN `setting` = 'credits.scheduler.interval' THEN 'coins'
      WHEN `setting` = 'credits.scheduler.timeunit' THEN 'coins'
      WHEN `setting` = 'daily.credits.amount' THEN 'coins'
      WHEN `setting` = 'daily.credits.wait.time' THEN 'coins'
      WHEN `setting` = 'delete.chatlogs.after.x.age' THEN NULL
      WHEN `setting` = 'delete.iplogs.after.x.age' THEN NULL
      WHEN `setting` = 'delete.tradelogs.after.x.age' THEN NULL
      WHEN `setting` = 'discussions.per.page' THEN 'site'
      WHEN `setting` = 'discussions.replies.per.page' THEN 'site'
      WHEN `setting` = 'email.smtp.enable' THEN 'email'
      WHEN `setting` = 'email.smtp.from.email' THEN 'email'
      WHEN `setting` = 'email.smtp.from.name' THEN 'email'
      WHEN `setting` = 'email.smtp.host' THEN 'email'
      WHEN `setting` = 'email.smtp.login.password' THEN 'email'
      WHEN `setting` = 'email.smtp.login.username' THEN 'email'
      WHEN `setting` = 'email.smtp.port' THEN 'email'
      WHEN `setting` = 'email.static.content.path' THEN 'email'
      WHEN `setting` = 'enforce.strict.packet.policy' THEN 'miscellaneous'
      WHEN `setting` = 'events.category.count' THEN NULL
      WHEN `setting` = 'events.expiry.minutes' THEN NULL
      WHEN `setting` = 'free.month.hc.registration' THEN NULL
      WHEN `setting` = 'fuck.aaron' THEN NULL
      WHEN `setting` = 'group.default.badge' THEN NULL
      WHEN `setting` = 'group.purchase.cost' THEN NULL
      WHEN `setting` = 'groups.ids.permission.addcredits' THEN NULL
      WHEN `setting` = 'groups.ids.permission.ban' THEN NULL
      WHEN `setting` = 'groups.ids.permission.bot' THEN NULL
      WHEN `setting` = 'groups.ids.permission.checkbalance' THEN NULL
      WHEN `setting` = 'groups.ids.permission.coords' THEN NULL
      WHEN `setting` = 'groups.ids.permission.copyroom' THEN NULL
      WHEN `setting` = 'groups.ids.permission.dc' THEN NULL
      WHEN `setting` = 'groups.ids.permission.deletebadge' THEN NULL
      WHEN `setting` = 'groups.ids.permission.giftroom' THEN NULL
      WHEN `setting` = 'groups.ids.permission.givebadge' THEN NULL
      WHEN `setting` = 'groups.ids.permission.headrotate' THEN NULL
      WHEN `setting` = 'groups.ids.permission.hotelalert' THEN NULL
      WHEN `setting` = 'groups.ids.permission.itemdebug' THEN NULL
      WHEN `setting` = 'groups.ids.permission.mute' THEN NULL
      WHEN `setting` = 'groups.ids.permission.packet' THEN NULL
      WHEN `setting` = 'groups.ids.permission.permban' THEN NULL
      WHEN `setting` = 'groups.ids.permission.reload' THEN NULL
      WHEN `setting` = 'groups.ids.permission.removecredits' THEN NULL
      WHEN `setting` = 'groups.ids.permission.resetpw' THEN NULL
      WHEN `setting` = 'groups.ids.permission.roomalert' THEN NULL
      WHEN `setting` = 'groups.ids.permission.roommute' THEN NULL
      WHEN `setting` = 'groups.ids.permission.setconfig' THEN NULL
      WHEN `setting` = 'groups.ids.permission.shutdown' THEN NULL
      WHEN `setting` = 'groups.ids.permission.talk' THEN NULL
      WHEN `setting` = 'groups.ids.permission.teleport' THEN NULL
      WHEN `setting` = 'groups.ids.permission.tradeban' THEN NULL
      WHEN `setting` = 'groups.ids.permission.unacceptable' THEN NULL
      WHEN `setting` = 'groups.ids.permission.unban' THEN NULL
      WHEN `setting` = 'groups.ids.permission.unmute' THEN NULL
      WHEN `setting` = 'guide.completion.minutes' THEN NULL
      WHEN `setting` = 'guide.search.timeout.minutes' THEN NULL
      WHEN `setting` = 'guides.group.id' THEN 'server'
      WHEN `setting` = 'habbo.experts.group.id' THEN 'server'
      WHEN `setting` = 'happy.hour.weekday.end' THEN NULL
      WHEN `setting` = 'happy.hour.weekday.start' THEN NULL
      WHEN `setting` = 'happy.hour.weekend.end' THEN NULL
      WHEN `setting` = 'happy.hour.weekend.start' THEN NULL
      WHEN `setting` = 'homepage.template.file' THEN 'site'
      WHEN `setting` = 'hot.groups.community.limit' THEN 'site'
      WHEN `setting` = 'hot.groups.limit' THEN 'site'
      WHEN `setting` = 'hotel.check.online' THEN 'server'
      WHEN `setting` = 'loader.dcr' THEN 'loader'
      WHEN `setting` = 'loader.dcr.http' THEN 'loader'
      WHEN `setting` = 'loader.external.texts' THEN 'loader'
      WHEN `setting` = 'loader.external.texts.http' THEN 'loader'
      WHEN `setting` = 'loader.external.variables' THEN 'loader'
      WHEN `setting` = 'loader.external.variables.http' THEN 'loader'
      WHEN `setting` = 'loader.flash.base' THEN 'loader'
      WHEN `setting` = 'loader.flash.beta.base' THEN 'loader'
      WHEN `setting` = 'loader.flash.beta.external.flash.texts' THEN 'loader'
      WHEN `setting` = 'loader.flash.beta.external.variables' THEN 'loader'
      WHEN `setting` = 'loader.flash.beta.swf' THEN 'loader'
      WHEN `setting` = 'loader.flash.external.texts' THEN 'loader'
      WHEN `setting` = 'loader.flash.external.variables' THEN 'loader'
      WHEN `setting` = 'loader.flash.external.variables.exe' THEN 'loader'
      WHEN `setting` = 'loader.flash.port' THEN 'host'
      WHEN `setting` = 'loader.flash.swf' THEN 'loader'
      WHEN `setting` = 'loader.game.ip' THEN 'host'
      WHEN `setting` = 'loader.game.port' THEN 'host'
      WHEN `setting` = 'loader.mus.ip' THEN 'host'
      WHEN `setting` = 'loader.mus.port' THEN 'host'
      WHEN `setting` = 'maintenance' THEN 'maintenance'
      WHEN `setting` = 'max.connections.per.ip' THEN 'server'
      WHEN `setting` = 'max.tags.groups' THEN 'server'
      WHEN `setting` = 'max.tags.users' THEN 'server'
      WHEN `setting` = 'messenger.enable.official.update.speed' THEN 'client'
      WHEN `setting` = 'messenger.max.friends.club' THEN 'client'
      WHEN `setting` = 'messenger.max.friends.nonclub' THEN 'client'
      WHEN `setting` = 'navigator.hide.empty.public.categories' THEN 'client'
      WHEN `setting` = 'navigator.show.hidden.rooms' THEN 'client'
      WHEN `setting` = 'normalise.input.strings' THEN 'miscellaneous'
      WHEN `setting` = 'pixels.max.tries.single.room.instance' THEN 'coins'
      WHEN `setting` = 'pixels.received.interval' THEN 'coins'
      WHEN `setting` = 'pixels.received.timeunit' THEN 'coins'
      WHEN `setting` = 'players.all.time.peak' THEN NULL
      WHEN `setting` = 'players.daily.peak' THEN NULL
      WHEN `setting` = 'players.daily.peak.date' THEN NULL
      WHEN `setting` = 'players.online' THEN NULL
      WHEN `setting` = 'profile.editing' THEN 'miscellaneous'
      WHEN `setting` = 'rare.cycle.page.id' THEN 'catalogue'
      WHEN `setting` = 'regenerate.map.enabled' THEN NULL
      WHEN `setting` = 'regenerate.map.interval' THEN NULL
      WHEN `setting` = 'registration.disabled' THEN 'site'
      WHEN `setting` = 'reset.sso.after.login' THEN NULL
      WHEN `setting` = 'reward.credits.loser.range' THEN NULL
      WHEN `setting` = 'reward.credits.winner.range' THEN NULL
      WHEN `setting` = 'roller.tick.default' THEN NULL
      WHEN `setting` = 'room.ads' THEN NULL
      WHEN `setting` = 'room.dispose.timer.enabled' THEN NULL
      WHEN `setting` = 'room.dispose.timer.seconds' THEN NULL
      WHEN `setting` = 'room.intersitial.ads' THEN 'client'
      WHEN `setting` = 'seasonal.items' THEN NULL
      WHEN `setting` = 'shutdown.minutes' THEN 'client'
      WHEN `setting` = 'site.housekeeping' THEN 'hotel'
      WHEN `setting` = 'site.imaging.endpoint' THEN 'hotel'
      WHEN `setting` = 'site.imaging.endpoint.timeout' THEN 'hotel'
      WHEN `setting` = 'site.imaging.path' THEN 'hotel'
      WHEN `setting` = 'site.imaging.timeout' THEN 'hotel'
      WHEN `setting` = 'site.name' THEN 'hotel'
      WHEN `setting` = 'site.path' THEN 'hotel'
      WHEN `setting` = 'sleep.timer.seconds' THEN 'client'
      WHEN `setting` = 'snowstorm.create.game.enabled' THEN 'games'
      WHEN `setting` = 'snowstorm.game.lifetime.seconds' THEN 'games'
      WHEN `setting` = 'snowstorm.increase.points' THEN 'games'
      WHEN `setting` = 'snowstorm.preparing.game.seconds' THEN 'games'
      WHEN `setting` = 'snowstorm.restart.game.seconds' THEN 'games'
      WHEN `setting` = 'snowstorm.start.minimum.active.teams' THEN 'games'
      WHEN `setting` = 'snowstorm.ticket.charge' THEN 'games'
      WHEN `setting` = 'stack.height.limit' THEN 'client'
      WHEN `setting` = 'static.content.path' THEN 'hotel'
      WHEN `setting` = 'talk.bubble.timeout.seconds' THEN 'client'
      WHEN `setting` = 'talk.garbled.text' THEN 'client'
      WHEN `setting` = 'trade.email.verification' THEN 'server'
      WHEN `setting` = 'tutorial.enabled' THEN 'client'
      WHEN `setting` = 'vouchers.enabled' THEN 'server'
      WHEN `setting` = 'walk.spam.count' THEN 'client'
      WHEN `setting` = 'welcome.message.content' THEN 'client'
      WHEN `setting` = 'welcome.message.enabled' THEN 'client'
      WHEN `setting` = 'wordfilter.word.replacement' THEN 'server'
      WHEN `setting` = 'wordfitler.enabled' THEN 'server'
      WHEN `setting` = 'xp.monthly.expiry' THEN 'games'
      ELSE NULL
    END;

/*ALTER TABLE `settings` MODIFY COLUMN `category` varchar(55) NOT NULL;*/
