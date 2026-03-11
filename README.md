# ToDo管理くん

ToDo管理くんは、タスクをカテゴリ・期限・優先度で整理して管理できる Spring Boot 製の Web アプリです。  
日本語 / 英語の言語切り替えに対応し、期限の近いタスクを視覚的に把握しやすい UI を備えています。

## Overview

- タスクの作成・編集・削除
- カテゴリ分類
- 期限設定
- 優先度表示
- 期限に応じたカード色の変化
- 優先度に応じたカラー表示
- 日本語 / 英語の言語切り替え
- ログイン認証
- 管理者向けユーザー管理
- CSV エクスポート
- REST API

## Demo Credentials

アプリ起動時に以下のサンプルユーザーが自動作成されます。

| Role | Username | Password |
| --- | --- | --- |
| Admin | `admin` | `password` |
| User | `user1` | `password` |

## Screens / Features

### 1. ToDo 一覧
- タスク一覧表示
- キーワード検索
- カテゴリ絞り込み
- 優先度絞り込み
- 並び替え
- ページネーション
- 完了 / 未完了の切り替え

### 2. ToDo 作成
- 作成者
- タイトル
- カテゴリ
- 期限
- 優先度
- 詳細
- 確認画面付き登録フロー

### 3. 管理機能
- 一括削除
- CSV ダウンロード
- 管理者用ユーザー一覧 / 編集

### 4. 多言語対応
- `?lang=ja`
- `?lang=en`

## Tech Stack

| Category | Technology |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.2.2 |
| Web | Spring MVC |
| Template Engine | Thymeleaf |
| Security | Spring Security |
| ORM | Spring Data JPA |
| SQL Mapper | MyBatis |
| Validation | Jakarta Validation |
| AOP | Spring AOP |
| Database | H2 Database |
| Build Tool | Maven |
| Frontend | HTML / CSS / JavaScript |

## Directory Structure

```text
.
├─ pom.xml
├─ mvnw
├─ mvnw.cmd
├─ src
│  └─ main
│     ├─ java
│     │  └─ com/example/todo
│     │     ├─ api          # REST API
│     │     ├─ aop          # ログ出力・性能計測
│     │     ├─ config       # Security / i18n / 初期データ
│     │     ├─ controller   # MVC コントローラ
│     │     ├─ entity       # JPA エンティティ
│     │     ├─ exception    # 例外ハンドリング
│     │     ├─ form         # フォームオブジェクト
│     │     ├─ mapper       # MyBatis Mapper
│     │     ├─ repository   # JPA Repository
│     │     └─ service      # ビジネスロジック
│     └─ resources
│        ├─ static          # CSS / JavaScript
│        ├─ templates       # Thymeleaf テンプレート
│        ├─ mappers         # MyBatis XML
│        ├─ application.properties
│        ├─ messages*.properties
│        └─ schema.sql
└─ target
```

## Getting Started

### Requirements

- Java 17
- Maven 3.9 以上、または Maven Wrapper

### Run

```bash
./mvnw spring-boot:run
```

Windows の場合:

```bash
mvnw.cmd spring-boot:run
```

起動後、以下にアクセスします。

- Application: `http://localhost:8085/login`
- H2 Console: `http://localhost:8085/h2-console`

## Configuration

現在の主な設定は以下です。

- Port: `8085`
- Database: H2 In-Memory Database
- JDBC URL: `jdbc:h2:mem:todo;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- Username: `sa`
- Password: 空文字

## API Endpoints

認証後に以下の API を利用できます。

- `GET /api/todos`
- `GET /api/todos/{id}`
- `POST /api/todos`
- `PUT /api/todos/{id}`
- `DELETE /api/todos/{id}`

## Notes

- H2 のインメモリ DB を使用しているため、アプリ停止後にデータは保持されません。
- CSRF は無効化されています。学習用・ポートフォリオ用途としては扱いやすい構成ですが、本番運用時は再設定を推奨します。
- `target/` はビルド生成物です。GitHub 管理では通常 `.gitignore` 対象にします。

## Recommended License

個人ポートフォリオ用途で公開する場合は、`MIT License` を推奨します。

理由:

- 内容がシンプルで分かりやすい
- GitHub 上で広く使われている
- ポートフォリオとして公開しやすい
- 第三者が閲覧・再利用しやすい

必要であれば、`LICENSE` ファイルとして MIT License 本文を追加してください。

## Future Improvements

- テストコードの追加
- Docker 対応
- 永続DB対応（PostgreSQL / MySQL など）
- CI/CD 導入
- 権限管理の詳細化

