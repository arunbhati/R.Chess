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