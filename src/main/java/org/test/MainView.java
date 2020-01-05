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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.router.Route;
import org.json.JSONArray;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Route("Mainview")
@PWA(name = "My Application", shortName = "My Application")
public class MainView extends Div {
    FormLayout fl = new FormLayout();

    public MainView() throws IOException {


        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/ApiWow-0.0.1-SNAPSHOT/rest/servidor/getServidores");

        String s = target.request().get(String.class);
        JSONArray jsonArray = new JSONArray(s);
        client.close();

        crearServidor(jsonArray);


    }

    private void crearServidor(JSONArray jsonArray) throws IOException {
        List<Servidor> Servidor = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Servidor.add(new Servidor(jsonArray.getJSONObject(i).getInt("idservidor"), jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("region"), jsonArray.getJSONObject(i).getInt("capacidad"), jsonArray.getJSONObject(i).getString("img")));
        }
        addCards(Servidor);
    }

    private void addCards(List<Servidor> servidores) throws IOException {
        for (Servidor servidorOBJ : servidores) {
            Image image = new Image(servidorOBJ.getImg(), "bg.png");
            image.setSizeFull();
            com.github.appreciated.card.Card card = new com.github.appreciated.card.Card(
                    // if you don't want the title to wrap you can set the whitespace = nowrap
                    new TitleLabel(servidorOBJ.getName()).withWhiteSpaceNoWrap(),
                    image,
                    new Label(" Región"),
                    new PrimaryLabel(servidorOBJ.getRegion()),
                    new Label(" Capacidad"),
                    new SecondaryLabel(String.valueOf(servidorOBJ.getCapacidad())),
                    new Actions(
                            new ActionButton("Personajes", event -> {
                               getUI().ifPresent(ui -> ui.navigate("listaservidores" + "/" + String.valueOf(servidorOBJ.getIdservidor())));
                            })
                    )

            );

            fl.add(card);
            add(fl);

        }

    }




}
