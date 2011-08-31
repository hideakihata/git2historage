package jp.ac.osaka_u.ist.sdl.scorpio.gui.intra.sourcecode;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.Element;


class LineNumberView extends JComponent {
    private static final int MARGIN = 5;

    private final JTextArea text;

    private final FontMetrics fontMetrics;

    private final int topInset;

    private final int fontAscent;

    private final int fontHeight;

    public LineNumberView(JTextArea textArea) {
        text = textArea;
        Font font = text.getFont();
        fontMetrics = getFontMetrics(font);
        fontHeight = fontMetrics.getHeight();
        fontAscent = fontMetrics.getAscent();
        topInset = text.getInsets().top;
        text.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                repaint();
            }

            public void removeUpdate(DocumentEvent e) {
                repaint();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        text.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    private int getComponentWidth() {
        Document doc = text.getDocument();
        Element root = doc.getDefaultRootElement();
        int lineCount = root.getElementIndex(doc.getLength());
        int maxDigits = Math.max(3, String.valueOf(lineCount).length());
        return maxDigits * fontMetrics.stringWidth("0") + MARGIN * 2;
    }

    public int getLineAtPoint(int y) {
        Element root = text.getDocument().getDefaultRootElement();
        int pos = text.viewToModel(new Point(0, y));
        return root.getElementIndex(pos);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getComponentWidth(), text.getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        Rectangle clip = g.getClipBounds();
        g.setColor(getBackground());
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        g.setColor(getForeground());
        int base = clip.y - topInset;
        int start = getLineAtPoint(base);
        int end = getLineAtPoint(base + clip.height);
        int y = topInset - fontHeight + fontAscent + start * fontHeight;
        for (int i = start; i <= end; i++) {
            String text = String.valueOf(i + 1);
            int x = getComponentWidth() - MARGIN - fontMetrics.stringWidth(text);
            y = y + fontHeight;
            g.drawString(text, x, y);
        }
    }
}
