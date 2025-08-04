# Gsc-be-core

## Technologies

- Spring boot 3.4.1
- MySQL 9+
- Gradle 8.11.1+
- Mapstruct 1.6.3, Lombok Mapstruct Binding 0.2.0 (2020)
- Redocly - 1.26.1

## Setup Dev Environment

- Open in VSCode -> Open in Workspace
- There should be `.env` file to in `src/main/resources` to configure environment variables

## Project Structure

```bash
gsc-be-core/
├── .vscode/                    # VSCodeの設定ファイル
├── docker/                     # Dockerの設定ファイル
│   ├── docker-compose.yaml     # データベース用のDockerコンポーズ設定
│   ├── db                      # Db config
│   └── gsc-be-core/            # バックエンドサービス用のDocker設定
├── k8s/                        # Kubernetesの設定ファイル
├── scripts/                    # 各種スクリプトファイル
│   ├── generate-fe.sh          # フロントエンド用API仕様生成スクリプト
│   └── generate-spring.sh      # Spring Boot用API仕様生成スクリプト
├── specs/                      # API仕様書のソースファイル
├── src/
│   └── main/
│       ├── java/jp/co/goalist/gsc/
│       │   ├── common/         # 共通機能とユーティリティクラス
│       │   ├── configurations/ # アプリケーション設定
│       │   ├── controllers/    # APIエンドポイントコントローラー
│       │   ├── dtos/           # データ転送オブジェクト
│       │   ├── entities/       # データベースエンティティモデル
│       │   ├── enums/          # 列挙型クラス（定数と固定値の定義）
│       │   ├── exceptions/     # 例外処理クラス
│       │   ├── gen/            # OpenAPI generated code
│       │   ├── mappers/        # エンティティとDTOの間のデータ変換を行うMapStructマッパークラス
│       │   ├── middlewares/    # リクエスト処理の前後に実行される共通処理（フィルタ、インターセプタなど）
│       │   ├── repositories/   # データアクセスレイヤー
│       │   ├── services/       # ビジネスロジック
│       │   └── util/           # ユーティリティクラス
│       └── resources/
│           ├── application-test.yml # テスト用設定ファイル
│           ├── application.yml      # メイン設定ファイル
│           ├── .env                 # 環境変数設定ファイル
│           └── db/changelog/        # データベースマイグレーションスクリプト
├── README.md                   # プロジェクトの説明
├── build.gradle                # Gradleビルド設定
└── settings.gradle             # Gradleプロジェクト設定
```

## Useful tips

Every command should be run in the `gsc-be-core` directory

```bash

cd <path>/gsc-be-core

```

### To build project

```bash
./gradlew build

# build without test
./gradlew build -x test
```

### To run project

```bash
./gradlew bootRun
```

### To run tests

```bash
./gradlew test
```

### Generate the private key

```bash
# This command will generate a private key and write it to the path we want
openssl genrsa -out privateKey.pem
```

### Generate the public key

```bash
# Generate the public key based on private key
openssl rsa -pubout -in privateKey.pem -out publicKey.pem
```

### Convert to Base64 string (For local development)

#### Unix(macOS/Linux)

```bash
openssl base64 -in privateKey.pem | tr -d '\n' > <path-to-output>
```

_Note_: **Git Bash** also support this command.

#### Windows (PowerShell)

```bash
[Convert]::ToBase64String([System.IO.File]::ReadAllBytes("<path-to-private-pem>")) -replace "`n", "" | Out-File "<path-to-output>"
```

### Docker

Firstly, we need to create a network for related containers in local environment

```bash
docker network create gsc_network
```

Then, we will create the related containers with below commands.

_Note_: If there is no effects, please add `--build` flag after `-d`

#### gsc-db container

```bash
cd <path-to>/gsc-be-core

# gsc-db container
docker compose -f docker/docker-compose.yaml up -d
```

#### gsc-be-core container

```bash
cd <path-to>/gsc-be-core

# gsc-be-core container
docker compose -f docker/gsc-be-core/docker-compose.yaml up -d

```

#### To check whether the server is running

```bash
curl -i http://localhost:8080/core/healthcheck
```

### Redocly

```bash
npm i -g @redocly/cli@latest
```

### Gradle / Gradlew

```bash
# before generate code gen, add gradle (v8.12.x) wrapper to `specs/`
gradle wrapper
```

_Note_: if gradle is not found or error, please install gradle via [here](https://docs.gradle.org/current/userguide/installation.html).

### To generate API specification for Spring boot

```bash
bash scripts/generate-spring.sh
```

### To generate API specification for FE

```bash
bash scripts/generate-fe.sh
```

_Note_: After generating successfully, the output directory is `output/`
