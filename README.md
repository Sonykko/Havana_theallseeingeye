![](https://i.imgur.com/Et9Elti.png)
# Information

Havana_theallseeingeye is a project to recreate the original "Hobbanet" or theallseeingeye Habbo Housekeeping created by Sulake. Almost all design and features of this Housekeeping it's a pixel perfect clone and reverse engineered from theallseeingeye.

This project is based on [Quackster/Havana](https://github.com/Quackster/Havana). So it have all features from Havana plus the detailed below.

Developed by Sonykko.

# Features

### Housekeeping
- Login/Session
  - Save and show last login IP from HK
  - Save and show last login time from HK
  - Housekeeping all Staff activity log system
  - Trusted Person system
- System status
  - Re-organized all config settings
  - Trusted Person access
- Statistics
  - Suspicious Staff logs
  - Housekeeping logs
  - Housekeeping login logs
- Admin tools
  - Remote alerting
  - Remote baning and kicking
  - Remote superban (original feature)
  - Remote room alerting & kicking
  - User action log
  - Remote Calls for help admin
  - Remote mass alert
  - Remote mass ban
  - Remote mass unban
  - Habbo restore tool
  - User search & information tool
  - Current furniture
  - Room admin (private rooms)
  - Room action log
  - CFH action log
  - User edit (original feature)
  - User create (original feature)
  - Wordfilter tool
  - Voucher codes tool
  - Group admin (badge editor included)
  - Hobba applications
  - Check Hobba applicant tool
  - Remote give badge (badge manager)
  - Rank manager
  - Trusted person admin
  - Stickie viewer
  - MiniMail Reports
  - Habbo Name Reports
  - Habbo Motto Reports
  - Guestbook Reports
  - Group Name Reports
  - Group Description Reports
  - Discussion Post Reports
- Campaign management
  - Ads banners tool
  - Staff picks tool (including rooms)
  - Recommended groups tool
  - Hot campaigns tool
  - Create catalogue pages
  - Manage catalogue pages
- theallseeingeye - Hobbanet design

### Website
  - Ads banners
  - Staff Picks - groups & rooms
  - Hot Campaigns
  - Habbo Hobba application form
  - MiniMail report system
  - Habbo Name report system
  - Habbo Motto report system
  - Guestbook report system
  - Group Name report system
  - Group Description report system
  - Discussion Post report system

### Client
  - Support for Dual Flash/Shockwave connection for RELEASE39-22643-22891-200911110035_07c3a2a30713fd5bea8a8caf07e33438.
  - Mod Tools
    - Room kick
      - Stop event automatically
    - Calls for help
      - Access to Housekeeping user chatlog
      - All activity log system
  - Navigator
    - Pets count for room visitors count
  - Room
    - Pets can walk in door tile

# Screenshots

![image](https://i.imgur.com/CVrMF0x.png)

![image](https://i.imgur.com/YVU36ac.png)

![image](https://i.imgur.com/BMNleoU.png)

![image](https://i.imgur.com/12lUtG1.gif)

### (theallseeingeye with new r39 dual client support)

![image](https://i.imgur.com/JfuUyxD.gif)

# Download

Download the latest development build from the [releases page](https://github.com/Sonykko/Havana_theallseeingeye/releases).

# Installation

1. Create a folder called Havana, after clone or download this repo and extract it to your new Havana folder. After run from tool folder in your DB first the havana.sql and optional the groups.sql

2. Put the Havana-Server.jar and Havana-Web.jar files downloaded in the Releases page in the root folder and after, run it up. When they go ready, close it

3. Run the theallseeingeye.sql from tool folder in your DB in order to do work properly this project

4. Download the [havana_www_10_09_2024.7z](https://www.mediafire.com/file/xzjfsvb3k2962xo/havana_www_10_09_2024.7z/file) file, and then extract it to /tools/www/ and move and replace the www folder from Git repro/root folder to your Havana tool path

5. Go to all your external_texts.txt files from v31 and search for this var:
```html
chatlog.url
```
And remplace with this:
```html
chatlog.url=https://example.com/ase/habbo/es/housekeeping/extra/hobba/chatlog.action?chatId=
```

### Important for Linux users

Install the font manager, to enable the captcha to work on the website.

```
apt-get install font-manager
```

## License

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.