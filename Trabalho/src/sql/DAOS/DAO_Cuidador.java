package sql.DAOS;

import GUI.InterfacePrincipalCuidador;
import java.sql.*;
import java.util.HashMap;
import static sql.Conexao.open;
import javax.swing.JOptionPane;
import users.UsrCuidador;
public class DAO_Cuidador {
    private static String instrucao_insert = "INSERT INTO UsuarioCuidador(TipoContrato, Cpf, NomePerfil, DescricaoEspecialidades, periodo, senhaCuidador) VALUES (?,?,?,?,?,?);";
    private static String instrucao_select = "SELECT * FROM UsuarioCuidador;";
    private static String instrucao_pesquisa = "SELECT * FROM UsuarioCuidador WHERE DescricaoEspecialidades LIKE ?;";
    private static String instrucao_pesquisa2 = "SELECT periodo FROM UsuarioCuidador WHERE IDCuidador=?;";
    

        public static HashMap<String, UsrCuidador> consultaCuidador() throws Exception{
            HashMap<String, UsrCuidador> mapaCuidador = new HashMap<>(); // Inicializa o HashMap
            Connection con = open(); // Abre conexão
            Statement sentenca = con.createStatement();
            ResultSet rs = sentenca.executeQuery(instrucao_select); // Executa query

            try {
                while (rs.next()) {
                    UsrCuidador usr = new UsrCuidador(); // Inicia instância de usuário

                    // Seta os dados da query como dados do objeto usuário
                    usr.setId(rs.getString("IDCuidador"));
                    usr.setTipoContrato(rs.getString("TipoContrato"));
                    usr.setNome(rs.getString("NomePerfil"));
                    usr.setCpf(rs.getString("Cpf"));
                    usr.setDescricaoEspecialidade(rs.getString("DescricaoEspecialidades"));
                    usr.setPeriodo(rs.getString("periodo"));
                    usr.setSenhaCuidador(rs.getString("senhaCuidador"));

                    // Adiciona o objeto no HashMap usando o CPF como chave
                    mapaCuidador.put(usr.getCpf(), usr);
                }
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println(mapaCuidador);
            return mapaCuidador;
        }
        
        public static HashMap<String, UsrCuidador> pesquisaCuidador(String busca) throws Exception{
            HashMap<String, UsrCuidador> mapaCuidador = new HashMap<>(); // Inicializa o HashMap
            Connection con = open(); // Abre conexão
            PreparedStatement stm = con.prepareStatement(instrucao_pesquisa);
            stm.setString(1, "%"+busca+"%");
            ResultSet rs = stm.executeQuery(); // Executa query
            

            try {
                while (rs.next()) {
                    UsrCuidador usr = new UsrCuidador(); // Inicia instância de usuário

                    // Seta os dados da query como dados do objeto usuário
                    
                    usr.setId(rs.getString("IDCuidador"));
                    usr.setTipoContrato(rs.getString("TipoContrato"));
                    usr.setNome(rs.getString("NomePerfil"));
                    usr.setCpf(rs.getString("Cpf"));
                    usr.setDescricaoEspecialidade(rs.getString("DescricaoEspecialidades"));
                    usr.setPeriodo(rs.getString("periodo"));
                    usr.setSenhaCuidador(rs.getString("senhaCuidador"));

                    // Adiciona o objeto no HashMap usando o CPF como chave
                    mapaCuidador.put(usr.getCpf(), usr);
                }
                System.out.println(mapaCuidador);
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            return mapaCuidador;
        }
        
       public static String pesquisaCuidador2(String idCuidador) throws Exception {
            Connection con = open(); // Abre conexão
            PreparedStatement stm = con.prepareStatement(instrucao_pesquisa2);
            stm.setString(1, idCuidador);
            ResultSet rs = stm.executeQuery(); // Executa query

            String periodo = null;
            if (rs.next()) {
                periodo = rs.getString("periodo");
            }

            con.close();
            return periodo;
        }
        
        public static Boolean insertCuidador(String TipoContrato, String cpf, String NomePerfil, String DescricaoEspecialidades, String periodo, String senha){
            try{
                Connection con = open();
                PreparedStatement stm = con.prepareStatement(instrucao_insert);
                stm.setString(1, TipoContrato);
                stm.setString(2, cpf);
                stm.setString(3, NomePerfil);
                stm.setString(4, DescricaoEspecialidades);
                stm.setString(5, periodo);
                stm.setString(6, senha);
                stm.execute();
                con.close();
            return true;
            }catch(Exception ex){
                System.out.println(ex.getMessage());
                return false;
            }
        }
      

        
    public boolean login(String cpf, String senha) {
        try {
            HashMap<String, UsrCuidador> cuidadores = consultaCuidador();

            // Verificar se o cuidador existe e se a senha corresponde
            UsrCuidador cuidador = cuidadores.get(cpf);

            if (cuidador != null && cuidador.getSenhaCuidador().equals(senha)) {
                JOptionPane.showMessageDialog(null, "Logado");
                
                InterfacePrincipalCuidador inter = new InterfacePrincipalCuidador(cuidador.getId(), cuidador.getNome(), cuidador.getTipoContrato(), cuidador.getPeriodo(), cuidador.getDescricaoEspecialidade());
                inter.dispose();
                inter.setVisible(true);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "CPF ou senha inválidos");
                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
