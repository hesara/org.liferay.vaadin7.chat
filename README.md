Liferay 7 Vaadin 7 Chat example
===========================================

Overview
--------
This example has been create to be used at Vaadin cruise at my Vaadin cruise presentation

https://vaadin.com/meetup/jfokus-2016

Requirements:
-------------
- Java 8 and Maven
- Liferay Portal 7 (latest)
- Install https://github.com/sammso/org.liferay.vaadin7.compatibilitypack to your Lifieray 7

To compile:
-----------

~~~
mvn package
~~~

Deploy:
-----------

portal-ext.properties

~~~
vaadin.resources.path=/o/vaadin7
~~~

if you are servicing Vaadin resources from resources bundle.

~~~
vaadin.resources.path=/o/vaadin7
~~~

~~~
cp org.liferay.vaadin7.compatibilitypack/target/org.liferay.vaadin7.compatibilitypack.distribution-<version>.lpkg -d <replace-this-to-your-liferay7-home>/deploy
~~~