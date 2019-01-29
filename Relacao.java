public class Relacao
{
    private Palavra hangul;
    private Palavra portugues;
    private Descricao descricao;
    public Relacao(Palavra h, Palavra p, String mensagem, int tipo){
        hangul=h;
        portugues=p;
        descricao=new Descricao(mensagem, tipo);
    }
    Palavra getHangul(){return hangul;}
    Palavra getPortugues(){return portugues;}
    Descricao getDescricao(){return descricao;}
}
