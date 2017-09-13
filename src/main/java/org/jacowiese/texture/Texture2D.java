/**
 * File name : Texture2D.java
 * Created by: Jaco Wiese (CP325745)
 * Created on: 13 Sep 2017 at 1:37:26 PM
 */
package org.jacowiese.texture;

import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 *
 * @author Jaco Wiese (CP325745)
 */
public class Texture2D {

    private Buffer textureBuffer;
    private int stride;
    private Format format;
    private int width;
    private int height;

    public Texture2D() {
    }

    public ByteBuffer getTextureBuffer() {
        return (ByteBuffer)textureBuffer;
    }

    public void setTextureBuffer(Buffer textureBuffer) {
        this.textureBuffer = textureBuffer;
    }

    public int getStride() {
        return stride;
    }

    public void setStride(int stride) {
        this.stride = stride;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
