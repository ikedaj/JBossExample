# JBoss Forge による Scaffold 作成デモ

* 2015年7月10日(金)に行われた「インフラ技術者向けJBoss入門」の冒頭で実施したデモを再現
 * http://d-cube.connpass.com/event/17377/
 * http://slides.com/kuniim/deck#/

* 本手順書の実行環境
 * Windows 8.1(64bit)
 * メモリ 16GB


* 前提条件
 * インターネットに接続可能であること

## 実行環境の準備

### JDKのインストール
* インストーラをダウンロードする。
 * https://java.com/ja/download/
 * 2015/07/15時点の最新版はVersion 8 Update 51


*  JDKをインストールする。
 * ダウンロードしたインストーラをダブルクリックし、ダイアログボックスの指示に従ってインストール作業を実施する。
 * システム環境変数にJAVA_HOMEを設定する。

### Developer Studioのインストール
* Developer Studio(Installer With EAP)をダウンロードする。
 * http://www.jboss.org/products/devstudio/download/
 * JBossDeveloperのユーザ登録が必要
 * 2015/07/15時点の最新版は9.0.0.Beta1(Release Date 2015/06/22)
 * ダウンロードするjarファイルのサイズは628MB


* ダウンロードしたjarファイルを実行する。
 * インストーラに従ってインストール作業を実施する。
 * コマンドラインプロンプトからの実行例
```
> java -jar jboss-devstudio-9.0.0.Beta1-installer-eap.jar
```

### Apache Mavenのインストール
* Apache Maven(apache-maven-3.3.3-bin.zip)をダウンロードする。
 * https://maven.apache.org/download.cgi
 * 2015/07/15時点の最新版は3.3.3
 * zipファイルを展開して「C:\Program Files (x86)\apache-maven-3.3.3」に配置する。
 * システム環境変数のPATHにMavenを追加する。
 * プロキシ環境で作業する場合は下記URLを参照して、プロキシサーバやポート番号などを設定する。
  * https://maven.apache.org/guides/mini/guide-proxies.html

## Forgeを使用したScaffoldの作成
* 参考サイト
 * http://forge.jboss.org/document/write-a-java-ee-web-application---advanced

### Forge Consoleでのコマンド実行例
* 「jbdevstudio.bat」をダブルクリックし「Developer Studio」を起動する。
* 「Developer Studio」の「Forge Console」タブで緑色の矢印を押す。
* コンソール画面で下記のコマンドを実行する。

#### コマンド実行例
* Create a new project
```
$ project-new --named conference --topLevelPackage com.example.project --finalName conferenceApp
```
* Setup your JPA Provider
    * デフォルトのデータソースを使用しJBoss内蔵のインメモリDB(H2)へアクセスする場合は、引数なしで jpa-setup を実行する。
 ```
$ jpa-setup
```
    * デフォルト以外のデータソースを使用する場合は、下記の例を参考にすること。
 ```
$ jpa-setup --provider Eclipse Link --dbType POSTGRES --dataSourceName java:comp/DefaultDataSource
```
* Create a JPA entity with Bean Validation constraints
```
$ jpa-new-entity --named Speaker
$ jpa-new-field --named firstname
$ jpa-new-field --named surname
$ jpa-new-field --named bio --length 2000
$ jpa-new-field --named twitter
$ constraint-add --onProperty firstname --constraint NotNull
$ constraint-add --onProperty surname --constraint NotNull
$ constraint-add --onProperty bio --constraint Size --max 2000
```
* Create another JPA entity with a One-to-Many relationship
```
$ jpa-new-entity --named Talk
$ jpa-new-field --named title
$ jpa-new-field --named description --length 2000
$ jpa-new-field --named room
$ jpa-new-field --named date --type java.util.Date --temporalType DATE
$ jpa-new-field --named speakers --type com.example.project.model.Speaker --relationshipType One-to-Many
$ constraint-add --onProperty title --constraint NotNull
$ constraint-add --onProperty room --constraint NotNull
$ constraint-add --onProperty description --constraint Size --max 2000
```
* Create a simple Java enum
```
$ java-new-enum --named Language --targetPackage com.example.project.model
$ java-new-enum-const ENGLISH
$ java-new-enum-const FRENCH
```
* Create another JPA entity with an enumerated type
```
$ jpa-new-entity --named Book
$ jpa-new-field --named isbn
$ jpa-new-field --named title
$ jpa-new-field --named author
$ jpa-new-field --named description --length 2000
$ jpa-new-field --named price --type java.lang.Float
$ jpa-new-field --named nbOfPages --type java.lang.Integer
$ jpa-new-field --named publicationDate --typeName java.util.Date --temporalType DATE
$ jpa-new-field --named language --type com.example.project.model.Language
```
* Move to the project top
```
$ cd D:\jbdevstudio\workspace\conference
```
* Set up scaffolding
```
$ faces-setup --facesVersion 2.2
```
* Generate scaffolding for all the entities
```
$ scaffold-generate --targets com.example.project.model.*
```
* build
```
$ build
```

