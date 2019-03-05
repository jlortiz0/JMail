/*
 * Copyright (C) 2019 jlortiz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        return "QUIT This message should never appear.\nIf it does, System.exit() has failed and your computer is screwed.";
    }
    public void flush() {
        try {
            output.flush();
        } catch (IOException e) {
            JMail.crash("There was an error sending data:\n"+e, "Communication error");
        }
    }
}
