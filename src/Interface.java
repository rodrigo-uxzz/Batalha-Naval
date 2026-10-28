
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Interface extends JFrame {

    private Jogo jogo;
    private JButton[][] botoes;
    private int dificuldadeAtual;
    private ImageIcon agua;
    private ImageIcon[] magikarp;
    private ImageIcon[] slowpoke;
    private ImageIcon[] vaporeon;
    private ImageIcon[] dragonair;
    private ImageIcon[] gyarados;
    private ImageIcon pokebola;
    private ImageIcon erro;

    private final Color AZUL_ESCURO = new Color(23, 43, 77);
    private final Color VERMELHO = new Color(230, 57, 70);
    private final Color BRANCO = Color.WHITE;

    private final String LOGO = "src/imagens/logoPokemon.png";
    private final String MAGIKARP = "src/imagens/128.png";
    private final String POKEBOLA = "src/imagens/pokebola.png";

    private Cursor cursorPokebola;

    public Interface() {
        setTitle("Pokemon - Batalha Naval");
        setSize(600, 650);
        setMinimumSize(new Dimension(450, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarCursorPokebola();
        mostrarMenuInicial();
        agua = new ImageIcon("src/imagens/agua.png");
        magikarp = dividirImagem("src/imagens/magikarp1.png", 1);
        slowpoke = dividirImagem("src/imagens/Slowpoke2.png", 2);
        vaporeon = dividirImagem("src/imagens/Vaporeon3.png", 3);
        dragonair = dividirImagem("src/imagens/Dragonier4.png", 4);
        gyarados = dividirImagem("src/imagens/Gyrados5.png", 5);
        pokebola = new ImageIcon("src/imagens/300 Sem Título_20261026081426.png");
        erro = new ImageIcon("src/imagens/X.png");
    }

    // Configura a Pokebola como cursor personalizado
    private void configurarCursorPokebola() {
        try {
            ImageIcon icone = new ImageIcon(POKEBOLA);

            if (icone.getIconWidth() <= 0) {
                return;
            }

            Image imagem = icone.getImage().getScaledInstance(
                    32, 32, Image.SCALE_SMOOTH);

            Toolkit toolkit = Toolkit.getDefaultToolkit();

            cursorPokebola = toolkit.createCustomCursor(
                    imagem,
                    new Point(4, 4),
                    "Cursor Pokebola");

            setCursor(cursorPokebola);

        } catch (Exception e) {
            System.out.println("Não foi possível carregar o cursor.");
        }
    }

    // Mostra o menu inicial
    private void mostrarMenuInicial() {
        PainelFundo fundo = new PainelFundo(AZUL_ESCURO);
        fundo.setLayout(new BorderLayout(10, 10));
        fundo.setBorder(new EmptyBorder(15, 20, 20, 20));

        JLabel logo = criarImagem(LOGO, 350, 230);
        JLabel magikarp = criarImagem(MAGIKARP, 180, 110);

        JLabel subtitulo = new JLabel(
                "Encontre todos os Pokemon!",
                SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.BOLD, 20));
        subtitulo.setForeground(BRANCO);

        JPanel painelCentral = new JPanel();
        painelCentral.setOpaque(false);
        painelCentral.setLayout(
                new BoxLayout(painelCentral, BoxLayout.Y_AXIS));

        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        magikarp.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelCentral.add(Box.createVerticalGlue());
        painelCentral.add(logo);
        painelCentral.add(Box.createVerticalStrut(5));
        painelCentral.add(magikarp);
        painelCentral.add(Box.createVerticalStrut(10));
        painelCentral.add(subtitulo);
        painelCentral.add(Box.createVerticalGlue());

        JButton botaoJogar = new JButton("JOGAR");
        estilizarBotao(botaoJogar);
        botaoJogar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoJogar.setPreferredSize(new Dimension(220, 50));
        botaoJogar.setMaximumSize(new Dimension(220, 50));

        botaoJogar.addActionListener(e -> mostrarDificuldade());

        JPanel painelInferior = new JPanel();
        painelInferior.setOpaque(false);
        painelInferior.add(botaoJogar);

        fundo.add(painelCentral, BorderLayout.CENTER);
        fundo.add(painelInferior, BorderLayout.SOUTH);

        setContentPane(fundo);
        reaplicarCursor();
        revalidate();
        repaint();
    }

    // Mostra as opções de dificuldade
    private void mostrarDificuldade() {
        PainelFundo fundo = new PainelFundo(AZUL_ESCURO);
        fundo.setLayout(new BorderLayout(15, 15));
        fundo.setBorder(new EmptyBorder(30, 25, 30, 25));

        JLabel titulo = new JLabel(
                "ESCOLHA A DIFICULDADE",
                SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(BRANCO);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setLayout(
                new BoxLayout(painelBotoes, BoxLayout.Y_AXIS));

        JButton botaoFacil = new JButton("FÁCIL - 6x6");
        JButton botaoDificil = new JButton("DIFÍCIL - 8x8");
        JButton botaoVoltar = new JButton("VOLTAR");

        estilizarBotao(botaoFacil);
        estilizarBotao(botaoDificil);
        estilizarBotao(botaoVoltar);

        limitarTamanho(botaoFacil, 280, 65);
        limitarTamanho(botaoDificil, 280, 65);
        limitarTamanho(botaoVoltar, 180, 45);

        JLabel marcadorFacil = criarMarcador();
        JLabel marcadorDificil = criarMarcador();

        JPanel linhaFacil = criarLinha(marcadorFacil, botaoFacil);
        JPanel linhaDificil = criarLinha(marcadorDificil, botaoDificil);

        configurarMarcadorAoPassarMouse(
                botaoFacil, marcadorFacil);
        configurarMarcadorAoPassarMouse(
                botaoDificil, marcadorDificil);

        painelBotoes.add(Box.createVerticalGlue());
        painelBotoes.add(linhaFacil);
        painelBotoes.add(Box.createVerticalStrut(25));
        painelBotoes.add(linhaDificil);
        painelBotoes.add(Box.createVerticalGlue());

        JPanel painelInferior = new JPanel();
        painelInferior.setOpaque(false);
        painelInferior.add(botaoVoltar);

        botaoVoltar.addActionListener(e -> mostrarMenuInicial());

        // A conexão desses dois botões com o jogo será feita depois.
        botaoFacil.addActionListener(e -> iniciarPartida(1));

        botaoDificil.addActionListener(e -> iniciarPartida(2));

        fundo.add(titulo, BorderLayout.NORTH);
        fundo.add(painelBotoes, BorderLayout.CENTER);
        fundo.add(painelInferior, BorderLayout.SOUTH);

        setContentPane(fundo);
        reaplicarCursor();
        revalidate();
        repaint();
    }

    private void iniciarPartida(int dificuldade) {

        dificuldadeAtual = dificuldade;

        jogo = new Jogo();
        jogo.configurarJogo(dificuldade);

        int tamanhoTabuleiro = jogo.getTamanhoTabuleiro();

        JPanel tabuleiro = new JPanel(
                new GridLayout(tamanhoTabuleiro, tamanhoTabuleiro));

        botoes = new JButton[tamanhoTabuleiro][tamanhoTabuleiro];

        for (int linha = 0; linha < tamanhoTabuleiro; linha++) {
            for (int coluna = 0; coluna < tamanhoTabuleiro; coluna++) {

                botoes[linha][coluna] = new BotaoAgua();

                botoes[linha][coluna].setHorizontalAlignment(SwingConstants.CENTER);
                botoes[linha][coluna].setVerticalAlignment(SwingConstants.CENTER);
                botoes[linha][coluna].setIconTextGap(0);
                botoes[linha][coluna].setOpaque(false);
                botoes[linha][coluna].setContentAreaFilled(false);
                botoes[linha][coluna].setBorderPainted(false);
                botoes[linha][coluna].setFocusPainted(false);
                botoes[linha][coluna].setMargin(new Insets(0, 0, 0, 0));

                tabuleiro.add(botoes[linha][coluna]);

                int linhaEscolhida = linha;
                int colunaEscolhida = coluna;

                botoes[linha][coluna].addActionListener(e -> {

                    String resultado = jogo.atirar(
                            linhaEscolhida,
                            colunaEscolhida);

                    if (resultado.equals("Coordenadas inválidas!") ||
                            resultado.equals("Você já atirou nessa posição!")) {

                        JOptionPane.showMessageDialog(this, resultado);
                        return;
                    }

                    int pokemon = jogo.getPokemonAt(
                            linhaEscolhida,
                            colunaEscolhida);

                    BotaoAgua botao = (BotaoAgua) botoes[linhaEscolhida][colunaEscolhida];

                    if (pokemon != 0) {

                        int segmento = jogo.getSegmentoAt(
                                linhaEscolhida,
                                colunaEscolhida);

                        ImageIcon icone = obterImagemPokemon(pokemon, segmento);

                        botao.revelar(icone);

                    } else {

                        botao.revelar(erro);

                    }

                    botoes[linhaEscolhida][colunaEscolhida].setDisabledIcon(
                            botoes[linhaEscolhida][colunaEscolhida].getIcon());

                    botoes[linhaEscolhida][colunaEscolhida].setEnabled(false);

                    boolean venceu = resultado.equals(
                            "Parabéns! Você venceu!");

                    boolean perdeu = resultado.equals(
                            "Fim de jogo! Você perdeu!");

                    if (venceu || perdeu) {

                        for (int l = 0; l < botoes.length; l++) {
                            for (int c = 0; c < botoes[l].length; c++) {
                                botoes[l][c].setEnabled(false);
                            }
                        }

                        mostrarResultado(
                                venceu,
                                () -> iniciarPartida(dificuldadeAtual),
                                () -> mostrarMenuInicial());

                    } else {
                        JOptionPane.showMessageDialog(this, resultado);
                    }
                });
            }
        }

        setContentPane(tabuleiro);
        reaplicarCursor();
        revalidate();
        repaint();
    }

    private ImageIcon obterImagemPokemon(int pokemon, int segmento) {
        switch (pokemon) {
            case 1:
                return magikarp[segmento];
            case 2:
                return slowpoke[segmento];
            case 3:
                return vaporeon[segmento];
            case 4:
                return dragonair[segmento];
            case 5:
                return gyarados[segmento];
            default:
                return null;
        }
    }

    private ImageIcon[] dividirImagem(String caminho, int quantidade) {
        try {
            BufferedImage original = ImageIO.read(new File(caminho));

            if (original == null) {
                throw new IOException("Não foi possível ler: " + caminho);
            }

            int larguraParte = original.getWidth() / quantidade;
            int altura = original.getHeight();

            ImageIcon[] partes = new ImageIcon[quantidade];

            for (int i = 0; i < quantidade; i++) {
                BufferedImage recorte = original.getSubimage(
                        i * larguraParte,
                        0,
                        larguraParte,
                        altura);

                Image redimensionada = recorte.getScaledInstance(
                        60, 60, Image.SCALE_SMOOTH);

                partes[i] = new ImageIcon(redimensionada);
            }

            return partes;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao carregar: " + caminho, e);
        }
    }

    private ImageIcon redimensionarImagem(
            ImageIcon icone,
            int largura,
            int altura) {
        Image imagemOriginal = icone.getImage();

        int larguraOriginal = icone.getIconWidth();
        int alturaOriginal = icone.getIconHeight();

        double escala = Math.min(
                (double) largura / larguraOriginal,
                (double) altura / alturaOriginal);

        int novaLargura = (int) (larguraOriginal * escala);
        int novaAltura = (int) (alturaOriginal * escala);

        Image imagemRedimensionada = imagemOriginal.getScaledInstance(
                novaLargura,
                novaAltura,
                Image.SCALE_SMOOTH);

        return new ImageIcon(imagemRedimensionada);
    }

    // Cria uma imagem redimensionada
    private JLabel criarImagem(
            String caminho, int largura, int altura) {
        ImageIcon icone = new ImageIcon(caminho);

        if (icone.getIconWidth() <= 0) {
            return new JLabel("Imagem não encontrada");
        }

        Image imagem = icone.getImage().getScaledInstance(
                largura, altura, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(imagem));
    }

    // Cria o marcador de Pokebola
    private JLabel criarMarcador() {
        JLabel marcador = criarImagem(POKEBOLA, 40, 40);
        marcador.setPreferredSize(new Dimension(45, 45));
        marcador.setVisible(false);
        return marcador;
    }

    // Organiza o marcador ao lado do botão
    private JPanel criarLinha(JLabel marcador, JButton botao) {
        JPanel linha = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 10, 5));

        linha.setOpaque(false);
        linha.add(marcador);
        linha.add(botao);

        return linha;
    }

    // Exibe a Pokebola ao passar o mouse
    private void configurarMarcadorAoPassarMouse(
            JButton botao, JLabel marcador) {
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

    // Estiliza os botões
    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 15));
        botao.setBackground(VERMELHO);
        botao.setForeground(BRANCO);
        botao.setFocusPainted(false);
        botao.setBorder(new EmptyBorder(10, 15, 10, 15));
        botao.setCursor(cursorPokebola);
    }

    private void limitarTamanho(
            JButton botao, int largura, int altura) {
        Dimension tamanho = new Dimension(largura, altura);
        botao.setPreferredSize(tamanho);
        botao.setMaximumSize(tamanho);
        botao.setMinimumSize(tamanho);
    }

    // Reaplica o cursor após trocar de tela
    private void reaplicarCursor() {
        if (cursorPokebola != null) {
            setCursor(cursorPokebola);
        }
    }

    // Tela de vitória ou derrota
    public void mostrarResultado(
            boolean venceu,
            Runnable jogarNovamente,
            Runnable voltarInicio) {
        JDialog dialog = new JDialog(
                this, true);

        dialog.setUndecorated(true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(this);

        if (cursorPokebola != null) {
            dialog.setCursor(cursorPokebola);
        }

        JPanel painel = new JPanel();
        painel.setBackground(AZUL_ESCURO);
        painel.setBorder(BorderFactory.createLineBorder(
                venceu ? new Color(80, 200, 120) : VERMELHO,
                4));
        painel.setLayout(new BoxLayout(
                painel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel(
                venceu ? "VOCÊ VENCEU!" : "GAME OVER",
                SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 27));
        titulo.setForeground(
                venceu ? new Color(80, 220, 120) : VERMELHO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel imagemMagikarp = criarImagem(
                MAGIKARP, 120, 90);
        imagemMagikarp.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mensagem = new JLabel(
                venceu
                        ? "Você encontrou todos os Pokemon!"
                        : "Suas tentativas acabaram!",
                SwingConstants.CENTER);

        mensagem.setFont(new Font("Arial", Font.PLAIN, 14));
        mensagem.setForeground(BRANCO);
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton botaoNovamente = new JButton(
                venceu ? "JOGAR NOVAMENTE" : "TENTAR NOVAMENTE");
        JButton botaoInicio = new JButton("VOLTAR AO INÍCIO");

        estilizarBotao(botaoNovamente);
        estilizarBotao(botaoInicio);

        limitarTamanho(botaoNovamente, 260, 45);
        limitarTamanho(botaoInicio, 260, 45);

        botaoNovamente.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoInicio.setAlignmentX(Component.CENTER_ALIGNMENT);

        botaoNovamente.addActionListener(e -> {
            dialog.dispose();

            if (jogarNovamente != null) {
                jogarNovamente.run();
            }
        });

        botaoInicio.addActionListener(e -> {
            dialog.dispose();

            if (voltarInicio != null) {
                voltarInicio.run();
            } else {
                mostrarMenuInicial();
            }
        });

        painel.add(Box.createVerticalStrut(20));
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10));
        painel.add(imagemMagikarp);
        painel.add(Box.createVerticalStrut(8));
        painel.add(mensagem);
        painel.add(Box.createVerticalStrut(20));
        painel.add(botaoNovamente);
        painel.add(Box.createVerticalStrut(10));
        painel.add(botaoInicio);
        painel.add(Box.createVerticalStrut(15));

        dialog.setContentPane(painel);
        dialog.setVisible(true);
    }

    private class BotaoAgua extends JButton {

        private ImageIcon resultado;
        private boolean clicado = false;

        public BotaoAgua() {
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setMargin(new Insets(0, 0, 0, 0));
            setIcon(null);
            setBorder(BorderFactory.createLineBorder(
                    new Color(100, 200, 255, 180), 5));
            setBorderPainted(true);
        }

        public void revelar(ImageIcon imagem) {
            resultado = imagem;
            clicado = true;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            // Desenha a água como fundo
            if (agua != null && agua.getIconWidth() > 0) {
                float opacidade = clicado ? 0.25f : 1.0f;

                g2.setComposite(
                        AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER,
                                opacidade));

                g2.drawImage(
                        agua.getImage(),
                        0, 0,
                        getWidth(), getHeight(),
                        this);
            }

            // Desenha o Pokémon ou o X por cima
            if (clicado && resultado != null
                    && resultado.getIconWidth() > 0) {

                g2.setComposite(AlphaComposite.SrcOver);

                int larguraOriginal = resultado.getIconWidth();
                int alturaOriginal = resultado.getIconHeight();

                double escala = Math.min(
                        (double) getWidth() / larguraOriginal,
                        (double) getHeight() / alturaOriginal);

                int largura = (int) (larguraOriginal * escala);
                int altura = (int) (alturaOriginal * escala);

                int x = (getWidth() - largura) / 2;
                int y = (getHeight() - altura) / 2;

                g2.drawImage(
                        resultado.getImage(),
                        x, y,
                        largura, altura,
                        this);
            }

            g2.dispose();
        }
    }

    // Fundo com efeito de fumaça azul
    private class PainelFundo extends JPanel {

        private final Color corBase;

        public PainelFundo(Color corBase) {
            this.corBase = corBase;
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(corBase);
            g2.fillRect(0, 0, getWidth(), getHeight());

            desenharNuvem(
                    g2, getWidth() * 0.2f,
                    getHeight() * 0.25f, 230);

            desenharNuvem(
                    g2, getWidth() * 0.8f,
                    getHeight() * 0.65f, 260);

            desenharNuvem(
                    g2, getWidth() * 0.5f,
                    getHeight() * 0.9f, 220);

            g2.dispose();
        }

        private void desenharNuvem(
                Graphics2D g2, float x, float y, float raio) {
            Point2D centro = new Point2D.Float(x, y);

            float[] limites = { 0f, 1f };

            Color[] cores = {
                    new Color(70, 130, 210, 65),
                    new Color(70, 130, 210, 0)
            };

            RadialGradientPaint gradiente = new RadialGradientPaint(
                    centro, raio, limites, cores);

            g2.setPaint(gradiente);
            g2.fill(
                    new Ellipse2D.Float(
                            x - raio, y - raio,
                            raio * 2, raio * 2));
        }
    }
}
