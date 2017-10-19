# Keystore

Keystore aims to provide an web interface in order to manage passwords and secret keys. 

## User and roles

A user should be able to log in the application
He is also able to change its own information
He should be able to ask for reseting his password
He is created by an admin who is authorized to attach him several roles

A same user can be reader for a given perimeter and contributor for another.
When a user want "reader" or "manager" roles, he has to ask the manager for being granted.

### Reader

The reader role authorize access to view and copy some secret keys

### Manager

A manager is responsible of secret key group.
A manager is able to grant any user for any group he manages.
He is able to grant read roles but also manager roles to other users.
He is notified when a user asks for a roles on a group he manages and can accept or refuse.

### Admin

An admin is responsible of creating and removing users.
An admin is manager for all groups and inherit their rights.