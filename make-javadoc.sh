mv pom.xml pom.xml.orig
sed s/.SNAPSHOT/-SNAPSHOT/g pom.xml.orig > pom.xml
mvn clean install javadoc:javadoc
rm pom.xml
mv pom.xml.orig pom.xml
