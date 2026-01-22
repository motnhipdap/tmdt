# API Đăng ký với xác thực OTP

## Tổng quan
Hệ thống đăng ký tài khoản với xác thực OTP qua email gồm 2 bước:
1. Gửi OTP về email
2. Xác thực OTP và tạo tài khoản

## Cấu hình Email

### Gmail
1. Truy cập: https://myaccount.google.com/apppasswords
2. Tạo App Password mới
3. Cập nhật trong `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### Mailgun
```properties
spring.mail.host=smtp.mailgun.org
spring.mail.port=587
spring.mail.username=postmaster@your-domain.mailgun.org
spring.mail.password=your-mailgun-password
```

### SendGrid
```properties
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=your-sendgrid-api-key
```

## API Endpoints

### 1. Gửi OTP về Email

**Endpoint:** `POST /api/v1/auth/send-otp`

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "OTP đã được gửi về email user@example.com",
  "data": null
}
```

**Response Error:**
- Email đã tồn tại (500):
```json
{
  "success": false,
  "message": "Email đã được đăng ký",
  "data": null
}
```

- Email không hợp lệ (400):
```json
{
  "success": false,
  "message": "Email không đúng định dạng",
  "data": null
}
```

### 2. Xác thực OTP và Đăng ký

**Endpoint:** `POST /api/v1/auth/register`

**Request Body:**
```json
{
  "email": "user@example.com",
  "otpCode": "123456",
  "username": "johndoe",
  "password": "password123",
  "phone": "0123456789"
}
```

**Response Success (201):**
```json
{
  "success": true,
  "message": "Đăng ký tài khoản thành công",
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "user@example.com",
    "phone": "0123456789",
    "role": "customer",
    "status": "active",
    "createdAt": "2026-01-22T10:30:00",
    "updatedAt": "2026-01-22T10:30:00"
  }
}
```

**Response Error:**

- OTP không hợp lệ (400):
```json
{
  "success": false,
  "message": "OTP không hợp lệ hoặc đã hết hạn",
  "data": null
}
```

- Username đã tồn tại (400):
```json
{
  "success": false,
  "message": "Tên đăng nhập đã tồn tại",
  "data": null
}
```

- Phone đã tồn tại (400):
```json
{
  "success": false,
  "message": "Số điện thoại đã được đăng ký",
  "data": null
}
```

## Validation Rules

### Email
- Không được để trống
- Phải đúng định dạng email

### OTP Code
- Không được để trống
- Phải có đúng 6 ký tự

### Username
- Không được để trống
- Tối thiểu 3 ký tự, tối đa 50 ký tự

### Password
- Không được để trống
- Tối thiểu 6 ký tự

### Phone
- Không được để trống
- Phải có đúng 10 chữ số

## Flow Đăng ký

```
1. User nhập email
   ↓
2. Frontend gọi POST /api/v1/auth/send-otp
   ↓
3. Backend kiểm tra email chưa tồn tại
   ↓
4. Backend tạo OTP (6 số random), lưu DB với expires_at = now + 5 phút
   ↓
5. Backend gửi email chứa OTP
   ↓
6. User nhập OTP + thông tin đăng ký
   ↓
7. Frontend gọi POST /api/v1/auth/register
   ↓
8. Backend verify OTP (kiểm tra code, email, chưa verify, chưa hết hạn)
   ↓
9. Backend kiểm tra username, phone chưa tồn tại
   ↓
10. Backend tạo account, mark OTP là verified
    ↓
11. Backend gửi email chào mừng
    ↓
12. Return account info
```

## OTP Details

- **Độ dài:** 6 số (100000 - 999999)
- **Thời gian hết hạn:** 5 phút
- **Tự động xóa:** Các OTP hết hạn tự động xóa mỗi giờ
- **Logic:** Khi gửi OTP mới, tất cả OTP cũ chưa verify của email đó sẽ bị xóa

## Testing với Postman/cURL

### Step 1: Gửi OTP
```bash
curl -X POST http://localhost:8080/api/v1/auth/send-otp \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com"}'
```

### Step 2: Đăng ký
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "otpCode": "123456",
    "username": "testuser",
    "password": "password123",
    "phone": "0123456789"
  }'
```

## TODO - Improvements

1. **Security:**
   - [ ] Hash password bằng BCrypt trước khi lưu
   - [ ] Rate limiting cho API send-otp (tránh spam)
   - [ ] CAPTCHA để tránh bot

2. **Features:**
   - [ ] Resend OTP (với cooldown time)
   - [ ] Forgot password với OTP
   - [ ] Email template đẹp hơn (HTML email)

3. **Database:**
   - [ ] Index optimization
   - [ ] Cleanup old accounts (chưa verify sau X ngày)
