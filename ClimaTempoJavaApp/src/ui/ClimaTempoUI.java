package ui;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

import api.ClimaTempoAPI;

public class ClimaTempoUI extends JFrame {
	
	private JSONObject dadosDoClima;
		
	public ClimaTempoUI() {
		super("ClimaTempo");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(450, 650);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		
		adicionarComponentes();
		
	}
	
	private void adicionarComponentes() {
		JTextField campoDePesquisa = new JTextField();
		campoDePesquisa.setBounds(15, 15, 351, 45);
		campoDePesquisa.setFont(new Font("Dialog", Font.PLAIN, 24));
		add(campoDePesquisa);
		
		JLabel ImagemCondicaoDoClima = new JLabel(carregarImagem("C:\\projetos java\\ClimaTempoJavaApp\\src\\assets\\cloudy.png"));
		ImagemCondicaoDoClima.setBounds(0, 125, 450, 217);
		add(ImagemCondicaoDoClima);
		
		JLabel textoTemperatura = new JLabel("10 ºC");
		textoTemperatura.setBounds(0, 350, 450, 54);
		textoTemperatura.setFont(new Font("Dialog", Font.BOLD, 48));
		textoTemperatura.setHorizontalAlignment(SwingConstants.CENTER);
		add(textoTemperatura);
		
		JLabel textoCondicaoDoClima = new JLabel("Nublado");
		textoCondicaoDoClima.setBounds(0, 405, 450, 36);
		textoCondicaoDoClima.setFont(new Font("Dialog", Font.PLAIN, 36));
		textoCondicaoDoClima.setHorizontalAlignment(SwingConstants.CENTER);
		add(textoCondicaoDoClima);
		
		JLabel imagemHumidade = new JLabel(carregarImagem("C:\\projetos java\\ClimaTempoJavaApp\\src\\assets\\humidity.png"));
		imagemHumidade.setBounds(15, 500, 74, 66);
		add(imagemHumidade);
		
		JLabel textoHumidade = new JLabel("<html><b>Humidade</b> 100%</html>");
		textoHumidade.setBounds(90, 500, 85, 55);
		textoHumidade.setFont(new Font("Dialog", Font.PLAIN, 16));
		add(textoHumidade);
		
		JLabel imagemVelocidadeVento = new JLabel(carregarImagem("C:\\projetos java\\ClimaTempoJavaApp\\src\\assets\\windspeed.png"));
		imagemVelocidadeVento.setBounds(220, 500, 74, 66);
		add(imagemVelocidadeVento);
		
		JLabel textoVelocidadeVento = new JLabel("<html><b>Vento</b> <r>15km/h</html>");
		textoVelocidadeVento.setBounds(310, 500, 85, 55);
		textoVelocidadeVento.setFont(new Font("Dialog", Font.PLAIN, 16));
		add(textoVelocidadeVento);
		
		JButton botaoDePesquisa = new JButton(carregarImagem("C:\\projetos java\\ClimaTempoJavaApp\\src\\assets\\search.png"));
		botaoDePesquisa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botaoDePesquisa.setBounds(375, 13, 47, 45);
		botaoDePesquisa.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String entradaDoUsuario = campoDePesquisa.getText();
				
				if(entradaDoUsuario.replaceAll("\\s", "").length() <=0) {
					return;
				}
				
				dadosDoClima = ClimaTempoAPI.getCondicaoClima(entradaDoUsuario);
				
				String condicaoClima = (String) dadosDoClima.get("condicaoClima");
				
				 switch(condicaoClima){
                 case "Limpo":
                	 ImagemCondicaoDoClima.setIcon(carregarImagem("C:\\projetos java\\ClimaTempoJavaApp\\src\\assets\\clear.png"));
                     break;
                 case "Nublado":
                	 ImagemCondicaoDoClima.setIcon(carregarImagem("C:\\projetos java\\ClimaTempoJavaApp\\src\\assets\\cloudy.png"));
                     break;
                 case "Chuva":
                	 ImagemCondicaoDoClima.setIcon(carregarImagem("C:\\projetos java\\ClimaTempoJavaApp\\src\\assets\\rain.png"));
                     break;
                 case "Neve":
                	 ImagemCondicaoDoClima.setIcon(carregarImagem("C:\\projetos java\\ClimaTempoJavaApp\\src\\assets\\snow.png"));
                     break;
             }
				 
				 double temperatura = (double) dadosDoClima.get("temperatura");
				 textoTemperatura.setText(temperatura + "C");
				 
				 textoCondicaoDoClima.setText(condicaoClima);
				 
				 long humidade = (long) dadosDoClima.get("humidade");
				 textoHumidade.setText("<html><b>Humidade</b> " + humidade + "%</html>");
				 
				 double velocidadeDoVento = (double) dadosDoClima.get("velocidadeDoVento");
				 textoVelocidadeVento.setText("<html><b>Vento</b>\n" + velocidadeDoVento + "<r>km/h</html>");
				 
			}
		});
		
		add(botaoDePesquisa);
	}
	
	private ImageIcon carregarImagem (String caminhoComponente) {
		try {
			BufferedImage imagem = ImageIO.read(new File(caminhoComponente));
			return new ImageIcon(imagem);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Não foi possível encontrar o componente");
		return null;
	}
}
