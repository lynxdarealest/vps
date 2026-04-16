# Deploy Rong Than Online len VPS Linux

Huong dan nay dung cho Ubuntu 22.04/24.04, Java 17, MySQL 8.

Project nay co 2 port:

- `707`: Spring Boot web/cpanel, vi du `/cpanel`
- `1707`: game socket cho Unity client

Database mac dinh:

- Ten database: `rto`
- SQL dump: `rongthan.sql`

## 1. Cai moi truong tren VPS

Dang nhap VPS:

```bash
ssh root@IP_VPS
```

Cai goi can thiet:

```bash
apt update && apt upgrade -y
apt install -y git curl unzip ufw nginx mysql-server openjdk-17-jdk
java -version
mysql --version
```

Tao user deploy:

```bash
adduser deploy
usermod -aG sudo deploy
su - deploy
```

## 2. Tao database MySQL

Vao MySQL:

```bash
sudo mysql
```

Chay SQL, doi mat khau cho manh:

```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'ROOT_PASSWORD_MANH';

CREATE DATABASE rto CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE USER 'rto_user'@'localhost' IDENTIFIED BY 'RTO_DB_PASSWORD_MANH';
GRANT ALL PRIVILEGES ON rto.* TO 'rto_user'@'localhost';
FLUSH PRIVILEGES;

EXIT;
```

Neu VPS dung MariaDB, co the loi `utf8mb4_0900_ai_ci`. Nen dung MySQL 8 cho dung voi file dump hien tai.

## 3. Clone source

```bash
sudo mkdir -p /opt/rongthanonline
sudo chown deploy:deploy /opt/rongthanonline
git clone https://github.com/TEN_USER/TEN_REPO.git /opt/rongthanonline
cd /opt/rongthanonline
```

Neu `rongthan.sql` khong nam trong repo, upload tu Windows:

```powershell
scp "C:\Users\phand\Desktop\RONG THAN ONLINE\rongthan.sql" deploy@IP_VPS:/home/deploy/rongthan.sql
```

Import database:

```bash
mysql -u rto_user -p rto < /opt/rongthanonline/rongthan.sql
```

Hoac neu upload vao home:

```bash
mysql -u rto_user -p rto < /home/deploy/rongthan.sql
```

## 4. Build server

```bash
cd /opt/rongthanonline/server
chmod +x mvnw run-server.sh
./mvnw -DskipTests package
```

File jar sau khi build:

```bash
/opt/rongthanonline/server/target/rongthanonline-0.0.1-SNAPSHOT.jar
```

## 5. Chay thu

```bash
cd /opt/rongthanonline/server

SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/rto?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Taipei' \
SPRING_DATASOURCE_USERNAME='rto_user' \
SPRING_DATASOURCE_PASSWORD='RTO_DB_PASSWORD_MANH' \
SERVER_PORT=707 \
SERVER_SWING_GUI_ENABLED=false \
./run-server.sh
```

Kiem tra tu SSH khac:

```bash
ss -lntp | grep -E ':707|:1707|:3306'
curl -I http://127.0.0.1:707/cpanel
```

Neu thay port `707` va `1707` dang listen la dung.

## 6. Cai systemd service

Sua file truoc khi copy:

```bash
nano /opt/rongthanonline/deploy/rongthanonline.service
```

Doi dong nay thanh mat khau database that:

```ini
Environment=SPRING_DATASOURCE_PASSWORD=CHANGE_ME_STRONG_PASSWORD
```

Copy service:

```bash
sudo cp /opt/rongthanonline/deploy/rongthanonline.service /etc/systemd/system/rongthanonline.service
sudo systemctl daemon-reload
sudo systemctl enable --now rongthanonline
sudo systemctl status rongthanonline
```

Xem log:

```bash
journalctl -u rongthanonline -f
```

Restart:

```bash
sudo systemctl restart rongthanonline
```

## 7. Mo firewall

Bat buoc mo SSH va game socket:

```bash
sudo ufw allow OpenSSH
sudo ufw allow 1707/tcp
```

Chi nen mo cpanel `707` cho IP cua ban:

```bash
sudo ufw allow from IP_NHA_BAN to any port 707 proto tcp
```

Neu can mo tam cho moi noi:

```bash
sudo ufw allow 707/tcp
```

Bat firewall:

```bash
sudo ufw enable
sudo ufw status
```

Khong mo MySQL `3306` ra internet.

## 8. Tro Unity client ve VPS

Sua file client:

```text
client/Assets/Scripts/Networks/ServerManager.cs
```

Doi:

```csharp
public static string LINKWEB = "http://IP_VPS:707";
public static string baseIP = "IP_VPS";
```

Port game giu `1707`.

Sau do build lai client trong Unity.

## 9. Update server sau nay

Tren Windows:

```powershell
git add .
git commit -m "Update server"
git push
```

Tren VPS:

```bash
cd /opt/rongthanonline/deploy
chmod +x update-server.sh
./update-server.sh
```

## 10. Backup database

```bash
cd /opt/rongthanonline/deploy
chmod +x backup-db.sh
./backup-db.sh
```

File backup nam trong:

```bash
~/backups
```

