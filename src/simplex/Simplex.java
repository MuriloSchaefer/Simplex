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

    public Simplex(int nVar, JTable tabela) {
        this.nVar = nVar;        
        this.tabela = tabela;
        int nFolga = 0;
        
        int colunaOp = tabela.getColumnCount()-2;
        for(int i=1; i<tabela.getRowCount(); i++){
            String celula = (String)tabela.getValueAt(i, colunaOp);
            if (celula.equals("<=")){
                nFolga++;
                tabela.setValueAt("=", i, colunaOp);
            }
        }
        int total = nVar+nFolga+2;
        
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
        novaDtm.removeRow(tabela.getRowCount()-1);
        
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
            if(valor>0){
                Double div = resposta/valor;
                if(i==1){
                    menorDiv = div;
                    pivo[0] = i;
                }
                if(div < menorDiv){
                    menorDiv = div;
                    pivo[0] = i;
                }
            }
        }
        
        return pivo;
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
                Double aux = Double.valueOf(tabela.getValueAt(i, pivo[1]).toString());
                Double fatorMult = -(aux/valorPivo);
                for(int j=1; j<=tamLinha; j++){
                    Double valorLinhaAntiga = Double.valueOf(tabela.getValueAt(i, j).toString());
                    linhaAntiga[j-1] = valorLinhaAntiga;
                    Double valorLinhaPivo = Double.valueOf(tabela.getValueAt(pivo[0], j).toString());
                    linhaNova[j-1] = (fatorMult*valorLinhaPivo)+valorLinhaAntiga;
                    tabela.setValueAt(linhaNova[j-1], i, j);
                }
                
            }
        }
    }
    
    public void impressao(){
        for(int i=0; i<tabela.getColumnCount(); i++){
            System.out.print(tabela.getModel().getColumnName(i) + "\t|");
        }
        System.out.println(); 
        for(int i=0; i<tabela.getRowCount(); i++){
            for(int j=0; j<tabela.getColumnCount(); j++){
                Object dado = tabela.getValueAt(i, j);
                if (dado == null){
                    dado = "0";
                }
                System.out.print(dado.toString() + "\t|");
            }       
            System.out.println();         
        }
    }

    

    /**
     * @param args the command line arguments
     */
    
    
}
