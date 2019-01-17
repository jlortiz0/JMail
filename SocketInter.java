import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author jlortiz
 */
public class SocketInter
{
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    
    public SocketInter(String addr, int port) throws UnknownHostException, IOException {
        socket = new Socket(addr, port);
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
    }
    public void close() {
        try {
            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            JMail.crash("There was an error disconnecting:\n"+e, "Disconnection error");
        }
    }
    public boolean isClosed() {
        return socket.isClosed();
    }
    public int avail() {
        try {
            return input.available();
        } catch (IOException e) {
            JMail.crash("There was an error receiving data:\n"+e, "Communication error");
        }
        return 0;
    }
    public void send(String msg) {
        try {
            output.writeUTF(msg);
        } catch (IOException e) {
            JMail.crash("There was an error sending data:\n"+e, "Communication error");
        }
    }
    public String receive() {
        try {
            return input.readUTF();
        } catch (IOException e) {
            JMail.crash("There was an error receiving data:\n"+e, "Communication error");
        }
        return "QUIT This message shoul never appear.\nIf it does, System.exit() has failed and your computer is screwed.";
    }
    public void flush() {
        try {
            output.flush();
        } catch (IOException e) {
            JMail.crash("There was an error sending data:\n"+e, "Communication error");
        }
    }
}
