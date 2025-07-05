package util;

public class Dimensao {
	private int comprimento;
	private int largura;
	private int altura;
	
	public Dimensao(int comprimento, int largura, int altura) {
		
		this.setDimensao(comprimento, largura, altura);
	}
	
	public Dimensao(String dimensao) {
		if (dimensao == null || !dimensao.matches("\\d+x\\d+x\\d+")) {
            throw new IllegalArgumentException(
        		"Formato inválido. Use o padrão 'CxLxA', ex: '10x5x3'."
            );
        }
		
		String[] partes = dimensao.split("x");
        int c = Integer.parseInt(partes[0]);
        int l = Integer.parseInt(partes[1]);
        int a = Integer.parseInt(partes[2]);
        
        this.setDimensao(c, l, a);
	}
	
	private void setDimensao(int c, int l, int a) {
		if (c <= 0 || l <= 0 || a <= 0) {
            throw new IllegalArgumentException(
        		"Todas as dimensões devem ser maiores que zero."
    		);
		}
		this.setComprimento(c);
		this.setLargura(l);
		this.setAltura(a);	
	}
	
	@Override
    public String toString() {
        return comprimento + "x" + largura + "x" + altura;
    }
	

	public int getComprimento() {
		return this.comprimento;
	}

	private void setComprimento(int comprimento) {
		this.comprimento = comprimento;
	}

	
	public int getLargura() {
		return this.largura;
	}

	private void setLargura(int largura) {
		this.largura = largura;
	}

	
	public int getAltura() {
		return this.altura;
	}

	private void setAltura(int altura) {
		this.altura = altura;
	}
}