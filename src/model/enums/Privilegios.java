package model.enums;

public enum Privilegios {
    ADMINISTRADOR("administrador"),
    USUARIO("usuario");

    private final String valorDb;

    Privilegios(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case ADMINISTRADOR: return "Administrador";
            case USUARIO: return "Usuário";
            default: return valorDb;
        }
    }

    public static Privilegios fromDb(String valor) {
        for (Privilegios p : values()) {
            if (p.valorDb.equals(valor)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Valor inválido para Privilegios: " + valor);
    }
}
