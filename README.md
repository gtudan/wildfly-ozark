[![Build Status](https://travis-ci.org/gtudan/wildfly-ozark.svg?branch=master)](https://travis-ci.org/gtudan/wildfly-ozark)

# About this project

This module adds support for the MVC 1.0 spec to wildfly >= 10.x by installing the 
reference implementation "Ozark".

# Installation

## Installing the Module

Unpack the distribution (you can find the latest binaries on the [release page](https://github.com/gtudan/wildfly-ozark/releases)) to the root of your wildfly installation. This will create
a new folder `ozark` in `modules/system/addons/`.

## Activating the subsystem

You can use the jboss-cli to activate the subsystem using these commands:

```
/extension=org.mvc-spec.ozark.wildfly:add(module=org.mvc-spec.ozark.wildfly)
/subsystem=mvc:add()
```

Or you can directly edit the config file (standalone.xml or domain.xml).

Add this inside the  `extensions` tag at the top of the file:
```xml
<extension module="org.mvc-spec.ozark.wildfly"/>
```

Then add the subsystem the profile your using:

```xml
<subsystem xmlns="urn:org.mvc-spec.ozark:mvc:1.0"/>
```

Finally you need to restart the server.

# Using MVC 1.0 in your deployment

The subsystem will automatically add the necessary dependencies to your deployment
if it defects a `@Controller`-annotated class.

All you need is the MVC-API. If you are using Maven, you can add it like this:

```xml
<dependencies>
    <!-- ... -->
    <dependency>
        <groupId>javax.mvc</groupId>
        <artifactId>javax.mvc-api</artifactId>
        <version>1.0-pr</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

The API-Package does not depend on the JAX-RS-API, so you will need to add this as well
if you are not using the full Java-EE 7 API package.
