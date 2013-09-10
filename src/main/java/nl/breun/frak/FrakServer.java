package nl.breun.frak;

import clojure.lang.RT;
import clojure.lang.Symbol;
import org.simpleframework.http.Method;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class FrakServer implements Container {

    private final static Logger LOGGER = Logger.getLogger(FrakServer.class.getName());
    private final static String FRAK_NS = "frak";
    private final static int DEFAULT_PORT = 8080;

    public FrakServer() {
        initializeFrak();
    }

    private void initializeFrak() {
        RT.var("clojure.core", "require").invoke(Symbol.intern(FRAK_NS));
    }

    @Override
    public void handle(Request request, Response response) {
        if (!Method.POST.equals(request.getMethod())) {
            throw new UnsupportedOperationException("Only HTTP POST is supported.");
        }

        try {
            final String input = request.getContent();
            final String output = getPattern(input.split(" ")).pattern();

            write(response, output);

            LOGGER.info("Input: '" + input + "', output: '" + output + "'");
        } catch (Exception e) {
            LOGGER.severe("Failed to handle request: " + e.getMessage());
        }
    }

    private Pattern getPattern(String[] input) {
        return (Pattern) RT.var(FRAK_NS, "pattern").invoke(input);
    }

    private void write(Response response, String regex) throws IOException {
        setHeaders(response);

        PrintStream responsePrintStream = response.getPrintStream();
        responsePrintStream.println(regex);
        responsePrintStream.close();
    }

    private void setHeaders(Response response) {
        response.setValue("Content-Type", "text/plain");
        response.setValue("Server", "FrakServer/1.0");

        final long time = System.currentTimeMillis();
        response.setDate("Date", time);
        response.setDate("Last-Modified", time);
    }

    public static void main(String[] args) throws Exception {
        Container container = new FrakServer();
        Server server = new ContainerServer(container);
        Connection connection = new SocketConnection(server);
        int port = determinePort(args);
        SocketAddress address = new InetSocketAddress(port);

        connection.connect(address);

        LOGGER.info("FrakServer listening on port " + port + ", POST some strings!");
    }

    private static int determinePort(String[] args) {
        if (args != null && args.length == 1) {
            final String argument = args[0];
            try {
                return Integer.parseInt(argument);
            }
            catch (NumberFormatException e) {
                LOGGER.warning("Could not parse argument '" + argument + "' to an integer port number, using default.");
            }
        }
        return DEFAULT_PORT;
    }
}
