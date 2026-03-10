package com.cricketfest.cricketfestivalplanner.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cricketfest.cricketfestivalplanner.ui.theme.LocalAppTheme

@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val shapes = LocalAppTheme.shapes

    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled,
        shape = shapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.accent,
            contentColor = colors.onAccent,
            disabledContainerColor = colors.divider,
            disabledContentColor = colors.textSecondary
        )
    ) {
        Text(text = text, style = typo.bodyLarge)
    }
}

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = LocalAppTheme.colors
    val typo = LocalAppTheme.typography
    val shapes = LocalAppTheme.shapes

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled,
        shape = shapes.button,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = colors.accent
        )
    ) {
        Text(text = text, style = typo.bodyLarge)
    }
}
