package seoyunnie.dbapp.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ListPanel<T> extends JPanel {
    private static final int CELL_WIDTH = 200;
    private static final int CELL_HEIGHT = 20;

    private final DefaultListModel<T> listModel = new DefaultListModel<>();
    private final JList<T> list = new JList<>(listModel);

    public ListPanel() {
        list.setFixedCellWidth(CELL_WIDTH);
        list.setFixedCellHeight(CELL_HEIGHT);

        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public Optional<T> getSelectedValue() {
        return Optional.ofNullable(list.getSelectedValue());
    }

    public void addElement(T elm) {
        listModel.addElement(elm);
    }

    public void removeElement(T elm) {
        listModel.removeElement(elm);
    }

    @Override
    public synchronized void addMouseListener(MouseListener listener) {
        list.addMouseListener(listener);
    }
}
