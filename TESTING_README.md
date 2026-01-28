# Hướng dẫn Unit Testing cho Module Authorization

## Tổng quan

Dự án này đã được cài đặt đầy đủ các unit tests cho module authorization, bao gồm:

### 1. Service Layer Tests
- **AuthServiceImplTest**: Test cho service xác thực (đăng ký, đăng nhập, refresh token, logout)
- **AccountServiceImplTest**: Test cho service quản lý tài khoản (kiểm tra email/username, cập nhật mật khẩu, lấy profile)
- **JwtServiceImplTest**: Test cho service JWT (tạo token, trích xuất thông tin, validate token)
- **OtpServiceImplTest**: Test cho service OTP (gửi OTP, verify OTP, reset password)

### 2. Controller Layer Tests
- **AuthControllerTest**: Test cho controller xác thực
- **AccountControllerTest**: Test cho controller quản lý tài khoản
- **OtpControllerTest**: Test cho controller OTP
- **EmailUpdateControllerTest**: Test cho controller cập nhật email

## Cấu trúc Test

```
src/test/java/com/dev/dungcony/modules/authorization/
├── services/impl/
│   ├── AuthServiceImplTest.java
│   ├── AccountServiceImplTest.java
│   ├── JwtServiceImplTest.java
│   └── OtpServiceImplTest.java
├── controllers/
│   ├── AuthControllerTest.java
│   ├── AccountControllerTest.java
│   ├── OtpControllerTest.java
│   └── EmailUpdateControllerTest.java
└── TestConfig.java
```

## Chạy Tests

### Chạy tất cả tests
```bash
./mvnw test
```

### Chạy tests cho một class cụ thể
```bash
./mvnw test -Dtest=AuthServiceImplTest
```

### Chạy tests cho một method cụ thể
```bash
./mvnw test -Dtest=AuthServiceImplTest#login_Success
```

### Chạy tests cho module authorization
```bash
./mvnw test -Dtest=com.dev.dungcony.modules.authorization.**
```

### Chạy tests và tạo báo cáo coverage (nếu có plugin)
```bash
./mvnw clean test jacoco:report
```

## Dependencies Đã Thêm

Trong file `pom.xml`, đã thêm các dependencies sau:

```xml
<!-- Test Dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

## Các Test Case Chính

### AuthServiceImplTest
- ✅ Đăng ký thành công
- ✅ Đăng ký với email đã tồn tại
- ✅ Đăng ký với username đã tồn tại
- ✅ Đăng nhập thành công
- ✅ Đăng nhập với username không đúng
- ✅ Đăng nhập với password không đúng
- ✅ Refresh token thành công
- ✅ Logout thành công

### AccountServiceImplTest
- ✅ Kiểm tra email tồn tại
- ✅ Kiểm tra username tồn tại
- ✅ Cập nhật mật khẩu thành công
- ✅ Cập nhật mật khẩu với mật khẩu cũ sai
- ✅ Lấy profile theo ID thành công
- ✅ Lấy profile với ID không tồn tại

### JwtServiceImplTest
- ✅ Tạo token với username và role
- ✅ Tạo token với email
- ✅ Trích xuất username từ token
- ✅ Trích xuất user ID từ token
- ✅ Trích xuất role từ token
- ✅ Validate token hợp lệ
- ✅ Validate token không hợp lệ
- ✅ Kiểm tra token hết hạn

### OtpServiceImplTest
- ✅ Gửi OTP thành công
- ✅ Verify OTP thành công
- ✅ Verify OTP sai
- ✅ Verify OTP đã hết hạn
- ✅ Gửi reset password thành công

### Controller Tests
- ✅ Test các endpoints với request hợp lệ
- ✅ Test các endpoints với request không hợp lệ
- ✅ Test validation errors
- ✅ Test exception handling

## Công nghệ Sử dụng

- **JUnit 5**: Framework testing chính
- **Mockito**: Mock dependencies
- **MockMvc**: Test Spring MVC controllers
- **Spring Boot Test**: Test utilities cho Spring Boot
- **H2 Database**: In-memory database cho tests

## Best Practices

1. **Arrange-Act-Assert Pattern**: Tất cả tests đều tuân theo pattern AAA
2. **Descriptive Test Names**: Tên test mô tả rõ ràng kịch bản test
3. **Mock External Dependencies**: Sử dụng mock cho tất cả dependencies
4. **Isolated Tests**: Mỗi test độc lập, không phụ thuộc vào test khác
5. **Test Coverage**: Cover cả happy path và edge cases

## Metrics

Tổng số test cases: **60+**
- Service Layer: ~30 tests
- Controller Layer: ~30 tests

## Lưu Ý

1. Tests sử dụng H2 in-memory database, không ảnh hưởng đến database thật
2. Tests chạy với profile `test`, sử dụng config từ `application-test.yml`
3. Tất cả tests đều độc lập và có thể chạy song song
4. Mock được sử dụng cho tất cả external dependencies (Redis, Email, Database)

## Troubleshooting

### Lỗi "Cannot find bean"
- Kiểm tra `@MockBean` annotation
- Đảm bảo `@WebMvcTest` hoặc `@ExtendWith(MockitoExtension.class)` được sử dụng

### Lỗi "Security filter chain"
- Sử dụng `@AutoConfigureMockMvc(addFilters = false)` để disable security filters trong tests

### Lỗi "Validation failed"
- Kiểm tra DTO validation annotations
- Đảm bảo request body đúng format

## Mở Rộng

Để thêm test mới:

1. Tạo test class với annotation phù hợp (`@ExtendWith(MockitoExtension.class)` hoặc `@WebMvcTest`)
2. Mock các dependencies cần thiết
3. Viết test methods theo pattern AAA
4. Thêm `@DisplayName` để mô tả test
5. Verify kết quả và interactions với mocks

## Liên Hệ

Nếu có vấn đề với tests, vui lòng:
1. Kiểm tra logs chi tiết
2. Đảm bảo dependencies được cài đặt đúng
3. Verify version của Spring Boot và các thư viện liên quan
