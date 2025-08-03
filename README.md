# LenaNW Log Sistemi

Minecraft 1.8.8 sunucuları için geliştirilmiş kapsamlı log sistemi.

## Özellikler

### 🔐 Kullanıcı - Şifre Log
- Oyuncu giriş/çıkış logları
- IP adresi kayıtları
- UUID bilgileri
- Kick/ban logları

### 📦 İtem Log Sistemi
- **İtem Atma Log**: Oyuncuların attığı itemler
- **İtem Toplama Log**: Oyuncuların topladığı itemler
- **Yetkili Log**: Admin/OP oyuncuların item işlemleri

### 🗃️ Sandık Log
- Sandık açma/kapama
- Sandıktan item alma/koyma
- Sandık konumu kayıtları

### 💀 Ölüm Log
- Ölüm nedenleri (düşme, yangın, saldırı vb.)
- Ölüm konumu
- Saldırgan bilgileri

### 🏠 Claim (Özel Bölge) Log
- Claim içinde yapılan işlemler
- Blok kırma/yapma
- Etkileşim logları

### 💰 Kinas (Para) Log
- Para transferleri
- Bakiye değişiklikleri
- Komut logları

### 🧹 Item Temizleme Log
- 1 dakikada bir çalışır
- Yerdeki itemlerin temizlenme logları
- Item türleri ve sayıları

### 🎯 Discord Webhook Entegrasyonu
- Her log türü için ayrı Discord kanalları
- Renkli embed mesajları
- Gerçek zamanlı bildirimler
- Özelleştirilebilir webhook URL'leri

## Kurulum

### Gereksinimler
- Java 8 veya üzeri
- Spigot/Paper 1.8.8
- Maven (derleme için)

### Derleme
```bash
mvn clean package
```

### Kurulum
1. Derlenen JAR dosyasını `plugins/` klasörüne kopyalayın
2. Sunucuyu yeniden başlatın
3. Plugin otomatik olarak log klasörünü oluşturacaktır

## Discord Webhook Kurulumu

### 1. Discord Webhook URL'leri Oluşturma
1. Discord sunucunuzda her log türü için ayrı kanallar oluşturun
2. Her kanalın ayarlarına gidin
3. "Integrations" > "Webhooks" bölümüne gidin
4. "New Webhook" butonuna tıklayın
5. Webhook URL'ini kopyalayın

### 2. Config Dosyasını Düzenleme
`plugins/LenaNW-Log/config.yml` dosyasını açın ve Discord bölümünü düzenleyin:

```yaml
discord:
  enabled: true
  webhooks:
    auth: "https://discord.com/api/webhooks/YOUR_AUTH_WEBHOOK_URL"
    item_drop: "https://discord.com/api/webhooks/YOUR_ITEM_DROP_WEBHOOK_URL"
    item_pickup: "https://discord.com/api/webhooks/YOUR_ITEM_PICKUP_WEBHOOK_URL"
    chest: "https://discord.com/api/webhooks/YOUR_CHEST_WEBHOOK_URL"
    death: "https://discord.com/api/webhooks/YOUR_DEATH_WEBHOOK_URL"
    claim: "https://discord.com/api/webhooks/YOUR_CLAIM_WEBHOOK_URL"
    money: "https://discord.com/api/webhooks/YOUR_MONEY_WEBHOOK_URL"
    item_cleanup: "https://discord.com/api/webhooks/YOUR_CLEANUP_WEBHOOK_URL"
```

### 3. Webhook Test Etme
```bash
/log testwebhook auth
/log testwebhook item_drop
/log testwebhook death
```

## Komutlar

### `/log`
Yönetici komutları:
- `/log reload` - Sistemi yeniden yükle
- `/log info` - Sistem bilgilerini göster
- `/log testwebhook <type>` - Discord webhook test et

### `/logs`
Log görüntüleme komutları:
- `/logs <type> [player] [page]`

**Log Türleri:**
- `auth` - Kullanıcı giriş/çıkış
- `item_drop` - İtem atma
- `item_pickup` - İtem toplama
- `chest` - Sandık işlemleri
- `death` - Ölüm logları
- `claim` - Claim işlemleri
- `money` - Para işlemleri
- `item_cleanup` - Item temizleme

**Örnekler:**
```
/logs auth
/logs death PlayerName
/logs money PlayerName 2
```

## İzinler

