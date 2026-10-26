# Exercise 5.8 - 5.10: PokerHand Testing

Một project duy nhất cho ba bài tập liên tiếp, gồm hai mức kiểm thử:

- **Minimum suite**: đúng số lần chạy UUT tối thiểu theo đề.
- **Extended suite**: kiểm thử rộng hơn, bao gồm đủ **36 Straight Flush** mà implementation nhận diện và thêm các nhóm trường hợp sai/robustness quan trọng.

> Lưu ý: Extended suite vẫn **không phải exhaustive testing** cho toàn bộ 2,598,960 bộ bài 5 lá.

## Yêu cầu
- JDK 17+
- Maven 3.8+

## Chạy từng bài với số executions tối thiểu

### Exercise 5.8 - 1 test
```powershell
mvn clean test -Dgroups=ex58
```

### Exercise 5.9 - 2 tests
```powershell
mvn clean test -Dgroups=ex59
```

### Exercise 5.10 - 3 tests
```powershell
mvn clean test -Dgroups=ex510
```

## Chạy tất cả 36 Straight Flush của Exercise 5.8
```powershell
mvn clean test -Dgroups=all-straight-flushes
```

Kết quả mong đợi: **36 tests, 0 failures**.

## Chạy bộ kiểm thử mở rộng
```powershell
mvn clean test -Dgroups=extended
```

Kết quả mong đợi: **45 tests, 0 failures**.

## Chạy toàn bộ project
```powershell
mvn clean test
```

Kết quả mong đợi: **48 tests, 0 failures**.

## Xem JaCoCo coverage
Sau khi chạy test, mở:

```text
target/site/jacoco/index.html
```

Trong PowerShell:
```powershell
start target/site/jacoco/index.html
```

## Ý nghĩa

### Minimum suite
1. `A♣ 2♣ 3♣ 4♣ 5♣` -> `true` (Exercise 5.8).
2. `A♣ 2♣ 3♣ 4♣ 6♣` -> `false`, phủ `return false` (Exercise 5.9).
3. `A♣ 2♦ 3♣ 4♣ 5♣` -> `false`, phủ nhánh khác chất (Exercise 5.10).

### Extended suite
- 36 Straight Flush: 9 chuỗi rank x 4 suits.
- 3 trường hợp rank bị đứt.
- 3 trường hợp khác suit.
- 1 trường hợp trùng rank.
- 2 trường hợp Straight Flush truyền vào không theo thứ tự để xác nhận constructor sắp xếp đúng.

Tổng extended executions: **45**. Tổng toàn project: **48**.
