package teste;

public class teste {

	public static void main(String[] args) {
		binarioParaDecimal("11000000.01000000.00100000.010 | 00000");
	}

	private static void binarioParaDecimal(String id) {
		Integer[] values = { 0, 0, 0, 0 };
		Integer oceteto = 0;
		Integer tabela = 128;
		int x = 0;
		while (x < 3) {
			while (tabela >= 1) {
				for (Character c : id.toCharArray()) {
					if (c.equals('.')) {
						values[x] = oceteto;
						oceteto = 0;
						tabela = 128;
						x++;
					}
					if (!c.equals('|') && !c.equals(' ') && !c.equals('.')) {
						Integer bit = Integer.parseInt(c.toString());
						if (bit == 1) {
							oceteto += tabela;
						}
						tabela = tabela / 2;
					}
				}
			}			
			values[x] = oceteto;
		}
		System.out.println(values[0] + "." + values[1] + "." + values[2] + "." + values[3]);
	}

}
