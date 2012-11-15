package com.jspservlets;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.AlphaComposite;

/**
 * @author Ranjan This class is use for create image of different resolutions from input image bytes. Also return
 *         different resolutions image bytes from input of image bytes.
 */
public class ImageCreator {

    public static boolean createImageFile(String p_imageNameWithPath, byte[] p_imageData) {
        Image l_image = Toolkit.getDefaultToolkit().createImage(p_imageData);
        MediaTracker l_mediaTracker = new MediaTracker(new Container());
        l_mediaTracker.addImage(l_image, 0);
        try {
            l_mediaTracker.waitForID(0);
        } catch (InterruptedException e)
        {
            return false;
        }

        int l_width = l_image.getWidth(null);
        int l_height = l_image.getWidth(null);

        // draw original image
        BufferedImage l_bufferedImage = new BufferedImage(l_width, l_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D l_graphics2D = l_bufferedImage.createGraphics();
        l_graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        l_graphics2D.drawImage(l_image, 0, 0, l_width, l_height, null);
        l_graphics2D.dispose();

        // Save to file
        File file = new File(p_imageNameWithPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        BufferedOutputStream l_out = null;
        try
        {
            l_out = new BufferedOutputStream(new FileOutputStream(p_imageNameWithPath));

            if (p_imageNameWithPath.endsWith("jpeg")
                    || p_imageNameWithPath.endsWith("JPEG")
                    || p_imageNameWithPath.endsWith("jpg"))
                ImageIO.write(l_bufferedImage, "jpeg", l_out);
            else if (p_imageNameWithPath.endsWith("png")
                    || p_imageNameWithPath.endsWith("PNG"))
                ImageIO.write(l_bufferedImage, "png", l_out);
            l_out.flush();
            l_out.close();
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block

            return false;
        } catch (IOException e)
        {
            // TODO Auto-generated catch block

            return false;
        }
        l_out = null;
        l_graphics2D = null;
        l_bufferedImage = null;
        l_mediaTracker = null;
        l_image = null;

        return true;
    }

    public static boolean createImageFile(File p_imageNameWithPath, byte[] p_imageData) {
        Image l_image = Toolkit.getDefaultToolkit().createImage(p_imageData);
        MediaTracker l_mediaTracker = new MediaTracker(new Container());
        l_mediaTracker.addImage(l_image, 0);
        try {
            l_mediaTracker.waitForID(0);
        } catch (InterruptedException e) {
            return false;
        }

        int l_width = l_image.getWidth(null);
        int l_height = l_image.getWidth(null);

        // draw original image
        BufferedImage l_bufferedImage = new BufferedImage(l_width, l_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D l_graphics2D = l_bufferedImage.createGraphics();
        l_graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        l_graphics2D.drawImage(l_image, 0, 0, l_width, l_height, null);
        l_graphics2D.dispose();

        // Save to file
        // File file = new File(p_imageNameWithPath);

        if (!p_imageNameWithPath.getParentFile().exists()) {
            p_imageNameWithPath.getParentFile().mkdirs();
        }
        BufferedOutputStream l_out = null;
        try
        {
            l_out = new BufferedOutputStream(new FileOutputStream(p_imageNameWithPath));

            if (p_imageNameWithPath.getName().endsWith("jpeg")
                    || p_imageNameWithPath.getName().endsWith("JPEG")
                    || p_imageNameWithPath.getName().endsWith("jpg"))
                ImageIO.write(l_bufferedImage, "jpeg", l_out);
            else if (p_imageNameWithPath.getName().endsWith("png")
                    || p_imageNameWithPath.getName().endsWith("PNG"))
                ImageIO.write(l_bufferedImage, "png", l_out);
            l_out.flush();
            l_out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block

            return false;
        } catch (IOException e)
        {
            // TODO Auto-generated catch block

            return false;
        }
        l_out = null;
        l_graphics2D = null;
        l_bufferedImage = null;
        l_mediaTracker = null;
        l_image = null;

        return true;
    }

    public static byte[] getResizeImage(byte[] p_imageData,
            int p_imgWidth, int p_imgHeight) {
        try {
            byte[] l_result = null;
            Image l_image = Toolkit.getDefaultToolkit().createImage(p_imageData);
            MediaTracker l_mediaTracker = new MediaTracker(new Container());
            l_mediaTracker.addImage(l_image, 0);
            try {
                l_mediaTracker.waitForID(0);
            } catch (InterruptedException e) {

                return l_result;
            }
            int l_imageWidth = l_image.getWidth(null);
            int l_imageHeight = l_image.getHeight(null);
            int l_size_swap = 0;
            double l_thumbRatio = 0.0;
            double l_imageRatio = 0.0;
            if (l_imageWidth >= l_imageHeight) {
                // determine thumbnail size from WIDTH and HEIGHT
            } else {
                l_size_swap = p_imgWidth;
                p_imgWidth = p_imgHeight;
                p_imgHeight = l_size_swap;
            }
            l_thumbRatio = (double) p_imgWidth / (double) p_imgHeight;
            l_imageRatio = (double) l_imageWidth / (double) l_imageHeight;
            if (l_thumbRatio < l_imageRatio) {
                p_imgHeight = (int) (p_imgWidth / l_imageRatio);
            } else {
                p_imgWidth = (int) (p_imgHeight * l_imageRatio);
            }
            // draw original image to thumbnail image object and
            // scale it to the new size on-the-fly
            BufferedImage l_thumbImage = new BufferedImage(p_imgWidth, p_imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D l_graphics2D = l_thumbImage.createGraphics();
            l_graphics2D
                    .setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            l_graphics2D.drawImage(l_image, 0, 0, p_imgWidth, p_imgHeight, null);
            // save thumbnail image to OUTFILE
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(l_thumbImage, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException ex) {

        }
        return null;
    }

    // public static byte[] getResizeImage(byte[] p_imageData,String p_imageResolution){
    //
    // int l_imgSize = ImageResolutionTypes.getImageSize(p_imageResolution);
    //
    // //return getResizeImage(p_imageData, l_imgSize,l_imgSize);
    // return getResizeImageWithHeightFixed(p_imageData, l_imgSize);
    // }

    private static BufferedImage createImageWithHint(BufferedImage p_originalImage, int p_width, int p_height, int type) {

        BufferedImage resizedImage = new BufferedImage(p_width, p_height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(p_originalImage, 0, 0, p_height, p_height, null);
        g.dispose();

        return resizedImage;
    }

    public static boolean createImageWithHint(String p_imageNameWithPath, byte[] p_imageData) {
        Image l_image = Toolkit.getDefaultToolkit().createImage(p_imageData);
        MediaTracker l_mediaTracker = new MediaTracker(new Container());
        l_mediaTracker.addImage(l_image, 0);
        try
        {
            l_mediaTracker.waitForID(0);
        } catch (InterruptedException e)
        {

            return false;
        }

        int l_width = l_image.getWidth(null);
        int l_height = l_image.getWidth(null);

        // draw original image
        BufferedImage l_bufferedImage = new BufferedImage(l_width, l_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D l_graphics2D = l_bufferedImage.createGraphics();
        // l_graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        l_graphics2D.setComposite(AlphaComposite.Src);
        l_graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        l_graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        l_graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        l_graphics2D.drawImage(l_image, 0, 0, l_width, l_height, null);
        l_graphics2D.dispose();

        // BufferedImage l_bufferedImageWithHint = createImageWithHint(l_bufferedImage,l_width,
        // l_height,BufferedImage.TYPE_INT_RGB);

        // Save to file
        File file = new File(p_imageNameWithPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        BufferedOutputStream l_out = null;
        try
        {
            l_out = new BufferedOutputStream(new FileOutputStream(p_imageNameWithPath));
            if (p_imageNameWithPath.endsWith("jpeg")
                    || p_imageNameWithPath.endsWith("JPEG")
                    || p_imageNameWithPath.endsWith("jpg"))
                ImageIO.write(l_bufferedImage, "jpeg", l_out);
            else if (p_imageNameWithPath.endsWith("png")
                    || p_imageNameWithPath.endsWith("PNG"))
                ImageIO.write(l_bufferedImage, "png", l_out);
            l_out.flush();
            l_out.close();
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block

            return false;
        } catch (IOException e)
        {
            // TODO Auto-generated catch block

            return false;
        }
        l_out = null;
        l_graphics2D = null;
        l_bufferedImage = null;
        l_mediaTracker = null;
        l_image = null;

        return true;
    }

    public static File createResizeImageWithHeightFixed(String p_inputImageWithPath, final int p_imgHeight) {
        try {
            File l_inFile = new File(p_inputImageWithPath);
            System.out.println(l_inFile);
            File l_outPutImageWithPath = new File(l_inFile.getParentFile(), "new_" + l_inFile.getName());
            System.out.println(l_outPutImageWithPath);
            byte[] l_result = null;
            Image l_image = Toolkit.getDefaultToolkit().createImage(p_inputImageWithPath);
            MediaTracker l_mediaTracker = new MediaTracker(new Container());
            l_mediaTracker.addImage(l_image, 0);
            try {
                l_mediaTracker.waitForID(0);
            } catch (InterruptedException e) {

                return null;
            }
            int l_orgImageWidth = l_image.getWidth(null);
            int l_orgImageHeight = l_image.getHeight(null);
            int l_size_swap = 0;
            double l_thumbRatio = 0.0;
            double l_imageRatio = 0.0;
            int l_ImagenewWidth = (int) (Math.round((l_orgImageWidth * p_imgHeight) / l_orgImageHeight));
            System.out.println(l_ImagenewWidth);
            // draw original image to thumbnail image object and
            // scale it to the new size on-the-fly
            BufferedImage l_thumbImage = new BufferedImage(l_ImagenewWidth, p_imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D l_graphics2D = l_thumbImage.createGraphics();
            l_graphics2D
                    .setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            l_graphics2D.drawImage(l_image, 0, 0, l_ImagenewWidth, p_imgHeight, null);
            // save thumbnail image to OUTFILE
            ImageIO.write(l_thumbImage, "jpg", l_outPutImageWithPath);
            return l_outPutImageWithPath;
        } catch (IOException ex) {

        }
        return null;
    }

    public static byte[] getResizeImageWithHeightFixed(byte[] p_imageData, final int p_imgHeight) {

        try
        {
            byte[] l_result = null;
            Image l_image = Toolkit.getDefaultToolkit().createImage(p_imageData);
            MediaTracker l_mediaTracker = new MediaTracker(new Container());
            l_mediaTracker.addImage(l_image, 0);
            try {
                l_mediaTracker.waitForID(0);
            } catch (InterruptedException e) {

                return l_result;
            }
            int l_orgImageWidth = l_image.getWidth(null);
            int l_orgImageHeight = l_image.getHeight(null);
            int l_size_swap = 0;
            double l_thumbRatio = 0.0;
            double l_imageRatio = 0.0;
            int l_ImagenewWidth = p_imgHeight; // (int)(Math.round((l_orgImageWidth*p_imgHeight)/l_orgImageHeight));
            // draw original image to thumbnail image object and
            // scale it to the new size on-the-fly
            BufferedImage l_thumbImage = new BufferedImage(l_ImagenewWidth, p_imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D l_graphics2D = l_thumbImage.createGraphics();
            l_graphics2D
                    .setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            l_graphics2D.drawImage(l_image, 0, 0, l_ImagenewWidth, p_imgHeight, null);
            // save thumbnail image to OUTFILE

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(l_thumbImage, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException ex)
        {

        }
        return null;
    }

    public static byte[] getResizeImageWithHeightFixed(String p_inputImageWithPath, final int p_imgHeight) {
        try {
            File l_inFile = new File(p_inputImageWithPath);
            System.out.println(l_inFile);
            byte[] l_result = null;
            Image l_image = Toolkit.getDefaultToolkit().createImage(p_inputImageWithPath);
            MediaTracker l_mediaTracker = new MediaTracker(new Container());
            l_mediaTracker.addImage(l_image, 0);
            try {
                l_mediaTracker.waitForID(0);
            } catch (InterruptedException e) {

                return l_result;
            }
            int l_orgImageWidth = l_image.getWidth(null);
            int l_orgImageHeight = l_image.getHeight(null);
            int l_size_swap = 0;
            double l_thumbRatio = 0.0;
            double l_imageRatio = 0.0;
            int l_ImagenewWidth = (int) (Math.round((l_orgImageWidth * p_imgHeight) / l_orgImageHeight));
            System.out.println(l_ImagenewWidth);
            // draw original image to thumbnail image object and
            // scale it to the new size on-the-fly
            BufferedImage l_thumbImage = new BufferedImage(l_ImagenewWidth, p_imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D l_graphics2D = l_thumbImage.createGraphics();
            l_graphics2D
                    .setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            l_graphics2D.drawImage(l_image, 0, 0, l_ImagenewWidth, p_imgHeight, null);
            // save thumbnail image to OUTFILE
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(l_thumbImage, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException ex) {

        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // getResizeImageWithHeightFixed("E:\\images\\DIMAYOR.png",80,80);
        byte[] data = getResizeImageWithHeightFixed("C:\\testing\\index.JPEG", 100);
        FileOutputStream fos = new FileOutputStream(new File("c:\\testing\\test.jpg"));
        fos.write(data);
        fos.close();
    }
}