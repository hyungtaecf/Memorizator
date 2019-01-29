import java.util.ArrayList;
import java.util.Random;
import java.text.Normalizer;
import java.util.Locale;
import java.text.Collator;
import java.util.Collections;
import java.lang.Comparable;
public class Dicionario
{
    private ArrayList<Palavra> listaHangul;
    private ArrayList<Palavra> listaPortugues;
    private ArrayList<Relacao> relacoes;
    private ArrayList<Palavra> ultimasPalavras;
    private int palavras;
    Collator collator = Collator.getInstance(new Locale("pt","BR"));
    public Dicionario(){
        listaHangul = new ArrayList<Palavra>();
        listaPortugues = new ArrayList<Palavra>();
        relacoes = new ArrayList<Relacao>();
        ultimasPalavras = new ArrayList<Palavra>();
        palavras = 0;
        collator.setStrength(Collator.PRIMARY);
    }
    public int getPalavras(){return palavras;}
    public ArrayList<Palavra> getListaHangul(){return listaHangul;}
    public ArrayList<Palavra> getListaPortugues(){return listaPortugues;}
    public ArrayList<Relacao> getRelacoes(){return relacoes;}
    public ArrayList<Palavra> getUltimasPalavras(){return ultimasPalavras;}
    public void setPalavras(int valor){
        if(valor>listaHangul.size()||valor<0)palavras = listaHangul.size();
        else palavras = valor;
    }
    public boolean importarPalavra(String h, String p, String mensagem, int tipo){
        Palavra hangul = buscaPalavraHangul(h);
        Palavra portugues = buscaPalavraPortugues(p);
        if(hangul==null)listaHangul.add(hangul = new Palavra(h));
        if(portugues==null)listaPortugues.add(portugues = new Palavra(p));
        if(buscaRelacao(hangul,portugues)==null){
            relacoes.add(new Relacao(hangul,portugues, mensagem, tipo));
            return true;
        }
        return false;
    }
    public boolean excluirPalavraHalgul(String h){
        Palavra hangul = buscaPalavraHangul(h);
        if(hangul!=null){
            ArrayList<Palavra> traducoes = buscaTraducoesPortugues(h);
            for(Palavra portugues: traducoes)relacoes.remove(buscaRelacao(hangul,portugues));
            listaHangul.remove(hangul);
            palavras--;
            return true;
        }
        return false;
    }
    public void atualizaUltimasPalavras(Palavra p){
        if(palavras>10/*11*/&&ultimasPalavras.size()>9)ultimasPalavras.remove(0);
        else if(palavras==2)
            if(ultimasPalavras.size()>=2)for(int c=0;c<2;c++)ultimasPalavras.remove(0);
            else ultimasPalavras.remove(0);
        else if(palavras==3)
            if(ultimasPalavras.size()>=2)for(int c=0;c<2;c++)ultimasPalavras.remove(0);
            else ultimasPalavras.remove(0);
        else if(ultimasPalavras.size()>2&&ultimasPalavras.size()>palavras-2)for(int c=0;c<2;c++)ultimasPalavras.remove(0);
        //else if()ultimasPalavras.remove(0);
        if(palavras!=1)ultimasPalavras.add(p);
    }
    public Palavra buscaPalavraHangulAleatoria(){
        Random gerador = new Random();
        int numero;
        do{numero = gerador.nextInt(listaHangul.size());}while(listaHangul.get(numero).getDecorada());
        return listaHangul.get(numero);
    }
    public Palavra buscaPalavraHangul(String palavra){
        for(Palavra p: listaHangul)if(collator.compare(palavra, p.getNome())==0/*palavra.equals(p.getNome())*/)return p;
        return null;
    }
    public Palavra buscaPalavraPortugues(String palavra){
        palavra = retiraAcentos(palavra);
        for(Palavra p: listaPortugues)if(collator.compare(palavra, retiraAcentos(p.getNome()))==0/*palavra.equals(p.getNome())*/)return p;
        return null;
    }
    public Relacao buscaRelacao(Palavra h, Palavra p){
        for(Relacao r: relacoes)if(r.getHangul()==h&&r.getPortugues()==p)return r;
        return null;
    }
    public ArrayList<Palavra> buscaTraducoesPortugues(String hangul){
        ArrayList<Palavra> traducoes = new ArrayList<Palavra>();
        Palavra h = buscaPalavraHangul(hangul);
        if(h==null)return null;
        for(Relacao r: relacoes)if(r.getHangul()==h)traducoes.add(r.getPortugues());
        return traducoes;
    }
    public ArrayList<Palavra> buscaTraducoesHangul(String portugues){
        ArrayList<Palavra> traducoes = new ArrayList<Palavra>();
        Palavra p = buscaPalavraPortugues(portugues);
        if(p==null)return null;
        for(Relacao r: relacoes)if(r.getPortugues()==p)traducoes.add(r.getHangul());
        return traducoes;
    }
    public String retornaRelatorioDeErros(){
        ArrayList<Palavra> palavrasErradas = new ArrayList<Palavra>();
        for(Palavra p: listaHangul)if(p.getErros()>0)palavrasErradas.add(p);
        if(palavrasErradas.size()>0){
            organizaPalavrasPorErros(palavrasErradas);
            String msg="";
            for(Palavra p: palavrasErradas){
                msg+=p.getNome()+" [";
                ArrayList<Palavra> traducoes = buscaTraducoesPortugues(p.getNome());
                for(int pos = 0; pos<traducoes.size(); pos++){
                    msg+=traducoes.get(pos).getNome();
                    if(pos!=traducoes.size()-1)msg+="; ";
                }
                msg+="] - "+p.getErros()+" erro";
                if(p.getErros()>1)msg+="s";
                msg+="\n";
            }
            return msg;
        }
        return null;
    }
    public String retiraAcentos(String input){  
        return input.replaceAll("á|à|â|ã|ä","a")  
            .replaceAll("é|è|ê|ë","e")
            .replaceAll("ó|ò|ô|ö|õ","o")
            .replaceAll("í|ì|î|ï","i")
            .replaceAll("ú|ù|û|ü","u")
            .replaceAll("ç","c");
    }
    private void organizaPalavrasPorErros(ArrayList<Palavra> l){
        boolean troca;
        troca = true;
        for (int i=l.size()-1;(i>=1)&&(troca==true); i--) { 
            troca = false; 
            for (int j=0;j<i;j++){ 
                if (l.get(j).getErros()< l.get(j+1).getErros()) { 
                    Palavra temp = l.get(j);
                    l.set(j,l.get(j+1));
                    l.set(j+1,temp);
                    troca = true;
                }    
            } 
        }
    }
    /*public String removeAcentos(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFKD);
        System.out.println("A palavra normalizada: "+str);//retirar
        str = str.replaceAll("[^\\p{ASCII}]", "");
        System.out.println("A palavra depois do replace: "+str);//retirar
        return str;
    }*/  
    /*public String removerAcentos(){  
        String acentuado = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";  
        String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";  
        char[] tabela;
        tabela = new char[256];  
        for (int i = 0; i < tabela.length; ++i) {  
        tabela [i] = (char) i;  
        }  
        for (int i = 0; i < acentuado.length(); ++i) {  
            tabela [acentuado.charAt(i)] = semAcento.charAt(i);  
        }  
    }  
    public String remover (final String s) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < s.length(); ++i) {  
            char ch = s.charAt (i);  
            if (ch < 256) {   
                sb.append (tabela [ch]);  
            } else {  
                sb.append (ch);  
            }  
        }   
        return sb.toString();  
    }*/
}