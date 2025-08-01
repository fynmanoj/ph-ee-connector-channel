package org.mifos.connector.channel.api.fineract_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.mifos.connector.channel.api.fineract_api.dto.WithdrawHookRequest;
import org.mifos.connector.channel.api.fineract_api.service.MpesaConnectorService;
import org.mifos.connector.channel.api.fineract_api.service.MpesaConnectorServiceImpl;
import org.mifos.connector.common.gsma.dto.GSMATransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.mifos.connector.channel.camel.config.CamelProperties.BATCH_ID_HEADER;

/**
 * @author manoj
 **/

@RestController
public class TransactionAPIController {

    @Autowired
    private MpesaConnectorService mpesaConnectorService;

    @PostMapping("/channel/hook/withdraw")
    ResponseEntity<Object> withdrawHook(@RequestBody WithdrawHookRequest request,
                                        @RequestHeader("fineract-platform-tenantid") String tenantId) {
        // Example: logging received data
        System.out.println("Received withdrawal hook for clientId: " + request.getClientId());
        System.out.println("Transaction amount: " + request.getRequest().getTransactionAmount());
        System.out.println("Transaction date: " + request.getRequest().getTransactionDate());

        mpesaConnectorService.sendCreditRequest(tenantId, request);

        return ResponseEntity.ok("Withdrawal hook processed successfully");
    }
}
