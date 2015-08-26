mv pom.xml pom.xml.orig
sed s/-SNAPSHOT/--SNAPSHOT/g pom.xml.orig > pom.xml
mvn clean install javadoc:javadoc
find target/site/apidocs -type f -exec sed -i -e s/--SNAPSHOT/-SNAPSHOT/g {} \;
rm pom.xml
mv pom.xml.orig pom.xml
