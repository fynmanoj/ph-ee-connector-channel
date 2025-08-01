package org.mifos.connector.channel.api.fineract_api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author manoj
 **/

@Getter
@Setter
public class AccountLookupResponse {
    private String KYCName;
    private String ResponseCode;
    private String ResponseDesc;
}
