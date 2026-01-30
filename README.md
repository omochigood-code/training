# Users App（SvelteKit + Quarkus + PostgreSQL）

ユーザーの **一覧 / 検索 / 詳細 / 新規登録 / 更新 / 削除** を行うサンプルアプリです。

- Frontend: SvelteKit（pnpm）
- Backend: Quarkus 3.x（RESTEasy Reactive / Panache / Flyway）
- DB: PostgreSQL 16（Docker Compose）

## リポジトリ構成

├── mono-back/   # Quarkus API
└── mono-front/  # SvelteKit UI

## 使用ポート

- Frontend: http://localhost:5173
- Backend:  http://localhost:8080
- DB:       localhost:5432

## 前提条件

- Java 17
- Node.js（推奨: LTS）
- pnpm
- Docker / Docker Compose

## 環境（DB）

PostgreSQL（Docker）の設定

- DB名: `userdb`
- ユーザー: `user`
- パスワード: `password`
- ポート: `5432`

## 起動手順

### 1 DB 起動（PostgreSQL）

```bash
cd mono-back
docker compose -f src/main/docker/postgres/docker-compose.yml up -d
````

停止する場合：

```bash
docker compose -f src/main/docker/postgres/docker-compose.yml down
```

### 2 Backend 起動（Quarkus）

```bash
cd mono-back
./mvnw quarkus:dev
```

### 3 Frontend 起動（SvelteKit）

```bash
cd mono-front
pnpm install
pnpm dev
```

## 画面ルーティング（SvelteKit）

* 一覧：`/users`

  * `mono-front/src/routes/users/+page.svelte`
* 詳細：`/users/[id]`

  * `mono-front/src/routes/users/[id]/+page.svelte`
  * `mono-front/src/routes/users/[id]/+page.server.ts`
* 新規登録：`/users/new`

  * `mono-front/src/routes/users/new/+page.svelte`

## API（Quarkus）

ベースURL：`http://localhost:8080`

* `GET /users?id=&name=`

  * 一覧/検索（id, name は任意）
* `GET /users/{id}`

  * 詳細
* `POST /users`

  * 新規作成
  * body: `{"name":"..."}`
* `PUT /users/{id}`

  * 更新
  * body: `{"name":"..."}`
* `DELETE /users/{id}`

  * 削除（成功時: `204 No Content`）

実装位置：

* Resource: `mono-back/src/main/java/jp/co/monocrea/resource/UserResource.java`
* DTO:

  * `mono-back/src/main/java/jp/co/monocrea/resource/CreateUserRequest.java`
  * `mono-back/src/main/java/jp/co/monocrea/resource/UpdateUserRequest.java`

---

## DB / マイグレーション（Flyway）

Flyway は起動時に自動実行されます。

* スキーマ作成:
  `mono-back/src/main/resources/db/migration/V1.0.0__create_users.sql`
* 初期データ投入:
  `mono-back/src/main/resources/db/migration/V1.0.1__insert_users.sql`

---

## 設定（Backend）

DB 接続（`mono-back/src/main/resources/application.properties`）：

* `quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/userdb`
* `quarkus.datasource.username=user`
* `quarkus.datasource.password=password`

---

## 設定（Frontend）

API の接続先は `mono-front/src/lib/api.ts` に定義されています。

```ts
export const BASE_URL = 'http://localhost:8080';
```
