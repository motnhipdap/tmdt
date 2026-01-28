****- customer:
+ đăng nhập đăng ký
+ quản lý tài khoản
+ xem chi tiết sản phẩm
+ tìm kiếm
+ thêm vào giỏ hàng
+ mua hàng
+ quản lý đơn hàng
+ thanh toán
+ đánh giá
+ điểm mua hàng (thêm, sử dụng)
+ giảm giá

- nhân viên:
    + thêm hóa đơn
    + thêm sản phẩm (thêm NCC)
    + tìm kiếm, xem chi tiết
    + quản lý đơn hàng
    + gửi thông báo

- admin
    + quản lý user (thêm, sửa, xóa, khóa)
    + quản lý sản phẩm (xem, thêm, sửa, xóa)


- ADVANCED:
    + hệ thống gợi ý sản phẩm
    + hệ thống quảng cáo sản phẩm
    + các chế độ khuyến mãi

- Lớp thực thể - Entities:
    + Account       : id(int), username(String), password(String), role(String), email(String), create_at(long),
      update_at(long)
    + User          : id(int), name(String), addr(String), point(int), acc(Account), cart(Cart), vouchers(
      List<Voucher>), comments(List<Comment>)
    + Category      : id(int), name(String), desc(String), categories(List<Category>), products(List<Product>),
      created_at(long), updated_at(long)
    + Comment       : id(int), desc(String), rating(float), create_at(long), create_update(long)
    + Provider-NCC  : id(int), name(String), addr(String), email(String), phone(String), create_at(long), products(
      List<Product>)
    + Product       : id(int), name(String), img, desc(String), price(int), rated(float), comments(List<Comment>)
      ,quantity(int),
      create_at(long), update_at(long)
    + Cart-Giỏ Hàng : id(int), List<ItemCart> items
    + ItemCart      : product(Product), int quantity
    + Voucher-MGG  : id(int), percent(int), value(int), require(int)
    + Uservoucher  : id(int), vouchers(Voucher), quantity(int), time_user(long)
    + Invoice-HD    : id(int), user(User), type(String), details(List<InvoiceDetails>), total_money(int), create_at(
      long)
    + InvoiceDetails: id_Invoice(int), id_product(int), quantity(int), voucher(Voucher), total(int)

- Lớp thiết kế :
    + entities:  
      + account   : id(INT PK NOT NULL), username(varchar(50) UNIQUE NOT NULL), password(varchar(30) NOT NULL), role(
      varchar(10)), email(varchar(50)), phone()
      create_at(timstamp DEFAULT CURRENT_timestamp), update_at(timestamp deflaut current_timestamp)
      + user      : id(INT PK NOT NULL), name(nvarchar(50) not null), addr(nvarchar(50)), point(int), acc_id(int FK
      account(id))
      + category  : id(INT PK NOT NULL), name(nvarchar(50) not null), category_id(fk categoty(id)),
      create_at(timstamp DEFAULT CURRENT_timestamp), update_at(timestamp deflaut current_timestamp)
      + product   : id(int pk not null), name(nvarchar(50) not null), desc(nvarchar(255)), price(int), rated(float),
      provider_id(fk provider(id)),
      create_at(timstamp DEFAULT CURRENT_timestamp), update_at(timestamp deflaut current_timestamp)
      + comment   : id(int pk not null), desc(nvarchar(255)), user_id(fk user(id)), product_id(fk product(id)),
      create_at(timstamp DEFAULT CURRENT_timestamp), update_at(timestamp deflaut current_timestamp)
      + cart      : id(int pk not null), user_id(fk int user(id))
      + item_cart : cart_id(fk cart(id)), product_id(fk product(id)), quantity(int)
      + voucher   : id(int pk not null), percent(int), value(int)
      + user_voucher: user_id(fk user(id)), voucher_id(fk voucher(id)), user_at(timestamp)
      + invoice   : id(int pk not null),type(varchar(10)),status(varchar(20)), total_money(int), creat_at(timestamp)
      ,user_id(fk user(id))
      + invoice_detail: invoice_id(fk invoice(id)), product_id(fk product(id)), quantity(int), voucher_id(fk voucher(
      id)), total(int)

Chức năng:

- customer:
    + đổi email : click đổi email -> yêu cầu nhập password -> yêu cầu nhập Otp gửi về email cũ -> nhập email mới -> nhập otp gửi về email mới -> xác nhận
    + đổi thông tin (tên, addr,..): click chỉnh sửa profile -> chỉnh lại -> click save
    + tìm kiếm:
        + tìm bằng ô nhập: nhập thông tin -> server tìm kiếm gần đúng -> trả về kết quả -> hiển thị
        + tìm kiếm bằng category: click category -> chọn đến khi đúng category cần -> tìm kiếm -> hiển thị toàn bộ
    + xem chi tiết sp: click sp -> hiển thị thông tin sản phẩm
    + thêm vào giỏ hàng: click vào add cart-> server kiểm tra còn hàng hay không -> trả về kq -> hiển thị thêm thành
      công
    + quản lý đơn hàng - xem các sp đã mua : bấm vào giỏ hàng -> click đã mua -> server tìm kiếm -> trả kq -> hiển thị
      cho user
    + đánh giá: click vào sp -> click đánh giá -> form đánh giá hiện ra (bình luận, thêm ảnh, rate) -> click oke -> hiển
      thị thành công
    + tích điểm:
        + điểm danh: đăng nhập -> click điểm danh -> hiển thị số điểm nhận được -> click ok
        + sử dụng voucher hoàn xu:

- Employee:
    + thêm sản phẩm: chọn thêm sản phẩm -> yêu cầu nhập mã nhà cc -> nhập thông tin sản phẩm -> click ok -> thêm thành
      công

- Admin:
    + quản lý user:
        + thêm nhân viên: click thêm nhân viên -> nhập thông tin nhân viên -> click add -> thêm thành công
        + tìm kiếm thông tin : chọn tìm kiếm user -> nhập thông tin -> hiển thị các user