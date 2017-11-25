# Intro

[![Build Status](https://travis-ci.org/sanskrit-coders/db-interface.svg?branch=master)](https://travis-ci.org/sanskrit-coders/db-interface)
An interface to various data stores, defining common scala case classes corresponding to JSON objects and XML nodes, with helpful methods to (de)serialize such objects.

# Deployment:
- Use sbt command `release` to publish to maven repos.
- You should be able to use it roughly immediately; and after many hours you should see at maven repo listings [here](https://mvnrepository.com/artifact/com.github.sanskrit-coders). 

# couchdb tips
## User addition

curl -X PUT http://vedavaapi.org:5984/_users/org.couchdb.user:abc \
     -H "Accept: application/json" \
     -H "Content-Type: application/json" \
     -d '{"name": "abc", "password": "something", "roles": [], "type": "user"}'
     
## Replication url
* http://username:pass@vedavaapi.org:5984/demo
* http://sanskrit-coders:pass@vedavaapi.org:5984/dict_entries