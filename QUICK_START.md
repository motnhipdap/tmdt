# 🚀 Hướng dẫn chạy dự án nhanh

## Các cách chạy (từ nhanh đến chậm):

### 1. ⚡ Chạy JAR (NHANH NHẤT - sau lần build đầu)
```bash
./run-jar.sh
```
- **Lần đầu**: Build JAR (~30-60s)
- **Các lần sau**: Chạy trực tiếp (~2-3s)
- ✅ Nhanh nhất sau lần build đầu tiên
- ✅ Không cần Maven mỗi lần chạy

### 2. 🏃 Chạy nhanh với Maven (skip tests)
```bash
./run-fast.sh
# hoặc
./run.sh fast
```
- Skip tests → nhanh hơn 10-20s
- Vẫn compile code mới

### 3. 🔄 Chạy với DevTools (hot reload)
```bash
./run.sh dev
```
- **Hot reload**: Tự động reload khi code thay đổi
- Không cần restart khi sửa code
- ⚠️ Lần đầu vẫn chậm (~30-40s)

### 4. 📦 Chạy bình thường
```bash
./run.sh
# hoặc
mvn spring-boot:run
```

## 💡 Tips để chạy nhanh hơn:

### 1. Sử dụng IDE (IntelliJ IDEA / Eclipse)
- **Nhanh nhất**: Click Run trong IDE
- Hot reload tự động với DevTools
- Debug dễ dàng

### 2. Tắt DevTools trong production
```properties
# application.properties
spring.devtools.restart.enabled=false
```

### 3. Sử dụng JVM options
```bash
java -Xms512m -Xmx1024m -jar target/dungcony-0.0.1-SNAPSHOT.jar
```

### 4. Skip tests khi không cần
```bash
mvn spring-boot:run -DskipTests
```

## ⏱️ So sánh thời gian:

| Cách chạy | Lần đầu | Các lần sau |
|-----------|---------|-------------|
| JAR | ~60s | ~3s ⚡ |
| Fast (skip tests) | ~40s | ~40s |
| DevTools | ~35s | ~5s (hot reload) |
| Bình thường | ~50s | ~50s |

## 🎯 Khuyến nghị:

- **Development**: Dùng IDE hoặc `./run.sh dev` (hot reload)
- **Testing nhanh**: Dùng `./run-jar.sh` (sau lần build đầu)
- **Production**: Build JAR và deploy
