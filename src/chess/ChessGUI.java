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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ChessGUI extends JFrame implements MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 1L;
	JTextArea bottom = new JTextArea();
	private String display = " Begin The Game";
	private TCPSender msgSender;
	private static TCPReceiver recieve;
	private String Msg = "";
	private JPanel bottombar = new JPanel();
	private JPanel leftbar = new JPanel(new GridLayout(8, 1));
	private JPanel topbar = new JPanel(new GridLayout(1, 8));
	private JPanel sidebar = new JPanel();
	private JPanel[][] pnlChessCells = new JPanel[8][8];
	private JPanel[] toppart = new JPanel[8];
	private JPanel[] leftpart = new JPanel[8];
	private JPanel pnlMain = new JPanel(new GridLayout(8, 8));
	private String[][] strChessBoard = new String[][] {
			{ "RB", "NB", "BB", "QB", "KB", "BB", "NB", "RB" },
			{ "PB", "PB", "PB", "PB", "PB", "PB", "PB", "PB" },
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "PW", "PW", "PW", "PW", "PW", "PW", "PW", "PW" },
			{ "RW", "NW", "BW", "QW", "KW", "BW", "NW", "RW" } };

	private ImageIcon rookBlack = new ImageIcon(System.getProperty("user.dir")
			+ "/Figurines/Black R.png");

	private ImageIcon rookWhite = new ImageIcon(System.getProperty("user.dir")
			+ "/Figurines/White R.png");

	private ImageIcon bishopBlack = new ImageIcon(
			System.getProperty("user.dir") + "/Figurines/Black B.png");

	private ImageIcon bishopWhite = new ImageIcon(
			System.getProperty("user.dir") + "/Figurines/White B.png");

	private ImageIcon knightBlack = new ImageIcon(
			System.getProperty("user.dir") + "/Figurines/Black N.png");

	private ImageIcon knightWhite = new ImageIcon(
			System.getProperty("user.dir") + "/Figurines/White N.png");

	private ImageIcon kingBlack = new ImageIcon(System.getProperty("user.dir")
			+ "/Figurines/Black K.png");

	private ImageIcon kingWhite = new ImageIcon(System.getProperty("user.dir")
			+ "/Figurines/White K.png");

	private ImageIcon queenBlack = new ImageIcon(System.getProperty("user.dir")
			+ "/Figurines/Black Q.png");

	private ImageIcon queenWhite = new ImageIcon(System.getProperty("user.dir")
			+ "/Figurines/White Q.png");

	private ImageIcon pawnBlack = new ImageIcon(System.getProperty("user.dir")
			+ "/Figurines/Black P.png");

	private ImageIcon pawnWhite = new ImageIcon(System.getProperty("user.dir")
			+ "/Figurines/White P.png");

	private boolean boolMoveSelection = false, bWhite = true, bMyTurn = true;
	// bBlack = false;

	private Point pntMoveFrom, pntMoveTo;

	private Container c;

	static InetAddress addr = null;
	static int oPort, pPort;

	public static void main(String[] args) {
		infoSend();

	}

	// the whole constructor is for setting up the UI of the form

	@SuppressWarnings("deprecation")
	public ChessGUI()

	{
		c = getContentPane();

		setBounds(100, 100, 641, 533);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setTitle("Roaming Chess v1.0");

		setResizable(false);

		c.setLayout(null);

		pnlMain.setBounds(10, 10, 460, 460);

		pnlMain.setBackground(Color.BLACK);

		sidebar.setBounds(470, 0, 165, 505);

		bottombar.setBounds(0, 470, 470, 35);

		leftbar.setBounds(0, 18, 10, 470);

		topbar.setBounds(10, -8, 460, 18);

		c.add(pnlMain);

		c.add(sidebar);

		c.add(bottombar);

		c.add(leftbar);

		c.add(topbar);

		c.addMouseMotionListener(this);

		sidebar.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		bottombar.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		this.drawChessBoard();

		this.arrangeChessPieces();

		show();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	// This method receives the move on the opponents chess board and then make

	// it happen, on chess board

	// the both the hosts
	public void recieveMsg(String move) {
		// if (!bMyTurn)
		{

			String unit = move.substring(0, 2);
			int X1 = move.charAt(2) - 65;

			int Y1 = move.charAt(3) - 49;

			int X2 = move.charAt(4) - 65;

			int Y2 = move.charAt(5) - 49;

			moveChessPiece(X1, Y1, X2, Y2, unit);
		}
	}

	// This method sends the move on the opponents chess board

	public void sendMsg(String move) {
		// if (bMyTurn)
		{
			TuplePacket TP = new TuplePacket();
			TP.setDestAddr(addr);
			TP.setDestPort(oPort);
			TP.setMsg(move);
			msgSender = new TCPSender();
			msgSender.sendTuplePacket(TP);
			// if () {
			// bMyTurn = !bMyTurn;
			// }

		}
	}

	public static void infoSend() {

		try {
			addr = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JFormattedTextField ip = new JFormattedTextField(addr.getHostAddress());
		JTextField port1 = new JTextField();
		JTextField port2 = new JTextField();
		final JComponent[] inputs = new JComponent[] { new JLabel("IP"), ip,
				new JLabel("Opponent Receiving Port"), port1,
				new JLabel("Player Receiving Port"), port2 };
		JOptionPane.showMessageDialog(null, inputs, "Game Information",
				JOptionPane.PLAIN_MESSAGE);
		try {
			addr = InetAddress.getByName(ip.getText());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		oPort = Integer.parseInt(port1.getText());
		pPort = Integer.parseInt(port2.getText());
		System.out.print(addr);
		recieve = new TCPReceiver(pPort);
		recieve.run();
	}

	public void mouseClicked(MouseEvent e)

	{

		if (bMyTurn)

		{

			Object source = e.getSource();

			JPanel pnlTemp = (JPanel) source;

			int intX = (pnlTemp.getX() / 57);

			int intY = (pnlTemp.getY() / 57);

			this.boolMoveSelection = !this.boolMoveSelection;

			if (this.boolMoveSelection)

			{
				this.pntMoveFrom = new Point(intX, intY);

				if (this.strChessBoard[this.pntMoveFrom.y][this.pntMoveFrom.x]
						.toString().trim().equals(""))

					this.boolMoveSelection = !this.boolMoveSelection;

				if ((!this.strChessBoard[this.pntMoveFrom.y][this.pntMoveFrom.x]
						.toString().trim().equals(""))
						&&

						this.strChessBoard[this.pntMoveFrom.y][this.pntMoveFrom.x]
								.toString().charAt(1) == 'B')

					this.boolMoveSelection = !this.boolMoveSelection;

				if ((!this.strChessBoard[this.pntMoveFrom.y][this.pntMoveFrom.x]
						.toString().trim().equals(""))
						&&

						!this.bWhite
						&& this.strChessBoard[this.pntMoveFrom.y][this.pntMoveFrom.x]
								.toString().charAt(1) == 'W')

					this.boolMoveSelection = !this.boolMoveSelection;

				if (this.boolMoveSelection) {

					this.makeChessPieceDifferent(true);
				}
			}

			else

			{

				this.pntMoveTo = new Point(intX, intY);

				if (!this.pntMoveFrom.equals(this.pntMoveTo))

				{

					if (this.strChessBoard[this.pntMoveFrom.y][this.pntMoveFrom.x]
							.toString().trim() != "")

						if (this.isMoveValid(this.pntMoveTo, this.pntMoveFrom))

						{
							String unitName = this.strChessBoard[this.pntMoveFrom.y][this.pntMoveFrom.x]
									.toString();

							this.moveChessPiece(this.pntMoveFrom.x,
									this.pntMoveFrom.y, this.pntMoveTo.x,
									this.pntMoveTo.y, unitName);
							this.sendMsg(Msg);

						}

						else

						{

							JOptionPane.showMessageDialog(this,
									"Invalid Move Request.", "Warning",
									JOptionPane.ERROR_MESSAGE);

							this.makeChessPieceDifferent(false);

						}

				}

				else

					this.makeChessPieceDifferent(false);

			}

		}
	}

	// This method checks if attempted move is valid or not

	private boolean isMoveValid(Point to, Point from)

	{

		boolean isMoveValid = false;
		if (!(this.strChessBoard[to.y][to.x].toString().charAt(1) == 'W')) {
			if (this.strChessBoard[from.y][from.x].toString().trim() == "PW") {
				if (from.y == to.y + 1) {
					if (to.x == from.x)
						isMoveValid = true;

					if (to.x - from.x == 1 || from.x - to.x == 1) {
						if (this.strChessBoard[to.y][to.x].toString().charAt(1) == 'B')
							isMoveValid = true;

					}
				}
			} else if (this.strChessBoard[from.y][from.x].toString().trim() == "NW") {

				if (from.y == to.y + 2)
					if (to.x - from.x == 1 || from.x - to.x == 1)
						isMoveValid = true;

				if (from.y == to.y - 2)
					if (to.x - from.x == 1 || from.x - to.x == 1)
						isMoveValid = true;

				if (from.x == to.x - 2)
					if (to.y - from.y == 1 || from.y - to.y == 1)
						isMoveValid = true;

				if (from.x == to.x + 2)
					if (to.y - from.y == 1 || from.y - to.y == 1)
						isMoveValid = true;

			} else if (this.strChessBoard[from.y][from.x].toString().trim() == "RW") {
				if (from.x == to.x) {
					isMoveValid = true;
					if (from.y > to.y) {
						for (int i = to.y + 1; i < from.y; i++) {
							if (!this.strChessBoard[i][to.x].toString().trim()
									.equals("")) {
								isMoveValid = false;
							}
						}
					} else {
						for (int i = from.y + 1; i < to.y; i++) {
							if (!this.strChessBoard[i][from.x].toString()
									.trim().equals(""))
								isMoveValid = false;
						}
					}
				} else if (from.y == to.y) {
					isMoveValid = true;
					if (from.x > to.x)

					{
						for (int i = to.x + 1; i < from.x; i++) {
							if (!this.strChessBoard[from.y][i].toString()
									.trim().equals(""))
								isMoveValid = false;
						}
					} else {
						for (int i = from.x + 1; i < to.x; i++) {
							if (!this.strChessBoard[from.y][i].toString()
									.trim().equals(""))
								isMoveValid = false;
						}
					}

				}

			} else if (this.strChessBoard[from.y][from.x].toString().trim() == "BW") {
				if (from.x != to.x && to.y != from.y) {
					if (from.x - to.x > 0) {
						if (from.y - to.y > 0) {
							if (from.x - to.x == from.y - to.y) {
								isMoveValid = true;
							}

							for (int i = 1; i < from.x - to.x; i++) {
								if (!this.strChessBoard[to.y + i][to.x + i]
										.toString().trim().equals("")) {
									isMoveValid = false;
								}
							}

						} else {
							if (from.x - to.x == to.y - from.y)
								isMoveValid = true;

							for (int i = 1; i < from.x - to.x; i++) {
								if (!this.strChessBoard[to.y - i][to.x + i]
										.toString().trim().equals("")) {
									isMoveValid = false;
								}
							}

						}

					} else {
						if (from.y - to.y > 0) {
							if (to.x - from.x == from.y - to.y) {
								isMoveValid = true;
							}

							for (int i = 1; i < to.x - from.x; i++) {
								if (!this.strChessBoard[to.y + i][to.x - i]
										.toString().trim().equals("")) {
									isMoveValid = false;
								}
							}

						} else {
							if (to.x - from.x == to.y - from.y)
								isMoveValid = true;

							for (int i = 1; i < to.x - from.x; i++) {
								if (!this.strChessBoard[to.y - i][to.x - i]
										.toString().trim().equals("")) {
									isMoveValid = false;
								}
							}
						}

					}

				}
			} else if (this.strChessBoard[from.y][from.x].toString().trim() == "KW") {
				if ((to.x - from.x <= 1 || from.x - to.x <= 1)
						&& (to.y - from.y <= 1 || from.y - to.y <= 1))
					isMoveValid = true;

			} else if (this.strChessBoard[from.y][from.x].toString().trim() == "QW") {
				if (from.x == to.x) {
					isMoveValid = true;
					if (from.y > to.y) {
						for (int i = to.y + 1; i < from.y; i++) {
							if (!this.strChessBoard[i][to.x].toString().trim()
									.equals("")) {
								isMoveValid = false;
							}
						}
					} else {
						for (int i = from.y + 1; i < to.y; i++) {
							if (!this.strChessBoard[i][from.x].toString()
									.trim().equals(""))
								isMoveValid = false;
						}
					}
				} else if (from.y == to.y) {
					isMoveValid = true;
					if (from.x > to.x)

					{
						for (int i = to.x + 1; i < from.x; i++) {
							if (!this.strChessBoard[from.y][i].toString()
									.trim().equals(""))
								isMoveValid = false;
						}
					} else {
						for (int i = from.x + 1; i < to.x; i++) {
							if (!this.strChessBoard[from.y][i].toString()
									.trim().equals(""))
								isMoveValid = false;
						}
					}

				}
				if (from.x != to.x && to.y != from.y) {
					if (from.x - to.x > 0) {
						if (from.y - to.y > 0) {
							if (from.x - to.x == from.y - to.y) {
								isMoveValid = true;
							}

							for (int i = 1; i < from.x - to.x; i++) {
								if (!this.strChessBoard[to.y + i][to.x + i]
										.toString().trim().equals("")) {
									isMoveValid = false;
								}
							}

						} else {
							if (from.x - to.x == to.y - from.y)
								isMoveValid = true;

							for (int i = 1; i < from.x - to.x; i++) {
								if (!this.strChessBoard[to.y - i][to.x + i]
										.toString().trim().equals("")) {
									isMoveValid = false;
								}
							}

						}

					} else {
						if (from.y - to.y > 0) {
							if (to.x - from.x == from.y - to.y) {
								isMoveValid = true;
							}

							for (int i = 1; i < to.x - from.x; i++) {
								if (!this.strChessBoard[to.y + i][to.x - i]
										.toString().trim().equals("")) {
									isMoveValid = false;
								}
							}

						} else {
							if (to.x - from.x == to.y - from.y)
								isMoveValid = true;

							for (int i = 1; i < to.x - from.x; i++) {
								if (!this.strChessBoard[to.y - i][to.x - i]
										.toString().trim().equals("")) {
									isMoveValid = false;
								}
							}
						}

					}

				}
			}

		}
		return isMoveValid;

	}

	// This method makes the selected chess piece looks like selected

	private void makeChessPieceDifferent(boolean bSelected)

	{
		for (int z = 0; z < this.pnlChessCells[this.pntMoveFrom.y][this.pntMoveFrom.x]
				.getComponentCount(); z++) {
			if (this.pnlChessCells[this.pntMoveFrom.y][this.pntMoveFrom.x]
					.getComponent(z).getClass().toString().indexOf("JLabel") > -1)

			{

				JLabel lblTemp = (JLabel) this.pnlChessCells[this.pntMoveFrom.y][this.pntMoveFrom.x]
						.getComponent(z);

				lblTemp.setEnabled(!bSelected);

			}
		}
	}

	// If class level variables Point-From and Point-To are set,

	// then this method actually moves a piece, if any exists, from

	// one cell to the other
	public void moveChessPiece(int X1, int Y1, int X2, int Y2, String unit) {
		for (int z = 0; z < pnlChessCells[Y2][X2].getComponentCount(); z++)

			if (pnlChessCells[Y2][X2].getComponent(z).getClass().toString()
					.indexOf("JLabel") > -1)

			{

				pnlChessCells[Y2][X2].remove(z);
				// To Check is A Player Won
				if (strChessBoard[Y2][X2].toString().equals("KB"))
					JOptionPane.showMessageDialog(this, "PLAYER 2 WON",
							"Notification", JOptionPane.INFORMATION_MESSAGE);

				pnlChessCells[Y2][X2].repaint();

			}
		for (int z = 0; z < pnlChessCells[Y1][X1].getComponentCount(); z++)

			if (pnlChessCells[Y1][X1].getComponent(z).getClass().toString()
					.indexOf("JLabel") > -1)

			{

				pnlChessCells[Y1][X1].remove(0);

				pnlChessCells[Y1][X1].repaint();

			}

		strChessBoard[Y2][X2] = strChessBoard[Y1][X1].toString();
		strChessBoard[Y1][X1] = "  ";
		pnlChessCells[Y2][X2].add(getPieceObject(strChessBoard[Y2][X2]),
				BorderLayout.CENTER);

		pnlChessCells[Y2][X2].validate();
		display = "Player 1 (" + nameType(strChessBoard[Y2][X2].charAt(1))
				+ ") : " + nameUnit(strChessBoard[Y2][X2].charAt(0)) + " from "
				+ getPoint(X1, Y1) + " to " + getPoint(X2, Y2);
		Msg = strChessBoard[Y2][X2].toString() + getPoint(X1, Y1)
				+ getPoint(X2, Y2);

		bottom.setSize(460, 20);
		bottom.setText(display);
		JOptionPane.showMessageDialog(this, display, "Notification",
				JOptionPane.INFORMATION_MESSAGE);
		// bMyTurn = !bMyTurn;

	}

	// Given the code of a piece as a string, this method instantiates

	// a label object with the right image inside it

	private JLabel getPieceObject(String strPieceName)

	{

		JLabel lblTemp;

		if (strPieceName.equals("RB"))

			lblTemp = new JLabel(this.rookBlack);

		else if (strPieceName.equals("BB"))

			lblTemp = new JLabel(this.bishopBlack);

		else if (strPieceName.equals("NB"))

			lblTemp = new JLabel(this.knightBlack);

		else if (strPieceName.equals("QB"))

			lblTemp = new JLabel(this.queenBlack);

		else if (strPieceName.equals("KB"))

			lblTemp = new JLabel(this.kingBlack);

		else if (strPieceName.equals("PB"))

			lblTemp = new JLabel(this.pawnBlack);

		else if (strPieceName.equals("RW"))

			lblTemp = new JLabel(this.rookWhite);

		else if (strPieceName.equals("BW"))

			lblTemp = new JLabel(this.bishopWhite);

		else if (strPieceName.equals("NW"))

			lblTemp = new JLabel(this.knightWhite);

		else if (strPieceName.equals("QW"))

			lblTemp = new JLabel(this.queenWhite);

		else if (strPieceName.equals("KW"))

			lblTemp = new JLabel(this.kingWhite);

		else if (strPieceName.equals("PW"))

			lblTemp = new JLabel(this.pawnWhite);

		else

			lblTemp = new JLabel();

		return lblTemp;

	}

	// A method to return the type of Chess Piece moved
	private String nameUnit(char u) {
		String unit = " ";
		switch (u) {
		case 'P':
			unit = "Pawn";
			break;
		case 'B':
			unit = "Bishop";
			break;
		case 'R':
			unit = "Rook";
			break;
		case 'K':
			unit = "King";
			break;
		case 'N':
			unit = "Knight";
			break;
		case 'Q':
			unit = "Queen";
			break;

		}

		return unit;
	}

	// A method to return the whether the Chess Piece moved is White/Black
	private String nameType(char u) {
		String type = " ";
		switch (u) {
		case 'W':
			type = "White";
			break;
		case 'B':
			type = "Black";
			break;
		}

		return type;
	}

	private String getPoint(int x, int y) {

		char xc = (char) (x + 65);
		String position = xc + "" + (y + 1);

		return position;
	}

	// This method reads strChessBoard two-dimensional array of string

	// and places chess pieces at their right positions

	private void arrangeChessPieces()

	{

		for (int y = 0; y < 8; y++)

			for (int x = 0; x < 8; x++)

			{

				this.pnlChessCells[y][x].add(
						this.getPieceObject(strChessBoard[y][x]),
						BorderLayout.CENTER);

				this.pnlChessCells[y][x].validate();

			}

	}

	// This method draws chess board, i.e. black and white cells on the board

	private void drawChessBoard()

	{

		for (int y = 0; y < 8; y++)

			for (int x = 0; x < 8; x++)

			{

				pnlChessCells[y][x] = new JPanel(new BorderLayout());

				pnlChessCells[y][x].addMouseListener(this);

				pnlChessCells[y][x].addMouseMotionListener(this);

				pnlMain.add(pnlChessCells[y][x]);

				if (y % 2 == 0)

					if (x % 2 != 0)

						pnlChessCells[y][x].setBackground(Color.DARK_GRAY);

					else

						pnlChessCells[y][x].setBackground(Color.WHITE);

				else

				if (x % 2 == 0)

					pnlChessCells[y][x].setBackground(Color.DARK_GRAY);

				else

					pnlChessCells[y][x].setBackground(Color.WHITE);

			}
		JTextArea[] number = new JTextArea[8];
		for (int i = 0; i < 8; i++) {
			number[i] = new JTextArea();
			number[i].setText(String.valueOf(i + 1));
			number[i].setEditable(false);
			number[i].setBackground(null);
			number[i].setFont(new Font("Sans-Serif", Font.BOLD, 10));
			toppart[i] = new JPanel();
			toppart[i].add(number[i]);
			topbar.add(toppart[i]);
		}
		JTextArea[] alpha = new JTextArea[8];
		for (int i = 0; i < 8; i++) {
			alpha[i] = new JTextArea();
			alpha[i].setText(String.valueOf((char) (i + 65)));
			alpha[i].setEditable(false);
			alpha[i].setBackground(null);
			alpha[i].setFont(new Font("Sans-Serif", Font.BOLD, 10));
			leftpart[i] = new JPanel();
			leftpart[i].add(alpha[i]);
			leftbar.add(leftpart[i]);
		}
		JButton supply = new JButton("Users");
		supply.setPreferredSize(new Dimension(125, 30));
		sidebar.add(supply);
		bottom.setText(display);
		bottom.setBounds(260, 560, 460, 50);
		bottom.setEditable(false);
		bottom.setFont(new Font("Sans-Serif", Font.BOLD, 14));
		bottombar.add(bottom);
		bottombar.setBackground(Color.WHITE);

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		Object source = e.getSource();

		JPanel pnlTemp = (JPanel) source;

		int intX = (pnlTemp.getX() / 57);

		int intY = (pnlTemp.getY() / 57);

		if (strChessBoard[intY][intX].toString().charAt(1) == 'W' && bMyTurn)
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}