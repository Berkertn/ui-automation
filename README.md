# ui-automation

**mvn -Dtest=TextExample test**


- mvn test -DsuiteXmlFile=localRun.xml
- mvn test -DsuiteXmlFile=localRun.xml -Dgroups="regression"
- mvn test -DsuiteXmlFile=localRun.xml -Dgroups="regression" -Dbrowser="chrome"

--!
- mvn test -Dparallel=methods -DthreadCount=4
- mvn test -Dparallel=tests -DthreadCount=4
- mvn test -Dparallel=classes -DthreadCount=4

