# db-interface
An interface to various databases, defining schemas and such

# couchdb tips
## User addition

curl -X PUT http://vedavaapi.org:5984/_users/org.couchdb.user:abc \
     -H "Accept: application/json" \
     -H "Content-Type: application/json" \
     -d '{"name": "abc", "password": "something", "roles": [], "type": "user"}'
     
## Replication url
http://username:pass@vedavaapi.org:5984/demo