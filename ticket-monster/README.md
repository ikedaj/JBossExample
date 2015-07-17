# Ticket Monster デモ環境構築手順

## Ticket Monsterとは
Ticket MonsterはJBossを使用してWebアプリケーションをビルド、実行するためのチュートリアルを提供するサンプルアプリケーションである。

### ダウンロードサイト
以下のURLからzipファイルをダウンロードすることができる。
* http://www.jboss.org/ticket-monster/

GitHubからもダウンロード可能である。
* https://github.com/jboss-developer/ticket-monster

## 環境構築の前提条件
* インターネットに接続可能であること

## 本手順書の実行環境
* CentOS 7.1(64bit)
* メモリ 1GB

## ビルド環境の整備
### JDK
* JDKをインストールする。

```
# yum -y install java-1.8.0-openjdk-devel.x86_64
```

### Maven
* Mavenをインストールする。

```
# cd /tmp
# wget http://ftp.yz.yamagata-u.ac.jp/pub/network/apache/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.zip
# unzip apache-maven-3.3.3-bin.zip -d /opt
```

* プロキシ環境で作業する場合は下記URLを参照して、プロキシサーバやポート番号などを設定する。
 * ユーザ名、パスワード、プロキシサーバのホスト名およびポートについては各環境にあわせて編集すること。
  * https://maven.apache.org/guides/mini/guide-proxies.html

```
# vim /opt/apache-maven-3.3.3/conf/settings.xml

<proxies>
  <proxy>
    <id>optional</id>
    <active>true</active>
    <protocol>http</protocol>
    <username>ユーザ名</username>
    <password>パスワード</password>
    <host>10.0.2.2</host>
    <port>8080</port>
    <nonProxyHosts>local.net|some.host.com</nonProxyHosts>
  </proxy>
</proxies>

```

## Wildfly
* Wildflyをインストールする。

```
# cd /tmp
# wget http://download.jboss.org/wildfly/8.2.0.Final/wildfly-8.2.0.Final.zip
# mkdir /opt/jboss/
# unzip wildfly-8.2.0.Final.zip -d /opt/jboss
```

* ヒープサイズを変更する。
 * -Xms64m -> -Xms512m
 * -Xmx512m -> -Xmx1024m
 * -XX:MaxPermSize=256m -> -XX:MaxPermSize=1024m

```
# cd /opt/jboss/wildfly-8.2.0.Final/bin/
# cp -p standalone.conf standalone.conf.org
# vim standalone.conf
# diff -urNp standalone.conf.org standalone.conf

--- standalone.conf.org 2014-11-20 22:43:22.000000000 +0900
+++ standalone.conf     2015-07-15 11:15:21.582349413 +0900
@@ -47,7 +47,8 @@ fi
 # Specify options to pass to the Java VM.
 #
 if [ "x$JAVA_OPTS" = "x" ]; then
-   JAVA_OPTS="-Xms64m -Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"
+#   JAVA_OPTS="-Xms64m -Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"
+   JAVA_OPTS="-Xms512m -Xmx1024m -XX:MaxPermSize=1024m -Djava.net.preferIPv4Stack=true"
    JAVA_OPTS="$JAVA_OPTS -Djboss.modules.system.pkgs=$JBOSS_MODULES_SYSTEM_PKGS -Djava.awt.headless=true"
 else
    echo "JAVA_OPTS already set in environment; overriding default settings with values: $JAVA_OPTS"
```

* バインドアドレスを変更する。
 * 127.0.0.1を0.0.0.0へ変更する。

