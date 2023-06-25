package com.example.AloneShip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ViewsController {
    private final RegisteredClientRepository registeredClientRepository;
    @GetMapping("/mylogin")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/oauth2/consent")
    public String consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state
    ) {
        Set<String> scopesToApprove = new LinkedHashSet<>();
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient != null) {
            Set<String> scopes = registeredClient.getScopes();
            for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
                if (scopes != null && scopes.contains(requestedScope)) {
                    scopesToApprove.add(requestedScope);
                }
            }
            model.addAttribute("clientId", clientId);
            model.addAttribute("clientName", registeredClient.getClientName());
            model.addAttribute("state", state);
            model.addAttribute("scopes", withDescription(scopesToApprove));
            model.addAttribute("principalName", principal.getName());
            model.addAttribute("redirectUri", registeredClient.getRedirectUris().iterator().next());
        }
        return "consent";
    }

    private static Set<ScopeWithDescription> withDescription(Set<String> scopes) {
        Set<ScopeWithDescription> scopeWithDescriptions = new LinkedHashSet<>();
        for (String scope : scopes) {
            scopeWithDescriptions.add(new ScopeWithDescription(scope));

        }
        return scopeWithDescriptions;
    }
    public static class ScopeWithDescription {
        private static final String DEFAULT_DESCRIPTION = "无法提供有关此权限的信息";
        private static final Map<String, String> scopeDescriptions = new HashMap<>();

        static {
            scopeDescriptions.put(
                    "music",
                    "about music permissions"
            );
        }

        public final String scope;
        public final String description;

        ScopeWithDescription(String scope) {
            this.scope = scope;
            this.description = scopeDescriptions.getOrDefault(scope, DEFAULT_DESCRIPTION);
        }
    }
}
