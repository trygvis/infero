#!/bin/sh
set -x
set -e
rm -rf target/tmp
mkdir target/tmp
cd target/tmp

wget --no-check-certificate https://abeille.dev.java.net/files/documents/2703/16951/formsdesigner2_0.zip
unzip formsdesigner2_0.zip

mvn install:install-file -Dfile=abeilleforms/formsrt.jar -DgeneratePom -Dpackaging=jar -DgroupId=net.java.dev.abeille -DartifactId=formsrt -Dversion=2.0

releases="http://kenai.com/projects/guts/sources/guts-maven-repository/content/releases"
snapshots="http://kenai.com/projects/guts/sources/guts-maven-repository/content/snapshots"

version="1.03-patch-jfpoilpret"
wget -q "$releases/org/jdesktop/application/AppFramework/$version/AppFramework-$version.jar"

mvn install:install-file -Dfile=AppFramework-$version.jar -DgroupId=org.jdesktop.application -DartifactId=AppFramework -Dversion=$version -Dpackaging=jar -DgeneratePom

version="0.1-20091123.145632-6"
wget -q "$snapshots/net/guts/guts-parent/0.1-SNAPSHOT/guts-parent-$version.pom"

mvn install:install-file -Dfile=guts-parent-$version.pom -DgroupId=net.guts -DartifactId=guts-parent -Dversion=0.1-SNAPSHOT -Dpackaging=pom

version="0.1-20091123.145704-7"
wget -q "$snapshots/net/guts/guts-common/0.1-SNAPSHOT/guts-common-$version.jar"
wget -q "$snapshots/net/guts/guts-common/0.1-SNAPSHOT/guts-common-$version.pom"
wget -q "$snapshots/net/guts/guts-common/0.1-SNAPSHOT/guts-common-$version-sources.jar"

mvn install:install-file -Dfile=guts-common-$version.jar -DpomFile=guts-common-$version.pom -DgroupId=net.guts -DartifactId=guts-common -Dversion=0.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=guts-common-$version-sources.jar -DgroupId=net.guts -DartifactId=guts-common -Dversion=0.1-SNAPSHOT -Dpackaging=jar -Dclassifier=sources

version="0.1-20091123.145737-4"
wget -q "$snapshots/net/guts/guts-events/0.1-SNAPSHOT/guts-events-$version.jar"
wget -q "$snapshots/net/guts/guts-events/0.1-SNAPSHOT/guts-events-$version.pom"
wget -q "$snapshots/net/guts/guts-events/0.1-SNAPSHOT/guts-events-$version-sources.jar"

mvn install:install-file -Dfile=guts-events-$version.jar -DpomFile=guts-events-$version.pom -DgroupId=net.guts -DartifactId=guts-events -Dversion=0.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=guts-events-$version-sources.jar -DgeneratePom -DgroupId=net.guts -DartifactId=guts-events -Dversion=0.1-SNAPSHOT -Dpackaging=jar -Dclassifiers=sources

version="0.1-20091123.145820-1"
wget -q "$snapshots/net/guts/guts-gui/0.1-SNAPSHOT/guts-gui-$version.jar"
wget -q "$snapshots/net/guts/guts-gui/0.1-SNAPSHOT/guts-gui-$version.pom"
wget -q "$snapshots/net/guts/guts-gui/0.1-SNAPSHOT/guts-gui-$version-sources.jar"

mvn install:install-file -Dfile=guts-gui-$version.jar -DpomFile=guts-gui-$version.pom -DgroupId=net.guts -DartifactId=guts-gui -Dversion=0.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=guts-gui-$version-sources.jar -DgeneratePom -DgroupId=net.guts -DartifactId=guts-gui -Dversion=0.1-SNAPSHOT -Dpackaging=jar -Dclassifiers=sources
