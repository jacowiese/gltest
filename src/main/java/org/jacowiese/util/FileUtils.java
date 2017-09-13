/**
 * File name : FileUtils.java
 * Created by: Jaco Wiese (CP325745)
 * Created on: 08 Sep 2017 at 1:14:36 PM
 */
package org.jacowiese.util;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.jacowiese.texture.Texture2D;

/**
 *
 * @author Jaco Wiese (CP325745)
 */
public class FileUtils {

    public static String loadResource(String filename) throws FileNotFoundException, IOException {
        StringBuilder tempStr = new StringBuilder();

        FileReader reader = new FileReader(filename);

        char[] buf = new char[1024];
        while (reader.read(buf, 0, 1024) != -1) {
            tempStr.append(buf);
        }

        return tempStr.toString();
    }

    public static Texture2D loadPNG(String fileName) throws FileNotFoundException, IOException {
        FileInputStream stream = new FileInputStream(fileName);
        Texture2D texture = new Texture2D();
        try {
            PNGDecoder decoder = new PNGDecoder(stream);

            System.out.println(String.format("Loading texture %s. (%s,%s)", fileName, decoder.getWidth(), decoder.getHeight()));
            texture.setWidth(decoder.getWidth());
            texture.setHeight(decoder.getHeight());
            texture.setStride(decoder.getWidth() * 4);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(texture.getWidth() * texture.getHeight() * 4);
            decoder.decode(byteBuffer, texture.getStride(), Format.RGBA);
            texture.setTextureBuffer(byteBuffer.flip());
        } finally {
            stream.close();
        }

        return texture;
    }
}
