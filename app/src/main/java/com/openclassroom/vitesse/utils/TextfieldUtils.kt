package com.openclassroom.vitesse.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.openclassroom.vitesse.R

@Composable
fun LabelledTextField(
    labelId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    val focusRequester = remember { FocusRequester() }
    Column(modifier = Modifier.padding(top = 16.dp, start = 32.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(id = labelId)) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            maxLines = maxLines,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            modifier = Modifier.fillMaxWidth()
                .focusRequester(focusRequester)
                .clickable { focusRequester.requestFocus() },
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorTrailingIconColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error
            )
        )
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun LabelledIconTextField(
    labelId: Int,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    val focusRequester = remember { FocusRequester() }
    Row(
        modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon of " + stringResource(id = labelId),
            modifier = Modifier.padding(end = 8.dp)
                .then(
                    if (labelId == R.string.notes) Modifier else Modifier.align(Alignment.CenterVertically)
                ),
        )
        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(stringResource(id = labelId))
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = singleLine,
                maxLines = maxLines,
                readOnly = readOnly,
                trailingIcon = trailingIcon,
                modifier = Modifier.fillMaxWidth()
                    .then(
                        if (labelId == R.string.notes) Modifier.height(175.dp) else Modifier
                    )
                    .focusRequester(focusRequester)
                    .clickable { focusRequester.requestFocus() },
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorTrailingIconColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )
            if (isError && errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
        }
    }
}