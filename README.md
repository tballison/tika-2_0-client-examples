Test-Repo for using Tika 2.0 programmatically
=======

Get Tika
--------

`git clone https://github.com/apache/tika.git my_tika`

Build Tika
----
`cd my_tika`

`mvn clean install`
or maybe `mvn clean install -pl tika-parsers-classic-package -am` (to build only the classic parser package module)

Use Tika
-----
See the imports in pom.xml