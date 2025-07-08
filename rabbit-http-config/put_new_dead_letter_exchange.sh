#!/bin/bash

echo Put policy message-ttl

curl -u document-check:document-check -XPUT -H "content-type:application/json" --url http://localhost:15672/api/policies/document-check-host/document-check-dead-letter-exchange-new-policy -d@dead-letter-exchange-policy-config.json -i