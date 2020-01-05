package org.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

@Route("formulario")

public class Formulario extends VerticalLayout{

    Button botonbackpage = new Button ("Back");



        TextField tagFieldField;
        TextField correoFieldField;


    public Formulario(){
        botonbackpage.addClickListener(e-> backpage());

        this.add(botonbackpage);

            setForm();
        }

        private void setForm() {
            tagFieldField = new TextField();
            tagFieldField.setLabel("Tag");
            tagFieldField.setSizeFull();
            correoFieldField = new TextField();
            correoFieldField.setLabel("Correo");
            correoFieldField.setSizeFull();



            Button createUser = new Button("Crear Usuario / Create User");
            Button cancelButton = new Button("Cancelar / Cancel Operation");

            createUser.addClickListener(e->{
                addUser();
            });

            cancelButton.addClickListener(e->{
                getUI().ifPresent(ui -> ui.navigate("listausuarios"));
            });

            add(tagFieldField, correoFieldField, createUser, cancelButton);
        }

        private void addUser() {


            if(tagFieldField.getValue() != "" && correoFieldField.getValue() != ""){
                try{
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpPost post = new HttpPost("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/usuario/anadirUsuario");
                    JSONObject jsonObjectUser = new JSONObject();
                    jsonObjectUser.put("correo", tagFieldField.getValue());
                    jsonObjectUser.put("tag", correoFieldField.getValue());
                    post.setEntity(new StringEntity(jsonObjectUser.toString()));
                    post.setHeader("Content-type", "application/json");
                    CloseableHttpResponse response = httpClient.execute(post);
                    System.out.println(EntityUtils.toString(response.getEntity()));
                }catch(IOException e){
                    e.printStackTrace();
                }

            }else{
                Label message = new Label("There are some empty fields");
                Notification notification = new Notification(String.valueOf(message));
                notification.setPosition(Notification.Position.MIDDLE);
                notification.setDuration(3000);
                notification.open();

            }



        }

    public void backpage(){
        getUI().ifPresent(ui -> ui.navigate("listausuarios"));
    }
}
