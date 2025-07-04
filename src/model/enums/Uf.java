package model.enums;

public enum Uf {
    AC("AC"), AL("AL"), AP("AP"), AM("AM"), BA("BA"), CE("CE"), DF("DF"), ES("ES"),
    GO("GO"), MA("MA"), MT("MT"), MS("MS"), MG("MG"), PA("PA"), PB("PB"), PR("PR"),
    PE("PE"), PI("PI"), RJ("RJ"), RN("RN"), RS("RS"), RO("RO"), RR("RR"), SC("SC"),
    SP("SP"), SE("SE"), TO("TO");

    private final String valorDb;

    Uf(String valorDb) {
        this.valorDb = valorDb;
    }

    public String getValorDb() {
        return valorDb;
    }

    @Override
    public String toString() {
        return valorDb;
    }
    
    public String getNomeCompleto() {
        switch (this) {
            case AC: return "Acre";
            case AL: return "Alagoas";
            case AP: return "Amapá";
            case AM: return "Amazonas";
            case BA: return "Bahia";
            case CE: return "Ceará";
            case DF: return "Distrito Federal";
            case ES: return "Espírito Santo";
            case GO: return "Goiás";
            case MA: return "Maranhão";
            case MT: return "Mato Grosso";
            case MS: return "Mato Grosso do Sul";
            case MG: return "Minas Gerais";
            case PA: return "Pará";
            case PB: return "Paraíba";
            case PR: return "Paraná";
            case PE: return "Pernambuco";
            case PI: return "Piauí";
            case RJ: return "Rio de Janeiro";
            case RN: return "Rio Grande do Norte";
            case RS: return "Rio Grande do Sul";
            case RO: return "Rondônia";
            case RR: return "Roraima";
            case SC: return "Santa Catarina";
            case SP: return "São Paulo";
            case SE: return "Sergipe";
            case TO: return "Tocantins";
            default: return valorDb;
        }
    }

    public static Uf fromDb(String valor) {
        for (Uf uf : values()) {
            if (uf.valorDb.equals(valor)) {
                return uf;
            }
        }
        throw new IllegalArgumentException("Valor inválido para Uf: " + valor);
    }
    
    public static Uf fromString(String rotulo) {
        for (Uf uf : values()) {
            if (uf.toString().equals(rotulo)) {
                return uf;
            }
        }
        throw new IllegalArgumentException("UF inválido: " + rotulo);
    }
}
