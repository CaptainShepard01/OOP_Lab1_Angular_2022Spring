package ua.university.eventListeners;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.*;
import ua.university.DAO.FacultyDAO;
import ua.university.models.Student;
import ua.university.models.Teacher;

import javax.ws.rs.core.MultivaluedMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;

import static org.apache.http.impl.client.HttpClients.createDefault;

public class RegistrationEventListenerProvider implements EventListenerProvider {
    private final KeycloakSession session;
    private final RealmProvider model;

    public RegistrationEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) throws RuntimeException {

        if (EventType.REGISTER.equals(event.getType())) {
            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel newRegisteredUser = this.session.users().getUserById(event.getUserId(), realm);

            org.jboss.resteasy.spi.HttpRequest req = session.getContext().getContextObject(HttpRequest.class);
            MultivaluedMap<String, String> formParameters = req.getFormParameters();

            String ourRole = formParameters.get("role").toString();
            FacultyDAO facultyDAO = null;

            try {
                facultyDAO = new FacultyDAO();
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

            if (Objects.equals(ourRole, "[student]")) {
                RoleModel roleModel = realm.getClientById(realm.getClientByClientId("Faculty").getId()).getRole("student");
                System.out.println("Our Role model: " + roleModel.getName());
                newRegisteredUser.grantRole(roleModel);

                facultyDAO.saveStudent(new Student(-1, newRegisteredUser.getFirstName()));
            }

            if (Objects.equals(ourRole, "[teacher]")) {
                RoleModel roleModel = realm.getClientById(realm.getClientByClientId("Faculty").getId()).getRole("teacher");
                System.out.println("Our Role model: " + roleModel.getName());
                newRegisteredUser.grantRole(roleModel);

                facultyDAO.saveTeacher(new Teacher(-1, newRegisteredUser.getFirstName()));
            }

            System.out.println("Hello, am I alive? Am I? (•_•) ( •_•)>⌐■-■ (⌐■_■) -> " + newRegisteredUser.getUsername());
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}