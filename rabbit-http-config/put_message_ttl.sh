#!/bin/bash

echo Put policy message-ttl

curl -u document-check:document-check -XPUT -H "content-type:application/json" --url http://localhost:15672/api/policies/document-check-host/document-check-queue.dlq.message-ttl -d@ttl-policy-config.json -i