# hello
[![Build Status](https://travis-ci.com/SerhiiIvko/hello.svg?branch=master)](https://travis-ci.com/SerhiiIvko/hello)

This web-application use address bar for filtering contacts from database.

It use MySQL database in main case and H2 in test case.

For deploying used Tomcat servlet container, in application used Jersey Servlet Library.

During deploying perhaps error, if regular expression in parameter "nameFilter" contain character '^' and some
other. For resolving this problem necessary find file server.xml in Tomcat configuration directory and replace
tag <Connector ... /> on this configuration:
port="8080" protocol="HTTP/1.1"
URIEncoding="UTF-8"
connectionTimeout="20000"
redirectPort="8443"
relaxedQueryChars='^{}[]|$*\'

Database configuration is written in DBManager.java, it can be changed for use on other config data.

Before deploying application use scripts from resources: schema.sql and data.sql for create and fill database
and table, if it doesn't exists.