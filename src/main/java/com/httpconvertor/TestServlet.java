package com.httpconvertor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FormServlet", urlPatterns = "/")
public class TestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        res.addHeader("Transfer-encoding", "chunked");
        res.addHeader("TE", "trailers");
        res.addHeader("Trailer", "bar");

        StringBuilder sb = new StringBuilder();

        final InputStream in = req.getInputStream();
        int b;
        while ((b = in.read()) != -1) {
            sb.append((char) b);
        }

        String foo = "test";
        int size = -1;

        if (req.isTrailerFieldsReady()) {
            Map<String, String> reqTrailerFields = req.getTrailerFields();
            size = reqTrailerFields.size();
//            foo = reqTrailerFields.get("foo");
        }

        final String finalFoo = foo;
        final int finalSize = size;
        res.setTrailerFields(() -> {
            Map<String, String> map = new HashMap<>();
            map.put("bar", finalFoo + finalSize);
            return map;
        });
        res.getWriter().write(sb.toString());
    }
}
