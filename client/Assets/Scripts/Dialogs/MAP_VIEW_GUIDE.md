# Tính Năng Xem Bản Đồ - Map View Feature

## Mô Tả Chung
Chức năng xem bản đồ cho phép người chơi dễ dàng xem bố cục của bản đồ hiện tại và di chuyển đến các khu vực khác một cách nhanh chóng.

## Cách Sử Dụng

### 1. Mở/Đóng Bản Đồ
- **Phím tắt**: Nhấn phím **M** để mở và đóng bản đồ
- Hoặc nhấn **ESC** khi đang xem bản đồ để đóng nó

### 2. Chức Năng Xem Bản Đồ
Khi bản đồ được mở:
- **Hiển thị**: Toàn bộ bố cục lưới của bản đồ hiện tại
- **Khu hiện tại**: Hiển thị khu vực hiện tại với màu **vàng**
- **Khu khác**: Các khu vực khác hiển thị với màu **xanh lam**
- **Hover**: Khi di chuột vào một khu vực, nó sẽ sáng lên để chỉ ra bạn đang chỉ đến khu nào

### 3. Di Chuyển Giữa Các Khu
- **Click chuột**: Click vào bất kỳ khu vực nào trên bản đồ để di chuyển đến đó
- **Tự động**: Hệ thống sẽ gửi lệnh thay đổi khu (tương tự như lệnh chat "k X")
- **Kiểm tra**: Trò chơi sẽ kiểm tra:
  - Thời gian chảy dừa giữa các lần đổi khu
  - Số lượng người chơi trong khu vực đích
  - Các điều kiện khác để đảm bảo tính hợp lệ

## Giao Diện Bản Đồ

```
┌─────────────────────────────────┐
│         BẢN ĐỒ                  │
│   Tên Map - Khu X                │
├─────────────────────────────────┤
│                                  │
│  ┌──┬──┬──┬──┐                   │
│  │  │  │  │  │                   │
│  ├──┼──┼──┼──┤                   │
│  │  │█ │  │  │  (█ = Khu hiện tại)
│  ├──┼──┼──┼──┤                   │
│  │  │  │  │  │                   │
│  └──┴──┴──┴──┘                   │
│                                  │
│ Click vào khu để di chuyển       │
│ Nhấn M để đóng                   │
└─────────────────────────────────┘
```

## Thành Phần Đã Thêm

### 1. MapViewDialog.cs
- **Vị trí**: `Assets/Scripts/Dialogs/MapViewDialog.cs`
- **Chức năng**:
  - Quản lý giao diện bản đồ
  - Xử lý vẽ lưới các khu vực
  - Xử lý tương tác chuột (hover, click)
  - Gửi yêu cầu thay đổi khu

### 2. Cập Nhật DisplayManager
- **Vị trí**: `Assets/Scripts/Displays/DisplayManager.cs`
- **Thay đổi**:
  - Thêm `MapViewDialog mapViewDialog;`
  - Khởi tạo `mapViewDialog` trong constructor

### 3. Cập Nhật GameScreen
- **Vị trí**: `Assets/Scripts/Screens/GameScreen.cs`
- **Thay đổi**:
  - Thay đổi phím **M** từ mở menu thành mở/đóng bản đồ
  - Thêm vẽ `mapViewDialog` trong phương thức `Paint()`
  - Thêm xử lý chuột cho bản đồ trong `PointerClicked()`
  - Thêm xử lý di chuyển chuột trong `PointerMove()`
  - Thêm xử lý phím trong `KeyPress()`

## Tài Năng Kỹ Thuật

### Màu Sắc
- **Khu hiện tại**: RGB(255, 200, 50) - Vàng
- **Khu khác**: RGB(100, 100, 150) - Xanh lam
- **Khu hover**: RGB(150, 150, 200) - Xanh lam sáng hơn
- **Biên**: RGB(200, 200, 200) - Trắng xám

### Tính Toán Kích Thước
- Kích thước ô được tính toán động dựa trên kích thước bản đồ
- Công thức: `cellSize = Min(gridWidth / columns, gridHeight / rows)`

### Tính Toán Tọa Độ
- Ô được xác định bằng hàng và cột
- ID khu: `zoneId = row * maxCols + col`

## Ưu Điểm

✅ **Dễ sử dụng**: Chỉ cần nhấn M để xem bản đồ
✅ **Trực quan**: Giao diện lưới dễ hiểu
✅ **Nhanh chóng**: Di chuyển giữa khu vực chỉ với một click
✅ **An toàn**: Kiểm tra điều kiện trước khi di chuyển
✅ **Phải hòa**: Tương thích với hệ thống di chuyển hiện có

## Lưu Ý

⚠️ **Thời gian chảy dừa**: Không thể đổi khu quá nhanh
⚠️ **Giới hạn người chơi**: Một khu không thể chứa quá nhiều người
⚠️ **Bản đồ đặc biệt**: Một số bản đồ có thể không cho phép đổi khu

## Hợp Tác Với Lệnh Chat

Bản đồ tương tác với các lệnh chat hiện có:
- **Tương tự**: Phím M + Click tương đương với lệnh chat `k X`
- **Không thay thế**: Menu ban đầu (Phím M) vẫn có thể truy cập qua menu chính
