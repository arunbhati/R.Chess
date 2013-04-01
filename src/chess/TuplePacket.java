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
