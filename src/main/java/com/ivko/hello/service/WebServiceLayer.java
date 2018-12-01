package com.ivko.hello.service;

import com.ivko.hello.manager.ManagementLayer;
import com.ivko.hello.model.Contact;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Path("contacts")
public class WebServiceLayer {
    private static final Logger LOG = Logger.getLogger(String.valueOf(WebServiceLayer.class));

    @GET
    @Produces("application/json; charset=UTF-8")
    public List<Contact> getNameFilter(@QueryParam("nameFilter") String val) {
        if (val == null || val.isEmpty() || !isRegex(val)) {
            throw new WebApplicationException(Response
                    .status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .entity("Error! Parameter is wrong or empty!")
                    .build());
        } else {
            List<Contact> contacts = new ArrayList<>();
            try {
                contacts = ManagementLayer
                        .getInstance()
                        .getFilteredContacts(URLDecoder.decode(val, "UTF-8"), null);
            } catch (SQLException e) {
                LOG.info(e.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.getMessage();
            }
            return contacts;
        }
    }

    private static boolean isRegex(String val) {
        try {
            Pattern.compile(val);
            return true;
        } catch (PatternSyntaxException ex) {
            return false;
        }
    }
}