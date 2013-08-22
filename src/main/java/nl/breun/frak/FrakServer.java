package nl.breun.frak;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.regex.Pattern;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import clojure.lang.RT;
import clojure.lang.Symbol;

public class FrakServer implements Container {

    private final static String FRAK_NS = "frak";

    public FrakServer() {
        initializeFrak();
    }

    private void initializeFrak() {
        RT.var("clojure.core", "require").invoke(Symbol.intern(FRAK_NS));
    }

    @Override
    public void handle(Request request, Response response) {
        if (!"POST".equals(request.getMethod())) {
            throw new UnsupportedOperationException("Only HTTP POST is supported.");
        }

        try {
            setHeaders(response);

            final String[] inputStrings = getInputStrings(request);
            final Pattern pattern = getPattern(inputStrings);

            write(response, pattern.pattern());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHeaders(Response response) {
        response.setValue("Content-Type", "text/plain");
        response.setValue("Server", "FrakServer/1.0");

        final long time = System.currentTimeMillis();
        response.setDate("Date", time);
        response.setDate("Last-Modified", time);
    }

    private String[] getInputStrings(Request request) throws IOException {
        final String requestBody = request.getContent();
        return requestBody.split(" ");
    }

    private Pattern getPattern(String[] inputStrings) {
        return (Pattern) RT.var(FRAK_NS, "pattern").invoke(inputStrings);
    }

    private void write(Response response, String regex) throws IOException {
        PrintStream responsePrintStream = response.getPrintStream();
        responsePrintStream.println(regex);
        responsePrintStream.close();
    }

    public static void main(String[] args) throws Exception {
        Container container = new FrakServer();
        Server server = new ContainerServer(container);
        Connection connection = new SocketConnection(server);
        int port = 8080;
        SocketAddress address = new InetSocketAddress(port);

        connection.connect(address);

        System.out.println("FrakServer listening on port " + port + ", POST some strings!");
    }
}
