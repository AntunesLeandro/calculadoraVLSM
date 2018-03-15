import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.JSeparator;

public class Tela extends JFrame {

	/**
	 * @author Leandro Antunes
	 * Date: 01-03-17
	 */
	private static final long serialVersionUID = 1L;
	private JPanel painelPrincipal;
	private JTextField txtField;
	private List<Integer> arrayComOsHosts = new ArrayList<Integer>();
	private List<Integer> arrayComOsHostsValidos= new ArrayList<Integer>();
	private JTextArea txtTela;
	private String mascPadraoBin;
	private Integer somaHosts = 0;
	private StringBuilder sb;
	private String[] mascaraCust;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela frame = new Tela();
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Tela() {
		criaTela();
	}

	private void criaTela()
	{
		/*FORMATA FRAME*/
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 355);
		
		/*CRIA COMPONENTES*/
		painelPrincipal = new JPanel();
		JLabel lblInformaIp = new JLabel("Informe o IP Inicial");
		txtField = new JTextField();
		JButton btnAddRede = new JButton("Adicionar Rede");
		JScrollPane painelRolagem = new JScrollPane();
		txtTela = new JTextArea();
		JSeparator spt = new JSeparator();
		JButton btnLimpar = new JButton("Limpar");
		
		/*EDITA COMPONENTES*/
		painelPrincipal.setBackground(Color.WHITE);
		painelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(painelPrincipal);
		painelPrincipal.setLayout(null);
		lblInformaIp.setBounds(10, 14, 78, 14);
		painelPrincipal.add(lblInformaIp);
		txtField.setBounds(89, 11, 118, 20);
		painelPrincipal.add(txtField);
		txtField.setColumns(12);
		btnAddRede.setBounds(10, 49, 364, 23);
		btnAddRede.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				String ipDec = txtField.getText();				
				String[] octetos = ipDec.split(Pattern.quote("."));
				for(String octeto : octetos){
					Integer valorDoOcteto  = Integer.valueOf(octeto);
					if(valorDoOcteto > 255){
						JOptionPane.showMessageDialog(null, "IP Inválido!");
						return;
					}
				}
				String qtdRedes = JOptionPane.showInputDialog("Informe a Quantidade de Redes");
				Integer qtdRedesInt = 0;
				try {
					qtdRedesInt = Integer.parseInt(qtdRedes);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Caracter inválido!");
					return;
				}
				for (int x = 0; x < qtdRedesInt; x++) {
					try {
						Integer y = x+1;
						arrayComOsHosts.add(
								Integer.parseInt(JOptionPane.showInputDialog("Quantidade de hosts para a " + y + " rede")));
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Caracter inválido!");						
						return;
					}
				}
				organizarHosts();				
				sb = new StringBuilder();
				for (int x = 0; x < octetos.length; x++) {
					String binario = Integer.toBinaryString(Integer.parseInt(octetos[x]));
					while (binario.length() < 8) {
						binario = "0" + binario;
					}
					sb.append(binario);
					if (x != 3) {
						sb.append(".");
					}
				}
				txtTela.append("REDE EM BIN: " + sb.toString() + "\n");
				String classe = null;
				if (Integer.parseInt(octetos[0]) >= 1 && Integer.parseInt(octetos[0]) <= 127) { //CLASSE A
					mascPadraoBin = "11111111.00000000.00000000.00000000";
					txtTela.append("MÁSC PADRÃO BIN: " + mascPadraoBin + "\n");
					if (somaHosts > Math.pow(2, 24)) {
						txtTela.append("Não é possivel colocar: " + somaHosts + " hosts nessa rede");
						return;
					}
					classe = "A";
				} else if (Integer.parseInt(octetos[0]) >= 128 && Integer.parseInt(octetos[0]) <= 191) { //CLASSE B
					mascPadraoBin = "11111111.11111111.00000000.00000000";
					txtTela.append("MÁSC PADRÃO BIN: " + mascPadraoBin + "\n");
					if (somaHosts > Math.pow(2, 16)) {
						txtTela.append("Não é possivel colocar: " + somaHosts + " hosts nessa rede");
						return;
					}
					classe = "B";
				} else if (Integer.parseInt(octetos[0]) >= 192 && Integer.parseInt(octetos[0]) <= 223) { //CLASSE C
					mascPadraoBin = "11111111.11111111.11111111.00000000";
					txtTela.append("MÁSC PADRÃO BIN: " + mascPadraoBin + "\n");
					if (somaHosts > Math.pow(2, 8)) {
						txtTela.append("Não é possivel colocar: " + somaHosts + " hosts nessa rede");
						return;
					}
					classe = "C";
				}
				txtTela.append("\nINFORMAÇÕES INSERIDAS: ");
				txtTela.append("\n");
				int contadorRedes = 1;
				for (Integer valor : arrayComOsHosts) {
					txtTela.append(contadorRedes + " REDE COM: " + valor + " HOSTS\n");
					contadorRedes++;
				}
				txtTela.append("\nINFORMAÇÕES QUE SERÃO CALCULADAS: ");
				contadorRedes = 1;
				for (Integer valor : arrayComOsHostsValidos) {
					txtTela.append(contadorRedes + " REDE COM: " + valor + " HOSTS (PRÓX POTÊNCIA VÁLIDA)\n");
					contadorRedes++;
				}
				txtTela.append("TOTAL DE HOSTS: " + somaHosts);
				txtTela.append("\n\n");
				

