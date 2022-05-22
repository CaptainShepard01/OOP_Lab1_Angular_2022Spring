package ua.university.utils;

import java.net.URI;

public class ServletUtils {
    public static int getURIId(String uri_str) throws Exception {
        URI uri = new URI(uri_str);
        String[] segments = uri.getPath().split("/");
        int id = -1;
        if (segments.length > 4)
            throw new Exception("");
        else if (segments.length == 4) {
            String idStr = segments[segments.length-1];
            id = Integer.parseInt(idStr);
        }
        return id;
    }
}
