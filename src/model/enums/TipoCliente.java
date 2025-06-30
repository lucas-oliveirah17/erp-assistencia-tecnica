package model.enums;

public enum TipoCliente {
    PF("pf"),
    PJ("pj");

    private final String valorDb;

    TipoCliente(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case PF: return "Pessoa Física";
            case PJ: return "Pessoa Jurídica";
            default: return valorDb;
        }
    }

    public static TipoCliente fromDb(String valor) {
        for (TipoCliente t : values()) {
            if (t.valorDb.equals(valor)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoCliente: " + valor);
    }
}
