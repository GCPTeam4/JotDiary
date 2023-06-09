package gcp.global.jotdiary.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gcp.global.jotdiary.R
import gcp.global.jotdiary.viewmodel.LoginViewModel
import gcp.global.jotdiary.viewmodel.SettingsUiState
import gcp.global.jotdiary.viewmodel.SettingsViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage:() -> Unit,
    onNavToSignUpPage:() -> Unit,
    preferences: SettingsViewModel?
) {

    val settingsUiState = preferences?.settingsUiState ?: SettingsUiState()

    val loginUiState = loginViewModel?.loginUiState

    val isError = loginUiState?.loginError != null

    val context = LocalContext.current

    val loginTextfieldStyle: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colors.onSurface,
        unfocusedBorderColor = MaterialTheme.colors.onSurface,
        focusedLabelColor = MaterialTheme.colors.onSurface,
        unfocusedLabelColor = MaterialTheme.colors.onSurface,
        cursorColor = MaterialTheme.colors.onSurface,
        errorCursorColor = Color.Red,
        errorLabelColor = Color.Red,
        errorTrailingIconColor = Color.Red,
        errorLeadingIconColor = Color.Red,
        trailingIconColor = MaterialTheme.colors.onSurface,
        leadingIconColor = MaterialTheme.colors.onSurface,
        textColor = MaterialTheme.colors.onSurface,
    )

    var themeEmoji = if (settingsUiState.darkMode) "🌙" else "☀️"

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = themeEmoji)
            Switch(checked = settingsUiState.darkMode, onCheckedChange = { preferences?.onDarkModeChange() } )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Image(
                modifier = Modifier
                    .size(100.dp),
                painter = painterResource(id = R.drawable.final_logo),
                contentDescription = "Logo")

            Text(
                text = "JotDiary",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onSurface,
                fontSize = 30.sp
            )
        }

        if (isError){
            Text(
                text = loginUiState?.loginError ?: "unknown error",
                color = Color.Red,
                textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = loginUiState?.userName ?: "",
                onValueChange = {loginViewModel?.onUserNameChange(it)},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )
                },
                label = {
                    Text(
                        text = "Email",
                        color = MaterialTheme.colors.onSurface
                    )
                },
                isError = isError,
                colors = loginTextfieldStyle
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = loginUiState?.password ?: "",
                onValueChange = { loginViewModel?.onPasswordNameChange(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )
                },
                label = {
                    Text(
                        text = "Password",
                        color = MaterialTheme.colors.onSurface
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = isError,
                colors = loginTextfieldStyle
            )

            Button(
                onClick = { loginViewModel?.loginUser(context) },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, end = 48.dp, top = 8.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "CONTINUE",
                    color = MaterialTheme.colors.primary,

                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an Account?",
                    fontStyle = MaterialTheme.typography.body1.fontStyle,
                    color = MaterialTheme.colors.surface,
                )
                Spacer(modifier = Modifier.size(8.dp))
                TextButton(onClick = { onNavToSignUpPage.invoke() }) {
                    Text(text = "SignUp", fontSize = 12.sp, color = MaterialTheme.colors.onSurface)
                }
            }
        }

        if (loginUiState?.isLoading == true){
            CircularProgressIndicator()
        }

        LaunchedEffect(key1 = loginViewModel?.hasUser){
            if (loginViewModel?.hasUser == true){
                onNavToHomePage.invoke()
            }
        }
    }
}

@Composable
fun SignUpScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage:() -> Unit,
    onNavToLoginPage:() -> Unit,
) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.signUpError != null
    val context = LocalContext.current

    val signupTextfieldStyle: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colors.onSurface,
        unfocusedBorderColor = MaterialTheme.colors.onSurface,
        focusedLabelColor = MaterialTheme.colors.onSurface,
        unfocusedLabelColor = MaterialTheme.colors.onSurface,
        cursorColor = MaterialTheme.colors.onSurface,
        errorCursorColor = Color.Red,
        errorLabelColor = Color.Red,
        errorTrailingIconColor = Color.Red,
        errorLeadingIconColor = Color.Red,
        trailingIconColor = MaterialTheme.colors.onSurface,
        leadingIconColor = MaterialTheme.colors.onSurface,
        textColor = MaterialTheme.colors.onSurface,
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar(
            modifier = Modifier.padding(vertical = 8.dp),
            backgroundColor = MaterialTheme.colors.onSurface,
            title = {
                Text(
                    text = "SignUp",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
        )

        if (isError){
            Text(
                text = loginUiState?.signUpError ?: "unknown error",
                color = Color.Red,
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            value = loginUiState?.userNameSignUp ?: "",
            onValueChange = {loginViewModel?.onUserNameChangeSignup(it)},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                )
            },
            label = {
                Text(
                    text = "Email",
                    color = MaterialTheme.colors.onSurface
                )
            },
            isError = isError,
            colors = signupTextfieldStyle
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            value = loginUiState?.passwordSignUp ?: "",
            onValueChange = { loginViewModel?.onPasswordChangeSignup(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                )
            },
            label = {
                Text(
                    text = "Password",
                    color = MaterialTheme.colors.onSurface
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isError,
            colors = signupTextfieldStyle
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            value = loginUiState?.confirmPasswordSignUp ?: "",
            onValueChange = { loginViewModel?.onConfirmPasswordChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                )
            },
            label = {
                Text(
                    text = "Confirm Password",
                    color = MaterialTheme.colors.onSurface
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isError,
            colors = signupTextfieldStyle
        )

        Button(
            onClick = { loginViewModel?.createUser(context) },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onSurface),
        ) {
            Text(
                text = "Sign Up",
                color = MaterialTheme.colors.primary)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an Account?",
                color = MaterialTheme.colors.surface,
            )
            Spacer(modifier = Modifier)
            TextButton(onClick = { onNavToLoginPage.invoke() }) {
                Text(text = "Sign In", fontSize = 12.sp, color = MaterialTheme.colors.onSurface)
            }

        }

        if (loginUiState?.isLoading == true){
            CircularProgressIndicator()
        }

        LaunchedEffect(key1 = loginViewModel?.hasUser){
            if (loginViewModel?.hasUser == true){
                onNavToHomePage.invoke()
            }
        }
    }
}

/*
@Preview(widthDp = 360, heightDp = 640, showBackground = true, name = "Login Screen")
@Composable
fun LoginScreenPreview() {
    LoginScreen(onNavToHomePage = {}, onNavToSignUpPage = {})
}

@Preview(widthDp = 360, heightDp = 640, showBackground = true, name = "Sign Up Screen")
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(onNavToHomePage = {}, onNavToLoginPage = {})
}
*/