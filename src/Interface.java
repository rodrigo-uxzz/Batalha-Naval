
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Interface extends JFrame {

    // Cores principais do menu
    private final Color AZUL_ESCURO = new Color(23, 43, 77);
    private final Color VERMELHO = new Color(230, 57, 70);
    private final Color BRANCO = Color.WHITE;

    // Componentes da tela de dificuldade
    private JLabel marcadorFacil;
    private JLabel marcadorDificil;
    private JButton botaoFacil;
    private JButton botaoDificil;

    // Construtor: cria a janela inicial
    public Interface() {
        setTitle("Pokemon - Batalha Naval");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(AZUL_ESCURO);

        // Logo do Pokémon
        ImageIcon logo = new ImageIcon("src/imagens/logoPokemon.png");
        JLabel titulo = new JLabel(logo, SwingConstants.CENTER);

        // Texto de apresentação
        JLabel subtitulo = new JLabel(
            "Encontre todos os Pokemon!",
            SwingConstants.CENTER
        );
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(BRANCO);

        // Botão JOGAR
        JButton botaoJogar = new JButton("JOGAR");
        estilizarBotao(botaoJogar);
        botaoJogar.addActionListener(e -> mostrarDificuldade());

        // Painel central
        JPanel painelCentral = new JPanel(
            new GridLayout(2, 1, 5, 5)
        );
        painelCentral.setBackground(AZUL_ESCURO);
        painelCentral.add(titulo);
        painelCentral.add(subtitulo);

        // Painel inferior
        JPanel painelInferior = new JPanel();
        painelInferior.setBackground(AZUL_ESCURO);
        painelInferior.add(botaoJogar);

        add(painelCentral, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);
    }

    // Mostra a tela de escolha de dificuldade
    private void mostrarDificuldade() {
        getContentPane().removeAll();

        JLabel titulo = new JLabel(
            "ESCOLHA A DIFICULDADE",
            SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(BRANCO);

        botaoFacil = new JButton("FÁCIL - 6x6");
        botaoDificil = new JButton("DIFÍCIL - 8x8");

        estilizarBotao(botaoFacil);
        estilizarBotao(botaoDificil);

        // Marcadores com a imagem da Pokébola
        marcadorFacil = criarMarcador();
        marcadorDificil = criarMarcador();

        JPanel linhaFacil = criarLinha(
            marcadorFacil, botaoFacil
        );
        JPanel linhaDificil = criarLinha(
            marcadorDificil, botaoDificil
        );

        JPanel painelCentral = new JPanel(
            new GridLayout(3, 1, 10, 12)
        );
        painelCentral.setBackground(AZUL_ESCURO);
        painelCentral.setBorder(
            BorderFactory.createEmptyBorder(25, 55, 25, 55)
        );

        painelCentral.add(titulo);
        painelCentral.add(linhaFacil);
        painelCentral.add(linhaDificil);

        configurarMarcadorAoPassarMouse(
            botaoFacil, marcadorFacil
        );
        configurarMarcadorAoPassarMouse(
            botaoDificil, marcadorDificil
        );

        add(painelCentral, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    // Cria o marcador usando a imagem da Pokébola
    private JLabel criarMarcador() {
        ImageIcon pokebola = new ImageIcon(
            "src/imagens/pokebola.png"
        );

        JLabel marcador = new JLabel(pokebola);
        marcador.setHorizontalAlignment(SwingConstants.CENTER);
        marcador.setPreferredSize(new Dimension(40, 40));
        marcador.setVisible(false);

        return marcador;
    }

    // Organiza o espaço reservado e o botão
    private JPanel criarLinha(JLabel marcador, JButton botao) {
        JPanel linha = new JPanel(
            new BorderLayout(8, 0)
        );
        linha.setBackground(AZUL_ESCURO);

        marcador.setPreferredSize(new Dimension(40, 40));

        linha.add(marcador, BorderLayout.WEST);
        linha.add(botao, BorderLayout.CENTER);

        return linha;
    }

    // Mostra a Pokébola ao passar o mouse
    private void configurarMarcadorAoPassarMouse(
        JButton botao,
        JLabel marcador
    ) {
        botao.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                marcador.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                marcador.setVisible(false);
            }
        });
    }

    // Define o estilo dos botões
    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setPreferredSize(new Dimension(200, 40));
        botao.setBackground(VERMELHO);
        botao.setForeground(BRANCO);
        botao.setFocusPainted(false);
    }
}
