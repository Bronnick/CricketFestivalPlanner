package com.cricketfest.cricketfestivalplanner.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val shapes = LocalAppTheme.shapes

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label, style = typo.caption) },
            placeholder = if (placeholder.isNotEmpty()) {
                { Text(text = placeholder, style = typo.bodyMedium, color = colors.textSecondary) }
            } else null,
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            shape = shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accent,
                unfocusedBorderColor = colors.divider,
                errorBorderColor = colors.error,
                focusedLabelColor = colors.accent,
                unfocusedLabelColor = colors.textSecondary,
                cursorColor = colors.accent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError && errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = errorMessage,
                style = typo.caption,
                color = colors.error
            )
        }
    }
}
