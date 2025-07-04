package model.enums;

public enum TipoPagamento {
    AVISTA("avista"),
    PARCELADO("parcelado");

    private final String valorDb;

    TipoPagamento(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case AVISTA: return "À vista";
            case PARCELADO: return "Parcelado";
            default: return valorDb;
        }
    }

    public static TipoPagamento fromDb(String valor) {
        for (TipoPagamento t : values()) {
            if (t.valorDb.equals(valor)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoPagamento: " + valor);
    }
    
    public static TipoPagamento fromString(String texto) {
        for (TipoPagamento t : values()) {
            if (t.toString().equalsIgnoreCase(texto)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Texto inválido para TipoPagamento: " + texto);
    }
}
