package com.vicheak.mbankingapi.api.user.web;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record UserDto(String uuid,
                      String username,
                      String email,
                      String gender,
                      String phoneNumber,
                      @JsonInclude(JsonInclude.Include.NON_NULL)
                      String oneSignalId,
                      @JsonInclude(JsonInclude.Include.NON_NULL)
                      Boolean isStudent,
                      @JsonInclude(JsonInclude.Include.NON_NULL)
                      String studentCardNo,
                      Boolean isVerified,
                      List<UserRoleDto> userRoles) {
}
