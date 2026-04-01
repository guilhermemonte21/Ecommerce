package com.github.guilhermemonte21.Ecommerce.Infra.Config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        if (stripeApiKey != null && !stripeApiKey.trim().isEmpty()) {
            Stripe.apiKey = stripeApiKey;
        }
    }
}
