
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Interface extends JFrame {

    private Jogo jogo;
    private JButton[][] botoes;
    private int dificuldadeAtual;

    private JLabel contadorTentativas;
    private JLabel contadorAcertos;
    private JLabel contadorFaltantes;

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

    private final String LOGO = "logoPokemon.png";
    private final String MAGIKARP = "128.png";
    private final String POKEBOLA = "pokebola.png";

    private Cursor cursorPokebola;

    private final List<String> arquivosFaltando = new ArrayList<>();

    private File pastaImagens;
    private boolean pastaProcurada = false;

    public Interface() {

        setTitle("Pokemon - Batalha Naval");

        setSize(600, 650);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        agua = carregarIcone("agua.png");

        magikarp = dividirImagem("magikarp1.png", 1);
        slowpoke = dividirImagem("Slowpoke2.png", 2);
        vaporeon = dividirImagem("Vaporeon3.png", 3);
        dragonair = dividirImagem("Dragonier4.png", 4);
        gyarados = dividirImagem("Gyrados5.png", 5);

        pokebola = carregarIcone("300 Sem Título_20261026081426.png");
        erro = carregarIcone("X.png");

        configurarCursorPokebola();
        mostrarMenuInicial();

        avisarArquivosFaltando();
    }

    private String normalizar(String texto) {

        return Normalizer.normalize(
                texto,
                Normalizer.Form.NFC
        ).toLowerCase();
    }

    private File descobrirPastaImagens() {

        if (pastaProcurada) {
            return pastaImagens;
        }

        pastaProcurada = true;

        String[] bases = {
            "src", ".", "..", "../src", "bin"
        };

        for (String base : bases) {

            File dirBase = new File(base);
            File[] filhos = dirBase.listFiles();

            if (filhos == null) {
                continue;
            }

            for (File filho : filhos) {

                if (filho.isDirectory()
                        && filho.getName().equalsIgnoreCase("imagens")) {

                    pastaImagens = filho;
                    return pastaImagens;
                }
            }
        }

        return null;
    }

    private File localizarArquivo(String nome) {

        File pasta = descobrirPastaImagens();

        if (pasta == null) {
            return null;
        }

        File[] arquivos = pasta.listFiles();

        if (arquivos == null) {
            return null;
        }

        String alvo = normalizar(nome);

        for (File arquivo : arquivos) {

            if (arquivo.isFile()
                    && normalizar(arquivo.getName()).equals(alvo)) {

                return arquivo;
            }
        }

        return null;
    }

    private BufferedImage carregarImagem(String nome) {

        try {

            File arquivo = localizarArquivo(nome);

            if (arquivo != null) {

                BufferedImage img = ImageIO.read(arquivo);

                if (img != null) {
                    return img;
                }
            }

            String[] caminhos = {
                "/imagens/" + nome,
                "/Imagens/" + nome
            };

            for (String caminho : caminhos) {

                try (InputStream in =
                        getClass().getResourceAsStream(caminho)) {

                    if (in != null) {

                        BufferedImage img = ImageIO.read(in);

                        if (img != null) {
                            return img;
                        }
                    }
                }
            }

        } catch (IOException e) {
            // Registra o arquivo como não encontrado.
        }

        if (!arquivosFaltando.contains(nome)) {
            arquivosFaltando.add(nome);
        }

        return null;
    }

    private ImageIcon carregarIcone(String nome) {

        BufferedImage img = carregarImagem(nome);

        if (img == null) {
            return null;
        }

        return new ImageIcon(img);
    }

    private void avisarArquivosFaltando() {

        if (arquivosFaltando.isEmpty()) {
            return;
        }

        StringBuilder mensagem = new StringBuilder();

        mensagem.append(
                "Não foi possível encontrar estas imagens:\n\n"
        );

        for (String nome : arquivosFaltando) {

            mensagem.append(" - ")
                    .append(nome)
                    .append("\n");
        }

        mensagem.append("\nPasta de execução:\n")
                .append(new File(".").getAbsolutePath());

        mensagem.append("\n\nPasta de imagens encontrada:\n")
                .append(pastaImagens != null
                        ? pastaImagens.getAbsolutePath()
                        : "(nenhuma)");

        JOptionPane.showMessageDialog(
                this,
                mensagem.toString(),
                "Imagens não encontradas",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void configurarCursorPokebola() {

        try {

            BufferedImage original = carregarImagem(POKEBOLA);

            if (original == null) {
                return;
            }

            Image imagem = original.getScaledInstance(
                    32, 32, Image.SCALE_SMOOTH
            );

            Toolkit toolkit = Toolkit.getDefaultToolkit();

            cursorPokebola = toolkit.createCustomCursor(
                    imagem,
                    new Point(4, 4),
                    "Cursor Pokebola"
            );

            setCursor(cursorPokebola);

        } catch (Exception e) {

            System.out.println(
                    "Não foi possível carregar o cursor."
            );
        }
    }

    private void mostrarMenuInicial() {

        PainelFundo fundo = new PainelFundo(AZUL_ESCURO);

        fundo.setLayout(new BorderLayout(10, 10));
        fundo.setBorder(new EmptyBorder(15, 20, 20, 20));

        JLabel logo = criarImagem(LOGO, 350, 230);
        JLabel magikarp = criarImagem(MAGIKARP, 180, 110);

        JLabel subtitulo = new JLabel(
                "Encontre todos os Pokemon!",
                SwingConstants.CENTER
        );

        subtitulo.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        subtitulo.setForeground(BRANCO);

        JPanel painelCentral = new JPanel();

        painelCentral.setOpaque(false);

        painelCentral.setLayout(
                new BoxLayout(
                        painelCentral,
                        BoxLayout.Y_AXIS
                )
        );

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

        botaoJogar.setPreferredSize(
                new Dimension(220, 50)
        );

        botaoJogar.setMaximumSize(
                new Dimension(220, 50)
        );

        botaoJogar.addActionListener(
                e -> mostrarDificuldade()
        );

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

    private void mostrarDificuldade() {

        PainelFundo fundo = new PainelFundo(AZUL_ESCURO);

        fundo.setLayout(new BorderLayout(15, 15));

        fundo.setBorder(
                new EmptyBorder(30, 25, 30, 25)
        );

        JLabel titulo = new JLabel(
                "ESCOLHA A DIFICULDADE",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titulo.setForeground(BRANCO);

        JPanel painelBotoes = new JPanel();

        painelBotoes.setOpaque(false);

        painelBotoes.setLayout(
                new BoxLayout(
                        painelBotoes,
                        BoxLayout.Y_AXIS
                )
        );

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

        JPanel linhaFacil = criarLinha(
                marcadorFacil,
                botaoFacil
        );

        JPanel linhaDificil = criarLinha(
                marcadorDificil,
                botaoDificil
        );

        configurarMarcadorAoPassarMouse(
                botaoFacil,
                marcadorFacil
        );

        configurarMarcadorAoPassarMouse(
                botaoDificil,
                marcadorDificil
        );

        painelBotoes.add(Box.createVerticalGlue());
        painelBotoes.add(linhaFacil);

        painelBotoes.add(
                Box.createVerticalStrut(25)
        );

        painelBotoes.add(linhaDificil);
        painelBotoes.add(Box.createVerticalGlue());

        JPanel painelInferior = new JPanel();

        painelInferior.setOpaque(false);
        painelInferior.add(botaoVoltar);

        botaoVoltar.addActionListener(
                e -> mostrarMenuInicial()
        );

        botaoFacil.addActionListener(
                e -> iniciarPartida(1)
        );

        botaoDificil.addActionListener(
                e -> iniciarPartida(2)
        );

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
                new GridLayout(
                        tamanhoTabuleiro,
                        tamanhoTabuleiro
                )
        );

        JPanel painelContadores = new JPanel(
                new GridLayout(1, 3, 5, 5)
        );

        painelContadores.setBackground(AZUL_ESCURO);

        contadorTentativas = criarContador();
        contadorAcertos = criarContador();
        contadorFaltantes = criarContador();

        painelContadores.add(contadorTentativas);
        painelContadores.add(contadorAcertos);
        painelContadores.add(contadorFaltantes);

        atualizarContadores();

        botoes = new JButton[tamanhoTabuleiro][tamanhoTabuleiro];

        for (int linha = 0; linha < tamanhoTabuleiro; linha++) {

            for (int coluna = 0; coluna < tamanhoTabuleiro; coluna++) {

                botoes[linha][coluna] = new BotaoAgua();

                botoes[linha][coluna].setHorizontalAlignment(
                        SwingConstants.CENTER
                );

                botoes[linha][coluna].setVerticalAlignment(
                        SwingConstants.CENTER
                );

                botoes[linha][coluna].setIconTextGap(0);

                botoes[linha][coluna].setOpaque(false);

                botoes[linha][coluna].setContentAreaFilled(false);

                botoes[linha][coluna].setBorderPainted(false);

                botoes[linha][coluna].setFocusPainted(false);

                botoes[linha][coluna].setMargin(
                        new Insets(0, 0, 0, 0)
                );

                tabuleiro.add(botoes[linha][coluna]);

                int linhaEscolhida = linha;
                int colunaEscolhida = coluna;

                botoes[linha][coluna].addActionListener(e -> {

                    String resultado = jogo.atirar(
                            linhaEscolhida,
                            colunaEscolhida
                    );

                    if (resultado.equals("Coordenadas inválidas!") ||
                            resultado.equals(
                                    "Você já atirou nessa posição!"
                            )) {

                        JOptionPane.showMessageDialog(
                                this,
                                resultado
                        );

                        return;
                    }

                    // Atualiza os contadores após cada tiro.
                    atualizarContadores();

                    int pokemon = jogo.getPokemonAt(
                            linhaEscolhida,
                            colunaEscolhida
                    );

                    BotaoAgua botao = (BotaoAgua)
                            botoes[linhaEscolhida][colunaEscolhida];

                    if (pokemon != 0) {

                        int segmento = jogo.getSegmentoAt(
                                linhaEscolhida,
                                colunaEscolhida
                        );

                        ImageIcon icone = obterImagemPokemon(
                                pokemon,
                                segmento
                        );

                        botao.revelar(icone);

                    } else {

                        botao.revelar(erro);
                    }

                    botoes[linhaEscolhida][colunaEscolhida]
                            .setDisabledIcon(
                                    botoes[linhaEscolhida][colunaEscolhida]
                                            .getIcon()
                            );

                    botoes[linhaEscolhida][colunaEscolhida]
                            .setEnabled(false);

                    boolean venceu = resultado.equals(
                            "Parabéns! Você venceu!"
                    );

                    boolean perdeu = resultado.equals(
                            "Fim de jogo! Você perdeu!"
                    );

                    if (venceu || perdeu) {

                        for (int l = 0; l < botoes.length; l++) {

                            for (int c = 0; c < botoes[l].length; c++) {

                                botoes[l][c].setEnabled(false);
                            }
                        }

                        mostrarResultado(
                                venceu,
                                () -> iniciarPartida(dificuldadeAtual),
                                () -> mostrarMenuInicial()
                        );
                    }

                    // Não há mais pop-up para acertos ou erros.
                });
            }
        }

        JPanel telaJogo = new JPanel(
                new BorderLayout(5, 8)
        );

        telaJogo.setBackground(AZUL_ESCURO);

        telaJogo.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );

        telaJogo.add(
                painelContadores,
                BorderLayout.NORTH
        );

        telaJogo.add(
                tabuleiro,
                BorderLayout.CENTER
        );

        setContentPane(telaJogo);

        reaplicarCursor();

        revalidate();
        repaint();
    }

    private JLabel criarContador() {

        JLabel contador = new JLabel(
                "",
                SwingConstants.CENTER
        );

        contador.setForeground(BRANCO);
        contador.setBackground(AZUL_ESCURO);
        contador.setOpaque(true);

        contador.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        return contador;
    }

    private void atualizarContadores() {

        contadorTentativas.setText(
                "Tentativas: " + jogo.getTirosRestantes()
        );

        contadorAcertos.setText(
                "Acertos: " + jogo.getAcertos()
        );

        contadorFaltantes.setText(
                "Faltam: " + jogo.getPartesFaltantes()
        );
    }

    private ImageIcon obterImagemPokemon(
            int pokemon,
            int segmento
    ) {

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

    private ImageIcon[] dividirImagem(
            String nome,
            int quantidade
    ) {

        ImageIcon[] partes = new ImageIcon[quantidade];

        BufferedImage original = carregarImagem(nome);

        if (original == null) {

            for (int i = 0; i < quantidade; i++) {

                partes[i] = new ImageIcon(
                        new BufferedImage(
                                60,
                                60,
                                BufferedImage.TYPE_INT_ARGB
                        )
                );
            }

            return partes;
        }

        int larguraParte = original.getWidth() / quantidade;

        int altura = original.getHeight();

        for (int i = 0; i < quantidade; i++) {

            BufferedImage recorte = original.getSubimage(
                    i * larguraParte,
                    0,
                    larguraParte,
                    altura
            );

            Image redimensionada = recorte.getScaledInstance(
                    60,
                    60,
                    Image.SCALE_SMOOTH
            );

            partes[i] = new ImageIcon(redimensionada);
        }

        return partes;
    }

    private ImageIcon redimensionarImagem(
            ImageIcon icone,
            int largura,
            int altura
    ) {

        Image imagemOriginal = icone.getImage();

        int larguraOriginal = icone.getIconWidth();
        int alturaOriginal = icone.getIconHeight();

        double escala = Math.min(
                (double) largura / larguraOriginal,
                (double) altura / alturaOriginal
        );

        int novaLargura = (int) (larguraOriginal * escala);
        int novaAltura = (int) (alturaOriginal * escala);

        Image imagemRedimensionada = imagemOriginal.getScaledInstance(
                novaLargura,
                novaAltura,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(imagemRedimensionada);
    }

    private JLabel criarImagem(
            String nome,
            int largura,
            int altura
    ) {

        BufferedImage original = carregarImagem(nome);

        if (original == null) {
            return new JLabel("Imagem não encontrada");
        }

        Image imagem = original.getScaledInstance(
                largura,
                altura,
                Image.SCALE_SMOOTH
        );

        return new JLabel(new ImageIcon(imagem));
    }

    private JLabel criarMarcador() {

        JLabel marcador = criarImagem(
                POKEBOLA,
                40,
                40
        );

        marcador.setPreferredSize(
                new Dimension(45, 45)
        );

        marcador.setVisible(false);

        return marcador;
    }

    private JPanel criarLinha(
            JLabel marcador,
            JButton botao
    ) {

        JPanel linha = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        5
                )
        );

        linha.setOpaque(false);

        linha.add(marcador);
        linha.add(botao);

        return linha;
    }

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

    private void estilizarBotao(JButton botao) {

        botao.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        botao.setBackground(VERMELHO);
        botao.setForeground(BRANCO);

        botao.setFocusPainted(false);

        botao.setBorder(
                new EmptyBorder(10, 15, 10, 15)
        );

        botao.setCursor(cursorPokebola);
    }

    private void limitarTamanho(
            JButton botao,
            int largura,
            int altura
    ) {

        Dimension tamanho = new Dimension(
                largura,
                altura
        );

        botao.setPreferredSize(tamanho);
        botao.setMaximumSize(tamanho);
        botao.setMinimumSize(tamanho);
    }

    private void reaplicarCursor() {

        if (cursorPokebola != null) {
            setCursor(cursorPokebola);
        }
    }

    public void mostrarResultado(
            boolean venceu,
            Runnable jogarNovamente,
            Runnable voltarInicio
    ) {

        JDialog dialog = new JDialog(this, true);

        dialog.setUndecorated(true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(this);

        if (cursorPokebola != null) {
            dialog.setCursor(cursorPokebola);
        }

        JPanel painel = new JPanel();

        painel.setBackground(AZUL_ESCURO);

        painel.setBorder(
                BorderFactory.createLineBorder(
                        venceu
                                ? new Color(80, 200, 120)
                                : VERMELHO,
                        4
                )
        );

        painel.setLayout(
                new BoxLayout(
                        painel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo = new JLabel(
                venceu ? "VOCÊ VENCEU!" : "GAME OVER",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 27)
        );

        titulo.setForeground(
                venceu
                        ? new Color(80, 220, 120)
                        : VERMELHO
        );

        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel imagemMagikarp = criarImagem(
                MAGIKARP,
                120,
                90
        );

        imagemMagikarp.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel mensagem = new JLabel(
                venceu
                        ? "Você encontrou todos os Pokemon!"
                        : "Suas tentativas acabaram!",
                SwingConstants.CENTER
        );

        mensagem.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        mensagem.setForeground(BRANCO);

        mensagem.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JButton botaoNovamente = new JButton(
                venceu
                        ? "JOGAR NOVAMENTE"
                        : "TENTAR NOVAMENTE"
        );

        JButton botaoInicio = new JButton(
                "VOLTAR AO INÍCIO"
        );

        estilizarBotao(botaoNovamente);
        estilizarBotao(botaoInicio);

        limitarTamanho(botaoNovamente, 260, 45);
        limitarTamanho(botaoInicio, 260, 45);

        botaoNovamente.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        botaoInicio.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

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

            setMargin(
                    new Insets(0, 0, 0, 0)
            );

            setIcon(null);

            setBorder(
                    BorderFactory.createLineBorder(
                            new Color(100, 200, 255, 180),
                            5
                    )
            );

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

            // Desenha a água como fundo.
            if (agua != null && agua.getIconWidth() > 0) {

                float opacidade = clicado ? 0.25f : 1.0f;

                g2.setComposite(
                        AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER,
                                opacidade
                        )
                );

                g2.drawImage(
                        agua.getImage(),
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );
            }

            // Desenha o Pokémon ou o X por cima.
            if (clicado && resultado != null
                    && resultado.getIconWidth() > 0) {

                g2.setComposite(
                        AlphaComposite.SrcOver
                );

                int larguraOriginal = resultado.getIconWidth();
                int alturaOriginal = resultado.getIconHeight();

                double escala = Math.min(
                        (double) getWidth() / larguraOriginal,
                        (double) getHeight() / alturaOriginal
                );

                int largura = (int) (larguraOriginal * escala);
                int altura = (int) (alturaOriginal * escala);

                int x = (getWidth() - largura) / 2;
                int y = (getHeight() - altura) / 2;

                g2.drawImage(
                        resultado.getImage(),
                        x,
                        y,
                        largura,
                        altura,
                        this
                );
            }

            g2.dispose();
        }
    }

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
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(corBase);

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            desenharNuvem(
                    g2,
                    getWidth() * 0.2f,
                    getHeight() * 0.25f,
                    230
            );

            desenharNuvem(
                    g2,
                    getWidth() * 0.8f,
                    getHeight() * 0.65f,
                    260
            );

            desenharNuvem(
                    g2,
                    getWidth() * 0.5f,
                    getHeight() * 0.9f,
                    220
            );

            g2.dispose();
        }

        private void desenharNuvem(
                Graphics2D g2,
                float x,
                float y,
                float raio
        ) {

            Point2D centro = new Point2D.Float(x, y);

            float[] limites = {
                0f,
                1f
            };

            Color[] cores = {
                new Color(70, 130, 210, 65),
                new Color(70, 130, 210, 0)
            };

            RadialGradientPaint gradiente =
                    new RadialGradientPaint(
                            centro,
                            raio,
                            limites,
                            cores
                    );

            g2.setPaint(gradiente);

            g2.fill(
                    new Ellipse2D.Float(
                            x - raio,
                            y - raio,
                            raio * 2,
                            raio * 2
                    )
            );
        }
    }
}
