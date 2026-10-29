package com.devy.global.model

enum class Gender(val displayName: String) {
    MALE("남"), FEMALE("여");

    companion object {
        fun fromDisplayName(displayName: String): Gender? {
            return Gender.entries.find { it.displayName == displayName }
        }

        fun fromName(name: String): Gender {
            return Gender.entries.find { it.name == name } ?: Gender.MALE
        }
    }
}