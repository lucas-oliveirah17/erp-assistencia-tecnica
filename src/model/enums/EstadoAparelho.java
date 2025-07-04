package model.enums;

public enum EstadoAparelho {
    EM_MANUTENCAO("em_manutencao"),
    EM_ESTOQUE("em_estoque"),
    EM_ENTREGA("em_entrega");

    private final String valorDb;

    EstadoAparelho(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case EM_MANUTENCAO: return "Em manutenção";
            case EM_ESTOQUE: return "Em estoque";
            case EM_ENTREGA: return "Em entrega";
            default: return valorDb;
        }
    }

    public static EstadoAparelho fromDb(String valor) {
        for (EstadoAparelho e : values()) {
            if (e.valorDb.equals(valor)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Valor inválido para EstadoAparelho: " + valor);
    }
    
    public static EstadoAparelho fromString(String texto) {
        for (EstadoAparelho e : values()) {
            if (e.toString().equalsIgnoreCase(texto)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Texto inválido para EstadoAparelho: " + texto);
    }
}

