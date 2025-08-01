package org.mifos.connector.channel.api.fineract_api.service;

import lombok.Data;
import lombok.extern.java.Log;
import org.mifos.connector.channel.api.fineract_api.dto.AccountLookupRequest;
import org.mifos.connector.channel.api.fineract_api.dto.AccountLookupResponse;
import org.mifos.connector.channel.api.fineract_api.dto.CreditAmountRequest;
import org.mifos.connector.channel.api.fineract_api.dto.CreditAmountResponse;
import org.mifos.connector.channel.api.fineract_api.dto.WithdrawHookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * @author manoj
 * Move this class to mepasa connector
 **/

@Service
public class MpesaConnectorServiceImpl implements MpesaConnectorService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mpesa.account.service.lookup.api.url}")
    private String accountLookUpUrl;
    @Value("${mpesa.account.service.credit.api.url}")
    private String accountCreditUrl;
    @Value("${mpesa.account.service.auth.token}")
    private String oauthToken;
    @Value("${mpesa.account.service.paymentTypeId}")
    private String paymentTypeId;

    public AccountLookupResponse lookupAccount(AccountLookupRequest requestPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", oauthToken);

            HttpEntity<AccountLookupRequest> requestEntity = new HttpEntity<>(requestPayload, headers);

            ResponseEntity<AccountLookupResponse> responseEntity = restTemplate.exchange(
                    accountLookUpUrl,
                    HttpMethod.POST,
                    requestEntity,
                    AccountLookupResponse.class
            );

            return responseEntity.getBody();

        } catch (Exception ex) {
            logger.error("Account lookup failed: ", ex);
            throw new RuntimeException("Account lookup failed", ex);
        }
    }

    public CreditAmountResponse creditAmount(CreditAmountRequest requestPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth("YOUR_ACCESS_TOKEN_HERE");  // Or: headers.add("Authorization", OAUTH_TOKEN);

            HttpEntity<CreditAmountRequest> requestEntity = new HttpEntity<>(requestPayload, headers);

            ResponseEntity<CreditAmountResponse> responseEntity = restTemplate.exchange(
                    accountCreditUrl,
                    HttpMethod.POST,
                    requestEntity,
                    CreditAmountResponse.class
            );

            return responseEntity.getBody();

        } catch (Exception ex) {
            logger.error("Credit amount API call failed: ", ex);
            throw new RuntimeException("Credit amount API call failed", ex);
        }
    }

    @Override
    public  void sendCreditRequest(String tenantId, WithdrawHookRequest request){

        if(!paymentTypeId.equals(request.getResponse().getChanges().getOrDefault("paymentTypeId","0"))){
            return;
        }

        AccountLookupRequest requestPayload = AccountLookupRequest.builder()
                .TenantID(tenantId)
                .AccountNO(request.getResponse().getResourceExternalId())
                .MSISDN((String) request.getResponse().getChanges().getOrDefault("clientMob", ""))
                .build();
        lookupAccount(requestPayload);

        CreditAmountRequest creditAmountRequest = CreditAmountRequest.builder()
                .Reference(request.getResponse().getResourceId().toString())
                .Amount(request.getRequest().getTransactionAmount())
                .MSISDN((String) request.getResponse().getChanges().getOrDefault("clientMob", ""))
                .TenantID(tenantId)
                .AccountNO(request.getResponse().getResourceExternalId())
                .build();
        creditAmount(creditAmountRequest);
    }

}
