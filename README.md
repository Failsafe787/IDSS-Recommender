# IDSS-Recommender
A Java implementation of a simple recommendation system for different problems made with Smile APIs by BayesFusion Inc.

## About this project
This is a university project I've developed for the exam of Intelligent Decision Support Systems

## State

### Current version
Version 0.1.0 - Wednesday, April 4, 2018

Made with Eclipse Oxygen.2 and Java 9

### Functionalities
- [x] Ability to solve static models created with GeNIe
- [x] Ability to solve temporal models created with GeNIe
- [x] Sample networks

### Before starting
Due to license issues I cannot upload all the files required for running the Resolver. To make this project working you need:
* Apogeo jbook Input class, available at http://www.apogeonline.com/2011/libri/9788850330690/allegati/pubblic/3069_classeJbookutilinput.zip
You need to insert Input.java class inside utils package of this java project
* Smile Java wrapper, available at https://www.bayesfusion.com. If you're an academic student, you can download an Academic version of this
library at https://download.bayesfusion.com/files.html?category=Academia.
You need to insert jsmile.dll and smile.jar inside the lib folder and then add them to the Referenced Libraries of the project

## Samples
Two samples are already included in this release and ready to use after the pre-configuration. One regarding a Medical Operation (Static Model) 
and the other a robot that always follow a target. For the whole description of each problem, please refer to the pdf inside the docs folder (in Italian).
