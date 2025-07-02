package model.enums;

public enum TipoServico {
	MANUTENCAO("manutencao"),
    ORCAMENTO("orcamento"),
    REPARO("reparo");

    private final String valorDb;

    TipoServico(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case MANUTENCAO: return "Manutenção";
            case ORCAMENTO: return "Orçamento";
            case REPARO: return "Reparo";
            default: return valorDb;
        }
    }

    public static TipoServico fromDb(String valor) {
        for (TipoServico t : values()) {
            if (t.valorDb.equals(valor)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoServico: " + valor);
    }
}
