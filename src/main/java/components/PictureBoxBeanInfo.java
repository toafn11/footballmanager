/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

/**
 *
 * @author Minh Dep Trai
 */
import java.beans.*;

public class PictureBoxBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor image = new PropertyDescriptor(
                    "image",
                    PictureBox.class,
                    "getImage",
                    "setImage"
            );
            image.setPropertyEditorClass(ImagePropertyEditor.class);

            PropertyDescriptor sizeMode = new PropertyDescriptor(
                    "sizeMode",
                    PictureBox.class,
                    "getSizeMode",
                    "setSizeMode"
            );

            return new PropertyDescriptor[]{ image, sizeMode };

        } catch (IntrospectionException ex) {
            return null;
        }
    }
}


