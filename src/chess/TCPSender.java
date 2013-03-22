package chess;

import java.io.*;
import java.net.*;

class TCPSender {
	private byte[] tuplePacket;
	private ByteArrayOutputStream lBA;
	private ObjectOutputStream lO;
	private InetAddress destAddr;
	private Socket tupleSocket;

	TCPSender() {
		tupleSocket = null;
	}

	public void setTuplePacket(TuplePacket TP) {
		try {
			lBA = new ByteArrayOutputStream();
			lO = new ObjectOutputStream(lBA);
			lO.flush();
			lO.writeObject(TP);
			lO.close();
			lBA.close();
			this.tuplePacket = lBA.toByteArray();

		} catch (Exception e) {
			System.err.println("\n" + e.toString());
			this.tuplePacket = null;
		}
	}

	public boolean sendTuplePacket(TuplePacket TP) {
		boolean flag = false;
		destAddr = TP.getDestAddr();
		int destPort = TP.getDestPort();
		this.setTuplePacket(TP);
		if (this.tuplePacket != null) {
			try {
				this.tupleSocket = new Socket(this.destAddr, destPort);
				DataOutputStream outToServer;

				System.out.println("client class is sending");

				outToServer = new DataOutputStream(
						tupleSocket.getOutputStream());
				outToServer.write(tuplePacket);
				System.out.println("sent");
				flag = true;
				this.tupleSocket.close();
			} catch (Exception e) {
				flag = false;
				System.err.println("\n" + e.toString());
			}
			this.tuplePacket = null;
		}
		return (flag);
	}
}