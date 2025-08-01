package org.mifos.connector.channel.api.fineract_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author manoj
 **/

@Getter
@Setter
public class HookResponseData {
    private Long clientId;
    private Long savingsId;
    private Long resourceId;
    private Map<String,Object> changes;
    private String resourceExternalId;
}