## 管理ユーザの作成
* コマンドラインプロンプトから下記の操作を実行し、JBossの管理コンソールへログインするためのユーザを作成する。

 * ユーザ名 : admin
 * パスワード : adminp@ssw0rd

```
C:\Users\user> d:
D:\> cd D:\jbdevstudio\runtimes\jboss-eap\bin
D:\jbdevstudio\runtimes\jboss-eap\bin> add-user.bat

どのようなユーザータイプを追加しますか？
 a) 管理ユーザー (mgmt-users.properties)
 b) アプリケーションユーザー (application-users.properties)
(a): a

追加する新規ユーザーの詳細を入力します。
レルム 'ManagementRealm' を既存のプロパティーファイルで見つかったとおりに使用しています。
ユーザ名 : admin
ユーザー名 'admin' は簡単に推測できます。
'admin' を本当に追加しますか？yes/no? yes
パスワードの要件は以下のとおりです。この制限を変更するには、add-user.properties 設定ファイルを編集します。
 - パスワードは、制限された次の値のいずれかでない必要があります {root, admin, administrator}
 - パスワードは 8 文字, 1 英字, 1 文字, 1 文字 (英数字以外) 文字以上である必要があります
 - パスワードとユーザー名は異なるものにする必要があります
パスワード :
パスワードを再度入力してください。 :
このユーザーが所属するグループはどれですか？ (カンマ区切りリストを入力してください。所属グループがない場合は空白のままにしてください。)[  ]:
レルム 'ManagementRealm' にユーザー 'admin' を追加します。
正しいですか yes/no? yes
```

## JBossの起動
* 「Developer Studio」の「Servers」タブを選択する。
* 「JBoss EAP 6.4」を右クリックし、「Start」を選択する。

## JBossの起動確認
* ブラウザから下記のURLにアクセスし、JBossが起動していることを確認する。
 * http://localhost:8080

## JBossの管理コンソールからアプリケーションをデプロイ
* ブラウザから下記のURLにアクセスし、JBossの管理コンソールにログインする。
 * http://localhost:9990
* 管理ユーザでログインする。
 * 「Deplpy an applicaion」から「Create Deployment」を選択する。
 * 「Add」ボタンを選択し、ビルドしたwarファイルを選択し、「Next」ボタンを選択する。
   * D:\jbdevstudio\workspace\conference\target\conferenceApp.war
 * 「Enable」にチェックして「Save」ボタンを選択する。

## デプロイしたアプリケーションにアクセス
* ブラウザから下記のURLにアクセスする。
 * http://localhost:8080/conferenceApp
 * 「Books」に新しい本を登録する。
 * 「Speaker」に新しい講演者を登録する。
 * 「Talks」に新しい講演を登録する。

## JBossの停止
* 「Developer Studio」の「Servers」タブを選択する。
*  赤いボタン(Terminate)を選択し、JBossを停止する。

## ポート番号が重複している場合の対処法
* 管理コンソール用のポート9990が既に使用されている場合、「Developer Studio」の「Console」にERRORが出力される
```
00:21:05,905 ERROR [org.jboss.msc.service.fail] (MSC service thread 1-8) MSC000001: 
Failed to start service jboss.serverManagement.controller.management.http: org.jboss.msc.service.StartException in service 
jboss.serverManagement.controller.management.http: Address already in use: bind localhost/127.0.0.1:9990
```
 * 方法1) ポートを使用しているサービスを停止する。
  * TCPViewをダウンロードし、9990ポートを使用しているプロセスを停止する。
 　 * https://technet.microsoft.com/ja-jp/sysinternals/bb897437.aspx
 * 方法2) 使用するポート番号を変更する。
  * standalone.xmlの当該ポート番号を変更する。
  * standalone.xmlのjboss.socket.binding.port-offsetを変更して、ポート番号をずらす。

## 「Developer Studio」からアプリケーションをデプロイする方法
* 「Servers」タブで「JBoss EAP 6.4」を選択し右クリックしする。
* 「Add and Remove」を選択し、「Available」から「conference」を選択し、「Add」ボタンでサーバに追加する。
 * 選択するのはプロジェクト名のため、接続先は、http://localhost:8080/conference となる。
