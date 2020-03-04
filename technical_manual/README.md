<div align="center">

# TastyTravel User Manual

![TastyTravel logo](images/96.png)


## Design Document Highlighting The Initial Design And Current Design.

### <em>Version 1 • March 2020</em>
<br/><br/>
<br/><br/>
<br/><br/>
<br/><br/>
**Michael Savage** - michaelsavage7@mail.dcu.ie  
•  
**Gerard Slowey** - gerard.slowey2@mail.dcu.ie  
---
</div>

<div align="center">
<br/><br/>
<br/><br/>

# Table Of Contents 
<div align="left"> 1.</div>                                                             [Introduction](#introduction)
<div align="left"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.1.</div>                       [Overview](#overview) 
<div align="left"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.2.</div>                       [Glossary](#glossary)  
<div align="left"> 2.</div>                                                             [System Architecture](#architecture)
<div align="left"> 3.</div>                                                             [High Level Design](#high-level)  
<div align="left"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.</div>                       [Sequence Diagram](#sequence-diagram)  
<div align="left"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2.</div>                       [Data Flow Diagram](#data-flow-diagram)  
<div align="left"> 4.</div>                                                             [Problems and Resolutions](#problems-resolutions)  
<div align="left"> 5.</div>                                                             [Installation Guide](#install)

<br/><br/>
<br/><br/>
---
</div>
<div align="justify"> 

<a name="introduction"></a>
# 1. Introduction  

<a name="overview"></a>
## 1.1. Overview 

TastyTravel is an Android app built with Android Studio 3.6.1.
The app allows one person to search for places for two parties to meet and the given results depend on their chosen mode of transport. 
The aim of the app is to return locations on an interactive Google Map that each party can arrive at roughly at the same time. 
They can choose to search for either Bars, Cafes, or Restaurants.

 
When users of the app successfully search using all the parameters, they can look through at a list of 20 places or less. 
If the user has created an account before searching, their searches will be stored in our Firebase cloud storage. 
They can find and delete their search history in the app. We also use Firebase’s Realtime Database to let users save 
places that they like and they can find and delete their saved places in the app too.

 
The app finds recommended locations by using Mapbox isochrones and midpoint interpolaters. When the user presses search, 
the app builds a URL that takes in the mode of transport (Walk, Car, or Bike) and requests the JSONObject from MapBox. 
The JSONObject contains information on the properties and geometry of the isochrone, but most importantly we parse the 
coordinates from the URL into a list and use our algorithm to find suitable meeting places. By default, the app does 
not draw the isochrone onto the app but a toggle can be switched on. This switch will add a Google GeoJsonLayer to the 
map using the coordinates list. 
<br></br>
<br></br>
<a name="glossary"></a>
## 1.2. Glossary

**Android Studio** - Android Studio is the official integrated development environment for Google's Android operating system, 
built on JetBrains' IntelliJ IDEA software and designed specifically for Android development. We are using version 3.6.1.  
**Firebase** - Google’s mobile application development platform that helps you build, improve, and grow your app using the cloud. 
It includes services like analytics, authentication, databases, configuration, file storage, and push messaging. We are using The Firebase Realtime Database. 
It is a cloud-hosted NoSQL database that lets you store and sync data between your users in realtime.  
**Isochrones** - A number of lines on a map connecting coordinates relating to the same time or equal times.  
**Mapbox** - An open source mapping platform for custom designed maps that contains many APIs and SDKs. Mapbox Studio is like Photoshop, for maps.  
<br></br>
<br></br>
<a name="architecture"></a>
# 2. System Architecture
In this section we will show the shared understanding that we have of the system design.
<br></br>
<br></br>
<a name="high-level"></a>
# 3. High Level Design
<a name="sequence-diagram"></a>
## 3.1. Sequence Diagram
<br></br>
<br></br>
<a name="data-flow-diagram"></a>
## 3.2. Data Flow Diagram
<br></br>
<br></br>
<a name="problems-resolutions"></a>
# 4. Problems and Resolutions
<br></br>
<br></br>
<a name="install"></a>
# 5. Installation Guide
<br></br>
<br></br>