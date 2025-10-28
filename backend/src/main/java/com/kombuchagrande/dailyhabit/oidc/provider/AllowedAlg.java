package com.kombuchagrande.dailyhabit.oidc.provider;

public enum AllowedAlg {
    RS256, PS256, ES256, EDDSA;

    public static AllowedAlg fromHeader(String headerAlg) {
        if (headerAlg == null || headerAlg.isBlank()) {
            throw new IllegalArgumentException("alg header missing");
        }
        String normalized = headerAlg
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase(java.util.Locale.ROOT);
        try {
            return AllowedAlg.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported alg in header: " + headerAlg);
        }
    }
}