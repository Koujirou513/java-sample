# マルチステージビルドを使用
FROM eclipse-temurin:21-jdk-alpine as builder

# 作業ディレクトリを設定
WORKDIR /app

# Gradleラッパーをコピー
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# 依存関係をダウンロード（キャッシュ効率化）
RUN ./gradlew dependencies --no-daemon

# ソースコードをコピー
COPY src/ src/

# アプリケーションをビルド
RUN ./gradlew bootJar --no-daemon

# 実行用のイメージ
FROM eclipse-temurin:21-jre-alpine

# curlをインストール（ヘルスチェック用）
RUN apk add --no-cache curl

# 非rootユーザーを作成
RUN addgroup --system spring && adduser --system --ingroup spring spring

# 作業ディレクトリを設定
WORKDIR /app

# ビルド済みのJARファイルをコピー
COPY --from=builder /app/build/libs/*.jar app.jar

# ファイルの所有者を変更
RUN chown spring:spring app.jar

# 非rootユーザーに切り替え
USER spring

# ポートを公開
EXPOSE 8080

# ヘルスチェック
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# アプリケーションを実行
ENTRYPOINT ["java", "-jar", "app.jar"]