package gov.irs.jios.service.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import gov.irs.jios.model.JiosRequest;

public abstract class AbstractETCSPClientAsync {

    @Async
    public abstract CompletableFuture<ResponseEntity<Object>> processFormAsync(JiosRequest request);
}
