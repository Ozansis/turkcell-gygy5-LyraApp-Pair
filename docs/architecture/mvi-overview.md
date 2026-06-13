# MVI Mimarisi — Genel Bakış

Bu döküman LyraApp'te benimsenen MVI (Model-View-Intent) mimarisinin yapısını, katman düzenini ve zorunlu dosya şablonunu tanımlar. Her yeni ekran bu yapıyı aynen takip etmek zorundadır.

---

## Temel Kavramlar

| Kavram | Karşılık | Açıklama |
|--------|----------|----------|
| Model | `XxxState` | Ekranın anlık UI durumunu tutan immutable veri sınıfı |
| View | `XxxScreen` | Stateless Composable; sadece state'i render eder |
| Intent | `XxxIntent` | Kullanıcıdan gelen her etkileşimi temsil eden sealed interface |
| Effect | `XxxEffect` | Tek seferlik yan etkiler (navigasyon, toast vb.) |

Veri akışı tek yönlüdür:

```
Kullanıcı → Intent → ViewModel → State (UI güncellenir)
                               → Effect (tek seferlik tetikler)
```

---

## Klasör ve Dosya Yapısı

Her feature için aşağıdaki şablon uygulanır:

```
app/src/main/java/com/turkcell/lyraapp/
├── data/
│   └── <feature>/
│       ├── <Feature>Repository.kt       ← interface (sözleşme)
│       └── Fake<Feature>Repository.kt   ← stub implementasyon
├── di/
│   └── <Feature>Module.kt               ← Hilt @Binds modülü
└── ui/
    └── screens/
        └── <feature>/
            ├── <Feature>Contract.kt     ← State + Intent + Effect (nested object)
            ├── <Feature>ViewModel.kt
            └── <Feature>Screen.kt       ← Route + Screen birlikte
```

Kurallar:
- State, Intent ve Effect `<Feature>Contract.kt` içinde `object <Feature>Contract { ... }` altında birleştirilir.
- `<Feature>Screen.kt` hem `<Feature>Route` hem de `<Feature>Screen` composable'larını içerir.
- Repository interface ve Fake implementasyonu **aynı pakette** bulunur.

---

## Route / Screen Ayrımı

Her ekran iki composable fonksiyondan oluşur:

### `<Feature>Route` (Stateful)
- `hiltViewModel()` ile ViewModel'i enjekte eder.
- `collectAsStateWithLifecycle()` ile state'i toplar.
- `LaunchedEffect(Unit)` içinde effect kanalını dinler.
- `<Feature>Screen`'i çağırır; herhangi bir iş mantığı içermez.

### `<Feature>Screen` (Stateless)
- Parametre olarak yalnızca `state: <Feature>State` ve `onIntent: (<Feature>Intent) -> Unit` alır.
- Preview'lar bu composable üzerinden yazılır.
- Herhangi bir ViewModel veya Hilt bağımlılığı içermez.

Örnek imza:
```kotlin
// Stateful wrapper — MainActivity veya NavGraph buraya bağlanır
@Composable
fun LoginRoute(viewModel: LoginViewModel = hiltViewModel()) { ... }

// Stateless — test ve preview edilebilir
@Composable
fun LoginScreen(state: LoginContract.State, onIntent: (LoginContract.Intent) -> Unit, modifier: Modifier = Modifier) { ... }
```

---

## Repository Katmanı

Backend API hazır olana kadar `Fake<Feature>Repository` kullanılır. Hilt, `<Feature>Module` üzerinden interface'e karşı Fake implementasyonu bağlar. API entegrasyon zamanı geldiğinde yalnızca:

1. Gerçek `<Feature>Repository` implementasyonu yazılır.
2. `<Feature>Module` içindeki binding güncellenir.
3. ViewModel ve Screen'e dokunulmaz.

Ayrıntılar için: `decisions.md` — "Repository Stub Stratejisi"
