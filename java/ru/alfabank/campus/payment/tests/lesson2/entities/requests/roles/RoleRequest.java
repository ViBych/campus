package ru.alfabank.campus.payment.tests.lesson2.entities.requests.roles;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.alfabank.campus.payment.tests.lesson2.enums.UserRights;

@Data
@Builder
public class RoleRequest {

    @JsonProperty("xPin")
    private String userId;

    @JsonProperty("uPin")
    private String organizationId;

    @JsonProperty("rule")
    private String right;

    public static RoleRequest of(String userId, String organizationId, UserRights rightName) {
        return RoleRequest.builder()
                .userId(userId)
                .organizationId(organizationId)
                .right(rightName.name())
                .build();
    }
}
