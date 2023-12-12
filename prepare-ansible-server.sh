#!/usr/bin/env bash

sudo su

apt-get update
apt-get install ansible -y
apt-get install python3-pip -y
pip3 install boto3 botocore