package com.example.jora.extensionAPI

fun String.toPasswordVisualTransformation(isActive: Boolean): String {
    return if (isActive) { "●".repeat(this.length) } else { this }
}