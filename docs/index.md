Keystore aims to provide an web interface in order to manage passwords and secret keys.<br>

## User and roles

A user should be able to log in the application.<br>
He is also able to change his own information.<br>
He should be able to ask for resetting his password.<br>
He is created by an admin who is authorized to attach him several roles.<br>

A same user can be reader for a given perimeter and contributor for another.<br>
When a user want "reader" or "manager" roles, he has to ask the manager for being granted.<br>

### Reader

The reader is authorized to view and copy some secret keys.<br>

### Manager

A manager is responsible of secret key group.<br>
He is able to grant any user for any group he manages.<br>
He is able to grant read roles but also manager roles to other users.<br>
He is notified when a user asks for a roles on a group he manages and can accept or refuse.<br>

### Admin

An admin is responsible of creating and removing users.<br>
An admin is manager for all groups and inherit their rights.<br>

## Secret perimeter

Defining a secret consists in setting a password or a secret value to an object and to attach this object to a group.<br>
Groups and secret objects can be built as a tree.<br>
Each group node can be granted for being read or managed.<br>

````
root
|
|- group_1
|  |- secret_10
|
|  |- group_12
|    |- secret_120
|    |- secret_121
|    |- group_123
|       |- secret_1230
|
|  |- group_22
|    |- secret_220
|    |- secret_221
````