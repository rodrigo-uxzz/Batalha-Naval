
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Interface extends JFrame {

    // ==========================================
    // 1. CORES DO JOGO
    // ==========================================

    private final Color AZUL_ESCURO = new Color(23, 43, 77);
    private final Color VERMELHO = new Color(230, 57, 70);
    private final Color BRANCO = Color.WHITE;

    // ==========================================
    // 2. COMPONENTES DA TELA DE DIFICULDADE
    // ==========================================

    private JLabel marcadorFacil;
    private JLabel marcadorDificil;

    private JButton botaoFacil;
    private JButton botaoDificil;

    // ==========================================
    // 3. CONSTRUTOR DA JANELA
    // ==========================================

    public Interface() {

        setTitle("Pokemon - Batalha Naval");
        setSize(500, 400);
        setMinimumSize(new Dimension(400, 350));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cria o painel de fundo com efeito esfumaçado.
        PainelFundo fundo = new PainelFundo(AZUL_ESCURO);
        setContentPane(fundo);

        // Os componentes serão organizados nas regiões da janela.
        fundo.setLayout(new BorderLayout(10, 10));

        // ==========================================
        // 4. LOGO DO POKÉMON
        // ==========================================

        // Tamanho da logo: altere os números para ajustar.
        int larguraLogo = 600;
        int alturaLogo = 400;

        ImageIcon imagemOriginal = new ImageIcon(
            "src/imagens/logoPokemon.png"
        );

        Image imagemRedimensionada = imagemOriginal.getImage()
            .getScaledInstance(
                larguraLogo,
                alturaLogo,
                Image.SCALE_SMOOTH
            );

        JLabel titulo = new JLabel(
            new ImageIcon(imagemRedimensionada),
            SwingConstants.CENTER
        );

        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ==========================================
        // 5. MAGIKARP
        // ==========================================

        // Você pode alterar a largura e a altura aqui.
        int larguraMagikarp = 250;
        int alturaMagikarp = 150;

        // ATENÇÃO: o nome usado aqui é 128.png.
        // Se seu arquivo for 129.png, troque o caminho.
        ImageIcon imagemOriginalMagikarp = new ImageIcon(
            "src/imagens/128.png"
        );

        Image imagemMagikarp = imagemOriginalMagikarp.getImage()
            .getScaledInstance(
                larguraMagikarp,
                alturaMagikarp,
                Image.SCALE_SMOOTH
            );

        JLabel magikarp = new JLabel(
            new ImageIcon(imagemMagikarp),
            SwingConstants.CENTER
        );

        magikarp.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ==========================================
        // 6. SUBTÍTULO
        // ==========================================

        JLabel subtitulo = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Batalha Naval inspirado em Pokémon!<br>"
            + "<span style='font-size:18px;'>"
            + "Encontre os Pokémon escondidos e conquiste a vitória!"
            + "</span></div></html>",
            SwingConstants.CENTER
        );

        subtitulo.setFont(
            new Font("Arial", Font.BOLD, 18)
        );

        subtitulo.setForeground(BRANCO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ==========================================
        // 7. PAINEL CENTRAL DO MENU
        // ==========================================

        // BoxLayout organiza os elementos verticalmente,
        // sem esticar todos para o mesmo tamanho.
        JPanel painelCentral = new JPanel();

        painelCentral.setLayout(
            new BoxLayout(painelCentral, BoxLayout.Y_AXIS)
        );

        // Deixa o fundo transparente para mostrar a fumaça.
        painelCentral.setOpaque(false);

        // Espaço acima da logo.
        painelCentral.add(Box.createVerticalGlue());

        painelCentral.add(titulo);
        painelCentral.add(Box.createVerticalStrut(5));

        painelCentral.add(magikarp);
        painelCentral.add(Box.createVerticalStrut(5));

        painelCentral.add(subtitulo);

        // Espaço abaixo do subtítulo.
        painelCentral.add(Box.createVerticalGlue());

        // ==========================================
        // 8. BOTÃO JOGAR
        // ==========================================

        JButton botaoJogar = new JButton("JOGAR");

        estilizarBotao(botaoJogar);

        botaoJogar.setPreferredSize(new Dimension(160, 38));
        botaoJogar.setMaximumSize(new Dimension(160, 38));

        botaoJogar.addActionListener(
            e -> mostrarDificuldade()
        );

        JPanel painelInferior = new JPanel();

        painelInferior.setOpaque(false);
        painelInferior.add(botaoJogar);

        // Adiciona os painéis à janela.
        fundo.add(painelCentral, BorderLayout.CENTER);
        fundo.add(painelInferior, BorderLayout.SOUTH);
    }

    // ==========================================
    // 9. TELA DE ESCOLHA DE DIFICULDADE
    // ==========================================

    private void mostrarDificuldade() {

        // Remove os componentes do menu anterior.
        // O painel de fundo continua existindo.
        getContentPane().removeAll();

        JLabel titulo = new JLabel(
            "ESCOLHA A DIFICULDADE",
            SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(BRANCO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botões de dificuldade.
        botaoFacil = new JButton("FÁCIL - 6x6");
        botaoDificil = new JButton("DIFÍCIL - 8x8");

        estilizarBotao(botaoFacil);
        estilizarBotao(botaoDificil);

        // Dimensões fixas para os botões não crescerem
        // excessivamente quando a janela for maximizada.
        Dimension tamanhoBotao = new Dimension(300, 100);

        botaoFacil.setPreferredSize(tamanhoBotao);
        botaoFacil.setMinimumSize(tamanhoBotao);
        botaoFacil.setMaximumSize(tamanhoBotao);

        botaoDificil.setPreferredSize(tamanhoBotao);
        botaoDificil.setMinimumSize(tamanhoBotao);
        botaoDificil.setMaximumSize(tamanhoBotao);

        // Cria os marcadores com Pokébolas maiores.
        marcadorFacil = criarMarcador();
        marcadorDificil = criarMarcador();

        JPanel linhaFacil = criarLinha(
            marcadorFacil, botaoFacil
        );

        JPanel linhaDificil = criarLinha(
            marcadorDificil, botaoDificil
        );

        // Organiza a tela verticalmente, sem esticar
        // as linhas para ocupar a tela inteira.
        JPanel painelCentral = new JPanel();

        painelCentral.setLayout(
            new BoxLayout(painelCentral, BoxLayout.Y_AXIS)
        );

        painelCentral.setOpaque(false);

        painelCentral.setBorder(
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        painelCentral.add(Box.createVerticalGlue());
        painelCentral.add(titulo);
        painelCentral.add(Box.createVerticalStrut(35));

        painelCentral.add(linhaFacil);
        painelCentral.add(Box.createVerticalStrut(22));

        painelCentral.add(linhaDificil);
        painelCentral.add(Box.createVerticalGlue());

        configurarMarcadorAoPassarMouse(
            botaoFacil, marcadorFacil
        );

        configurarMarcadorAoPassarMouse(
            botaoDificil, marcadorDificil
        );

        getContentPane().add(
            painelCentral,
            BorderLayout.CENTER
        );

        // Atualiza a aparência da janela.
        revalidate();
        repaint();
    }

    // ==========================================
    // 10. CRIAR A POKÉBOLA
    // ==========================================

    private JLabel criarMarcador() {

        // Tamanho da Pokébola.
        // Aumente ou diminua estes dois números.
        int larguraPokebola = 45;
        int alturaPokebola = 45;

        ImageIcon imagemOriginal = new ImageIcon(
            "src/imagens/pokebola.png"
        );

        Image imagemRedimensionada = imagemOriginal.getImage()
            .getScaledInstance(
                larguraPokebola,
                alturaPokebola,
                Image.SCALE_SMOOTH
            );

        ImageIcon pokebola = new ImageIcon(
            imagemRedimensionada
        );

        JLabel marcador = new JLabel(
            pokebola,
            SwingConstants.CENTER
        );

        // Reserva espaço para a imagem mesmo quando escondida.
        marcador.setPreferredSize(new Dimension(50, 50));
        marcador.setMinimumSize(new Dimension(50, 50));
        marcador.setMaximumSize(new Dimension(50, 50));

        // Começa invisível.
        marcador.setVisible(false);

        return marcador;
    }

    // ==========================================
    // 11. ORGANIZAR CADA LINHA DE DIFICULDADE
    // ==========================================

    private JPanel criarLinha(
        JLabel marcador,
        JButton botao
    ) {

        // FlowLayout mantém a Pokébola e o botão
        // agrupados no centro da tela.
        JPanel linha = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 12, 0)
        );

        linha.setOpaque(false);

        linha.add(marcador);
        linha.add(botao);

        linha.setAlignmentX(Component.CENTER_ALIGNMENT);

        return linha;
    }

    // ==========================================
    // 12. EFEITO AO PASSAR O MOUSE
    // ==========================================

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

    // ==========================================
    // 13. ESTILO DOS BOTÕES
    // ==========================================

    private void estilizarBotao(JButton botao) {

        botao.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        botao.setBackground(VERMELHO);
        botao.setForeground(BRANCO);

        botao.setFocusPainted(false);
        botao.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        // Remove a borda padrão para um visual mais limpo.
        botao.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }
}


// 14. PAINEL DE FUNDO COM EFEITO ESFUMAÇADO

// Esta classe desenha manchas suaves e translúcidas
// em tons de azul, criando uma aparência de fumaça.
class PainelFundo extends JPanel {

    private final Color corBase;

    public PainelFundo(Color corBase) {
        this.corBase = corBase;
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        // Melhora a qualidade dos desenhos.
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY
        );

        int largura = getWidth();
        int altura = getHeight();

        // Pinta a cor principal do fundo.
        g2.setColor(corBase);
        g2.fillRect(0, 0, largura, altura);

        // Manchas azuladas com transparência.
        // As manchas se ajustam ao tamanho da janela.
        desenharNuvem(
            g2,
            largura * 0.20f,
            altura * 0.30f,
            Math.max(150, largura * 0.45f),
            new Color(90, 145, 205, 45)
        );

        desenharNuvem(
            g2,
            largura * 0.80f,
            altura * 0.65f,
            Math.max(180, largura * 0.50f),
            new Color(45, 105, 175, 55)
        );

        desenharNuvem(
            g2,
            largura * 0.50f,
            altura * 0.95f,
            Math.max(140, largura * 0.40f),
            new Color(100, 155, 210, 30)
        );

        g2.dispose();
    }

    // Desenha uma mancha circular com bordas suaves.
    private void desenharNuvem(
        Graphics2D g2,
        float centroX,
        float centroY,
        float raio,
        Color cor
    ) {

        RadialGradientPaint gradiente =
            new RadialGradientPaint(
                new Point2D.Float(centroX, centroY),
                raio,
                new float[] { 0.0f, 0.45f, 1.0f },
                new Color[] {
                    cor,
                    new Color(
                        cor.getRed(),
                        cor.getGreen(),
                        cor.getBlue(),
                        15
                    ),
                    new Color(
                        cor.getRed(),
                        cor.getGreen(),
                        cor.getBlue(),
                        0
                    )
                }
            );

        g2.setPaint(gradiente);

        g2.fill(
            new Ellipse2D.Float(
                centroX - raio,
                centroY - raio,
                raio * 2,
                raio * 2
            )
        );
    }
}
