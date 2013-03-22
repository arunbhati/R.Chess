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