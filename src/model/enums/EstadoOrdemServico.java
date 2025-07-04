package model.enums;

public enum EstadoOrdemServico {
    FINALIZADO("finalizado"),
    EM_MANUTENCAO("em_manutencao"),
    AGUARDANDO_RESPOSTA("aguardando_resposta");

    private final String valorDb;

    EstadoOrdemServico(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case FINALIZADO: return "Finalizado";
            case EM_MANUTENCAO: return "Em Manutenção";
            case AGUARDANDO_RESPOSTA: return "Aguardando Resposta";
            default: return valorDb;
        }
    }

    public static EstadoOrdemServico fromDb(String valor) {
        for (EstadoOrdemServico e : values()) {
            if (e.valorDb.equals(valor)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Valor inválido para EstadoOrdemServico: " + valor);
    }
    
    public static EstadoOrdemServico fromString(String texto) {
        for (EstadoOrdemServico e : values()) {
            if (e.toString().equalsIgnoreCase(texto)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Texto inválido para EstadoOrdemServico: " + texto);
    }
}
