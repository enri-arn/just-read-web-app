package domain.service;

import sun.misc.BASE64Encoder;
import java.io.ByteArrayOutputStream;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Arnaudo Enrico, Giraduo Paolo, Impeduglia Alessia
 */
public class ImageOperationService {

    // test of encoded: perfect but String is very very very very very very very very very very very very very big :O...
    public static void main(String args[]) throws IOException {
        BufferedImage img = ImageIO.read(new File("C:\\xampp\\htdocs\\tmp enri\\Img\\icons\\ninja.png"));
        String imgstr;
        imgstr = encodeToString(img, "png");
        System.out.println(imgstr);

        /*try {

            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\User\\Pictures\\Saved Pictures\\2477913-abstract-wallpapers.jpg"));
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            BufferedImage resizeImageJpg = resizeImage(originalImage, type, 100, 100);
            ImageIO.write(resizeImageJpg, "jpg", new File("C:\\Users\\User\\Pictures\\Saved Pictures\\2477913-abstract-wallpapers1.jpg"));

            BufferedImage resizeImagePng = resizeImage(originalImage, type, 200, 200);
            ImageIO.write(resizeImagePng, "png", new File("C:\\Users\\User\\Pictures\\Saved Pictures\\2477913-abstract-wallpapers2.png"));

            BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type, 1000, 1000);
            ImageIO.write(resizeImageHintJpg, "jpg", new File("C:\\Users\\User\\Pictures\\Saved Pictures\\2477913-abstract-wallpapers3.jpg"));

            BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type, 2000, 2000);
            ImageIO.write(resizeImageHintPng, "png", new File("C:\\Users\\User\\Pictures\\Saved Pictures\\2477913-abstract-wallpapers4.png"));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }*/

    }

    /**
     * Encoding an image in JsonBase64
     *
     * @param image: Bufferedimage to encode
     * @param type: type of the outtput (.png, .jpeg, ..)
     * @return a String rapresent the encoded image.
     */
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, int width, int height) {

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }
}
