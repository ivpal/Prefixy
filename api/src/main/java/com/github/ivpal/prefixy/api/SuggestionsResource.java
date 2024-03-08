package com.github.ivpal.prefixy.api;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;

@Path("/api/suggestions")
@RunOnVirtualThread
public class SuggestionsResource {
    private static final Logger log = Logger.getLogger(SuggestionsResource.class);
    @Inject SuggestionStore suggestionStore;

    @Inject
    @Channel("prefixes")
    Emitter<String> prefixEmitter;

    @GET
    @Path("/")
    public List<String> list(@RestQuery("q") String query) {
        return suggestionStore.list(query);
    }

    @POST
    @Path("/")
    @ResponseStatus(202)
    public void create(@RestQuery("q") String query) {
        log.info("Received q=" + query);
        prefixEmitter.send(query.toLowerCase());
    }
}
