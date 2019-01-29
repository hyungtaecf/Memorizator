public class Descricao
{
    private String mensagem;
    private int tipo;
    public Descricao(String m, int t){
        mensagem = m;
        tipo = t;
    }
    String getMensagem(){return mensagem;}
    int getTipo(){return tipo;}
}
