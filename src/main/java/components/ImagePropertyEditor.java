package components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyEditorSupport;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ImagePropertyEditor extends PropertyEditorSupport {

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public Component getCustomEditor() {

        JPanel panel = new JPanel(new BorderLayout());
        JButton btn = new JButton("Choose Image...");

        btn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter(
                            "Image files", "png", "jpg", "jpeg", "gif"
                    )
            );

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                setValue(new ImageIcon(chooser.getSelectedFile().getAbsolutePath()));
            }
        });

        panel.add(btn, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public String getAsText() {
        ImageIcon icon = (ImageIcon) getValue();
        return icon == null ? "" : icon.toString();
    }

    @Override
    public void setAsText(String path) {
        if (path == null || path.isEmpty()) {
            setValue(null);
        } else {
            setValue(new ImageIcon(path));
        }
    }
}
