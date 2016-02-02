/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author murilo
 */
public class Simplex {
    
    JTable tabela;
    int nVar;
    int total;
    boolean fase2;

    public Simplex(int nVar, JTable tabela) {
        this.nVar = nVar;        
        this.tabela = tabela;
        fase2 = false;
    }
    
    public void organizarTabela(){
        int nFolga = 0;        
        
        
        int colunaOp = tabela.getColumnCount()-2;
        for(int i=1; i<tabela.getRowCount(); i++){
            String celula = (String)tabela.getValueAt(i, colunaOp);
            if (celula.equals("<=")){
                nFolga++;
                tabela.setValueAt("=", i, colunaOp);
            }
        }
        total = nVar+nFolga+2;
        
        String aux = ",";
        for (int j=0; j<nVar; j++){
            aux += "X"+j;
            aux += ",";
        }
        for (int j=0; j<nFolga; j++){
            aux += "F"+j;
            aux += ",";
        }
        aux += "Res";
        String[] columnNames = aux.split(",");
        
        Object[][] data;
        data = new Object[tabela.getRowCount()][total];
        for(int i=0; i<tabela.getRowCount(); i++){
            for(int j=0; j<=nVar; j++){ 
                Object obj;
                obj = tabela.getValueAt(i, j);
                
                if(obj == null){
                    obj = 0;
                }else if(j>0){
                    Double a = Double.valueOf(obj.toString());
                    if(i==0 && j >0){
                        a = a*(-1);
                    }
                    obj = a;
                }
                data[i][j] = obj;
            }
        }
        int l = 1; //linha
        for(int i=nVar+1; i<=total-2; i++){
           Object obj;
           obj = 1;
           data[l][i] = obj;
           String nome = columnNames[i];
           data[l][0] = nome;
           l++;
        }
        int c = tabela.getColumnCount()-1;
        for(int i=0; i<tabela.getRowCount(); i++){
            Object obj;
            obj = tabela.getValueAt(i, c);
            if(obj == null){
                    obj = 0;
            }
            data[i][total-1] = obj;
        }
        for(int i=0; i<tabela.getRowCount(); i++){
            for(int j=0; j<total; j++){ 
                Object obj;
                obj = data[i][j];
                if(obj == null){
                    obj = 0;
                    data[i][j] = obj;
                }
            }
        }
        DefaultTableModel novaDtm = new DefaultTableModel(data, columnNames);
        //dtm.isCellEditable(0, dtm.getColumnCount()-2);
        
        tabela.setModel(novaDtm);
    }
    
    public Integer[] buscaPivo(){
        Integer[] pivo = new Integer[2];
        Double maisNegativo = 0.00;
        for(int i=1; i<=tabela.getColumnCount()-2; i++){
            Double valor = Double.valueOf(tabela.getValueAt(0, i).toString());
            if(valor < maisNegativo){
                maisNegativo = valor;
                pivo[1] = i;
            }
        }
        int ColunaResposta = tabela.getColumnCount()-1;
        Double menorDiv=0.00;
        for(int i=1; i<tabela.getRowCount(); i++){
            Double resposta = Double.valueOf(tabela.getValueAt(i, ColunaResposta).toString());
            Double valor = Double.valueOf(tabela.getValueAt(i, pivo[1]).toString());
            if(valor>0 && resposta>=0){
                Double div = resposta/valor;
                if(menorDiv == 0.00){
                    menorDiv = div;
                    pivo[0] = i;
                }
                if(div < menorDiv){
                    menorDiv = div;
                    pivo[0] = i;
                }
            }
        }
        String titulo = tabela.getModel().getColumnName(pivo[1]);
        alteraBase(pivo[0], titulo);
        return pivo;
    }
    
    public Integer[] buscaPivo(int coluna){
        Integer[] pivo = new Integer[2];
        pivo[1] = coluna;
        int ColunaResposta = tabela.getColumnCount()-1;
        Double menorDiv=0.00;
        for(int i=1; i<tabela.getRowCount(); i++){
            Double resposta = Double.valueOf(tabela.getValueAt(i, ColunaResposta).toString());
            Double valor = Double.valueOf(tabela.getValueAt(i, pivo[1]).toString());
            if(valor>0 && resposta>=0){
                Double div = resposta/valor;
                if(menorDiv == 0.00){
                    menorDiv = div;
                    pivo[0] = i;
                }
                if(div < menorDiv){
                    menorDiv = div;
                    pivo[0] = i;
                }
            }
        }
        String titulo = tabela.getModel().getColumnName(pivo[1]);
        alteraBase(pivo[0], titulo);
        return pivo;
    }
    
