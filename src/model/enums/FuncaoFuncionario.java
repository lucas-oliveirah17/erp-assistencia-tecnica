package model.enums;

public enum FuncaoFuncionario {
	TECNICO("tecnico"),
    ATENDENTE("atendente"),
    GERENTE("gerente"),
    RH("rh"),
    DIRETOR("diretor");

    private final String valorDb;

    FuncaoFuncionario(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case TECNICO: return "Técnico";
            case ATENDENTE: return "Atendente";
            case GERENTE: return "Gerente";
            case RH: return "RH";
            case DIRETOR: return "Diretor";
            default: return valorDb;
        }
    }

    public static FuncaoFuncionario fromDb(String valor) {
        for (FuncaoFuncionario t : values()) {
            if (t.valorDb.equals(valor)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Valor inválido para FuncaoFuncionario: " + valor);
    }
}