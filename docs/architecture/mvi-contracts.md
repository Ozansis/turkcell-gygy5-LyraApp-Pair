# MVI Sözleşmeleri — State / Intent / Effect

Bu döküman her MVI ekranındaki üç sözleşme dosyasının (State, Intent, Effect) kurallarını ve isimlendirme standartlarını tanımlar.

---

## State

### Kural

```kotlin
data class <Feature>State(
    val <field>: <Type> = <default>,
    val isLoading: Boolean = false,
    val error: String? = null,
)
```

- `data class` kullanılır; `sealed class` veya başka bir yapı kabul edilmez.
- Tüm alanlar `val` (immutable) olmalıdır.
- Her alanın varsayılan değeri olmalıdır; böylece ViewModel `<Feature>State()` ile başlatılabilir.
- `isLoading` ve `error` her State'de standart olarak yer alır.
- `error` tipi `String?` olur; özel hata modeli kullanılmaz (API entegrasyonu aşamasında gözden geçirilir).

### Örnek

```kotlin
data class LoginState(
    val phoneNumber: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
```

---

## Intent

### Kural

```kotlin
sealed interface <Feature>Intent {
    data class <Field>Changed(val value: <Type>) : <Feature>Intent
    data object <Action>Clicked : <Feature>Intent
    data object <Field>Toggled : <Feature>Intent
}
```

- `sealed interface` kullanılır; `sealed class` kullanılmaz.
- Veri taşıyan intent'ler `data class`, taşımayanlar `data object` olur.

### İsimlendirme

| Durum | Kalıp | Örnek |
|-------|-------|-------|
| Form alanı değişikliği | `<Alan>Changed(val value: T)` | `PhoneNumberChanged(val value: String)` |
| Toggle/açma-kapama | `<Alan>Toggled` | `PasswordVisibilityToggled` |
| Buton tıklaması | `<Eylem>Clicked` | `LoginClicked` |
| Navigasyon talebi | `NavigateTo<Hedef>Clicked` | `RegisterClicked` |

### Örnek

```kotlin
sealed interface LoginIntent {
    data class PhoneNumberChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object PasswordVisibilityToggled : LoginIntent
    data object LoginClicked : LoginIntent
    data object ForgotPasswordClicked : LoginIntent
    data object RegisterClicked : LoginIntent
}
```

---

## Effect

### Kural

```kotlin
sealed interface <Feature>Effect {
    data object NavigateTo<Hedef> : <Feature>Effect
    data class Show<Sey>(val message: String) : <Feature>Effect
}
```

- `sealed interface` kullanılır.
- Effect yalnızca **tek seferlik** olayları temsil eder: navigasyon, Snackbar, dialog tetikleme.
- Kalıcı UI değişiklikleri (hata mesajı gösterme, yükleme durumu) **State** üzerinden yapılır; Effect üzerinden değil.

### İsimlendirme

| Durum | Kalıp | Örnek |
|-------|-------|-------|
| Ekrana geçiş | `NavigateTo<Ekran>` | `NavigateToHome` |
| Mesaj gösterme | `Show<Sey>` | `ShowError` (kullanımdan kaçın; `state.error` tercih edilir) |

### Örnek

```kotlin
sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data object NavigateToRegister : LoginEffect
    data object NavigateToForgotPassword : LoginEffect
}
```

---

## Dosya Yerleşimi

```
ui/screens/<feature>/
    <Feature>Contract.kt   ← State, Intent ve Effect tek dosyada, nested object içinde
```

Üç sözleşme `object <Feature>Contract { ... }` yapısı içinde birleştirilir. Kullanım örnekleri:

```kotlin
object LoginContract {
    data class State(...) { ... }
    sealed interface Intent { ... }
    sealed interface Effect { ... }
}
```

ViewModel ve Screen'de referanslar `LoginContract.State`, `LoginContract.Intent`, `LoginContract.Effect` biçiminde yazılır.
