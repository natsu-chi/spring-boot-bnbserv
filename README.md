# 旅宿資料蒐集平台

![專案封面圖](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img01.jpg?raw=true)

## 功能

資料管理
- [x] 爬蟲下載、讀取 csv 檔，並更新至資料庫
- [x] csv 檔案下載至本機
- [x] 房屋資料顯示

頁面管理
- [x] 關於我們：動態版面控制，支援文字編輯器編輯與圖片上傳，並可批次調整是否顯示
...

## 畫面
**房屋資料查詢**
![功能圖片 4](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img04.jpg?raw=true)
![功能圖片 1](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img01.jpg?raw=true)
![功能圖片 2](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img03.jpg?raw=true)
**房屋資料下載至本機**
基本資料：房屋資訊
詳細資料：房屋資訊 + 屋主資料 + 評價
![功能圖片 3](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img02.jpg?raw=true)
**動態版面調整：關於我們頁面**
標題設定
![功能圖片 5](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img05.jpg?raw=true)
![功能圖片 6](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img06.jpg?raw=true)
區塊 - 關於我們內容
![功能圖片 7](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img07.jpg?raw=true)
![功能圖片 8](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img08.jpg?raw=true)
對應前台顯示
![功能圖片 1](https://github.com/natsu-chi/images-folder/blob/main/laravel-bnbfront/img01.jpeg?raw=true)


## 專案技術

- Java v17
- Spring Boot v3.3.4
- Spring Web
- Thymeleaf
- Spring Data JPA
- MySQL Connector
- Spring Actuator
- Spring DevTools
- OpenCSV v5.7.1
- JSON (org.json) v20240303
- jsoup v1.17.2
- Apache Commons Compress v1.27.1
- Lombok
- CKEditor 5

## 資料夾說明

- controller - 控制器放置處
- dto - 資料傳輸對象放置處（給前端的資料格式）
- entity - 資料對象放置處（後端處理格式）
- repository - 資料存取層放置處
- service - 服務層放置處
  - impl - 實作類別
- util - 公用程式放置處
- exception - 例外處理對象放置處
- resources/static - 靜態資源放置處
    - assets
      - css - css 檔案放置處
      - js - js 檔案放置處
      - libs - 套件檔案放置處
      - images - 圖片放置處
    - dateset - 資料集下載檔案放置處
- templates - 畫面放置處

### 取得專案

```bash
git clone git@github.com:natsu-chi/spring-boot-bnbserv.git
```

### 環境變數設定

請在終端機輸入 `cp application-example.yml .application.yml` 來複製 application-example.yml 檔案，並依據 `application.yml` 內容調整相關欄位。

### 開啟專案

在瀏覽器網址列輸入以下即可看到畫面

```bash
http://localhost:8080/
```

## 環境變數說明

```env
datasource:
    url: jdbc:mysql://HOST/DB_NAME # HOST: 資料庫 IP 位址, DB_NAME: 資料庫名稱
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: # 資料庫登入帳號
    password: # 資料庫登入密碼
```