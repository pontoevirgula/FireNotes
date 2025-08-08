package com.chslcompany.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue



@Composable
fun AuthScreen(modifier: Modifier = Modifier,
               navigateToNotesNavGraph: () -> Unit){
    val viewModel = hiltViewModel<AuthViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val isLogin by viewModel.isLogin.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState.navigateToNotesNavGraph) {
            navigateToNotesNavGraph()
        }
    }

    AuthScreenContent(
        modifier = modifier.fillMaxSize(),
        email = email,
        onEmailChange = viewModel::onEmailChange,
        password = password,
        onPasswordChange = viewModel::onPasswordChange,
        isLogin = isLogin,
        onToggleChange = viewModel::onToggleChange,
        onLogin = viewModel::login,
        onRegister = viewModel::register,
        isLoading = isLoading
    )
}


@Composable
fun AuthScreenContent(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password : String,
    onPasswordChange: (String) -> Unit,
    isLogin: Boolean,
    onToggleChange: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    isLoading: Boolean
){
    Column(modifier
        .padding(horizontal = 16.dp)
        .fillMaxSize()
    ) {
        Text(
            "FireNotes", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text("Email")
        Spacer(Modifier.height(8.dp))
        TextField(
            value = email, onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Text("Senha")
        Spacer(Modifier.height(8.dp))
        TextField(
            value = password, onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (isLogin) {
                Button(
                    onClick = onLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                ) {
                    Text("Login")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Deseja criar uma conta?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        onToggleChange()
                    }
                )
            } else {
                Button(
                    onClick = onRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                ) {
                    Text("Criar conta")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Já tem uma conta? Faça seu Login",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        onToggleChange()
                    }
                )
            }
        }
    }
}