package Huff2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
// please create a .txt file on the desktop named input to take the input from

class Symbol {

    private Character name;
    private int Count;
    private double probability;
    private String BinaryCode;
    private int CodeLength;

    public Symbol(Character name, int Count) {
        this.name = name;
        this.Count = Count;
    }

    public int getCodeLength() {
        return CodeLength;
    }

    public void setCodeLength(int CodeLength) {
        this.CodeLength = CodeLength;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public void incrementCount() {
        this.Count++;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getBinaryCode() {
        return BinaryCode;
    }

    public void setBinaryCode(String BinaryCode) {
        this.BinaryCode = BinaryCode;
    }

}

class node {

    int data;
    char name;
    public node left;
    public node right;
}

class Myoperator implements Comparator<node> {

    @Override
    public int compare(node t, node t1) {
        return t.data - t1.data;
    }

}

public class Huff2 {

    String input, compressed, decompress;
    static int size = 0;
    static Symbol[] arrayOfcharachters;
    static int SymbolSize;
    static int smallSize;

    static int countUniqueChar(String input) {
        return (int) input.chars()
                .distinct()
                .count();
    }

    Huff2(String x) {
        this.input = x;
        characterCountAndCalculateProbability(this.input);
    }

    public String getCompressedString() {
        return compressed;
    }

    static void characterCountAndCalculateProbability(String input) {
        SymbolSize = countUniqueChar(input);
        int f = 0;
        arrayOfcharachters = new Symbol[SymbolSize];
        Symbol s = new Symbol(input.charAt(0), 1);
        arrayOfcharachters[0] = s;
        smallSize = 1;
        f = smallSize;
        int index = 1;
        for (int i = 1; i < input.length(); i++) {
            for (int j = 0; j < smallSize; j++) {
                Character x = input.charAt(i);
                Character y = arrayOfcharachters[j].getName();
                if (x.equals(y)) {
                    arrayOfcharachters[j].incrementCount();
                    break;
                }
                if (j == smallSize - 1) {
                    s = new Symbol(input.charAt(i), 1);
                    arrayOfcharachters[index] = s;
                    index++;
                    f++;
                }
            }
            smallSize = f;

        }

        for (int i = 0; i < smallSize; i++) ///calculating its probability
        {

            int variable = arrayOfcharachters[i].getCount();
            double nemo = variable;
            double deno = input.length();
            arrayOfcharachters[i].setProbability(nemo / deno);

        }

        for (int i = 0; i < SymbolSize; i++) //sorting the array in Ascending order
        {
            for (int j = i + 1; j < SymbolSize; j++) {
                if (arrayOfcharachters[i].getCount() > arrayOfcharachters[j].getCount()) {
                    Symbol temp = arrayOfcharachters[i];
                    arrayOfcharachters[i] = arrayOfcharachters[j];
                    arrayOfcharachters[j] = temp;
                }
            }
        }

    }

    public static void traverse(node r, String s) {

        if (r.left == null && r.right == null && (Character.isLetter(r.name) || r.name == ' ')) {
            System.out.println(r.name + ":" + s);
            for (int x = 0; x < smallSize; x++) {
                if (arrayOfcharachters[x].getName() == r.name) {
                    arrayOfcharachters[x].setBinaryCode(s);
                    arrayOfcharachters[x].setCodeLength(s.length());
                }

            }
            return;
        }

        traverse(r.left, s + "1");
        traverse(r.right, s + "0");

    }

    public void Compression() {

        PriorityQueue<node> pq = new PriorityQueue<node>(smallSize, new Myoperator());

        for (int i = 0; i < smallSize; ++i) {
            node n = new node();
            n.data = arrayOfcharachters[i].getCount();
            n.name = arrayOfcharachters[i].getName();
            n.left = null;
            n.right = null;
            pq.add(n);
        }

        node root = new node();

        while (pq.size() > 1) {

            node x = pq.peek();
            pq.poll();
            node y = pq.peek();
            pq.poll();
            node f = new node();
            f.data = x.data + y.data;
            f.name = '#';
            f.left = x;
            f.right = y;
            root = f;
            pq.add(f);
        }
        traverse(root, "");

        compressed = "";
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            for (int j = 0; j < smallSize; ++j) {
                if (arrayOfcharachters[j].getName() == c) {
                    compressed += arrayOfcharachters[j].getBinaryCode();
                    break;
                }
            }
        }
    }

    public String decompression(String S) throws FileNotFoundException, IOException {

        File file1 = new File(System.getProperty("user.home") + "\\Desktop\\dictionary.txt");
        BufferedReader br3 = new BufferedReader(new FileReader(file1));
        String st, input2 = "";
        Symbol[] tempo = new Symbol[smallSize];
        int r = 0;
        String codeTemp = "";
        while ((st = br3.readLine()) != null) //readig an input from file at the desktop named dictionary.txt
        {
            Symbol symbol = new Symbol('@', 0);
            symbol.setName(st.charAt(0));
            for (int x = 2; x < st.length(); x++) {
                codeTemp += st.charAt(x);
            }
            symbol.setBinaryCode(codeTemp);
            codeTemp = "";
            tempo[r] = symbol;
            r++;
        }
        br3.close();
        String temp = "";
        decompress = "";
        for (int i = 0; i < S.length(); ++i) {
            temp += S.charAt(i);
            for (int j = 0; j < tempo.length; ++j) {
                if (tempo[j].getBinaryCode().equals(temp)) {
                    decompress += tempo[j].getName();
                    temp = "";
                    break;
                }
            }

        }
        return decompress;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file1 = new File(System.getProperty("user.home") + "\\Desktop\\input.txt");
        BufferedReader br1 = new BufferedReader(new FileReader(file1));
        String st, input = "";
        while ((st = br1.readLine()) != null) //readig an input from file at the desktop named input.txt
        {
            input = input + st;
        }
        br1.close();
        Huff2 h = new Huff2(input);

        for (int i = 0; i < smallSize; i++) {
            System.out.println("char = " + arrayOfcharachters[i].getName() + " count = " + arrayOfcharachters[i].getCount());
        }
        h.Compression();
        int CompressionRatio = 0;
        try {
            FileWriter Dictionary = new FileWriter(System.getProperty("user.home") + "\\Desktop\\dictionary.txt");
            FileWriter compressedString = new FileWriter(System.getProperty("user.home") + "\\Desktop\\compressedString.txt");

            for (int i = 0; i < smallSize; i++) ///writes the dictionary to a file
            {
                Dictionary.write(arrayOfcharachters[i].getName() + "-" + arrayOfcharachters[i].getBinaryCode() + "\n");
                CompressionRatio += arrayOfcharachters[i].getCodeLength() * arrayOfcharachters[i].getCount();
            }
            compressedString.write(h.getCompressedString()); // writes the compressedString in a file named CompressedString.txt

            Dictionary.close();
            compressedString.close();
            System.out.println("Successfully wrote the dictionary to a file dictionary.txt on the desktop");
            System.out.println("Successfully wrote the Compressed String to a file named CompressedString.txt on the desktop");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred.");
        }

        System.out.println("compression Ratio is " + CompressionRatio + " bits.");
        String c = "Compression Ratio is :" + CompressionRatio + " bits.";
        File file2 = new File(System.getProperty("user.home") + "\\Desktop\\compressedString.txt");
        BufferedReader br2 = new BufferedReader(new FileReader(file2));
        String st2;
        input = "";
        while ((st2 = br2.readLine()) != null) //readig an input from file at the desktop named compressedString.txt
        {
            input = input + st2;
        }
        br2.close();
        String s;
        s = h.decompression(input);

        JButton b1 = new JButton("Compress");
        b1.setBounds(40, 100, 100, 60);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Successfully wrote to the 2 files.\n " + c);

            }
        });
        JButton b2 = new JButton("Deompress");
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null, s);

            }
        }
        );
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(200, 200, 200, 200));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(b1);
        panel.add(b2);
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Standard Huffman");
        frame.pack();
        frame.setVisible(true);
    }

}
