package model.enums;

public enum FormaPagamento {
    CARTAO_CREDITO("cartao_credito"),
    DEBITO("debito"),
    PIX("pix"),
    BOLETO("boleto");

    private final String valorDb;

    FormaPagamento(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case CARTAO_CREDITO: return "Cartão de Crédito";
            case DEBITO: return "Débito";
            case PIX: return "PIX";
            case BOLETO: return "Boleto";
            default: return valorDb;
        }
    }

    public static FormaPagamento fromDb(String valor) {
        for (FormaPagamento f : values()) {
            if (f.valorDb.equals(valor)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Valor inválido para FormaPagamento: " + valor);
    }
}
