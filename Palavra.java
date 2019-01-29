import java.util.ArrayList;
public class Palavra
{
    private String nome;
    private int erros;
    private boolean decorada;
    public Palavra(String nome){
        setNome(nome);
        erros = 0;
        decorada = false;
    }
    public String getNome(){return nome;}
    public int getErros(){return erros;}
    public boolean getDecorada(){return decorada;}
    public void setNome(String nome){this.nome=nome;}
    public void ligarDecorada(){decorada=true;}
    public void incrementaErros(){erros++;}
    public void resetar(){
        erros = 0;
        decorada = false;
    }
}