```
# cd /opt/jboss/wildfly-8.2.0.Final/standalone/configuration
# cp -p standalone.xml standalone.xml.org
# vim standalone.xml
# diff -urNp standalone.xml.org standalone.xml

--- standalone.xml.org  2014-11-20 22:43:26.000000000 +0900
+++ standalone.xml      2015-07-15 11:16:57.397343985 +0900
@@ -343,7 +343,7 @@
             </filters>
         </subsystem>
         <subsystem xmlns="urn:jboss:domain:webservices:1.2">
-            <wsdl-host>${jboss.bind.address:127.0.0.1}</wsdl-host>
+            <wsdl-host>${jboss.bind.address:0.0.0.0}</wsdl-host>
             <endpoint-config name="Standard-Endpoint-Config"/>
             <endpoint-config name="Recording-Endpoint-Config">
                 <pre-handler-chain name="recording-handlers" protocol-bindings="##SOAP11_HTTP ##SOAP11_HTTP_MTOM ##SOAP12_HTTP ##SOAP12_HTTP_MTOM">
@@ -356,10 +356,10 @@
     </profile>
     <interfaces>
         <interface name="management">
-            <inet-address value="${jboss.bind.address.management:127.0.0.1}"/>
+            <inet-address value="${jboss.bind.address.management:0.0.0.0}"/>
         </interface>
         <interface name="public">
-            <inet-address value="${jboss.bind.address:127.0.0.1}"/>
+            <inet-address value="${jboss.bind.address:0.0.0.0}"/>
         </interface>
         <!-- TODO - only show this if the jacorb subsystem is added  -->
         <interface name="unsecure">
@@ -367,7 +367,7 @@
               ~  Used for IIOP sockets in the standard configuration.
               ~                  To secure JacORB you need to setup SSL
               -->
-            <inet-address value="${jboss.bind.address.unsecure:127.0.0.1}"/>
+            <inet-address value="${jboss.bind.address.unsecure:0.0.0.0}"/>
         </interface>
     </interfaces>
     <socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}">
@@ -382,4 +382,4 @@
             <remote-destination host="localhost" port="25"/>
         </outbound-socket-binding>
     </socket-binding-group>
-</server>
\ ファイル末尾に改行がありません
+</server>
```
* JBossの管理コンソールへログインするためのユーザを作成する。
 * ユーザ名 : admin
 * パスワード : adminp@ssw0rd


```
# cd /opt/jboss/wildfly-8.2.0.Final/bin/
# ./add-user.sh

What type of user do you wish to add?
 a) Management User (mgmt-users.properties)
 b) Application User (application-users.properties)
(a): a

Enter the details of the new user to add.
Using realm 'ManagementRealm' as discovered from the existing property files.
Username : admin
The username 'admin' is easy to guess
Are you sure you want to add user 'admin' yes/no? yes
Password :
Re-enter Password :
What groups do you want this user to belong to? (Please enter a comma separated list, or leave blank for none)[  ]:
About to add user 'admin' for realm 'ManagementRealm'
Is this correct yes/no? yes
```

## Ticket Monster
* Ticket Monsterをダウンロードする。

```
# cd /tmp
# git clone https://github.com/jboss-developer/ticket-monster.git
```

* アプリケーションをビルドする。

```
# cd /tmp/ticket-monster/demo
# /opt/apache-maven-3.3.3/bin/mvn clean package
```

* アプリケーションがビルドできたことを確認する。

```
# ls -l /tmp/ticket-monster/demo/target/ticket-monster.war

```

## 動作確認
* Wildflyを起動する。

 ```
 # /opt/jboss/wildfly-8.2.0.Final/bin/standalone.sh
  ```

* ブラウザから下記のURLにアクセスし、JBossが起動していることを確認する。
 * http://<サーバのIPアドレス>:8080

* アプリケーションをデプロイする。

```
# cp -p /tmp/ticket-monster/demo/target/ticket-monster.war /opt/jboss/wildfly-8.2.0.Final/standalone/deployments/
```

* ブラウザから下記のURLにアクセスする。
 * http://<サーバのIPアドレス>::8080/ticket-monster/

* アプリケーションの動作確認を行う。
 * 「Events」タブを選択
 * 「Concert」を展開し、「Rock concert of the decade」をダブルクリックする。
 * 画面が遷移しない、もしくは時間がかかる。
  * Wildflyのログに「Caused by: java.net.ConnectException: 接続がタイムアウトしました」と出力されている

* 問題解決
 * Event.javaとかevent-detail.htmlを眺めてみて、画像の取得に失敗しているような気配を感じる。
 * import.sqlを眺めてみて、画像をインターネット(dropboxとかwikimediaとか)から取得していることに愕然とする。
 * プロキシ環境の場合、外部からの画像取得は困難なのでローカルの画像に置換する。
  * https://github.com/ikedaj/JBossExample/blob/master/ticket-monster/demo/src/main/resources/import.sql

* アプリケーションのビルドとデプロイ

```
# /opt/apache-maven-3.3.3/bin/mvn clean package
# cp -p /tmp/ticket-monster/demo/target/ticket-monster.war /opt/jboss/wildfly-8.2.0.Final/standalone/deployments/
```

