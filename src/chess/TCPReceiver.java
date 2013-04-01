/*  Copyright (C) 2013  Arihant Sethia

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
    
package chess;

import java.io.*;
import java.net.*;

import chess.ChessGUI;

class TCPReceiver implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	private ServerSocket server;
	private Socket receiver;
	private int dataPort; // 7737 [Unassigned port (IANA)]
	private ObjectInputStream lO;
	private ChessGUI chess;

	TCPReceiver(int port) {
		chess = new ChessGUI();
		this.server = null;
		this.dataPort = port;
		try {
			this.server = new ServerSocket(this.dataPort);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			receiver = null;
			try {
				receiver = server.accept();
				lO = new ObjectInputStream(receiver.getInputStream());
				if (lO != null) {
					Object data = lO.readObject();
					lO.close();
					String dataClassName = data.getClass().getSimpleName();
					if (dataClassName.equals("TuplePacket")) {
						TuplePacket TP = (TuplePacket) data;
						if ((TP.getDestAddr().getHostAddress()
								.equals(InetAddress.getLocalHost()
										.getHostAddress()))) {
							System.out.println("Recieved");
							chess.recieveMsg(TP.getMsg().trim());
						}
					}

				}
				receiver.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}