package com.example.fundflow.onboarding

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundflow.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginButtonsWithText(
    context: Context,
    firebaseAuth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient,
    onLoginSuccess: (String) -> Unit // Callback saat login berhasil
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            onLoginSuccess(user?.displayName ?: "Unknown User")
                        } else {
                            Log.e("Login", "Login with Google failed", task.exception)
                        }
                    }
            } catch (e: ApiException) {
                Log.e("Login", "Google Sign-In failed", e)
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonUi(
            text = "Login",
            backgroundColor = Color(0xFF3F40FC),
            textColor = Color.White,
            fontSize = 16,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clip(RoundedCornerShape(25.dp)),
            onClick = { /* Implementasikan login lainnya di sini */ }
        )

        Text(
            text = "atau",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        GoogleLoginButton(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clip(RoundedCornerShape(25.dp))
        )
    }
}

@Composable
fun ButtonUi(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    fontSize: Int
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(25.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = textColor, fontSize = fontSize.sp)
    }
}

@Composable
fun GoogleLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(25.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Login with Google",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Login with Google",
                fontSize = 16.sp
            )
        }
    }
}