* ブラウザから下記のURLにアクセスする。
 * http://<サーバのIPアドレス>::8080/ticket-monster/

* アプリケーションの動作確認を行う。
 * 「Events」タブを選択
 * 「Concert」を展開し、「Rock concert of the decade」をダブルクリックする。
 * 画面の遷移には成功！
 * ただし、「Where？」のプルダウンが選択できない。
 * Wildflyのログに「resteasy-jaxrs-3.0.10.Final.jar」がどうのこうのというエラーが出力されている。
```
16:29:19,156 ERROR [io.undertow.request] (default task-8) UT005023: Exception handling request to /ticket-monster/rest/shows: org.jboss.resteasy.spi.UnhandledException: Response is committed, can't handle exception
        at org.jboss.resteasy.core.SynchronousDispatcher.writeException(SynchronousDispatcher.java:148) [resteasy-jaxrs-3.0.10.Final.jar:]
        at org.jboss.resteasy.core.SynchronousDispatcher.writeResponse(SynchronousDispatcher.java:432) [resteasy-jaxrs-3.0.10.Final.jar:]
        at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:376) [resteasy-jaxrs-3.0.10.Final.jar:]
        at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:179) [resteasy-jaxrs-3.0.10.Final.jar:]
```

* 問題解決
 * resteasy-jaxrsを探すと、pom.xmlに定義されている。

```
# grep -R resteasy-jaxrs .
./pom.xml:            <artifactId>resteasy-jaxrs</artifactId>

# vim ./pom.xml

        <!-- RESTEasy dependencies that bring in Jackson Core and RESTEasy APIs+SPIs, which we use for
            fine tuning the content of the JSON responses -->
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-jackson-provider</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-jaxrs</artifactId>
            <scope>provided</scope>
        </dependency>
```

* いろいろ試してみたけど、うまくいったのは「jboss-deployment-structure.xml」に「resteasy-jackson-provider」を追記する方法
 * 参考にしたサイト
  * http://docs.jboss.org/resteasy/docs/3.0.9.Final/userguide/html_single/index.html#json
  * https://github.com/resteasy/Resteasy/blob/e8e8724e74cfd6856f4ece8ab1f5c928b72f6fbd/jaxrs/docbook/reference/en/en-US/modules/Json.xml#L142
 * 「jboss-deployment-structure.xml」に「resteasy-jackson-provider」を設定する。
```
# vim ./src/main/webapp/WEB-INF/jboss-deployment-structure.xml

<jboss-deployment-structure>
    <deployment>
        <exclusions>
        </exclusions>
        <!-- This allows you to define additional dependencies, it is the same
  as using the Dependencies: manifest attribute -->
        <dependencies>
            <module name="org.jboss.resteasy.resteasy-jackson-provider" annotations="true" />
            <module name="org.jboss.as.naming" />
            <module name="org.jboss.as.server" />
            <module name="org.jboss.msc" />
        </dependencies>
    </deployment>
</jboss-deployment-structure>
```

* アプリケーションのビルドとデプロイ

```
# /opt/apache-maven-3.3.3/bin/mvn clean package
# cp -p /tmp/ticket-monster/demo/target/ticket-monster.war /opt/jboss/wildfly-8.2.0.Final/standalone/deployments/
```

* ブラウザから下記のURLにアクセスする。
 * http://<サーバのIPアドレス>::8080/ticket-monster/

* アプリケーションの動作確認を行う。
 * 「Events」タブを選択
 * 「Concert」を展開し、「Rock concert of the decade」をダブルクリックする。
 * 画面が期待通り遷移して、チケットの予約が完了する。
 * 「Bookings」タブから予約を確認することができる。

## 接続先データベースの変更
* デフォルトの設定ではWildfly内蔵のインメモリDB(H2)へデータを格納する。
* データの格納先をPostgreSQLに変更する手順を記載する。
* PostgreSQLのインストールや設定は割愛する。
 * 本手順ではDockerの公式コンテナを利用する。
 * https://registry.hub.docker.com/_/postgres/

### JDBCドライバの設定
* JDBCドライバをダウンロードする。

```
# cd /tmp
# wget https://jdbc.postgresql.org/download/postgresql-9.4-1201.jdbc4.jar
```

* JDBCドライバを配置する。

