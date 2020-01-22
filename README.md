Start-up instructions
=====================

To get started you will need to run these commands in your terminal.

**New to Git? [Learn the basic Git commands](https://confluence.atlassian.com/bitbucketserver057/basic-git-commands-945543411.html?utm_campaign=in-app-help&utm_medium=in-app-help&utm_source=stash)** \
**Configure Git for the first time** - Example user: Noctis Lucis Caelum

*git config --global user.name "Noctis Lucis Caelum"* \
*git config --global user.email "Noctis.LucisCaelum@uantwerpen.be"*

**If you want to simply clone this empty repository then run this command in your terminal.**

*git clone https://Noctis.LucisCaelum%40uantwerpen.be@imaginelab.club/se/bitbucket/scm/g3y1920/labplanner.git*

**If you already have code ready to be pushed to this repository then run this in your terminal.**

*cd existing-project* \
*git init* \
*git add --all* \
*git commit -m "Initial Commit"* \
*git remote add origin https://Noctis.LucisCaelum%40uantwerpen.be@imaginelab.club/se/bitbucket/scm/g3y1920/imagineframe.git* \
*git push -u origin master*

**If your code is already tracked by Git then set this repository as your "origin" to push to.**

*cd existing-project* \
*git remote set-url origin https://Noctis.LucisCaelum%40uantwerpen.be@imaginelab.club/se/bitbucket/scm/g3y1920/imagineframe.git* \
*git push -u origin --all* \
*git push origin --tags*
