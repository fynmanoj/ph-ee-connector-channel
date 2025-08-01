package org.mifos.connector.channel.api.fineract_api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author manoj
 **/

@Getter
@Setter
public class CreditAmountResponse {
    private String Reference;
    private String RRN;
    private String ResultCode;
    private String ResultDesc;
    private String TenantID;
}
