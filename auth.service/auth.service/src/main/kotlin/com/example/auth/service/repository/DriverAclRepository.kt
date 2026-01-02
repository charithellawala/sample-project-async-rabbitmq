package com.example.auth.service.repository

import com.example.auth.service.model.DriverEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DriverAclRepository :
    JpaRepository<DriverEntity, String> {
}