```
# mkdir -p /opt/jboss/wildfly-8.2.0.Final/modules/system/layers/base/org/postgresql/main
# cd /opt/jboss/wildfly-8.2.0.Final/modules/system/layers/base/org/postgresql/main
# cp -p /tmp/postgresql-9.4-1201.jdbc4.jar .
```

* JDBCドライバをモジュールとして組み込むための設定ファイルを作成する。

```
# cd /opt/jboss/wildfly-8.2.0.Final/modules/system/layers/base/org/postgresql/main

# vim module.xml

<module xmlns="urn:jboss:module:1.0" name="org.postgresql">
   <resources>
     <resource-root path="postgresql-9.4-1201.jdbc4.jar"/>
   </resources>
   <dependencies>
      <module name="javax.api"/>
      <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

### データソースの設定
* データソースの設定を追加する。
 * PostgreSQLのユーザ名、パスワードは環境に合わせて編集すること。

```
# cd /opt/jboss/wildfly-8.2.0.Final/standalone/configuration
# cp -p standalone.xml standalone.xml.tmp
# vim standalone.xml
# diff -urNp standalone.xml.tmp standalone.xml
--- standalone.xml.tmp  2015-07-15 11:16:57.397343985 +0900
+++ standalone.xml      2015-07-15 11:28:36.596304377 +0900
@@ -142,10 +142,31 @@
                         <password>sa</password>
                     </security>
                 </datasource>
+               <datasource jndi-name="java:jboss/datasources/TicketMonsterPostgreSQLDS" pool-name="PostgreSQLDS">
+                   <connection-url>jdbc:postgresql://postgres:5432/postgres</connection-url>
+                   <driver>org.postgresql</driver>
+                   <transaction-isolation>TRANSACTION_READ_COMMITTED</transaction-isolation>
+                   <pool>
+                       <min-pool-size>10</min-pool-size>
+                       <max-pool-size>100</max-pool-size>
+                       <prefill>true</prefill>
+                   </pool>
+                   <security>
+                       <user-name>postgres</user-name>
+                       <password>postgres</password>
+                   </security>
+                   <statement>
+                       <prepared-statement-cache-size>32</prepared-statement-cache-size>
+                       <share-prepared-statements/>
+                   </statement>
+               </datasource>
                 <drivers>
                     <driver name="h2" module="com.h2database.h2">
                         <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
                     </driver>
+                   <driver name="org.postgresql" module="org.postgresql">
+                       <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
+                   </driver>
                 </drivers>
             </datasources>
         </subsystem>
```

### 接続先の名前解決
* DBサーバの名前解決を行う。
 * DBサーバのIPアドレスが192.168.100.102、ホスト名がpostgresの場合

```
# cat /etc/hosts

127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

<省略>

192.168.100.102 postgres
```
### アプリケーションのビルド
* PostgreSQLへ接続する設定でアプリケーションをビルドする。

```
# cd /tmp/ticket-monster/demo
# /opt/apache-maven-3.3.3/bin/mvn clean package -Ppostgresql
```

### 動作確認
* PostgreSQLを起動する。
 * 検証環境ではDockerコンテナを利用する。
 * コンテナへIPアドレスを割り当てるためPipeworkを使用する。
  * https://github.com/jpetazzo/pipework
 
```
# docker pull postgres
# docker run --name postgres -d postgres
# pipework br0 -i eth1 postgres 192.168.100.102/24
```

* アプリケーションのデプロイ
```
# cp -p /tmp/ticket-monster/demo/target/ticket-monster.war /opt/jboss/wildfly-8.2.0.Final/standalone/deployments/
```

* PostgreSQLにテーブルが作成されていることを確認する。
```
# docker exec -it postgres /bin/bash

[container]:/# su - postgres

$ psql
psql (9.4.4)
Type "help" for help.

postgres=# \d
                    List of relations
 Schema |           Name           |   Type   |  Owner
