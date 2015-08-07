##Web Programming – Final Project!

**Course Instructor**
Dr. Haggai Roitman, IBM Research - Haifa

**Publication date:** 1/1/2015

**Scope**
during the project we explore, design and implement a web application that is built on top of the various client-side and server-side technologies that were thought during the semester.

**Project general description**
the goal of this project is to implement a simple microblogging service ([http://en.wikipedia.org/wiki/Microblogging](http://en.wikipedia.org/wiki/Microblogging)) – a popular online social communication form on the Web. Popular microblogs such as Twitter, Tumblr, FriendFeed, etc., serve as an important media for knowledge exchange, situation awareness, information discovery and social online interaction.
The microblogging service, to be implemented in this project, would allow various users to chat together via simple user message exchange. Updates to various “user discussions” may be refreshed in the user view in a “real-time” manner. Users can post messages to newly started discussions or reply (via comments) to existing ones. Users may have the options to discover discussions, contribute updates of their own or comment. A user may decide to follow discussions made by other users. User popularity in the system is derived from such “followership”, where the more followers a user has, the higher is her popularity.

----------


#Deployment and Environment 

- Java7JRE [http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html)
- EclipseIDE(Luna)forJ2E: [https://eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/lunar](https://eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/lunar)
- Apache Tomcat v.7: [http://tomcat.apache.org/download-70.cgi](http://tomcat.apache.org/download-70.cgi)
- Bootstrap: [http://getbootstrap.com/getting-started/#download](http://getbootstrap.com/getting-started/#download)
- jQuery: [http://code.jquery.com/jquery-1.11.2.min.js](http://code.jquery.com/jquery-1.11.2.min.js)
- AngularJS: [https://ajax.googleapis.com/ajax/libs/angularjs/1.2.28/angular.min. js](https://ajax.googleapis.com/ajax/libs/angularjs/1.2.28/angular.min.%20js)
- Apache Derby : [http://db.apache.org/derby/releases/release-10.11.1.1.cgi](http://db.apache.org/derby/releases/release-10.11.1.1.cgi)


----------

#HOW TO: Use Derby Database as a Web. App. data source

1. Obtain the latest Apache Derby realease:
http://db.apache.org/derby/releases/release-10.11.1.1.cgi

	1.1 Unzip db-derby-10.11.1.1-bin.zip and obtain the files:
	derby.jar and derbyclient.jar
2. Let TOMCAT_DIR be the directory where you unzipped the Tomcat
package.
2.2 Copy derby.jar and derbyclient.jar files into TOMCAT_DIR/lib. Open the file TOMCAT_DIR/conf/context.xml

	2.3 	Add a new element `<Resource>...</Resource>` as a child element of the `<Context>...</Context>` element in context.xml as follows:

```
 <Context>
   <!--
   MicroBlogDatasource: The name of the datasource that
   represents Derby database.
   MicroBlogDB: The Derby database name
-->
   <Resource name="jdbc/MicroBlogDatasource" auth="Container"
   type="javax.sql.DataSource"
       driverClassName="org.apache.derby.jdbc.EmbeddedDriver"
       url="jdbc:derby:MicroBlogDB;create=true"
       username="username" password="password" maxActive="20"
       maxIdle="10" maxWait="-1" />
</Context>
```
   3 .  In your application, manually add the following resource-ref definition to your web.xml file:

```
<web-app>
     <resource-ref>
<!--
     MicroBlogDatasource: the name of the datasource that
     represents Derby database.
-->
	      <res-ref-name>jdbc/MicroBlogDatasource</res-ref-name>
          <res-type>javax.sql.DataSource</res-type>
          <res-auth>Container</res-auth>
      </resource-ref>
</web-app>
```

   4 . You now should be able to obtain a connection to your database using Tomcat’s connection pool as follows:

```
import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
       //obtain MicroBlogDB data source from Tomcat's context
Context context = new InitialContext();
     BasicDataSource ds =
(BasicDataSource)context.lookup(“java:comp/env/jdbc/MicroBlogDatasource”);
     Connection conn = ds.getConnection();
      //use connection as you wish...but close after usage! (this
      //is important for correct connection pool management
      //within Tomcat
```

![alt tag](https://jmargieh.github.io/MicroBlog/WebContent/images/ERD/ERDdiagram.png)