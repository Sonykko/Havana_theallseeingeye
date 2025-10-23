![](https://i.imgur.com/Et9Elti.png)
# Information

Havana_theallseeingeye is a project to recreate the original "Hobbanet" or theallseeingeye Habbo Housekeeping created by Sulake. Almost all design and features of this Housekeeping it's a pixel perfect clone and reverse engineered from theallseeingeye.

This project is based on [jeppe9821/HavanaR39](https://github.com/jeppe9821/HavanaR39). So it have all features from HavanaR39 plus the detailed below.

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
        - Room kick
            - Stop event automatically
        - Room alert
        - Private rooms
            - Change room lock to doorbell
            - Set room as 'Inappropriate to hotel management'
    - User action log
        - Room chatlogs
        - Messenger chatlogs
    - Remote Calls for help admin
        - Pick CFH
        - Reply CFH
        - Block CFH
        - Follow CFH room
    - Remote mass alert
    - Remote mass ban
        - Mass kick
        - Mass ban
    - Remote mass unban
    - Habbo restore tool
        - Mass restore
        - One user only restore
            - User email changer
        - SMTP email output
    - User search & information tool
    - Current furniture
    - Room admin (private rooms)
      - Search rooms (including filter by owner name)
      - Edit/manage rooms
      - Copy/clone rooms
    - Room action log
    - CFH action log
    - User edit (original feature)
    - User create (original feature)
    - Wordfilter tool
        - Create word
        - Search word
        - Edit word
        - Delete word
    - Voucher codes tool
        - Create voucher
            - Credit voucher
            - Item voucher
        - Search voucher
        - Delete voucher
    - Group admin (badge editor included)
    - Hobba applications
    - Check Hobba applicant tool
    - Remote give badge (badge manager)
    - Rank manager
        - Give rank
        - Staff info list
        - Staff var admin
    - Trusted person admin
        - Give trusted person
        - Remove trusted person
        - All trusted person activity log
    - Stickie viewer
        - Archive Stickie Notes
        - Delete Stickie Notes
        - Ban Stickie Notes owners
    - MiniMail Reports
        - Archive reports
        - Delete reports
        - Ban MiniMail complaint owner
    - Habbo Name Reports
        - Archive reports
        - Delete reports
        - Ban Habbo reported
    - Habbo Motto Reports
        - Archive reports
        - Delete reports
        - Reemplace Habbo Motto
    - Guestbook Reports
        - Archive reports
        - Delete reports
        - Reemplace Guestbook entry
    - Group Name Reports
        - Archive reports
        - Delete reports
        - Reemplace Group Name
    - Group Description Reports
        - Archive report
        - Delete reports
        - Reemplace Group Description
    - Discussion Post Reports
        - Archive report
        - Delete reports
        - Reemplace Discussion Post
    - CFH topics tool
        - Create topic
        - Search topic
        - Delete topic
        - Save topic
- Campaign management
    - Ads banners tool
        - Create Ads banners
        - Edit Ads banners
        - Delete Ads banners
    - Staff picks tool (including rooms)
        - Create Staff picks
        - Edit Staff picks
        - Delete Staff picks
    - Recommended groups tool
        - Create Recommended groups
        - Edit Recommended groups
        - Delete Recommended groups
    - Hot campaigns tool
        - Create Hot campaigns
        - Edit Hot campaigns
        - Delete Hot campaigns
    - Create catalogue pages
    - Manage catalogue pages
        - Edit pages
        - Copy pages
        - Delete pages
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
  - Commands
    - Staff
      - Alert user
      - Transfer user
      - Superban user
      - Kick user
      - Softkick user
      - Super pull user
      - Send user to room
      - Massteleport room
  - User
    - Mimic/copy look user
    - Push user
    - Pull user
    - Follow user
    - Go to room
  - Mod Tools
    - Room kick
      - Stop event automatically
    - Calls for help
      - Access to Housekeeping user chatlog
      - All activity log system
  - Navigator
    - Pets count for room visitors count

# Screenshots

![image](https://i.imgur.com/CVrMF0x.png)

![image](https://i.imgur.com/YVU36ac.png)

![image](https://i.imgur.com/BMNleoU.png)

![image](https://i.imgur.com/12lUtG1.gif)

# Download

Download the latest development build from the [releases page](https://github.com/Sonykko/Havana_theallseeingeye/releases).

# Installation

1. Create a folder called Havana, after clone or download this repo and extract it to your new Havana folder. After run from tool folder in your DB first the havana.sql and optional the groups.sql

2. Compile the Havana-Server and Havana-Web sources and put the .jar files in the root folder and after, run it up. When they go ready, close it

3. Run the theallseeingeye.sql from tool folder in your DB in order to do work properly this project

4. Download the [www.zip](https://www.mediafire.com/file/0w0lsy335vyrh1w/www.zip/file) file, and then extract it to /tools/www/ and move and replace the www folder from Git repro/root folder to your Havana tool path

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