--------+--------------------------+----------+----------
 public | appearance               | table    | postgres
 public | appearance_id_seq        | sequence | postgres
 public | booking                  | table    | postgres
 public | booking_id_seq           | sequence | postgres
 public | event                    | table    | postgres
 public | event_id_seq             | sequence | postgres
 public | eventcategory            | table    | postgres
 public | eventcategory_id_seq     | sequence | postgres
 public | mediaitem                | table    | postgres
 public | mediaitem_id_seq         | sequence | postgres
 public | performance              | table    | postgres
 public | performance_id_seq       | sequence | postgres
 public | section                  | table    | postgres
 public | section_id_seq           | sequence | postgres
 public | sectionallocation        | table    | postgres
 public | sectionallocation_id_seq | sequence | postgres
 public | ticket                   | table    | postgres
 public | ticket_id_seq            | sequence | postgres
 public | ticketcategory           | table    | postgres
 public | ticketcategory_id_seq    | sequence | postgres
 public | ticketprice              | table    | postgres
 public | ticketprice_id_seq       | sequence | postgres
 public | venue                    | table    | postgres
 public | venue_id_seq             | sequence | postgres
(24 rows)

postgres=# \q

$ exit
[container]:/# exit
exit
```

* デフォルトの設定では、インスタンスの停止時にテーブルを削除する設定のため、テーブルを維持する設定へ変更する。

```
# # cd /tmp/ticket-monster/demo/src/main/resources-postgresql/META-INF
# cp -p persistence.xml persistence.xml.org
# vim persistence.xml
# diff -urNp persistence.xml.org persistence.xml
--- persistence.xml.org 2015-07-17 15:56:37.984184103 +0900
+++ persistence.xml     2015-07-17 18:54:38.481554275 +0900
@@ -22,7 +22,7 @@ can find it in the source at src/main/we
         <jta-data-source>java:jboss/datasources/TicketMonsterPostgreSQLDS</jta-data-source>
         <properties>
             <!-- Properties for Hibernate -->
-            <property name="hibernate.hbm2ddl.auto" value="create-drop" />
+            <property name="hibernate.hbm2ddl.auto" value="update" />
             <property name="hibernate.show_sql" value="false" />
         </properties>
     </persistence-unit>
```

* アプリケーションのビルドとデプロイ

```
# cd /tmp/ticket-monster/demo/
# /opt/apache-maven-3.3.3/bin/mvn clean package -Ppostgresql
# cp -p /tmp/ticket-monster/demo/target/ticket-monster.war /opt/jboss/wildfly-8.2.0.Final/standalone/deployments/
```

## Dockerコンテナにアプリケーションをデプロイ
* Wildfly 8.2.0の公式イメージを入手する。

```
# docker pull jboss/wildfly:8.2.0.Final

* コンテナに取り込むファイルを収集する。

```

```
# mkdir /tmp/jboss
# cd /tmp/jboss
# cp -p /opt/jboss/wildfly-8.2.0.Final/modules/system/layers/base/org/postgresql/main/* .
# cp -p /tmp/ticket-monster/demo/target/ticket-monster.war .
# cp -p /opt/jboss/wildfly-8.2.0.Final/standalone/configuration/standalone.xml .
# cp -p /opt/jboss/wildfly-8.2.0.Final/bin/standalone.conf .
# cp -p /tmp/ticket-monster/demo/src/main/webapp/admin/img/forge-logo.png .
```
* Dockerfileを作成する。


```
# vim Dockerfile

FROM jboss/wildfly:8.2.0.Final
ADD postgresql-9.4-1201.jdbc4.jar /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main/
ADD module.xml /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main/
ADD ticket-monster.war /opt/jboss/wildfly/standalone/deployments/
ADD standalone.conf /opt/jboss/wildfly/bin/
ADD standalone.xml /opt/jboss/wildfly/standalone/configuration/
ADD forge-logo.png /tmp/ticket-monster/demo/src/main/webapp/admin/img/
RUN /opt/jboss/wildfly/bin/add-user.sh admin adminp@ssw0rd --silent
```
* イメージをビルドする。


```
# docker build --force-rm=true --rm=true --tag=local/ticket-monster .
```

* コンテナを起動する。

```
# docker run \
   --name ticket-monster \
   --add-host=postgres:192.168.100.102 \
   -d -p 9990:9990 -p 8080:8080 local/ticket-monster
```

* IPアドレスを割り当てる。

```
# pipework br0 -i eth1 ticket-monster 192.168.100.101/24
```

* Wildflyの起動ログを確認する。

```
# docker logs -f ticket-monster
```

* コンテナに割り当てたIPアドレスにアクセスし、アプリケーションの動作確認を行う。
 * http://<コンテナのIPアドレス>:8080/ticket-monster/


