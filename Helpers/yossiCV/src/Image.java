import org.opencv.core.*;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Image {

    static JFrame frame;
    static JLabel lbl;
    static ImageIcon icon;

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat source=null;
        Mat template=null;
        String filePath="D:\\Downloads\\OpenCvObjectDetection-master\\OpenCvObjectDetection-master\\data\\sample images\\";
        source=Imgcodecs.imread(filePath+"KIRSH.jpg");
        template=Imgcodecs.imread(filePath+"balon.jpg");

        CascadeClassifier cascadeEyeClassifier = new CascadeClassifier(
                "D:\\Downloads\\OpenCvObjectDetection-master\\OpenCvObjectDetection-master\\data\\haarcascade files\\haarcascade_eye_tree_eyeglasses.xml");
        int machMethod=Imgproc.TM_CCOEFF;

        MatOfRect eyes = new MatOfRect();
        cascadeEyeClassifier.detectMultiScale(source, eyes);
        for (Rect rect : eyes.toArray()) {
            //Sol �st k�?esine metin yaz
            //Imgproc.putText(source, "Eye", new Point(rect.x,rect.y-5), 1, 2, new Scalar(0,0,255));
            //Kare �iz
            Imgproc.rectangle(source, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(200, 200, 100),2);
        }
        PushImage(ConvertMat2Image(source));
        System.out.println(String.format("YOSSI %s", eyes.toArray().length));


    }

    private static BufferedImage ConvertMat2Image(Mat kameraVerisi) {


        MatOfByte byteMatVerisi = new MatOfByte();
        //Ara belle?e verilen formatta g�r�nt� kodlar
        Imgcodecs.imencode(".jpg", kameraVerisi, byteMatVerisi);
        //Mat nesnesinin toArray() metodu elemanlary byte dizisine �evirir
        byte[] byteArray = byteMatVerisi.toArray();
        BufferedImage goruntu = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            goruntu = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return goruntu;
    }

    public static void PencereHazirla() {
        frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(700, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void PushImage(java.awt.Image img2) {
        //Pencere olu?turulmamy? ise hazyrlanyr
        if (frame == null)
            PencereHazirla();
        //Daha �nceden bir g�r�nt� y�klenmi? ise yenisi i�in kaldyryr
        if (lbl != null)
            frame.remove(lbl);
        icon = new ImageIcon(img2);
        lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        //Frame nesnesini yeniler
        frame.revalidate();
    }

}
