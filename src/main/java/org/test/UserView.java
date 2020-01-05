package org.test;

import com.github.appreciated.card.action.ActionButton;
import com.github.appreciated.card.action.Actions;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Route("listausuarios")

public class UserView extends Div {
    FormLayout fl = new FormLayout();
    Button botonbackpage = new Button ("Back");



    public UserView () throws IOException {

        botonbackpage.addClickListener(e-> backpage());

        this.add(botonbackpage);


        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/usuario/getUsuarios");

        String s = target.request().get(String.class);
        JSONArray jsonArray = new JSONArray(s);
        client.close();

        Button botonformulario = new Button ("Crear Usuario");

        add(botonformulario);
        botonformulario.addClickListener(e-> crearusuario());
        crearUsuario(jsonArray);



    }


    public void crearUsuario (JSONArray jsonArray) throws IOException {
        List<Usuario> Usuario = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Usuario.add(new Usuario(jsonArray.getJSONObject(i).getInt("idusuario"), jsonArray.getJSONObject(i).getString("correo"), jsonArray.getJSONObject(i).getString("tag")));
        }
        addCards(Usuario);
    }

    private void addCards(List<Usuario> usuarios) throws IOException {
        for (Usuario usuarioOBJ : usuarios) {

            com.github.appreciated.card.Card card = new com.github.appreciated.card.Card(
                    // if you don't want the title to wrap you can set the whitespace = nowrap
                    new TitleLabel(usuarioOBJ.getTag()).withWhiteSpaceNoWrap(),

                    new Label(" Correo"),
                    new PrimaryLabel(usuarioOBJ.getCorreo()),
                    new Label(" Id Usuario"),
                    new SecondaryLabel(String.valueOf(usuarioOBJ.getIdusuario())),
                    new Actions(
                            new ActionButton("Borrar", event -> {
                                borrarusuario(usuarioOBJ.getIdusuario());
                            })




                    )

            );

            fl.add(card);
            add(fl);

        }

    }

    public void borrarusuario(int id){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/usuario/deleteUsuario?id=" + id);
        try {
            HttpResponse response = (HttpResponse) httpClient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };

    public void crearusuario(){
        getUI().ifPresent(ui -> ui.navigate("formulario"));


    }

    public void backpage(){
        getUI().ifPresent(ui -> ui.navigate("Mainview"));
    }




}
