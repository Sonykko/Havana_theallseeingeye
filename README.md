![](https://i.imgur.com/Et9Elti.png)
# Information

This is a project to recreate the supossed "Hobbanet" or theallseeingeye Habbo Housekeeping. Developed by Sonykko.

# Features

- Login/Session
  - Save and show last login IP from HK
  - Save and show last login time from HK
- Statistics
  - Housekeeping login logs
- Configurations
  - Re-organized all config settings
- Admin tools
  - Remote alerting
  - Remote baning and kicking
  - Remote superban (original feature)
  - User action log
  - Remote mass alert
  - Remote mass ban
  - Remote mass unban
  - User search & information tool
  - Current furniture
  - Room admin (private rooms)
  - Room action log
  - CFH action log
  - User edit (original feature)
  - User create (original feature)
  - Wordfilter tool
  - Voucher codes tool
- Campaign management
  - Staff picks tool (missing rooms)
  - Recommended groups tool
  - Create catalogue pages
  - Manage catalogue pages
- theallseeingeye - Hobbanet design

# Screenshots

![image](https://i.imgur.com/xHQeucy.png)
![image](https://i.imgur.com/88JJJ4v.png)
![image](https://i.imgur.com/Ro6161v.png)
![image](https://i.imgur.com/SroKyu1.gif)

# Download

Download the latest development build from the [releases page](https://github.com/Sonykko/Havana_theallseeingeye/releases).

# Installation

1. Run first the Havana-Server.jar and Havana-Web.jar files. When they go ready, close it

2. Run the theallseeingeye.sql from root folder of Git repro in your DB in order to do work properly this project

3. Insert this SQL if you want a custom path for Housekeeping:
```sql
INSERT INTO `settings` (`setting`, `value`, `category`)
VALUES ('site.housekeeping', 'http://localhost/', 'hotel');
```

4. Go to your tpl folder and go to /base/header.tpl and edit the URL of the Housekeeping with this:
```html
{{ site.sitePath }}/ase/housekeeping/es <!-- use this for no custom HK folder -->
{{ site.housekeeping }}/ase/housekeeping/es <!-- use this for custom HK folder -->
```

5. Move the www and www_tpl folder from Git repro to your Havana tool path

#

![](https://i.imgur.com/alAG9uW.png)
# Information
Havana is an emulator developed by [Quackster/Havana](https://github.com/Quackster/Havana) - built for V31 Shockwave from 2009 - with the goal of recreating that era of Habbo. ClassicHabbo was the primary hotel for this project, which had additonal features, the main one being support for the V39 Flash client running in tandem with the Shockwave client
Unfortunately ClassicHabbo closed at the end of 2022 for a variety of reasons (including but not limited to lack of interest in running a retro from Quackster and difficulties putting together a team of people who can help out with hotel management). Quackster thankfully released all the core Havana files for running the V31 client, minus the support for the V39 Flash client and some other CH-specific features
We've restored some of the missing CH features, with some additional features
❗ This repository will not be continuously updated and will be as is
# Features
Same as [the original project](https://github.com/Quackster/Havana), plus -
### Server
- V39 Flash client (see changelog [here](https://devbest.com/threads/dev-r39-havana-emulator-continuation-java.93385/#post-476583))
- Country badges (CH feature)
- New rare cycle (for more ease of use)
- Improvements to the catalogue (ordering, missing pictures and missing text has been fixed to be more true to 2009)
- user:Username can be used when searching for a specific owner in the navigator search tool
	![image](https://user-images.githubusercontent.com/41788480/236339922-82ccd749-6e4f-478d-95c6-d2f549ca2e1f.png)
### Website 
- Ability to change hotelview to user preference, reflected in the Shockwave client and on me page
- Discord integration to enable account verification before being able to enable trading
- API endpoints for the launcher, an all in one executable that allows launching both the Flash and Shockwave clients, when compiled totalling less than 15 MB (the source for which can be found [here](https://github.com/Palsternakka/HabboLauncher))
	![image](https://user-images.githubusercontent.com/41788480/236340297-431661f9-475f-4dc0-96b1-b683878f78da.png)
# Getting Started
### Requirements
Same as [the original project](https://github.com/quackster/Havana#requirements), plus -
- Minerva, required for Habbo avatars and badges to work on the site. You can find the latest version [here](https://github.com/quackster/Minerva#download)
- Chroma, required for furni rendering on the Collectables page to work. You can build it for yourself using the source [here](https://github.com/quackster/Chroma)
❗ The havana.sql file included in this fork of Havana is different to the one in Quacker's original, as well as the www folder contents which can be found [here](https://www.mediafire.com/file/0w0lsy335vyrh1w/www.zip/file)
### Summary

1. Clone the repository
2. Download [www.zip](https://www.mediafire.com/file/0w0lsy335vyrh1w/www.zip/file) and copy the contents to `tools/www`
3. Import havana.sql
4. Build and run Havana-Web and Havana-Server in your favourite IDE

Without any changes, Havana will run under localhost/127.0.0.1 and should work out of the box. You can log in using username `User` and password `password`. You will also be able to log in using the launcher [here](https://github.com/Palsternakka/HabboLauncher)
### Beginner's guide
#### Prerequisites
- Visual Studio Code
- Extension Pack for Java
- JDK 11
- Git
- MariaDB Server
#### Steps
1. Download and install all of the prerequisites. Make a note of the root password you set for MariaDB
2. Open a Command Prompt window and enter the following command to clone this repository -
```
git clone https://github.com/JeppsonDev/HavanaR39.git
```
3. Download [www.zip](https://www.mediafire.com/file/0w0lsy335vyrh1w/www.zip/file) and extract it to `tools\www` in the Havana directory
4. Open MySQL Client (will be a Start Menu entry) and enter your root password
5. Enter the below command to create the `havana` database -
```
CREATE DATABASE havana;
```
6. Open a MariaDB Command Prompt (will be a Start Menu entry)
7. Change directory to `tools` using the `cd` command, for example
```
cd C:\Users\User\Havana\tools
```
8. Enter the below command and enter your root password when prompted, this will import necessary tables and data -
```
mysql -u root -p havana < havana.sql
```
9. Open the Havana folder in Visual Studio Code, when prompted select Yes, I trust the authors
10. After a few minutes, the Java Projects will successfully open. You should see the Java Projects on the left hand pane underneath Explorer
11. Click the Debug button next to Havana-Web, this will create a webserver-config.ini file - edit this file and change `mysql.username` to `root` and `mysql.password` to your root password
12. Click the Debug button next to Havana-Server, this will create a server.ini file - edit this file and change `mysql.username` to `root` and `mysql.password` to your root password, as well as `server.bind` to `127.0.0.1`
13. Click the Debug button next to both to run the web and server together
14. Navigate to http://localhost in your browser
15. Log in using the username `User` and the password `password` - using [this browser](https://forum.ragezone.com/threads/portable-browser-with-flash-shockwave-basilisk.1192727/) you should be able to enter both the Flash and Shockwave clients. You will also be able to log in using the launcher [here](https://github.com/Palsternakka/HabboLauncher)
Additionally, you will need [Minerva](https://github.com/quackster/Minerva#download) mentioned above for badges and avatars to work on the site and [Chroma](https://github.com/quackster/Chroma) for furni rendering on the Collectables page, however this is not required.
## Credits
- Jeppe#9821 (Main contributor)
- Parsnip#5170 (Contributor)
- Sefhriloff#0878 (Help with certain packet structures and information relay)
- santi13#3433 (Help with certain packet structures and information relay and beta testing)
- miichi#9867 (Beta testing)
- oyasumi#7375 (Beta testing)
- Jerimu // Jeroen#5019 (Beta testing)
## License
This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
