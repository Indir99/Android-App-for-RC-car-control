## Android App for RC cars - with SQLite (Arduino/STM32 based)

### Android Application

We have created a simple application to communicate with the hardware using the Bluetooth communication protocol.
Application has following layouts:
1. Login Page
2. Register Page
3. Connect Page
4. Control page

### Login Page
The layout is shown below:
<p align="center">
<img src="https://github.com/Indir99/Android-App-for-RC-car-control/blob/master/images/Login-page.jpg?raw=true" width="300" height="600" />
</[>

First, you have to login.
It is necessary to enter the username and password of one of the users that are in the local database.
If you do not have a user in the database, you need to add one by clicking on the register button.
If you have a user in the database and you know his username and password, click on the Login button.

### Register Page
The layout is shown below:
<p align="center">
<img src="https://github.com/Indir99/Android-App-for-RC-car-control/blob/master/images/Register-page.jpg?raw=true" width="300" height="600" />
</[>

Fill in the specified fields and click the Add user button. After this operation, the user is added to the local database. 
Checking whether the username is taken and the password check is not implemented (simple implementation).


### Connect Page
The layout is shown below:
<p align="center">
<img src="https://github.com/Indir99/Android-App-for-RC-car-control/blob/master/images/Conncect-page.jpg?raw=true" width="300" height="600" />
</[>

1. BLUETOOTH ON -> Enable bluetooth
2. BLUETOOTH OFF -> Disable bluetooth
3. GET PAIRED DEVICES -> Scanning paired devices and trying to find HC-05/HC-06 (hardcoded for these two modules)
4. CONNECT TO HC MODULE -> Connecting (it will take a while)

### Control Page
The layout is shown below:
<p align="center">
<img src="https://github.com/Indir99/Android-App-for-RC-car-control/blob/master/images/Control-page.jpg?raw=true" width="600" height="300" />
</[>

By holding down one of the buttons, you move the RC car.

### SQLite Database
<p align="center">
<img src="https://github.com/Indir99/Android-App-for-RC-car-control/blob/master/images/sqlite-db.png?raw=true" width="600" height="250" />
</[>

For the realization of this project, we added a small database and tested the functionality of each user. 
To access this SQLite database we used: DB Browser for SQLite.

### A detailed explanation of how the application work

Feel free to contact me for a detailed explanation. Also, in the following github repository you can see how the RC car is implemented:

```
link:
```
