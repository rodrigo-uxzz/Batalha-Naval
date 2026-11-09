
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Jogo {

    private final Random random = new Random();

    private int tamanhoTabuleiro;
    private int tirosRestantes;
    private int partesRestantes;
    private int partesAfundadas;

    private int[] tamanhosNavios;
    private int[][] tabuleiroJogador;
    private int[][] tabuleiroNavios;
    private int[][] tabuleiroPokemons;
    private int[][] segmentosPokemons;

    public void configurarJogo(int dificuldade) {

        if (dificuldade == 1) {
            tamanhoTabuleiro = 6;
            tirosRestantes = 30;

            tamanhosNavios = new int[]{
                1, 1, 1, 1,
                2, 2,
                3,
                4,
                5
            };

        } else if (dificuldade == 2) {
            tamanhoTabuleiro = 8;
            tirosRestantes = 40;

            tamanhosNavios = new int[]{
                1, 1, 1, 1, 1, 1,
                2, 2,
                3, 3,
                4, 4,
                5
            };

        } else {
            throw new IllegalArgumentException(
                "Dificuldade inválida."
            );
        }

        tabuleiroJogador =
            new int[tamanhoTabuleiro][tamanhoTabuleiro];

        tabuleiroNavios =
            new int[tamanhoTabuleiro][tamanhoTabuleiro];

        tabuleiroPokemons =
            new int[tamanhoTabuleiro][tamanhoTabuleiro];

        segmentosPokemons =
            new int[tamanhoTabuleiro][tamanhoTabuleiro];

        partesRestantes = 0;
        partesAfundadas = 0;

        if (!posicionarTodos(0)) {
            throw new IllegalStateException(
                "Não foi possível posicionar os Pokémon."
            );
        }
    }

    private boolean posicionarTodos(int indiceNavio) {

        if (indiceNavio == tamanhosNavios.length) {
            return true;
        }

        int tamanho = tamanhosNavios[indiceNavio];

        List<int[]> candidatos = new ArrayList<>();

        // Posicionamento somente horizontal.
        for (int linha = 0; linha < tamanhoTabuleiro; linha++) {

            for (
                int coluna = 0;
                coluna <= tamanhoTabuleiro - tamanho;
                coluna++
            ) {

                boolean livre = true;

                for (int parte = 0; parte < tamanho; parte++) {

                    if (tabuleiroNavios[linha][coluna + parte] != 0) {
                        livre = false;
                        break;
                    }
                }

                if (livre) {
                    candidatos.add(new int[]{linha, coluna});
                }
            }
        }

        Collections.shuffle(candidatos, random);

        for (int[] posicao : candidatos) {

            int linha = posicao[0];
            int coluna = posicao[1];

            for (int parte = 0; parte < tamanho; parte++) {

                tabuleiroNavios[linha][coluna + parte] = 1;

                tabuleiroPokemons[linha][coluna + parte] = tamanho;

                segmentosPokemons[linha][coluna + parte] = parte;
            }

            partesRestantes += tamanho;

            if (posicionarTodos(indiceNavio + 1)) {
                return true;
            }

            // Desfaz a posição caso seja necessário tentar outra.
            for (int parte = 0; parte < tamanho; parte++) {

                tabuleiroNavios[linha][coluna + parte] = 0;

                tabuleiroPokemons[linha][coluna + parte] = 0;

                segmentosPokemons[linha][coluna + parte] = 0;
            }

            partesRestantes -= tamanho;
        }

        return false;
    }

    public int getTamanhoTabuleiro() {
        return tamanhoTabuleiro;
    }

    public int getPokemonAt(int linha, int coluna) {
        return tabuleiroPokemons[linha][coluna];
    }

    public int getSegmentoAt(int linha, int coluna) {
        return segmentosPokemons[linha][coluna];
    }

    public int getTirosRestantes() {
        return tirosRestantes;
    }

    public int getAcertos() {
        return partesAfundadas;
    }

    public int getPartesFaltantes() {
        return partesRestantes - partesAfundadas;
    }

    public String atirar(int linha, int coluna) {

        if (
            linha < 0 || linha >= tamanhoTabuleiro ||
            coluna < 0 || coluna >= tamanhoTabuleiro
        ) {
            return "Coordenadas inválidas!";
        }

        // Impede disparos depois do fim das tentativas.
        if (tirosRestantes <= 0) {
            return "Fim de jogo! Você perdeu!";
        }

        if (tabuleiroJogador[linha][coluna] != 0) {
            return "Você já atirou nessa posição!";
        }

        tirosRestantes--;

        boolean acertou = tabuleiroNavios[linha][coluna] == 1;

        if (acertou) {
            tabuleiroJogador[linha][coluna] = 1;
            partesAfundadas++;
        } else {
            tabuleiroJogador[linha][coluna] = 2;
        }

        // Verifica a vitória.
        if (partesAfundadas == partesRestantes) {
            return "Parabéns! Você venceu!";
        }

        // Verifica a derrota.
        if (tirosRestantes == 0) {
            return "Fim de jogo! Você perdeu!";
        }

        return acertou ? "Acertou!" : "Errou!";
    }
}
