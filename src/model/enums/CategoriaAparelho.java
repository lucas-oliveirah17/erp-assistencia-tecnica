package model.enums;

public enum CategoriaAparelho {
	TELEVISAO("televisao"),
	MONITOR("monitor"),
	HOME_THEATER("home_theater"),
    CELULAR("celular"),
    TABLET("tablet"),
    APARELHO_SOM("aparelho_som"),
    NOTEBOOK("notebook"),
    COMPUTADOR("computador");

    private final String valorDb;

    CategoriaAparelho(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        switch (this) {
            case TELEVISAO: return "Em manutenção";
            case MONITOR: return "Monitor";
            case HOME_THEATER: return "Home Theather";
            case CELULAR: return "Celular";
            case TABLET: return "Tablet";
            case APARELHO_SOM: return "Aparelho de Som";
            case NOTEBOOK: return "Notebook";
            case COMPUTADOR: return "Computador";
            default: return valorDb;
        }
    }

    public static CategoriaAparelho fromDb(String valor) {
        for (CategoriaAparelho e : values()) {
            if (e.valorDb.equals(valor)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Valor inválido para CategoriaAparelho: " + valor);
    }
}
