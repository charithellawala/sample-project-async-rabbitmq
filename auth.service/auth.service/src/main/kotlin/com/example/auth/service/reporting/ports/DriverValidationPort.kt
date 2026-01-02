package com.example.auth.service.reporting.ports

import com.example.auth.service.common.AuthorizationResult

interface DriverValidationPort {
    fun validate(driverToken: String): AuthorizationResult
}