				exibeResultado(classe);
			}
			
		});
		painelPrincipal.add(btnAddRede);
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtField.setText("");
				txtTela.setText("");
				arrayComOsHosts = new ArrayList<Integer>();
				arrayComOsHostsValidos = new ArrayList<Integer>();
				mascPadraoBin = new String();
				if(mascaraCust != null){
					Arrays.fill(mascaraCust, null);
				}
				somaHosts = 0;
			}
		});
		btnLimpar.setBounds(296, 282, 78, 23);
		painelPrincipal.add(btnLimpar);
		painelRolagem.setBounds(10, 83, 364, 189);
		painelPrincipal.add(painelRolagem);
		painelRolagem.setViewportView(txtTela);
		spt.setBounds(156, 303, 1, 2);
		painelPrincipal.add(spt);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { painelPrincipal, txtField, lblInformaIp }));
	}
	
	private void organizarHosts() {
		for (Integer value : arrayComOsHosts) {
			boolean parada = false;
			int x = 2;
			while (!parada) {
				if (value == 1) {
					arrayComOsHostsValidos.add(x);
					somaHosts += x;
					parada = true;
				} else if (value >= x && value < (x * 2)) {
					if ((value + 2) > (x * 2)) {
						arrayComOsHostsValidos.add((x * 2) * 2);
						somaHosts += (x * 2);
						parada = true;
					} else {
						arrayComOsHostsValidos.add(x * 2);
						somaHosts += (x * 2);
						parada = true;
					}
				}
				x = x * 2;
			}
		}
		arrayComOsHostsValidos.sort(Collections.reverseOrder());
	}

	private void exibeResultado(String classe) {
		String[] aux = sb.toString().split(Pattern.quote("."));
		Integer[] arrayBin;
		if (classe.equals("A")) {
			arrayBin = new Integer[24];
			int x = 0;
			int octeto = 1;
			while (x <= 23) {
				for (Character c : aux[octeto].toCharArray()) {
					arrayBin[x] = Integer.parseInt(c.toString());
					x++;
				}
				if (octeto < 3) {
					octeto++;
				}
			}
			Integer salto = saltoBinDecA(arrayBin);
			for (Integer qtdHost : arrayComOsHostsValidos) {
				String qtdHostsString = Integer.toBinaryString(qtdHost);
				while (qtdHostsString.length() < 24) {
					qtdHostsString = "0" + qtdHostsString;
				}
				String saltos = Integer.toBinaryString(salto);
				while (saltos.length() < 24) {
					saltos = "0" + saltos;
				}
				int auxSalto = 0;
				for (Character c : saltos.toCharArray()) {
					arrayBin[auxSalto] = Integer.parseInt(c.toString());
					auxSalto++;
				}

				Integer contador = 0;
				Integer contadorAuxiliarParaSaida = 0;
				Integer qtdHostsAux = qtdHost;
				while (qtdHostsAux >= 2) {
					qtdHostsAux = qtdHostsAux / 2;
					contador++;
				}

				StringBuilder bits = new StringBuilder();
				for (Integer bit : arrayBin) {
					bits.append(bit.toString());
					if (bits.length() == 8 || bits.length() == 17) {
						bits.append(".");
					}
				}

				String id = aux[0] + "." + bits.toString();
				String idFormatado[] = retornaIp(id, contador);
				txtTela.append("REDE COM: " + String.valueOf(qtdHost) + " HOSTS\n");
				txtTela.append("ID EM DECIMAL: " + idFormatado[1] + "\n");
				txtTela.append("ID EM BINÁRIO: " + idFormatado[0] + "\n");

				for (int xC = 23; contador >= 1; contador--) {
					arrayBin[xC] = 1;
					xC--;
					contadorAuxiliarParaSaida++;
				}

				bits = new StringBuilder();
				for (Integer bit : arrayBin) {
					bits.append(bit.toString());
					if (bits.length() == 8 || bits.length() == 17) {
						bits.append(".");
					}
				}
				String bc = aux[0] + "." + bits.toString();
				String bcFormatado[] = retornaIp(bc, contadorAuxiliarParaSaida);
				mascaraCust = retornaMascara(contadorAuxiliarParaSaida);

				txtTela.append("BC EM DECIMAL: " + bcFormatado[1] + "\n");
				txtTela.append("BC EM BINÁRIO: " + bcFormatado[0] + "\n");
				txtTela.append("MÁSCARA CUST. EM DEC: " + mascaraCust[1] + "\n");
				txtTela.append("MÁSCARA CUST. EM BIN: " + mascaraCust[0] + "\n\n");

				salto += qtdHost;
			}
		} else if (classe.equals("B")) {
			arrayBin = new Integer[16];
			int x = 0;
			int octeto = 2;
			while (x <= 15) {
				for (Character c : aux[octeto].toCharArray()) {
					arrayBin[x] = Integer.parseInt(c.toString());
					x++;
				}
				if (octeto < 3) {
					octeto++;
				}
			}
			Integer salto = saltoBinDecB(arrayBin);
			for (Integer qtdHost : arrayComOsHostsValidos) {
				String qtdHostsString = Integer.toBinaryString(qtdHost);
				while (qtdHostsString.length() < 16) {
					qtdHostsString = "0" + qtdHostsString;
				}
				String saltos = Integer.toBinaryString(salto);
				while (saltos.length() < 16) {
					saltos = "0" + saltos;
				}
				int auxSalto = 0;
				for (Character c : saltos.toCharArray()) {
					arrayBin[auxSalto] = Integer.parseInt(c.toString());
					auxSalto++;
				}

				Integer contador = 0;
				Integer contadorAuxiliarParaSaida = 0;
				Integer qtdHostsAux = qtdHost;
				while (qtdHostsAux >= 2) {
					qtdHostsAux = qtdHostsAux / 2;
					contador++;
				}

				StringBuilder bits = new StringBuilder();
				for (Integer bit : arrayBin) {
					bits.append(bit.toString());
					if (bits.length() == 8) {
						bits.append(".");
					}
				}

				String id = aux[0] + "." + aux[1] + "." + bits.toString();
				String idFormatado[] = retornaIp(id, contador);
				txtTela.append("REDE COM: " + String.valueOf(qtdHost) + " HOSTS\n");
				txtTela.append("ID EM DECIMAL: " + idFormatado[1] + "\n");
				txtTela.append("ID EM BINÁRIO: " + idFormatado[0] + "\n");

				for (int xC = 15; contador >= 1; contador--) {
					arrayBin[xC] = 1;
					xC--;
					contadorAuxiliarParaSaida++;
				}

				bits = new StringBuilder();
				for (Integer bit : arrayBin) {
					bits.append(bit.toString());
					if (bits.length() == 8) {
						bits.append(".");
					}
				}
				String bc = aux[0] + "." + aux[1] + "." + bits.toString();
				String bcFormatado[] = retornaIp(bc, contadorAuxiliarParaSaida);
				mascaraCust = retornaMascara(contadorAuxiliarParaSaida);

				txtTela.append("BC EM DECIMAL: " + bcFormatado[1] + "\n");
				txtTela.append("BC EM BINÁRIO: " + bcFormatado[0] + "\n");
				txtTela.append("MÁSCARA CUST. EM DEC: " + mascaraCust[1] + "\n");
				txtTela.append("MÁSCARA CUST. EM BIN: " + mascaraCust[0] + "\n\n");

				salto += qtdHost;

			}
		} else if (classe.equals("C")) {
			arrayBin = new Integer[8];
			int octeto = 3;
			int x = 0;
			while (x <= 7) {
				for (Character c : aux[octeto].toCharArray()) {
					arrayBin[x] = Integer.parseInt(c.toString());
					x++;
				}
				if (octeto < 3) {
					octeto++;
				}
			}
			
			Integer salto = saltoBinDecC(arrayBin);
			for (Integer qtdHost : arrayComOsHostsValidos) {
				String qtdHostsString = Integer.toBinaryString(qtdHost);
				while (qtdHostsString.length() < 8) {
					qtdHostsString = "0" + qtdHostsString;
				}				
				String saltos = Integer.toBinaryString(salto);
				while (saltos.length() < 8) {
					saltos = "0" + saltos;
				}
				int auxSalto = 0;
				for (Character c : saltos.toCharArray()) {
					arrayBin[auxSalto] = Integer.parseInt(c.toString());
					auxSalto++;
				}

				Integer contador = 0;
				Integer contadorAuxiliarParaSaida = 0;
				Integer qtdHostsAux = qtdHost;
				while (qtdHostsAux >= 2) {
					qtdHostsAux = qtdHostsAux / 2;
					contador++;
				}

				StringBuilder bits = new StringBuilder();
				for (Integer bit : arrayBin) {
					bits.append(bit.toString());
					if (bits.length() == (8 - contador)) {
						bits.append(" | ");
					}
				}
				String id = aux[0] + "." + aux[1] + "." + aux[2] + "." + bits.toString();
				String idRedeDecimal = binToDec(id);
				txtTela.append("REDE COM " + String.valueOf(qtdHost) + " HOSTS\n");
				txtTela.append("ID EM DECIMAL: " + idRedeDecimal + "\n");
				txtTela.append("ID EM BINÁRIO: " + id + "\n");
				for (int xC = 7; contador >= 1; contador--) {
					arrayBin[xC] = 1;
					xC--;
					contadorAuxiliarParaSaida++;
				}

				bits = new StringBuilder();
				for (Integer bit : arrayBin) {
					bits.append(bit.toString());
					if (bits.length() == (8 - contadorAuxiliarParaSaida)) {
						bits.append(" | ");
					}
				}
				String bc = aux[0] + "." + aux[1] + "." + aux[2] + "." + bits.toString();
				String bcRedeDecimal = binToDec(bc);
				mascaraCust = retornaMascara(contadorAuxiliarParaSaida);

				txtTela.append("BC EM DECIMAL: " + bcRedeDecimal + "\n");
				txtTela.append("BC EM BINÁRIO: " + bc + "\n");
				txtTela.append("MÁSCARA CUST. EM DEC: " + mascaraCust[1] + "\n");
				txtTela.append("MÁSCARA CUST. EM BIN: " + mascaraCust[0] + "\n\n");

				salto += qtdHost;
			}
		}
	}
	
	private Integer saltoBinDecC(Integer[] array){
		Integer ip = 0;
		Integer tabela = 128;
		for(Integer i : array){
			if(i == 1){
				ip += tabela;
				tabela = tabela/2;
			}else{
				tabela = tabela/2;
			}
		}
		return ip;
	}
	
	private Integer saltoBinDecB(Integer[] array){
		Integer ip = 0;
		Integer tabela = 32768;
		for(Integer i : array){
			if(i == 1){
				ip += tabela;
				tabela = tabela/2;
			}else{
				tabela = tabela/2;
			}
		}
		return ip;
	}
	
	private Integer saltoBinDecA(Integer[] array){
		Integer ip = 0;
		Integer tabela = 8388608;
		for(Integer i : array){
			if(i == 1){
				ip += tabela;
				tabela = tabela/2;
			}else{
				tabela = tabela/2;
			}
		}
		return ip;
	}

	private String binToDec(String id) {
		Integer[] values = { 0, 0, 0, 0 };
		StringBuilder ip = new StringBuilder();
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
						ip.append(values[x].toString() + ".");
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
			ip.append(values[x] = oceteto);
		}
		return ip.toString();
	}

	private String[] retornaMascara(int casas) {
		StringBuilder mascara = new StringBuilder();
		String[] mascaraDecimalBinario = new String[2];
		boolean casasAtualizadas = false;
		for (int x = 0; x < 35; x++) {
			if (casas > 16 && !casasAtualizadas) {
				casas += 2;
				casasAtualizadas = true;
			} else if (casas > 8 && !casasAtualizadas) {
				casas += 1;
				casasAtualizadas = true;
			}
			if (x == 8 || x == 17 || x == 26) {
				mascara.append(".");				
			} else {				
				if (x < (35 - casas)) {
					mascara.append("1");
				} else {
					mascara.append("0");
				}
			}
		}
		mascaraDecimalBinario[0] = mascara.toString();
		mascaraDecimalBinario[1] = binToDec(mascara.toString());
		return mascaraDecimalBinario;
	}

	private String[] retornaIp(String ip, int casas) {
		StringBuilder ipFormatado = new StringBuilder();
		String[] ipDecimalBinario = new String[2];
		boolean casasAtualizadas = false;
		for (int x = 0; x < 35; x++) {
			ipFormatado.append(ip.charAt(x));
			if (casas > 16 && !casasAtualizadas) {
				casas += 2;
				casasAtualizadas = true;
			} else if (casas > 8 && !casasAtualizadas) {
				casas += 1;
				casasAtualizadas = true;
			}
			if (x == (34 - casas)) {
				ipFormatado.append(" | ");
			}
		}
		ipDecimalBinario[0] = ipFormatado.toString();
		ipDecimalBinario[1] = binToDec(ipFormatado.toString());
		return ipDecimalBinario;
	}
}
