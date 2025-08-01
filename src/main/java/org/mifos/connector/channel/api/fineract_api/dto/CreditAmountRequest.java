package org.mifos.connector.channel.api.fineract_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author manoj
 **/

@Setter
@Getter
@Builder
public class CreditAmountRequest {
    private String Reference;
    private String Amount;
    private String MSISDN;
    private String TenantID;
    private String AccountNO;
}
