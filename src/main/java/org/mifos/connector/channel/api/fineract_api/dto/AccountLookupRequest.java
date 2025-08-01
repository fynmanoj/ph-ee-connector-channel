package org.mifos.connector.channel.api.fineract_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author manoj
 **/

@Getter
@Setter
@Builder
public class AccountLookupRequest {
    private String MSISDN;
    private String TenantID;
    private String AccountNO;
}
