# 旅宿資料蒐集平台

![專案封面圖](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img01.jpg?raw=true)

## 功能

資料管理
- [x] 讀取 csv 檔並更新至資料庫
- [x] csv 檔案下載
- [x] 房屋資料顯示
...

## 畫面

![功能圖片 1](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img01.jpg?raw=true)
![功能圖片 2](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img03.jpg?raw=true)
![功能圖片 3](https://github.com/natsu-chi/images-folder/blob/main/spring-boot-bnbserv/img02.jpg?raw=true)


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
- Jsoup v1.17.2
- Apache Commons Compress v1.27.1
- Lombok

## 資料夾說明

- controller - 控制器放置處
- dto - 資料傳輸對象放置處（給前端的資料格式）
- entity - 資料對象放置處（後端處理格式）
- repository - 資料存取層放置處
- service - 服務層放置處
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
http://localhost:8080/home
```

## 環境變數說明

```env
datasource:
    url: jdbc:mysql://HOST/DB_NAME # HOST: 資料庫 IP 位址, DB_NAME: 資料庫名稱
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: # 資料庫登入帳號
    password: # 資料庫登入密碼
```