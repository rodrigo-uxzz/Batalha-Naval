import java.util.Random;

public class Jogo { 

    private Random random = new Random();

    private int tamanhoTabuleiro;
    private int tirosRestantes;
    private int partesRestantes = 0;
    private int partesAfundadas = 0;

    private int[] tamanhosNavios;

    private int [][] tabuleiroJogador;
    private int [][] tabuleiroNavios;
    
    public void configurarJogo(int dificuldade){
        if(dificuldade == 1){
            tamanhoTabuleiro = 6;
            tirosRestantes = 30;
            tamanhosNavios = new int[]{
                1,1,1,1,
                2,2,
                3,
                4,
                5
            };
        }
        else if(dificuldade == 2){
            tamanhoTabuleiro = 8;
            tirosRestantes = 40;
            tamanhosNavios = new int[]{
                1,1,1,1,1,1,
                2,2,
                3,3,
                4,4,
                5
            };
        }

        tabuleiroJogador = new int[tamanhoTabuleiro][tamanhoTabuleiro];
        tabuleiroNavios = new int[tamanhoTabuleiro][tamanhoTabuleiro];

        for(int tamanhoNavio : tamanhosNavios){
            posicionarNavio(tamanhoNavio);
        }
    }

    private void posicionarNavio(int tamanhoNavio){

        boolean posicaoValida = false;

        while(!posicaoValida){

            int linha, coluna;
            linha = random.nextInt(tamanhoTabuleiro);
            coluna = random.nextInt(tamanhoTabuleiro);

            if(coluna + tamanhoNavio <= tamanhoTabuleiro){

                posicaoValida = true;

                for(int i = 0; i < tamanhoNavio; i++){

                    if(tabuleiroNavios[linha][coluna + i] != 0){
                        posicaoValida = false;
                        break;
                    }
                }

                if(posicaoValida){
                    for(int i = 0; i < tamanhoNavio; i++){
                        tabuleiroNavios[linha][coluna + i] = 1;
                        partesRestantes++;
                    }
                }
                
            }   
        } 
    }

    public String atirar (int linha, int coluna){
        String resultado;

        if(linha < 0 || linha >= tamanhoTabuleiro || coluna < 0 || coluna >= tamanhoTabuleiro){
            return "Coordenadas inválidas!";
        }

        if(tabuleiroJogador[linha][coluna] != 0){
            return "Você já atirou nessa posição!";
        }

        tirosRestantes--;

        if(tabuleiroNavios[linha][coluna] == 1){
            tabuleiroJogador[linha][coluna] = 1;
            partesAfundadas++;
            resultado = "Acertou!";
        } else {
            tabuleiroJogador[linha][coluna] = 2;
            resultado = "Errou!";
        }

        if(partesAfundadas == partesRestantes){
            return "Parabéns! Você venceu!";
        }

        if(tirosRestantes == 0){
            return "Fim de jogo! Você perdeu!";
        }

        if(resultado.equals("Acertou!")){
            return "Acertou!";
        } else {
            return "Errou!";
        }

    }
}  