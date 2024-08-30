package com.example.demo.dto.request;

import com.example.demo.constant.RoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest implements Serializable {
    @NotBlank(message = "Role name can not be blank")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
}