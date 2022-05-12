package com.example.bancodedados;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TableColumn<User, String> colunaNome;

    @FXML
    private TableColumn<User, String> colunaSobrenome;

    @FXML
    private TableView<User> tablePessoa;

    @FXML
    private TextField textNome;

    @FXML
    private TextField textSobrenome;

    @FXML
    private Label labelAux;

    @FXML
    void buttonCad(MouseEvent event) throws ClassNotFoundException, SQLException {
        String nome, sobrenome;
        nome = textNome.getText();
        sobrenome = textSobrenome.getText();
        User user = new User(nome, sobrenome);

        Conector conn = new Conector();
        conn.inserirUser(user);

        UpdateTable();
    }

    @FXML
    void buttonDel(MouseEvent event) throws ClassNotFoundException, SQLException {
        int id = -1;
        id = tablePessoa.getSelectionModel().getSelectedIndex();
        if(id != -1) {
            Conector conn = new Conector();
            conn.deletarUser(id);
            UpdateTable();
            labelAux.setText("Pessoa Excluida!");
        }else{
            labelAux.setText("Nenhum Item Selecionado!");
        }
    }

    @FXML
    void buttonEdit(MouseEvent event) throws ClassNotFoundException, SQLException {
        int id = -1;
        id = tablePessoa.getSelectionModel().getSelectedIndex();

        String nome, sobrenome;
        nome = textNome.getText();
        sobrenome = textSobrenome.getText();
        User user = new User(nome, sobrenome);

        if(id != -1) {
            Conector conn = new Conector();
            conn.editarUser(id, user);
            UpdateTable();
            labelAux.setText("Pessoa Editada");
        }else{
            labelAux.setText("Nenhum item selecionado!");
        }
    }

    public void UpdateTable(){
        colunaNome.setCellValueFactory(new PropertyValueFactory<User, String>("nome"));
        colunaSobrenome.setCellValueFactory(new PropertyValueFactory<User, String>("sobrenome"));
        ObservableList<User> lista = null;
        try{
            lista = Conector.getDadosPessoa();
        }catch (ClassNotFoundException ignored){
        }
        tablePessoa.setItems(lista);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UpdateTable();
    }
}
