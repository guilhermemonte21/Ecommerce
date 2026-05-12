package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Enum;

public enum TipoUsuario {
    COMPRADOR("Comprador"),
    VENDEDOR("Vendedor");

    private final String value;

    TipoUsuario(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean matches(String tipoStr) {
        return this.value.equalsIgnoreCase(tipoStr);
    }
}
