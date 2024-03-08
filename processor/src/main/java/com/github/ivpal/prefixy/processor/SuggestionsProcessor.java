package com.github.ivpal.prefixy.processor;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SuggestionsProcessor {
    private static final Logger log = Logger.getLogger(SuggestionsProcessor.class);

    @Inject
    SuggestionStore suggestionStore;

    @Incoming("prefix-requests")
    @RunOnVirtualThread
    public void consume(String prefix) {
        log.info(String.format("Received '%s'", prefix));
        suggestionStore.increment(prefix);
    }
}
