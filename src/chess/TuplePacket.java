package chess;

import java.io.Serializable;
import java.net.InetAddress;

public class TuplePacket implements Serializable {

	private static final long serialVersionUID = 1L;
	private InetAddress destAddr;
	private int destPort;
	private String moveMsg;

	public void setDestAddr(InetAddress addr) {
		this.destAddr = addr;
	}

	public void setDestPort(int port) {
		this.destPort = port;
	}

	public void setMsg(String msg) {
		this.moveMsg = msg;
	}

	public InetAddress getDestAddr() {
		return this.destAddr;
	}

	public int getDestPort() {
		return this.destPort;
	}

	public String getMsg() {
		return this.moveMsg;
	}
}
