package ua.university.eventListeners;

import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.*;
import ua.university.DAO.StudentDAO;
import ua.university.DAO.TeacherDAO;
import ua.university.models.Student;
import ua.university.models.Teacher;

import javax.ws.rs.core.MultivaluedMap;

import java.sql.SQLException;
import java.util.*;

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
            StudentDAO studentDAO = null;
            TeacherDAO teacherDAO = null;

            try {
                studentDAO = new StudentDAO();
                teacherDAO = new TeacherDAO();
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

            if (Objects.equals(ourRole, "[student]")) {
                RoleModel roleModel = realm.getClientById(realm.getClientByClientId("BackendClient").getId()).getRole("ROLE_STUDENT");
                System.out.println("Our Role model: " + roleModel.getName());
                newRegisteredUser.grantRole(roleModel);

                studentDAO.saveStudent(new Student(-1, newRegisteredUser.getFirstName()));
            }

            if (Objects.equals(ourRole, "[teacher]")) {
                RoleModel roleModel = realm.getClientById(realm.getClientByClientId("BackendClient").getId()).getRole("ROLE_TEACHER");
                System.out.println("Our Role model: " + roleModel.getName());
                newRegisteredUser.grantRole(roleModel);

                teacherDAO.saveTeacher(new Teacher(-1, newRegisteredUser.getFirstName()));
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