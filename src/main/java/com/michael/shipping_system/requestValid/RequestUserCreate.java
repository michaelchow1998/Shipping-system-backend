package com.michael.shipping_system.requestValid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.michael.shipping_system.Enum.Sex;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RequestUserCreate {


    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotNull
    private Sex sex;

    @NotNull
    @JsonProperty("ans")
    private String keyQuestionAns;

}
