import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class KeyPress extends Frame {
	Label label;
	TextField txtField;

	public static void main(String[] args) {
		KeyPress k = new KeyPress();
	}

	public KeyPress() {
		// super("Key Press Event Frame");
		Panel panel = new Panel();
		label = new Label();
		txtField = new TextField(20);
		txtField.addKeyListener(new MyKeyListener());
		txtField.addKeyListener(new MyKeyListener2());
		add(label, BorderLayout.NORTH);
		panel.add(txtField, BorderLayout.CENTER);
		add(panel, BorderLayout.CENTER);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		setSize(400, 400);
		setVisible(true);
	}

	public class MyKeyListener extends KeyAdapter {
		public void keyReleased(KeyEvent ke) {
			char i = ke.getKeyChar();
			String str = Character.toString(i);
			label.setText(str);
			System.out.println("Key Released");
		}
	}

	public class MyKeyListener2 implements KeyListener {
		public void keyPressed(KeyEvent ke) {
			char i = ke.getKeyChar();
			String str = Character.toString(i);
			label.setText(str);
			System.out.println("Key Pressed");
		}

		public void keyTyped(KeyEvent ke) {

		}

		public void keyReleased(KeyEvent ke) {

		}
	}
}