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
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JSeparator;

public class FramePrincipal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private List<Integer> arrayComOsHosts = new ArrayList<Integer>();
	private List<Integer> arrayComOsHostsValidos= new ArrayList<Integer>();
	private JTextArea txtTela;
	private String mascaraPadraoBinario;
	private Integer somaHosts = 0;
	private StringBuilder sb;
	private String[] mascaraCustomizada;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FramePrincipal frame = new FramePrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FramePrincipal() {
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 355);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblInformeOIp = new JLabel("Informe o Ip");
		lblInformeOIp.setBounds(10, 14, 78, 14);
		contentPane.add(lblInformeOIp);

		textField = new JTextField();
		textField.setBounds(89, 11, 118, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnAdicionarRede = new JButton("Adicionar Rede");
		btnAdicionarRede.setBounds(10, 49, 364, 23);
		btnAdicionarRede.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				String ipDecimal = textField.getText();				
				String[] octetos = ipDecimal.split(Pattern.quote("."));
				for(String octeto : octetos){
					Integer valorDoOcteto  = Integer.valueOf(octeto);
					if(valorDoOcteto > 255){
						JOptionPane.showMessageDialog(null, "IP Inválido!");
						return;
					}
				}
				String qtdRedes = JOptionPane.showInputDialog("Informe a Quantidade de Redes");
				Integer qtdRedesInteger = 0;
				try {
					qtdRedesInteger = Integer.parseInt(qtdRedes);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Caracter inválido!");
					return;
				}
				for (int x = 0; x < qtdRedesInteger; x++) {
					try {
						arrayComOsHosts.add(
								Integer.parseInt(JOptionPane.showInputDialog("Quantidade de Hosts para esta Rede")));
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
				txtTela.append("Rede: " + sb.toString() + "\n");
				String classe = null;
				if (Integer.parseInt(octetos[0]) >= 1 && Integer.parseInt(octetos[0]) <= 127) {
					mascaraPadraoBinario = "11111111.00000000.00000000.00000000";
					txtTela.append("Mascara Padrão: " + mascaraPadraoBinario + "\n");
					if (somaHosts > Math.pow(2, 24)) {
						txtTela.append("Não é possivel colocar: " + somaHosts + " hosts nessa rede");
						return;
					}
					classe = "A";
				} else if (Integer.parseInt(octetos[0]) >= 128 && Integer.parseInt(octetos[0]) <= 191) {
					mascaraPadraoBinario = "11111111.11111111.00000000.00000000";
					txtTela.append("Mascara Padrão: " + mascaraPadraoBinario + "\n");
					if (somaHosts > Math.pow(2, 16)) {
						txtTela.append("Não é possivel colocar: " + somaHosts + " hosts nessa rede");
						return;
					}
					classe = "B";
				} else if (Integer.parseInt(octetos[0]) >= 192 && Integer.parseInt(octetos[0]) <= 223) {
					mascaraPadraoBinario = "11111111.11111111.11111111.00000000";
					txtTela.append("Mascara Padrão: " + mascaraPadraoBinario + "\n");
					if (somaHosts > Math.pow(2, 8)) {
						txtTela.append("Não é possivel colocar: " + somaHosts + " hosts nessa rede");
						return;
					}
					classe = "C";
				}
				txtTela.append("Total Hosts: " + somaHosts);
				txtTela.append("\n");
				int contadorRedes = 1;
				for (Integer valor : arrayComOsHostsValidos) {
					txtTela.append("Rede " + contadorRedes + ": " + valor + "\n");
					contadorRedes++;
				}
				txtTela.append("\n");

				iniciarCalculoDasRedes(classe);
			}
			
		});
		contentPane.add(btnAdicionarRede);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 83, 364, 189);
		contentPane.add(scrollPane);

		txtTela = new JTextArea();
		scrollPane.setViewportView(txtTela);
		
		JButton btnNewButton = new JButton("Reset");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparVariaveis();
			}
		});
		btnNewButton.setBounds(296, 282, 78, 23);
		contentPane.add(btnNewButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(156, 303, 1, 2);
		contentPane.add(separator);

		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { contentPane, textField, lblInformeOIp }));
	}

	private void limparVariaveis() {
		textField.setText("");
		txtTela.setText("");
		arrayComOsHosts = new ArrayList<Integer>();
		arrayComOsHostsValidos = new ArrayList<Integer>();
		mascaraPadraoBinario = new String();
		if(mascaraCustomizada != null){
			Arrays.fill(mascaraCustomizada, null);
		}
		somaHosts = 0;
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

	private void iniciarCalculoDasRedes(String classe) {
		String[] aux = sb.toString().split(Pattern.quote("."));
		Integer[] arrayBinarios;
		if (classe.equals("A")) {
			arrayBinarios = new Integer[24];
			int x = 0;
			int octeto = 1;
			while (x <= 23) {
				for (Character c : aux[octeto].toCharArray()) {
					arrayBinarios[x] = Integer.parseInt(c.toString());
					x++;
				}
				if (octeto < 3) {
					octeto++;
				}
			}
			Integer salto = saltoBinarioDecimalClasseA(arrayBinarios);
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
					arrayBinarios[auxSalto] = Integer.parseInt(c.toString());
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
				for (Integer bit : arrayBinarios) {
					bits.append(bit.toString());
					if (bits.length() == 8 || bits.length() == 17) {
						bits.append(".");
					}
				}

				String id = aux[0] + "." + bits.toString();
				String idFormatado[] = devolverIpFormatoTela(id, contador);
				txtTela.append("REDE COM " + String.valueOf(qtdHost) + "\n");
				txtTela.append("ID: " + idFormatado[1] + "\n");
				txtTela.append(idFormatado[0] + "\n");

				for (int xC = 23; contador >= 1; contador--) {
					arrayBinarios[xC] = 1;
					xC--;
					contadorAuxiliarParaSaida++;
				}

				bits = new StringBuilder();
				for (Integer bit : arrayBinarios) {
					bits.append(bit.toString());
					if (bits.length() == 8 || bits.length() == 17) {
						bits.append(".");
					}
				}
				String bc = aux[0] + "." + bits.toString();
				String bcFormatado[] = devolverIpFormatoTela(bc, contadorAuxiliarParaSaida);
				mascaraCustomizada = retornarMascara(contadorAuxiliarParaSaida);

				txtTela.append("BC: " + bcFormatado[1] + "\n");
				txtTela.append(bcFormatado[0] + "\n");
				txtTela.append("Máscara Customizada: " + mascaraCustomizada[1] + "\n");
				txtTela.append(mascaraCustomizada[0] + "\n\n");

				salto += qtdHost;
			}
		} else if (classe.equals("B")) {
			arrayBinarios = new Integer[16];
			int x = 0;
			int octeto = 2;
			while (x <= 15) {
				for (Character c : aux[octeto].toCharArray()) {
					arrayBinarios[x] = Integer.parseInt(c.toString());
					x++;
				}
				if (octeto < 3) {
					octeto++;
				}
			}
			Integer salto = saltoBinarioDecimalClasseB(arrayBinarios);
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
					arrayBinarios[auxSalto] = Integer.parseInt(c.toString());
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
				for (Integer bit : arrayBinarios) {
					bits.append(bit.toString());
					if (bits.length() == 8) {
						bits.append(".");
					}
				}

				String id = aux[0] + "." + aux[1] + "." + bits.toString();
				String idFormatado[] = devolverIpFormatoTela(id, contador);
				txtTela.append("REDE COM " + String.valueOf(qtdHost) + "\n");
				txtTela.append("ID: " + idFormatado[1] + "\n");
				txtTela.append(idFormatado[0] + "\n");

				for (int xC = 15; contador >= 1; contador--) {
					arrayBinarios[xC] = 1;
					xC--;
					contadorAuxiliarParaSaida++;
				}

				bits = new StringBuilder();
				for (Integer bit : arrayBinarios) {
					bits.append(bit.toString());
					if (bits.length() == 8) {
						bits.append(".");
					}
				}
				String bc = aux[0] + "." + aux[1] + "." + bits.toString();
				String bcFormatado[] = devolverIpFormatoTela(bc, contadorAuxiliarParaSaida);
				mascaraCustomizada = retornarMascara(contadorAuxiliarParaSaida);

				txtTela.append("BC: " + bcFormatado[1] + "\n");
				txtTela.append(bcFormatado[0] + "\n");
				txtTela.append("Máscara Customizada: " + mascaraCustomizada[1] + "\n");
				txtTela.append(mascaraCustomizada[0] + "\n\n");

				salto += qtdHost;

			}
		} else if (classe.equals("C")) {
			arrayBinarios = new Integer[8];
			int octeto = 3;
			int x = 0;
			while (x <= 7) {
				for (Character c : aux[octeto].toCharArray()) {
					arrayBinarios[x] = Integer.parseInt(c.toString());
					x++;
				}
				if (octeto < 3) {
					octeto++;
				}
			}
			
			Integer salto = saltoBinarioDecimalClasseC(arrayBinarios);
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
					arrayBinarios[auxSalto] = Integer.parseInt(c.toString());
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
				for (Integer bit : arrayBinarios) {
					bits.append(bit.toString());
					if (bits.length() == (8 - contador)) {
						bits.append(" | ");
					}
				}
				String id = aux[0] + "." + aux[1] + "." + aux[2] + "." + bits.toString();
				String idRedeDecimal = binarioParaDecimal(id);
				txtTela.append("REDE COM " + String.valueOf(qtdHost) + "\n");
				txtTela.append("ID: " + idRedeDecimal + "\n");
				txtTela.append(id + "\n");
				for (int xC = 7; contador >= 1; contador--) {
					arrayBinarios[xC] = 1;
					xC--;
					contadorAuxiliarParaSaida++;
				}

				bits = new StringBuilder();
				for (Integer bit : arrayBinarios) {
					bits.append(bit.toString());
					if (bits.length() == (8 - contadorAuxiliarParaSaida)) {
						bits.append(" | ");
					}
				}
				String bc = aux[0] + "." + aux[1] + "." + aux[2] + "." + bits.toString();
				String bcRedeDecimal = binarioParaDecimal(bc);
				mascaraCustomizada = retornarMascara(contadorAuxiliarParaSaida);

				txtTela.append("BC: " + bcRedeDecimal + "\n");
				txtTela.append(bc + "\n");
				txtTela.append("Máscara Customizada: " + mascaraCustomizada[1] + "\n");
				txtTela.append(mascaraCustomizada[0] + "\n\n");

				salto += qtdHost;
			}
		}
	}
	
	private Integer saltoBinarioDecimalClasseC(Integer[] array){
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
	
	private Integer saltoBinarioDecimalClasseB(Integer[] array){
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
	
	private Integer saltoBinarioDecimalClasseA(Integer[] array){
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

	private String binarioParaDecimal(String id) {
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

	private String[] retornarMascara(int casas) {
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
		mascaraDecimalBinario[1] = binarioParaDecimal(mascara.toString());
		return mascaraDecimalBinario;
	}

	private String[] devolverIpFormatoTela(String ip, int casas) {
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
		ipDecimalBinario[1] = binarioParaDecimal(ipFormatado.toString());
		return ipDecimalBinario;
	}
}
