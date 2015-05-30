# Memeify

### TEAM SCAR
Sufei, Charlyn, Alvin, Reinard


### Tasks
* portrait orientation
* Handle vanilla meme format (toggle button)
* Handle demotivational poster format (toggle button)
* Intent for camera -> user take/retake -> jump back to app
* Intent for camera roll -> jumo back to app
* save meme to camera roll
* share meme on social media (implicit intent - android does it)
* layout/style sheet
* savedinstancestate

### Views
* Home Screen - Do you wanna use camera or camera roll? 
* Choose which meme you want (toggle?)
* Text editing view 
* Share on social media

### Permissions
* write external storage [android.permission.WRITE_EXTERNAL_STORAGE]
* read external storage [android.permission.READ_EXTERNAL_STORAGE]
* camera [android.permission.CAMERA]

---
---

### Meme-ify Me

#### Objective

The goal of this assignment is to work on project workflow and collaboration.

#### Instructions

You will work in teams of four, which should be sufficiently difficult to manage.
Each team member should be responsible for building a feature (or several) all on their own. The team should also
figure out how to dynamically reallocate workload based on issues that come up.

Before merging features to the master branch, each team member is required to be code-reviewed by two other team
members. Each team member is required to code review at least one other team member. If a code review does not result
in any constructive feedback then a third code review is required.

#### Requirements

You'll be creating an Android app to add captions onto an image. There are two types of memes this should support:
* [Vanilla memes](http://www.quickmeme.com/img/21/21c71509584aaf9f6576b8aeb80ad0d5afa6114e6da2c79e3b0d1808c948b6e7.jpg): That is, an image with text overlayed on the top/middle/bottom (any combination of those three).
* [Demotivational posters](http://www.marcofolio.net/images/stories/fun/imagedump/demotivational_posters/simplicity.jpg): An image with a black border, and a caption that includes larger text followed by
smaller text.

Users should be able to:
* Take a picture (i.e. open the camera) and create a meme.
* Use a picture from the camera roll to create a meme.
* Save a meme to the camera roll.
* Share the meme using other social apps (such as email or twitter).

Your code is expected to be well-commented and to eliminate redundancies. There should be a consistent look
and feel across your app, as well as across your code.

#### Bonus features

* Create a style for consistent look and feel across the app.
* Create unit tests for another team member's features.
* Allow users to select a font and size for the text.
* Give users templates for existing memes.

#### Submission

Submit a link to your team's repository as a pull request on this file. In your README, include your team name,
how many hours you believe each of you worked on this over the course of the week, who was responsible for what
features, and general project navigation.

If a particular team member functioned as project leader, mention that. If a particular team member was responsible
for design or for something that transcended feature-building, mention that as well. We will also be sending out a
Google form to collect more information on the teamwork component of this project.
