# 🛒 Đi Chợ Tiện Lợi (Smart Grocery) - Backend API

Backend API cho ứng dụng di động đa nền tảng giúp các gia đình quản lý danh sách mua sắm, theo dõi thực phẩm trong tủ lạnh (bao gồm hạn sử dụng), và lập kế hoạch bữa ăn.

## 📋 Mục Lục

- [Tổng Quan](#tổng-quan)
- [Công Nghệ Sử Dụng](#công-nghệ-sử-dụng)
- [Tính Năng](#tính-năng)
- [Cấu Trúc Dự Án](#cấu-trúc-dự-án)
- [Hướng Dẫn Cài Đặt](#hướng-dẫn-cài-đặt)
- [Tài Khoản Test](#tài-khoản-test)
- [Tài Liệu API](#tài-liệu-api)
- [Hướng Dẫn Chi Tiết Cho Frontend](#hướng-dẫn-chi-tiết-cho-frontend)
- [Sơ Đồ Database](#sơ-đồ-database)
- [Bảo Mật](#bảo-mật)
- [Xử Lý Lỗi](#xử-lý-lỗi)
- [Tác Vụ Nền](#tác-vụ-nền)
- [Danh Sách API](#danh-sách-api)

## 🎯 Tổng Quan

Đi Chợ Tiện Lợi là hệ thống quản lý thực phẩm gia đình toàn diện với các tính năng:
- **Quản lý Gia đình**: Tạo gia đình, mời thành viên bằng mã code, phân quyền
- **Danh sách Mua sắm**: Danh sách mua sắm cộng tác với xử lý xung đột đồng thời
- **Quản lý Tủ lạnh**: Theo dõi thực phẩm trong tủ lạnh và hạn sử dụng
- **Lập kế hoạch Bữa ăn**: Lên kế hoạch bữa ăn trong tuần kết hợp công thức nấu ăn
- **Quản lý Công thức**: Lưu trữ và chia sẻ công thức, gợi ý dựa trên nguyên liệu có sẵn
- **Thông báo**: Cảnh báo hàng ngày về thực phẩm sắp hết hạn

## 🛠 Công Nghệ Sử Dụng

| Thành phần | Công nghệ |
|-----------|------------|
| Ngôn ngữ | Kotlin 1.9.21 |
| Framework | Spring Boot 3.2.1 |
| Cơ sở dữ liệu | PostgreSQL |
| Migration | Flyway |
| Xác thực | JWT (Access + Refresh Token) |
| Tài liệu API | OpenAPI (Swagger) |
| Object Mapping | MapStruct |
| JSON | Jackson (Kotlin Module) |
| **Lưu trữ ảnh** | **Cloudinary CDN** 🆕 |

## ✨ Tính Năng

### 1. Xác Thực & Quản Lý Người Dùng
- ✅ Đăng ký người dùng với xác thực email
- ✅ Xác thực JWT (Access + Refresh token)
- ✅ Mã hóa mật khẩu với BCrypt
- ✅ Phân quyền theo vai trò (ADMIN, USER)
- ✅ Quản lý hồ sơ cá nhân
- ✅ Lưu trữ FCM token cho push notification
- ✅ **Tìm kiếm người dùng** với xếp hạng kết quả thông minh

### 2. Hệ Thống Bạn Bè 🆕
- ✅ Gửi lời mời kết bạn
- ✅ Chấp nhận / Từ chối lời mời kết bạn
- ✅ Xem danh sách bạn bè
- ✅ Xem danh sách lời mời đang chờ
- ✅ Hủy kết bạn
- ✅ Kiểm tra trạng thái bạn bè giữa 2 người dùng

### 3. Quản Lý Gia Đình
- ✅ Tạo gia đình với mã mời duy nhất
- ✅ **Upload ảnh đại diện gia đình** (Multipart Form)
- ✅ **Mời bạn bè vào gia đình** khi tạo
- ✅ Tham gia gia đình bằng mã mời
- ✅ Phân quyền theo vai trò (TRƯỞNG NHÓM, THÀNH VIÊN)
- ✅ Quản lý thành viên (thêm, xóa, cập nhật vai trò)
- ✅ Tạo lại mã mời
- ✅ Rời gia đình / Xóa gia đình

### 4. Lời Mời Gia Đình 🆕
- ✅ Mời bạn bè vào gia đình (chỉ Leader)
- ✅ Xem danh sách lời mời đang chờ
- ✅ Chấp nhận / Từ chối lời mời vào gia đình
- ✅ Tự động thêm thành viên khi chấp nhận lời mời

### 5. Danh Sách Mua Sắm
- ✅ Tạo danh sách mua sắm với các mục
- ✅ **Optimistic Locking** cho chỉnh sửa đồng thời
- ✅ Chọn sản phẩm linh hoạt (sản phẩm có sẵn HOẶC tên tùy chỉnh)
- ✅ Phân công mục cho thành viên gia đình
- ✅ Theo dõi trạng thái đã mua và người mua
- ✅ Thêm nhiều mục cùng lúc
- ✅ Lọc theo trạng thái (ĐANG LẬP, ĐANG MUA, HOÀN THÀNH)

### 6. Quản Lý Tủ Lạnh
- ✅ Thêm thực phẩm với ngày hết hạn
- ✅ Nhiều vị trí lưu trữ (NGĂN ĐÁ, NGĂN MÁT, TỦ ĐỒ KHÔ)
- ✅ Theo dõi trạng thái (TƯƠI, SẮP HẾT HẠN, HẾT HẠN, ĐÃ DÙNG, ĐÃ BỎ)
- ✅ Tiêu thụ một phần số lượng
- ✅ Lọc theo vị trí, trạng thái, hạn sử dụng
- ✅ Bảng thống kê tủ lạnh
- ✅ Tự động cập nhật trạng thái hết hạn

### 7. Công Thức Nấu Ăn
- ✅ Tạo/chỉnh sửa công thức với nguyên liệu
- ✅ Công thức công khai và riêng tư
- ✅ Mức độ khó (DỄ, TRUNG BÌNH, KHÓ)
- ✅ Theo dõi thời gian chuẩn bị và nấu
- ✅ **Gợi ý công thức** dựa trên nguyên liệu trong tủ lạnh
- ✅ Tính toán phần trăm nguyên liệu khớp

### 8. Lập Kế Hoạch Bữa Ăn
- ✅ Kế hoạch bữa ăn **Master-Detail** (Kế hoạch + Món ăn)
- ✅ Ràng buộc duy nhất: một kế hoạch cho mỗi gia đình/ngày/loại bữa
- ✅ Loại bữa ăn: SÁNG, TRƯA, TỐI, PHỤ
- ✅ Xem theo ngày và tuần
- ✅ Liên kết công thức hoặc dùng tên món tùy chỉnh

### 9. Upload & Lưu Trữ Ảnh (Cloudinary) 🆕
- ✅ Upload ảnh lên Cloudinary CDN (JPG, PNG, GIF, WebP)
- ✅ Giới hạn kích thước file (mặc định 5MB)
- ✅ Tự động tối ưu hóa ảnh (quality: auto, format: auto)
- ✅ **Trả về full URL Cloudinary** cho frontend sử dụng trực tiếp
- ✅ Hỗ trợ ảnh đại diện user và ảnh gia đình
- ✅ Tự động xóa ảnh cũ khi cập nhật

### 10. Tác Vụ Nền
- ✅ Kiểm tra hết hạn hàng ngày (8 giờ sáng)
- ✅ Cập nhật trạng thái hàng giờ cho thực phẩm hết hạn
- ✅ Push notification mô phỏng (sẵn sàng tích hợp FCM)

### 11. Dữ Liệu Danh Mục (Admin)
- ✅ Quản lý danh mục với icon
- ✅ Danh mục sản phẩm mẫu
- ✅ Quan hệ sản phẩm-danh mục
- ✅ Thông tin thời hạn sử dụng mặc định

## 📁 Cấu Trúc Dự Án

```
src/main/kotlin/com/smartgrocery/
├── SmartGroceryApplication.kt      # Điểm khởi chạy ứng dụng
├── config/                          # Các class cấu hình
│   ├── JpaConfig.kt
│   ├── JwtConfig.kt
│   ├── OpenApiConfig.kt
│   └── SecurityConfig.kt
├── controller/                      # REST Controllers
│   ├── AuthController.kt
│   ├── CategoryController.kt
│   ├── FamilyController.kt
│   ├── FileController.kt            # 🆕 Phục vụ file tĩnh
│   ├── FriendController.kt          # 🆕 Quản lý bạn bè
│   ├── FridgeController.kt
│   ├── MealPlanController.kt
│   ├── ProductController.kt
│   ├── RecipeController.kt
│   ├── ShoppingListController.kt
│   └── UserController.kt            # 🆕 Tìm kiếm user
├── dto/                             # Data Transfer Objects
│   ├── auth/
│   ├── category/
│   ├── common/
│   ├── family/
│   ├── fridge/
│   ├── friendship/                  # 🆕 DTOs cho bạn bè
│   ├── mealplan/
│   ├── product/
│   ├── recipe/
│   ├── shopping/
│   └── user/                        # 🆕 DTOs cho user
├── entity/                          # JPA Entities
│   ├── BaseEntity.kt
│   ├── Category.kt
│   ├── Family.kt
│   ├── FamilyInvitation.kt          # 🆕 Lời mời gia đình
│   ├── FamilyMember.kt
│   ├── FridgeItem.kt
│   ├── Friendship.kt                # 🆕 Quan hệ bạn bè
│   ├── MasterProduct.kt
│   ├── MealItem.kt
│   ├── MealPlan.kt
│   ├── Recipe.kt
│   ├── RecipeIngredient.kt
│   ├── Role.kt
│   ├── ShoppingItem.kt
│   ├── ShoppingList.kt
│   └── User.kt
├── exception/                       # Xử lý ngoại lệ
│   ├── Exceptions.kt
│   └── GlobalExceptionHandler.kt
├── repository/                      # JPA Repositories
├── scheduler/                       # Tác vụ nền
│   ├── ExpirationNotificationScheduler.kt
│   └── NotificationService.kt
├── security/                        # Các thành phần bảo mật
│   ├── CustomUserDetails.kt
│   ├── CustomUserDetailsService.kt
│   ├── JwtAuthenticationFilter.kt
│   └── JwtTokenProvider.kt
└── service/                         # Logic nghiệp vụ
    ├── AuthService.kt
    ├── CategoryService.kt
    ├── CloudinaryService.kt         # 🆕 Upload ảnh lên Cloudinary
    ├── FamilyService.kt
    ├── FridgeService.kt
    ├── FriendshipService.kt         # 🆕 Quản lý bạn bè
    ├── MealPlanService.kt
    ├── ProductService.kt
    ├── RecipeService.kt
    └── ShoppingListService.kt

src/main/resources/
├── application.yml                  # Cấu hình ứng dụng
└── db/migration/
    ├── V1__Initial_Schema.sql       # Migration database
    ├── V3__Add_Friendships_And_Family_Image.sql  # 🆕 Bạn bè & Lời mời gia đình
    └── V4__Add_Sample_Users.sql     # 🆕 User mẫu cho test
```

## 🚀 Hướng Dẫn Cài Đặt

### Yêu Cầu Hệ Thống

- JDK 17+
- PostgreSQL 13+
- Gradle 8+

### Thiết Lập Database

```sql
CREATE DATABASE smart_grocery;
```

### Cấu Hình

Cập nhật file `src/main/resources/application.yml` hoặc tạo file `.env`:

```yaml
# Database
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smart_grocery
    username: tên_đăng_nhập
    password: mật_khẩu

# JWT
jwt:
  secret: khóa-bí-mật-256-bit-ít-nhất-32-ký-tự

# Cloudinary (bắt buộc cho upload ảnh)
cloudinary:
  cloud-name: your-cloud-name
  api-key: your-api-key
  api-secret: your-api-secret
  folder: smart-grocery
```

#### Thiết lập Cloudinary:

1. Đăng ký tài khoản miễn phí tại [cloudinary.com](https://cloudinary.com)
2. Vào Dashboard → lấy thông tin:
   - Cloud name
   - API Key
   - API Secret
3. Thêm vào file `.env`:
```env
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=123456789012345
CLOUDINARY_API_SECRET=your-api-secret
```

### Chạy Ứng Dụng

```bash
# Sử dụng Gradle
./gradlew bootRun

# Hoặc build và chạy JAR
./gradlew build
java -jar build/libs/smart-grocery-1.0.0.jar
```

### Truy Cập Swagger UI

Mở http://localhost:8080/swagger-ui.html

## 👤 Tài Khoản Test

Hệ thống có sẵn các tài khoản để test. **Password cho tất cả là: `123456`**

### Tài Khoản Admin
| Username | Email | Full Name | Role |
|----------|-------|-----------|------|
| `admin` | admin@smartgrocery.com | System Admin | ADMIN, USER |

### Tài Khoản User Thường
| Username | Email | Full Name |
|----------|-------|-----------|
| `nguyenvana` | nguyenvana@gmail.com | Nguyễn Văn A |
| `tranthib` | tranthib@gmail.com | Trần Thị B |
| `levanc` | levanc@gmail.com | Lê Văn C |
| `phamthid` | phamthid@gmail.com | Phạm Thị D |
| `hoangvane` | hoangvane@gmail.com | Hoàng Văn E |
| `vuthif` | vuthif@gmail.com | Vũ Thị F |
| `dangvang` | dangvang@gmail.com | Đặng Văn G |
| `buithih` | buithih@gmail.com | Bùi Thị H |
| `dovani` | dovani@gmail.com | Đỗ Văn I |
| `ngothik` | ngothik@gmail.com | Ngô Thị K |

## 📚 Tài Liệu API

### Định Dạng Response Chuẩn

Tất cả API trả về JSON theo định dạng thống nhất:

```json
{
  "code": 1000,
  "message": "Thành công",
  "data": { ... }
}
```

### Mã Lỗi

| Mã | Mô tả |
|------|-------------|
| 1000 | Thành công |
| 1001 | Tạo mới thành công |
| 1100 | Yêu cầu không hợp lệ |
| 1101 | Lỗi xác thực dữ liệu |
| 1102 | Chưa xác thực |
| 1103 | Không có quyền |
| 1104 | Không tìm thấy |
| 1105 | Xung đột dữ liệu |
| **1106** | **Lỗi đồng thời (Optimistic Lock)** |
| 1200 | Thông tin đăng nhập không hợp lệ |
| 1300+ | Lỗi liên quan người dùng |
| 1400+ | Lỗi liên quan gia đình |
| 1500+ | Lỗi liên quan danh sách mua sắm |
| 1600+ | Lỗi liên quan tủ lạnh |
| 1700+ | Lỗi liên quan công thức |
| 1800+ | Lỗi liên quan kế hoạch bữa ăn |
| **2000+** | **Lỗi liên quan bạn bè** |
| **2100+** | **Lỗi liên quan lời mời gia đình** |
| **2200+** | **Lỗi liên quan file upload** |
| 5000 | Lỗi máy chủ nội bộ |

## 📖 Hướng Dẫn Chi Tiết Cho Frontend

### 1. Tìm Kiếm Người Dùng

API tìm kiếm người dùng hỗ trợ tìm theo username, full name, và email.

**Endpoint:** `GET /api/v1/users/search?keyword={keyword}&page=0&size=20`

**Ví dụ:**
```bash
# Tìm user có tên chứa "nguyen"
curl -X GET 'http://localhost:8080/api/v1/users/search?keyword=nguyen' \
  -H 'Authorization: Bearer <token>'
```

**Response:**
```json
{
  "code": 1000,
  "message": "Success",
  "data": {
    "content": [
      {
        "id": 2,
        "username": "nguyenvana",
        "fullName": "Nguyễn Văn A",
        "email": "nguyenvana@gmail.com"
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

**Lưu ý:**
- Kết quả được xếp hạng: ưu tiên match chính xác, sau đó match bắt đầu bằng keyword
- User hiện tại sẽ bị loại khỏi kết quả

---

### 2. Hệ Thống Bạn Bè

#### 2.1. Gửi Lời Mời Kết Bạn

**Endpoint:** `POST /api/v1/friends/request/{userId}`

```bash
curl -X POST 'http://localhost:8080/api/v1/friends/request/3' \
  -H 'Authorization: Bearer <token>'
```

**Response:**
```json
{
  "code": 1001,
  "message": "Friend request sent",
  "data": {
    "id": 1,
    "requester": {
      "id": 2,
      "username": "nguyenvana",
      "fullName": "Nguyễn Văn A"
    },
    "addressee": {
      "id": 3,
      "username": "tranthib",
      "fullName": "Trần Thị B"
    },
    "status": "PENDING",
    "createdAt": "2025-01-01T10:00:00Z"
  }
}
```

#### 2.2. Xem Lời Mời Đang Chờ

**Endpoint:** `GET /api/v1/friends/pending`

```bash
curl -X GET 'http://localhost:8080/api/v1/friends/pending' \
  -H 'Authorization: Bearer <token>'
```

#### 2.3. Chấp Nhận / Từ Chối Lời Mời

**Endpoint:** `POST /api/v1/friends/respond/{friendshipId}`

```bash
# Chấp nhận
curl -X POST 'http://localhost:8080/api/v1/friends/respond/1' \
  -H 'Authorization: Bearer <token>' \
  -H 'Content-Type: application/json' \
  -d '{"accept": true}'

# Từ chối
curl -X POST 'http://localhost:8080/api/v1/friends/respond/1' \
  -H 'Authorization: Bearer <token>' \
  -H 'Content-Type: application/json' \
  -d '{"accept": false}'
```

#### 2.4. Xem Danh Sách Bạn Bè

**Endpoint:** `GET /api/v1/friends`

```bash
curl -X GET 'http://localhost:8080/api/v1/friends' \
  -H 'Authorization: Bearer <token>'
```

**Response:**
```json
{
  "code": 1000,
  "message": "Success",
  "data": [
    {
      "id": 3,
      "username": "tranthib",
      "fullName": "Trần Thị B",
      "email": "tranthib@gmail.com"
    }
  ]
}
```

#### 2.5. Hủy Kết Bạn

**Endpoint:** `DELETE /api/v1/friends/{friendId}`

```bash
curl -X DELETE 'http://localhost:8080/api/v1/friends/3' \
  -H 'Authorization: Bearer <token>'
```

#### 2.6. Kiểm Tra Trạng Thái Bạn Bè

**Endpoint:** `GET /api/v1/friends/status/{userId}`

```bash
curl -X GET 'http://localhost:8080/api/v1/friends/status/3' \
  -H 'Authorization: Bearer <token>'
```

**Response:**
```json
{
  "code": 1000,
  "data": {
    "userId": 3,
    "status": "ACCEPTED",  // "NONE", "PENDING_SENT", "PENDING_RECEIVED", "ACCEPTED"
    "friendshipId": 1
  }
}
```

---

### 3. Tạo Gia Đình Với Ảnh & Mời Bạn Bè

Khi tạo gia đình mới, bạn **BẮT BUỘC phải mời ít nhất 1 bạn bè**.

**Endpoint:** `POST /api/v1/families` (multipart/form-data)

**Các field:**
| Field | Type | Required | Mô tả |
|-------|------|----------|-------|
| `name` | string | ✅ | Tên gia đình |
| `description` | string | ❌ | Mô tả |
| `friendIds` | array | ✅ | Danh sách ID bạn bè cần mời (ít nhất 1) |
| `image` | file | ❌ | Ảnh đại diện gia đình |

**Ví dụ với cURL:**
```bash
curl -X POST 'http://localhost:8080/api/v1/families' \
  -H 'Authorization: Bearer <token>' \
  -F 'name=Gia đình Nguyễn' \
  -F 'description=Gia đình hạnh phúc' \
  -F 'friendIds=3' \
  -F 'friendIds=4' \
  -F 'image=@/path/to/family-photo.jpg'
```

**Ví dụ với JavaScript (FormData):**
```javascript
const formData = new FormData();
formData.append('name', 'Gia đình Nguyễn');
formData.append('description', 'Gia đình hạnh phúc');
formData.append('friendIds', '3');
formData.append('friendIds', '4');
formData.append('image', imageFile);

const response = await fetch('/api/v1/families', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`
  },
  body: formData
});
```

**Response:**
```json
{
  "code": 1001,
  "message": "Family created successfully",
  "data": {
    "id": 1,
    "name": "Gia đình Nguyễn",
    "description": "Gia đình hạnh phúc",
    "imageUrl": "/files/families/abc123-family.jpg",
    "inviteCode": "ABC123",
    "createdBy": {
      "id": 2,
      "username": "nguyenvana",
      "fullName": "Nguyễn Văn A"
    },
    "memberCount": 1,
    "createdAt": "2025-01-01T10:00:00Z"
  }
}
```

---

### 4. Xem & Phản Hồi Lời Mời Gia Đình

#### 4.1. Xem Lời Mời Đang Chờ

**Endpoint:** `GET /api/v1/families/invitations`

```bash
curl -X GET 'http://localhost:8080/api/v1/families/invitations' \
  -H 'Authorization: Bearer <token>'
```

**Response:**
```json
{
  "code": 1000,
  "data": [
    {
      "id": 1,
      "familyId": 1,
      "familyName": "Gia đình Nguyễn",
      "inviter": {
        "id": 2,
        "username": "nguyenvana",
        "fullName": "Nguyễn Văn A"
      },
      "invitee": {
        "id": 3,
        "username": "tranthib",
        "fullName": "Trần Thị B"
      },
      "status": "PENDING",
      "createdAt": "2025-01-01T10:00:00Z"
    }
  ]
}
```

#### 4.2. Chấp Nhận / Từ Chối Lời Mời

**Endpoint:** `POST /api/v1/families/invitations/{invitationId}/respond`

```bash
# Chấp nhận
curl -X POST 'http://localhost:8080/api/v1/families/invitations/1/respond' \
  -H 'Authorization: Bearer <token>' \
  -H 'Content-Type: application/json' \
  -d '{"accept": true}'

# Từ chối
curl -X POST 'http://localhost:8080/api/v1/families/invitations/1/respond' \
  -H 'Authorization: Bearer <token>' \
  -H 'Content-Type: application/json' \
  -d '{"accept": false}'
```

**Lưu ý:** Khi chấp nhận lời mời, user sẽ tự động được thêm vào gia đình với role MEMBER.

#### 4.3. Mời Thêm Bạn Bè Vào Gia Đình (Leader Only)

**Endpoint:** `POST /api/v1/families/{familyId}/invite/{friendId}`

```bash
curl -X POST 'http://localhost:8080/api/v1/families/1/invite/5' \
  -H 'Authorization: Bearer <token>'
```

---

### 5. Cập Nhật Gia Đình Với Ảnh Mới

**Endpoint:** `PUT /api/v1/families/{id}` (multipart/form-data)

```bash
curl -X PUT 'http://localhost:8080/api/v1/families/1' \
  -H 'Authorization: Bearer <token>' \
  -F 'name=Gia đình Nguyễn (Updated)' \
  -F 'image=@/path/to/new-photo.jpg'
```

---

### 6. Sử Dụng Ảnh Từ API (Cloudinary) 🆕

⚠️ **QUAN TRỌNG**: Ảnh giờ được lưu trên **Cloudinary CDN** và API trả về **full URL**.

#### Các field trả về URL ảnh:
| API | Field | Mô tả |
|-----|-------|-------|
| `GET /api/v1/auth/me` | `avatarUrl` | Ảnh đại diện user |
| `GET /api/v1/families/{id}` | `imageUrl` | Ảnh đại diện gia đình |
| `POST /api/v1/families` | `imageUrl` | Ảnh gia đình sau khi tạo |

#### Định dạng URL Cloudinary:
```
https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{folder}/{public_id}.{format}
```

#### Ví dụ Response:
```json
{
  "code": 1000,
  "data": {
    "id": 1,
    "name": "Gia đình Nguyễn",
    "imageUrl": "https://res.cloudinary.com/your-cloud/image/upload/v1234567890/smart-grocery/families/abc123.jpg",
    ...
  }
}
```

#### Sử dụng trong Frontend:

**React/React Native:**
```jsx
// imageUrl đã là full URL, dùng trực tiếp
<Image source={{ uri: family.imageUrl }} />

// Hoặc với fallback
<Image 
  source={{ uri: family.imageUrl || 'https://placehold.co/200x200?text=No+Image' }} 
/>
```

**HTML:**
```html
<!-- Sử dụng trực tiếp URL từ API -->
<img src="${family.imageUrl}" alt="Family photo" />
```

**Flutter:**
```dart
Image.network(family.imageUrl ?? 'https://placehold.co/200x200')
```

#### ⚠️ Lưu ý:
- **KHÔNG** cần ghép URL base server nữa
- `imageUrl` và `avatarUrl` có thể là `null` nếu chưa upload ảnh
- Cloudinary tự động tối ưu ảnh (format, quality)
- Ảnh được cache trên CDN toàn cầu → load nhanh

---

### 7. Flow Tạo Gia Đình Hoàn Chỉnh

```
1. User A đăng nhập
2. User A tìm kiếm người dùng: GET /api/v1/users/search?keyword=...
3. User A gửi lời mời kết bạn cho User B: POST /api/v1/friends/request/{userBId}
4. User B đăng nhập, xem lời mời: GET /api/v1/friends/pending
5. User B chấp nhận: POST /api/v1/friends/respond/{friendshipId} { "accept": true }
6. User A tạo gia đình và mời User B:
   POST /api/v1/families (form-data với friendIds=[userBId])
7. User B xem lời mời gia đình: GET /api/v1/families/invitations
8. User B chấp nhận: POST /api/v1/families/invitations/{id}/respond { "accept": true }
9. User B giờ là thành viên gia đình!
```

---

### 8. Error Codes Mới

| Code | Message | Mô tả |
|------|---------|-------|
| 2000 | Friendship not found | Không tìm thấy quan hệ bạn bè |
| 2001 | Friend request already exists | Lời mời kết bạn đã tồn tại |
| 2002 | Cannot send request to self | Không thể gửi lời mời cho chính mình |
| 2003 | Not friends | Hai người chưa là bạn bè |
| 2004 | Already friends | Đã là bạn bè rồi |
| 2005 | Friend request not pending | Lời mời không ở trạng thái chờ |
| 2006 | Not your friend request | Lời mời này không phải của bạn |
| 2100 | Family invitation not found | Không tìm thấy lời mời gia đình |
| 2101 | Not invited to family | Bạn không được mời vào gia đình này |
| 2102 | Invitation not pending | Lời mời không ở trạng thái chờ |
| 2103 | Must invite at least one friend | Phải mời ít nhất 1 bạn bè khi tạo gia đình |
| 2104 | Can only invite friends | Chỉ có thể mời bạn bè |
| 2200 | File not found | Không tìm thấy file |
| 2201 | File upload failed | Upload file thất bại |
| 2202 | Invalid file type | Loại file không hợp lệ |
| 2203 | File too large | File quá lớn |

## 🗄 Sơ Đồ Database

### Quan Hệ Giữa Các Entity

```
User ──< UserRole >── Role

User ──< Friendship >── User                    # 🆕 Bạn bè
         │
         └── FriendshipStatus (PENDING, ACCEPTED, REJECTED)

User ──< FamilyMember >── Family
         │
         └── FamilyRole (LEADER, MEMBER)

User ──< FamilyInvitation >── Family            # 🆕 Lời mời gia đình
         │
         └── InvitationStatus (PENDING, ACCEPTED, REJECTED)

Family ──< ShoppingList ──< ShoppingItem
                           │
                           └── MasterProduct (tùy chọn)

Family ──< FridgeItem ── MasterProduct (tùy chọn)

Family ──< MealPlan ──< MealItem ── Recipe (tùy chọn)

Recipe ──< RecipeIngredient ── MasterProduct (tùy chọn)

MasterProduct ──< ProductCategory >── Category
```

### Quyết Định Thiết Kế Quan Trọng

1. **Optimistic Locking**: Sử dụng `@Version` trên ShoppingList và ShoppingItem để xử lý chỉnh sửa đồng thời
2. **Lazy Loading**: Tất cả quan hệ sử dụng `FetchType.LAZY` để tối ưu hiệu năng
3. **Soft Delete**: Sản phẩm sử dụng cờ `isActive` thay vì xóa cứng
4. **Sản phẩm Hybrid**: Các mục có thể tham chiếu MasterProduct HOẶC dùng tên tùy chỉnh
5. **Composite Key**: FamilyMember sử dụng khóa kết hợp (familyId, userId)

## 🔐 Bảo Mật

### Luồng Xác Thực

1. **Đăng ký**: `POST /api/v1/auth/register`
2. **Đăng nhập**: `POST /api/v1/auth/login` → Trả về access + refresh token
3. **Gọi API**: Thêm header `Authorization: Bearer <access_token>`
4. **Làm mới token**: `POST /api/v1/auth/refresh` với refresh token

### API Công Khai

- `/api/v1/auth/login`, `/api/v1/auth/register`, `/api/v1/auth/refresh` - Xác thực
- `/swagger-ui/**`, `/api-docs/**` - Tài liệu API
- `GET /api/v1/categories/**` - Danh sách danh mục
- `GET /api/v1/master-products/**` - Danh sách sản phẩm
- `GET /api/v1/recipes/**` - Danh sách công thức
- `GET /files/**` - Phục vụ file tĩnh (ảnh gia đình, v.v.) 🆕

### API Yêu Cầu Xác Thực

Tất cả các endpoint khác đều yêu cầu xác thực.

### API Chỉ Dành Cho Admin

- `POST/PUT/DELETE /api/v1/categories/**`
- `POST/PUT/DELETE /api/v1/master-products/**`

## ⚠️ Xử Lý Lỗi

`GlobalExceptionHandler` cung cấp response lỗi thống nhất:

```kotlin
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ApiException::class)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ExceptionHandler(ObjectOptimisticLockingFailureException::class)
    // ... các handler khác
}
```

### Ví Dụ Optimistic Locking

Khi cập nhật mục mua sắm:

```json
// Request
PATCH /api/v1/shopping-items/1
{
  "isBought": true,
  "version": 5  // Phiên bản hiện tại
}

// Response lỗi (nếu version không khớp)
{
  "code": 1106,
  "message": "Tài nguyên đã bị chỉnh sửa bởi người dùng khác. Vui lòng tải lại và thử lại."
}
```

## ⏰ Tác Vụ Nền

### Kiểm Tra Hết Hạn Hàng Ngày (8 giờ sáng)

```kotlin
@Scheduled(cron = "0 0 8 * * *")
fun checkExpiringItems() {
    // Tìm thực phẩm hết hạn trong 3 ngày tới
    // Gửi push notification cho thành viên gia đình
    // Cập nhật trạng thái thực phẩm hết hạn
}
```

### Cập Nhật Trạng Thái Hàng Giờ

```kotlin
@Scheduled(cron = "0 0 * * * *")
fun updateExpiredItemsStatus() {
    // Tự động đánh dấu thực phẩm hết hạn
}
```

## 📱 Danh Sách API

### Xác Thực
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Đăng ký người dùng mới |
| POST | `/api/v1/auth/login` | Đăng nhập |
| POST | `/api/v1/auth/refresh` | Làm mới access token |
| GET | `/api/v1/auth/me` | Lấy thông tin người dùng hiện tại |
| PATCH | `/api/v1/auth/me` | Cập nhật hồ sơ |
| POST | `/api/v1/auth/change-password` | Đổi mật khẩu |

### Người Dùng
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/v1/users/search?keyword=` | Tìm kiếm người dùng |
| GET | `/api/v1/users/{id}` | Lấy thông tin user |

### Bạn Bè 🆕
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/friends/request/{userId}` | Gửi lời mời kết bạn |
| GET | `/api/v1/friends/pending` | Lấy lời mời đang chờ |
| POST | `/api/v1/friends/respond/{friendshipId}` | Chấp nhận/từ chối lời mời |
| GET | `/api/v1/friends` | Lấy danh sách bạn bè |
| DELETE | `/api/v1/friends/{friendId}` | Hủy kết bạn |
| GET | `/api/v1/friends/status/{userId}` | Kiểm tra trạng thái bạn bè |

### Gia Đình
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/families` | Tạo gia đình (multipart, mời bạn bè) 🆕 |
| POST | `/api/v1/families/join` | Tham gia bằng mã mời |
| GET | `/api/v1/families` | Lấy danh sách gia đình của tôi |
| GET | `/api/v1/families/{id}` | Lấy chi tiết gia đình |
| GET | `/api/v1/families/{id}/members` | Lấy danh sách thành viên |
| PUT | `/api/v1/families/{id}` | Cập nhật gia đình (multipart) 🆕 |
| PATCH | `/api/v1/families/{familyId}/members/{userId}` | Cập nhật thành viên |
| DELETE | `/api/v1/families/{familyId}/members/{userId}` | Xóa thành viên |
| POST | `/api/v1/families/{id}/leave` | Rời gia đình |
| POST | `/api/v1/families/{id}/regenerate-invite-code` | Tạo mã mời mới |
| DELETE | `/api/v1/families/{id}` | Xóa gia đình |

### Lời Mời Gia Đình 🆕
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/v1/families/invitations` | Lấy lời mời đang chờ |
| POST | `/api/v1/families/invitations/{id}/respond` | Chấp nhận/từ chối lời mời |
| POST | `/api/v1/families/{familyId}/invite/{friendId}` | Mời bạn bè vào gia đình |

### Avatar (User) 🆕
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/auth/me/avatar` | Upload avatar (multipart) → trả về full Cloudinary URL |
| DELETE | `/api/v1/auth/me/avatar` | Xóa avatar |

### File ~~🆕~~ (DEPRECATED)
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| ~~GET~~ | ~~`/files/{path}`~~ | ⚠️ **DEPRECATED** - Ảnh giờ dùng Cloudinary URL trực tiếp |

### Danh Sách Mua Sắm
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/shopping-lists` | Tạo danh sách |
| GET | `/api/v1/families/{familyId}/shopping-lists` | Lấy danh sách |
| GET | `/api/v1/families/{familyId}/shopping-lists/active` | Lấy danh sách đang hoạt động |
| GET | `/api/v1/shopping-lists/{id}` | Lấy danh sách với các mục |
| PATCH | `/api/v1/shopping-lists/{id}` | Cập nhật danh sách |
| DELETE | `/api/v1/shopping-lists/{id}` | Xóa danh sách |
| POST | `/api/v1/shopping-lists/{listId}/items` | Thêm mục |
| POST | `/api/v1/shopping-lists/{listId}/items/bulk` | Thêm nhiều mục |
| PATCH | `/api/v1/shopping-items/{itemId}` | Cập nhật mục |
| DELETE | `/api/v1/shopping-items/{itemId}` | Xóa mục |

### Tủ Lạnh
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/fridge-items` | Thêm thực phẩm |
| GET | `/api/v1/families/{familyId}/fridge-items` | Lấy thực phẩm (có bộ lọc) |
| GET | `/api/v1/families/{familyId}/fridge-items/active` | Lấy thực phẩm còn dùng được |
| GET | `/api/v1/families/{familyId}/fridge-items/expiring` | Lấy thực phẩm sắp hết hạn |
| GET | `/api/v1/families/{familyId}/fridge-items/expired` | Lấy thực phẩm đã hết hạn |
| GET | `/api/v1/families/{familyId}/fridge-items/statistics` | Lấy thống kê |
| GET | `/api/v1/fridge-items/{id}` | Lấy chi tiết thực phẩm |
| PATCH | `/api/v1/fridge-items/{id}` | Cập nhật thực phẩm |
| POST | `/api/v1/fridge-items/{id}/consume` | Sử dụng một phần |
| POST | `/api/v1/fridge-items/{id}/discard` | Bỏ thực phẩm |
| DELETE | `/api/v1/fridge-items/{id}` | Xóa thực phẩm |

### Công Thức
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/v1/recipes` | Lấy tất cả công thức |
| GET | `/api/v1/recipes/{id}` | Lấy công thức với nguyên liệu |
| GET | `/api/v1/recipes/search?title=` | Tìm kiếm công thức |
| GET | `/api/v1/recipes/my-recipes` | Lấy công thức của tôi |
| GET | `/api/v1/recipes/suggestions/{familyId}` | Lấy gợi ý công thức |
| POST | `/api/v1/recipes` | Tạo công thức |
| PUT | `/api/v1/recipes/{id}` | Cập nhật công thức |
| DELETE | `/api/v1/recipes/{id}` | Xóa công thức |

### Kế Hoạch Bữa Ăn
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/meal-plans` | Tạo kế hoạch bữa ăn |
| GET | `/api/v1/meal-plans/{id}` | Lấy kế hoạch bữa ăn |
| GET | `/api/v1/families/{familyId}/meal-plans?startDate=&endDate=` | Lấy theo khoảng ngày |
| GET | `/api/v1/families/{familyId}/meal-plans/daily?date=` | Lấy kế hoạch theo ngày |
| GET | `/api/v1/families/{familyId}/meal-plans/weekly?startDate=` | Lấy kế hoạch theo tuần |
| PUT | `/api/v1/meal-plans/{id}` | Cập nhật kế hoạch |
| POST | `/api/v1/meal-plans/{mealPlanId}/items` | Thêm món ăn |
| DELETE | `/api/v1/meal-items/{itemId}` | Xóa món ăn |
| DELETE | `/api/v1/meal-plans/{id}` | Xóa kế hoạch |

### Danh Mục (Admin)
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/v1/categories` | Lấy tất cả danh mục |
| GET | `/api/v1/categories/{id}` | Lấy danh mục |
| GET | `/api/v1/categories/search?name=` | Tìm kiếm danh mục |
| POST | `/api/v1/categories` | Tạo danh mục (Admin) |
| PUT | `/api/v1/categories/{id}` | Cập nhật danh mục (Admin) |
| DELETE | `/api/v1/categories/{id}` | Xóa danh mục (Admin) |

### Sản Phẩm (Admin)
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/v1/master-products` | Lấy tất cả sản phẩm |
| GET | `/api/v1/master-products/{id}` | Lấy sản phẩm |
| GET | `/api/v1/master-products/search?name=` | Tìm kiếm sản phẩm |
| GET | `/api/v1/master-products/by-category/{categoryId}` | Theo danh mục |
| POST | `/api/v1/master-products` | Tạo sản phẩm (Admin) |
| PUT | `/api/v1/master-products/{id}` | Cập nhật sản phẩm (Admin) |
| DELETE | `/api/v1/master-products/{id}` | Xóa sản phẩm (Admin) |

---

## 📄 Giấy Phép

MIT License

## 👥 Đội Ngũ Phát Triển

Smart Grocery Team
