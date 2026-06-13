package com.turkcell.lyraapp.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.ui.theme.LyraTheme

@Composable
fun RegisterRoute(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RegisterContract.Effect.NavigateToHome  -> onNavigateToHome()
                RegisterContract.Effect.NavigateToLogin -> onNavigateToLogin()
                RegisterContract.Effect.NavigateUp      -> onNavigateUp()
            }
        }
    }

    RegisterScreen(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun RegisterScreen(
    state: RegisterContract.State,
    onIntent: (RegisterContract.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .imePadding(),
        horizontalAlignment = Alignment.Start,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(16.dp))

            IconButton(onClick = { onIntent(RegisterContract.Intent.BackClicked) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Hesap oluştur",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Birkaç adımda Lyra'ya katıl ve çalma listeni oluştur.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = { onIntent(RegisterContract.Intent.FirstNameChanged(it)) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Ad") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = { onIntent(RegisterContract.Intent.LastNameChanged(it)) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Soyad") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { onIntent(RegisterContract.Intent.PhoneNumberChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Telefon numarası") },
                placeholder = { Text("5XX XXX XX XX") },
                leadingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Smartphone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(8.dp))
                        VerticalDivider(
                            modifier = Modifier.height(24.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "+90",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { onIntent(RegisterContract.Intent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Şifre") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onIntent(RegisterContract.Intent.PasswordVisibilityToggled) }) {
                        Icon(
                            imageVector = if (state.passwordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (state.passwordVisible) "Şifreyi gizle" else "Şifreyi göster",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                visualTransformation = if (state.passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = state.error != null,
                supportingText = {
                    Text(
                        text = state.error ?: "En az 8 karakter, bir rakam içermeli.",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (state.error != null) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                },
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Checkbox(
                    checked = state.termsAccepted,
                    onCheckedChange = { onIntent(RegisterContract.Intent.TermsAcceptanceToggled) },
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("Kullanım Koşulları")
                        }
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                            append(" ve ")
                        }
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("Gizlilik Politikası")
                        }
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                            append("'nı okudum, kabul ediyorum.")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(16.dp))

            FilledTonalButton(
                onClick = { onIntent(RegisterContract.Intent.RegisterClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.termsAccepted && !state.isLoading,
            ) {
                Text(
                    text = if (state.isLoading) "Kaydediliyor..." else "Kayıt ol",
                    style = MaterialTheme.typography.labelLarge,
                )
                if (!state.isLoading) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Zaten hesabın var mı? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Giriş yap",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onIntent(RegisterContract.Intent.NavigateToLoginClicked) },
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenLightPreview() {
    LyraTheme(darkTheme = false) {
        RegisterScreen(
            state = RegisterContract.State(),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenDarkPreview() {
    LyraTheme(darkTheme = true) {
        RegisterScreen(
            state = RegisterContract.State(),
            onIntent = {},
        )
    }
}
