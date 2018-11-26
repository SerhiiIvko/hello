package com.ivko.hello.service;

import com.ivko.hello.manager.ManagementLayer;
import com.ivko.hello.model.Contact;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("contacts")
public class WebServiceLayer {
    private static final Logger LOG = Logger.getLogger(String.valueOf(WebServiceLayer.class));

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contact> getNameFilter(@QueryParam("nameFilter") String val) {
        if (val == null || val.isEmpty()) {
            throw new WebApplicationException(Response
                    .status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .entity("Error! Parameter is wrong or empty!")
                    .build());
        } else {
            List<Contact> contacts = new ArrayList<>();
            try {
                contacts = ManagementLayer
                        .getInstance()
                        .getFilteredContacts(val, null);
            } catch (SQLException e) {
                LOG.info(e.getMessage());
            }
            return contacts;
        }
    }
}