- `lenanw.log.admin` - Yönetici komutları
- `lenanw.log.view` - Log görüntüleme

## Konfigürasyon

`plugins/LenaNW-Log/config.yml` dosyasından ayarları düzenleyebilirsiniz:

```yaml
# Discord Webhook ayarları
discord:
  enabled: true
  webhooks:
    auth: "YOUR_WEBHOOK_URL"
    item_drop: "YOUR_WEBHOOK_URL"
    # ... diğer webhook URL'leri
  
  # Embed ayarları
  embed:
    colors:
      auth: "00ff00"      # Yeşil
      death: "ff0000"      # Kırmızı
      # ... diğer renkler
    
    titles:
      auth: "🔐 Kullanıcı Log"
      death: "💀 Ölüm"
      # ... diğer başlıklar
```

## Log Dosyaları

Log dosyaları `plugins/LenaNW-Log/logs/` klasöründe saklanır:

- `auth_YYYY-MM-DD.log` - Kullanıcı logları
- `item_drop_YYYY-MM-DD.log` - İtem atma logları
- `item_pickup_YYYY-MM-DD.log` - İtem toplama logları
- `chest_YYYY-MM-DD.log` - Sandık logları
- `death_YYYY-MM-DD.log` - Ölüm logları
- `claim_YYYY-MM-DD.log` - Claim logları
- `money_YYYY-MM-DD.log` - Para logları
- `item_cleanup_YYYY-MM-DD.log` - Item temizleme logları

## Discord Embed Örnekleri

### 🔐 Kullanıcı Log
```
🔐 Kullanıcı Log
PlayerName giriş yaptı
Detaylar: Address: 192.168.1.1, UUID: 12345678-1234-1234-1234-123456789012
Zaman: 14:30:25
```

### 💀 Ölüm Log
```
💀 Ölüm
PlayerName öldü
Neden: Killed by OtherPlayer
Konum: World: world, X: 150.25, Y: 65.00, Z: 250.75
Zaman: 14:40:15
```

### 💰 Para İşlemi
```
💰 Para İşlemi
PlayerName 1000.00 Kinas transfer etti
İşlem: TRANSFER
Miktar: 1000.00 Kinas
Detaylar: Target: OtherPlayer, Command: /pay OtherPlayer 1000
Zaman: 14:45:30
```

## Entegrasyon

### Claim Sistemi Entegrasyonu
`ClaimLogListener.java` dosyasındaki `isInClaim()` metodunu sunucunuzun claim sistemi ile entegre edin:

```java
private boolean isInClaim(Player player, Location location) {
    // WorldGuard entegrasyonu örneği
    // return WorldGuard.getInstance().getRegionManager(location.getWorld())
    //     .getApplicableRegions(location).size() > 0;
    
    // GriefPrevention entegrasyonu örneği
    // return GriefPrevention.instance.dataStore.getClaimAt(location) != null;
    
    return false; // Varsayılan
}
```

### Para Sistemi Entegrasyonu
`MoneyLogListener.java` dosyasındaki `logMoneyTransaction()` metodunu kullanarak diğer plugin'lerden para işlemlerini loglayabilirsiniz.

## Sorun Giderme

### Plugin Yüklenmiyor
- Java 8+ kullandığınızdan emin olun
- Spigot/Paper 1.8.8 kullandığınızdan emin olun
- Konsol hatalarını kontrol edin

### Log Dosyaları Oluşmuyor
- `plugins/LenaNW-Log/` klasörünün yazma izni olduğundan emin olun
- Sunucu yeniden başlatıldıktan sonra log dosyaları oluşacaktır

### Discord Webhook Çalışmıyor
- Webhook URL'lerinin doğru olduğundan emin olun
- Discord kanalının webhook izinlerini kontrol edin
- `/log testwebhook <type>` komutu ile test edin
- Konsol hatalarını kontrol edin

### Performans Sorunları
- `config.yml` dosyasından gereksiz log türlerini kapatabilirsiniz
- Log dosyalarının boyutunu sınırlayabilirsiniz
- Discord webhook'larını kapatabilirsiniz

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## Destek

Sorunlarınız için GitHub Issues kullanabilir veya LenaNW ile iletişime geçebilirsiniz.

## Sürüm Geçmişi

### v1.0.0
- İlk sürüm
- Tüm temel log özellikleri
- Komut sistemi
- Konfigürasyon dosyası
- Discord webhook entegrasyonu 