package org.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.flow.helper.HasUrlParameterMapping;
import org.vaadin.flow.helper.UrlParameter;
import org.vaadin.flow.helper.UrlParameterMapping;

import javax.swing.*;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Route("listaservidores")
@UrlParameterMapping(":idservidor")
public class ServerView extends VerticalLayout implements HasUrlParameterMapping {

    @UrlParameter
    public String idservidor;

    Button boton = new Button ("Avanzar/Next");
    Button botonusuarios = new Button ("Ir a usuarios");

    int idusuario;
    

    public ServerView() {

        boton.addClickListener(e-> getpersonajes());

        botonusuarios.addClickListener(e-> getusuarios());

        




        add(botonusuarios);
        this.add(boton);
    }

    private void getpersonajes() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/servidor/getpersonajesbyid?id="+idservidor);
        String s = target.request().get(String.class);
        JSONObject employee = new JSONObject(s);

        client.close();

        employee.getJSONArray("personajes");


        GridCrud<Personaje> personajes = new GridCrud(Personaje.class);
        personajes.setCrudListener(new CrudListener<Personaje>() {
            @Override
            public Collection<Personaje> findAll() {
                Client client = ClientBuilder.newClient();
                WebTarget target = client.target("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/servidor/getpersonajesbyid?id="+idservidor);
                String s = target.request().get(String.class);
                JSONObject employee = new JSONObject(s);

                client.close();

                employee.getJSONArray("personajes");
                JSONArray personajeArray = employee.getJSONArray("personajes");
                List<Personaje> personaje = new ArrayList<>();
                for (int i = 0; i < personajeArray.length(); i++) {


                    personaje.add(new Personaje(personajeArray.getJSONObject(i).getInt("idpersonaje"), personajeArray.getJSONObject(i).getString("name"), personajeArray.getJSONObject(i).getString("race"),
                            personajeArray.getJSONObject(i).getInt("level"), personajeArray.getJSONObject(i).getString("class_")));
                }
                return personaje;



            }



            @Override
            public Personaje add(Personaje personaje) {

                try{
                    HttpPost post = new HttpPost("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/personajes/anadirPersonaje?id="+ idusuario);
                    JSONObject jsonObjectPersonaje = new JSONObject();
                    jsonObjectPersonaje.put("idpersonaje", personaje.getIdpersonaje());
                    jsonObjectPersonaje.put("name", personaje.getName());
                    jsonObjectPersonaje.put("race", personaje.getRace());
                    jsonObjectPersonaje.put("level", String.valueOf(personaje.getLevel()));
                    jsonObjectPersonaje.put("class_", personaje.getClase());


                    post.setEntity(new StringEntity(jsonObjectPersonaje.toString()));
                    post.setHeader("Content-type", "application/json");
                    try (CloseableHttpClient httpClient = HttpClients.createDefault();
                         CloseableHttpResponse response = httpClient.execute(post)) {
                        System.out.println(EntityUtils.toString(response.getEntity()));


                    }

                    addmanymany(personaje.getIdpersonaje());

                } catch(IOException | SQLException e){
                    e.printStackTrace();
                }
                return personaje;
            }

            @Override
            public Personaje update(Personaje personaje) {
                JSONObject personajeJson = new JSONObject();
                personajeJson.put("idpersonaje", personaje.getIdpersonaje());
                personajeJson.put("name",personaje.getName());
                personajeJson.put("class_", personaje.getClase());
                personajeJson.put("race", personaje.getRace());
                personajeJson.put("level", personaje.getLevel());

                try{
                    String putEndpoint = "http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/personajes/updatePersonaje";
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpPut httpPut = new HttpPut(putEndpoint);
                    httpPut.setHeader("Accept", "application/json");
                    httpPut.setHeader("Content-type", "application/json");
                    StringEntity params =new StringEntity(personajeJson.toString());
                    httpPut.setEntity(params);

                    try (CloseableHttpClient httpClient = HttpClients.createDefault();
                         CloseableHttpResponse response = httpClient.execute(httpPut)) {

                        System.out.println(EntityUtils.toString(response.getEntity()));
                    }

                } catch(IOException e){
                    e.printStackTrace();
                }

                return personaje;


            }

            @Override
            public void delete(Personaje personaje) {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpDelete httpDelete = new HttpDelete("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/personajes/deletePersonaje?id="+personaje.getIdpersonaje());
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

            }

        });

        boton.setEnabled(false);
        add(personajes);
        com.vaadin.flow.component.html.Label labelsito = new com.vaadin.flow.component.html.Label("Para añadir un Personaje nuevo , primero seleccione el usuario donde desea añadirlo");
        add(labelsito);
        Listausuarios();






    }

    public void getusuarios(){
        getUI().ifPresent(ui -> ui.navigate("listausuarios"));
    }




    public void Listausuarios(){

        List<Usuario> Usuariolist = new ArrayList<>();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/usuario/getUsuarios");
        String s = target.request().get(String.class);
        JSONArray usuario = new JSONArray(s);

        client.close();

        for (int i = 0; i < usuario.length(); i++) {
            Usuariolist.add(new Usuario(usuario.getJSONObject(i).getInt("idusuario"), usuario.getJSONObject(i).getString("tag"), usuario.getJSONObject(i).getString("correo")));
        }




        Grid<Usuario> grid = new Grid<>(Usuario.class);
        grid.setItems(Usuariolist);




        add(grid);
        grid.addSelectionListener(event -> {
           Set<Usuario> selected =  event.getAllSelectedItems();
            for (Usuario a: selected) {
                idusuario =  a.getIdusuario();
                break;
            }
            Object[] usuarioData = selected.toArray();
            System.out.println(idusuario);
        });




    }




    public void backpage(){
        getUI().ifPresent(ui -> ui.navigate("Mainview"));
    }

































































        public Connection conectarBaseDeDatos() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
         Connection conn = null;


//        try {
//            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
//            String urlDatabase = "jdbc:mysql://localhost:3306/db_wow";
//            conn = DriverManager.getConnection(urlDatabase, "root", "1234");
//
//
//        } catch (SQLException e) {
//            conn = null;
//            throw new RuntimeException("CONNECTION ERROR!");
//        }
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_wow?serverTimezone=UTC",
                        "root", "1234");

                if(!conn.isClosed())
                    System.out.println("Successfully connected to " +
                            "MySQL server using TCP/IP...");


            return conn;
        }



    public void addmanymany(int idPersonaje) throws SQLException {
        String sql = "insert into personaje_servidor values (" + idPersonaje + " , " + idservidor + ")";
        Statement stmnt;
        try (Connection connection = conectarBaseDeDatos()) {
            stmnt = connection.createStatement();
            stmnt.execute(sql);
        } catch (IllegalAccessException e) {

        } catch (InstantiationException e) {

        } catch (ClassNotFoundException e) {

        }

    }
        }