    public Double getResultado(){
        int coluna = tabela.getColumnCount()-1;
        Double resultado = Double.valueOf(tabela.getValueAt(0, coluna).toString());
        return resultado;
    }
    
    public JTable getTable(){
        return tabela;
    }
    
    public void pivoteamento(Integer[] pivo){
        int tamLinha = tabela.getColumnCount()-1;
        Double valorPivo = Double.valueOf(tabela.getValueAt(pivo[0], pivo[1]).toString());
        Object[] linhaPivo, linhaAntiga, linhaNova;
        linhaPivo = new Object[tamLinha];
        linhaNova = new Object[tamLinha];
        linhaAntiga = new Object[tamLinha];
        
        for(int i=1; i<=tamLinha; i++){
            Double valor = Double.valueOf(tabela.getValueAt(pivo[0], i).toString());
            linhaPivo[i-1] = valor/valorPivo;
            tabela.setValueAt(linhaPivo[i-1], pivo[0], i);
        }
        
        for(int i=0; i<tabela.getRowCount(); i++){
            if(i!=pivo[0]){
                Double fator = Double.valueOf(tabela.getValueAt(i, pivo[1]).toString());
                for(int j=1; j<=tamLinha; j++){
                    Double valorLinhaAntiga = Double.valueOf(tabela.getValueAt(i, j).toString());
                    linhaAntiga[j-1] = valorLinhaAntiga;
                    Double valorLinhaPivo = Double.valueOf(tabela.getValueAt(pivo[0], j).toString());
                    linhaNova[j-1] = valorLinhaAntiga -(valorLinhaPivo *fator);
                    tabela.setValueAt(linhaNova[j-1], i, j);
                }
                
            }
        }
    }
    
    public String montarFuncao(){
        String funcao = "Z = ";
        int colRes = tabela.getColumnCount()-1;
        for(int i=1; i<tabela.getRowCount(); i++){
            String quantidade = tabela.getValueAt(i, colRes).toString();
            String variavel = tabela.getValueAt(i, 0).toString();
            if(i != tabela.getRowCount()-1)
                funcao +=  quantidade+variavel+" + ";
            else
                funcao +=  quantidade+variavel+" = ";
        }
        funcao += tabela.getValueAt(0, colRes);
        return funcao;
    }
    
    public int verificaMultipla(){
        int tamLinha = tabela.getColumnCount()-1;
         // --- Verifica se não é multiplas soluções ----------------------------
        int n = tabela.getRowCount();
        int coluna = -1;
        boolean multipla = false;
        for(int j = 1; j<=total-2; j++){
            String variavel = tabela.getModel().getColumnName(j);
            boolean aux = false;
            for(int i = 1; i < tabela.getRowCount(); i++){
                String nomeBase = tabela.getValueAt(i, 0).toString();
                if(variavel.equals(nomeBase)){
                   aux = true; 
                }
            }
            if(!aux){
                Double valor = Double.valueOf(tabela.getValueAt(0, j).toString());
                if (valor == 0.00){
                    multipla = true;
                    coluna = j;
                }
            }
        }
        return coluna;
        // ---------------------------------------------------------------------
    }
    
    public boolean verificaParada(){
        int tamLinha = tabela.getColumnCount()-1;
        for(int i =1; i<=tamLinha; i++){
            Double aux = Double.valueOf(tabela.getValueAt(0, i).toString());
            if (aux < 0.00){
                return false;
            }
        }
        return true;
    }
    
    public void alteraBase(int linha, String nome){
        tabela.setValueAt(nome, linha, 0);
    }
    
    public String impressao(){
       
        String txt = "";
        for(int i=0; i<tabela.getColumnCount(); i++){
            txt += tabela.getModel().getColumnName(i) + "\t|";
        }
        txt += "\n"; 
        for(int i=0; i<tabela.getRowCount(); i++){
            for(int j=0; j<tabela.getColumnCount(); j++){
                Object dado = tabela.getValueAt(i, j);
                if (dado == null){
                    dado = "0";
                }
                txt+= dado.toString() + "\t|";
            }       
            txt += "\n";   
        }
        txt += "\n";  
        return txt;
    }

    

    /**
     * @param args the command line arguments
     */
    
    
}
