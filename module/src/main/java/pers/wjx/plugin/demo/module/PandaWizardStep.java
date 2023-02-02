package pers.wjx.plugin.demo.module;

import com.intellij.codeInsight.preview.ImagePreviewComponent;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.vfs.VirtualFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PandaWizardStep extends ModuleWizardStep {
    private JPanel contentPane;
    private JTextField name;
    private JSpinner age;
    private JRadioButton women;
    private JRadioButton man;
    private JTextArea hobby;
    private JPanel photo;
    private JButton chooseButton;
    private VirtualFile image;
    private PandaModuleBuilder myModuleBuilder;

    /**
     * 🐼 最好可视化也加个提交按钮，可以反过来更新源文件，这里偷了个懒
     */
    public PandaWizardStep(PandaProfile profile) {
        name.setText(profile.getName());
        name.setEditable(false);

        age.setValue(profile.getAge());
        age.setEnabled(false);

        hobby.setText(profile.getHobby());
        hobby.setEditable(false);

        image = profile.getPhoto();
        chooseButton.setVisible(false);

        if (profile.getSex()) {
            women.setSelected(true);
            man.setEnabled(false);
        } else {
            man.setSelected(true);
            women.setEnabled(false);
        }
    }

    public PandaWizardStep(PandaModuleBuilder moduleBuilder) {
        this.myModuleBuilder = moduleBuilder;
        initComponent();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setAge(Integer age) {
        this.age.setValue(age);
    }

    public void setHobby(String hobby) {
        this.hobby.setText(hobby);
    }

    public void setSex(Boolean women) {
        this.women.setSelected(women);
        this.man.setSelected(!women);
    }

    private void initComponent() {
        if (image != null) {
            previewImage(image);
        }
        chooseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VirtualFile[] virtualFiles = FileChooser.chooseFiles(new SingleImageChooserDescriptor(), null, null);
                if (virtualFiles.length > 0) {
                    image = virtualFiles[0];
                    previewImage(image);
                }
            }
        });
        women.addItemListener(l -> man.setSelected(!women.isSelected()));
        man.addItemListener(l -> women.setSelected(!man.isSelected()));
    }

    @Override
    public JComponent getComponent() {
        return contentPane;
    }

    @Override
    public void updateDataModel() {
        PandaProfile pandaProfile = new PandaProfile(name.getText().trim(), (Integer) age.getValue(), women.isSelected(), hobby.getText(), image);
        myModuleBuilder.setPandaProfile(pandaProfile);
    }

    public void previewImage(VirtualFile file) {
        try {
            if (file == null) {
                return;
            }
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (photo.getComponentCount() == 1) {
                photo.remove(0);
            }
            ImagePreviewComponent previewComponent = ImagePreviewComponent.getPreviewComponent(bufferedImage, file.getLength());
            photo.add(previewComponent, BorderLayout.CENTER);
            photo.updateUI();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
