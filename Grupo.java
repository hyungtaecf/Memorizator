import java.util.ArrayList;
public class Grupo
{
    private String nome;
    private ArrayList<Relacao> itens;//tem que fazer o esquema ainda
    public Grupo(String nome){
        setNome(nome);
    }
    public String getNome(){return nome;}
    public void setNome(String nome){this.nome=nome;}
}
