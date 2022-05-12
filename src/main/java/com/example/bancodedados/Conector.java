package com.example.bancodedados;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conector {
    private final String url;
    private final String user;
    private final String senha;
    private Connection connection;

    public Conector() throws ClassNotFoundException {
        this.url = "jdbc:mysql://127.0.0.1:3306/pessoa";
        this.user = "erick";
        this.senha = "Games2011!";
        this.connection = null;
        conectar();
    }

    public Connection conectar() throws ClassNotFoundException {
        try {
            if(this.connection == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.connection = DriverManager.getConnection(this.url, this.user, this.senha);
                this.connection.setAutoCommit(false);
            }
        }catch(SQLException e) { //Erro de conexão com o banco de dados
            e.printStackTrace();
        }
        return connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    //exemplo de insert com passagem de objeto por parametro
    public void inserirUser(User usuario) throws SQLException{
        String sql = "insert into pessoa(nome, sobrenome) values(?,?);";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, usuario.getNome()); //substitui o primeiro ? pelo valor do objeto
        stm.setString(2, usuario.getSobrenome()); //substitui o segundo ? pelo valor do objeto
        stm.execute();
        connection.commit();
    }

    public void deletarUser (int id) throws ClassNotFoundException, SQLException {
        String idUser = getIdUser(id);
        String sql = "delete from pessoa where id = ?";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, idUser);
        stm.execute();
        connection.commit();

    }

    public void editarUser(int id, User usuario) throws ClassNotFoundException, SQLException {
        String idUser = getIdUser(id);
        String sql = "update pessoa set nome = ?, sobrenome = ? where id = ?";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, usuario.getNome());
        stm.setString(2, usuario.getSobrenome());
        stm.setString(3, idUser);
        stm.execute();
        connection.commit();
    }

    public String getIdUser(int id) throws ClassNotFoundException {
        ObservableList<User> lista = getDadosPessoa();
        Connection conn = conectar();
        String idUser = null;
        int i = 0;
        try {
            PreparedStatement ps = conn.prepareStatement("select * from pessoa");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(i == id){
                    idUser = rs.getString("id");
                    break;
                }else{
                    i++;
                }
            }
        }catch (Exception e) {
            System.out.println("Erro na busca de id!" + e);
        }
        return idUser;
    }

    public static ObservableList<User> getDadosPessoa() throws ClassNotFoundException {
        Conector conector = new Conector();
        Connection conn = conector.conectar();
        ObservableList<User> lista = FXCollections.observableArrayList();

        try{
            PreparedStatement ps = conn.prepareStatement("select * from pessoa");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                lista.add(new User(rs.getString("nome"), rs.getString("sobrenome")));
            }

        }catch (Exception e) {
            System.out.println("Deu erro!");
        }
        return lista;
    }

}