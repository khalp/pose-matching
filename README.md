# Pose matching game

Our goal for Hackathon 2021 was to create an app where users could use the Surface Duo to match poses!

## Game information

### Instructions

This app is intended to be used in [Dual Portrait](https://docs.microsoft.com/dual-screen/introduction#dual-screen-overview) mode only.

### Overview

In the game, users can select reference images that appear on the left screen, and then in the right screen, they are able to see themselves in a camera view. When they are ready to attempt matching the pose, they can click the **Take Photo** button to start a timer countdown.

At the end of the countdown, the reference image and camera view are both analyzed with a pose-detecting library. This library generates two skeletons with landmarks representing various joints. We then compare the angles of the four main joints (elbows, shoulders, hips, and knees) and use the angle differences to calculate a score out of 100. Depending on how many reference images were selected, they can continue to play or start a new game session.

### Modes

#### Pose selection

Users can simply start playing with the default poses we included in the app, or they can choose any reference image from their own photo library.

#### Reference image sequence

Similarly, users can simply start playing with a pre-generated sequence of default poses, create their own sequence of reference images, or play by matching one pose at a time.

## Highlights

### anything you think is cool + wanna highlight !

idk

### Figma design elements

Something that we also wanted to spend some time learning was how to use Figma! We ended up designing our app icon and different vector resources for the app using Figma, which was a great learning experience.

### Angle comparison accessibility

One issue we tried brainstorming solutions to was the accessibility of the app. We didn't want to exclude people with ......


## Tools and libraries

google lib

compose !

window manager


## If we had more time

designer haha

idk